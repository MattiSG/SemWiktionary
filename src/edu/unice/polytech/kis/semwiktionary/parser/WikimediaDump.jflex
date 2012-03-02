package edu.unice.polytech.kis.semwiktionary.parser;

import java.io.*;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import edu.unice.polytech.kis.semwiktionary.model.*;
import edu.unice.polytech.kis.semwiktionary.database.Relation;

import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;

%%


%{
	public static final String  OUTPUT_FILE = "log/parser-output.txt",
								ERROR_FILE = "log/parser-error.txt";
								
	public long wordCount = 0;

	private final PrintStream PREV_OUT = System.out,
							  PREV_ERR = System.err;
							  
	private final static int LOG_FREQUENCY = 100; // a count message will be output to the console on every multiple of this frequency

	private MutableWord currentWord;
	private Relation currentRelation;
	
	/**@name	Flags
	*These flags are used to give parsing information for a word to the user. They are reset between each word
	*/
	//@{
	private boolean errorFlag, // a parsing error happened
					syntaxErrorFlag, // the MediaWiki text was malformed
					funcChimEx;	// a chemical formula
	//@}

	private Definition currentDefinition;
	private int definitionCount = 0;
	private int definitionDepth = -1; // the depth is the number of sharps (#) in front of a definition, minus one (that's optimization to have only one substraction for the 0-based indexed list). So, to trigger comparisons, we need to be negative.
	
	private String buffer = ""; // an all-purpose buffer, to be initialized by groups that need it
	private String source = "";
	
	private Vector<String> definitionsBuffer;
	private Map<String, Relation> relationsMap;

	private WikiModel wikiModel = new WikiModel("http://www.mywiki.com/wiki/${image}", "http://www.mywiki.com/wiki/${title}");
	private PlainTextConverter converter = new PlainTextConverter();
	
	private long timer = System.nanoTime();
	private static final long FIRST_TICK = System.nanoTime();
	
	
	/** Returns nanoseconds since the last `tick()` call.
	*/
	private long tick() {
		long result = System.nanoTime() - timer;
		
		timer = System.nanoTime();
		
		return result;
	}

	private void initParser() {
		PREV_OUT.println("Word,Parsing time (ns),Syntax errors,Parser errors");
		PREV_OUT.print("**Init**"); // for logging purposes
	
		definitionsBuffer = new Vector<String>(8); // maximum level of foreseeable nested definitions
		relationsMap = new HashMap<String, Relation>(3);

		relationsMap.put("syn", Relation.SYNONYM);
		relationsMap.put("ant", Relation.ANTONYM);
		relationsMap.put("tropo", Relation.TROPONYM);
	}
	
	/** Logs a CSV entry to provide info about how the last word's parsing went.
	* Does not include the actual word title output, in case a crash happened on the given word.
	*/
	private void logWord() {
		PREV_OUT.println(","
						 + tick() + ","
						 + (syntaxErrorFlag ? "y" : "n") + ","
						 + (errorFlag ? "y" : "n")
						);
	}

	private void initWord(String word) {
		logWord();
		
		PREV_OUT.print(word); // output before the parsing starts, to have the culprit in case of a crash
		
		currentWord = MutableWord.create(word);	//TODO: delay until language was accepted? We currently create the word immediately, even though we might not store anything from it if its language is not supported
		resetFlags();
		
		initSection();
		
		wordCount++;
		
		if ((wordCount % LOG_FREQUENCY) == 0)
			PREV_ERR.println("\t\t\t\t\t\t\t" + wordCount + " WORDS PARSED!");
	}
	
	
	private void resetFlags() {
		errorFlag = false;
		syntaxErrorFlag = false;
	}
	
	private void initSection() {
		if (definitionDepth >= 0)
			saveCurrentDefinition(); // we matched some definitions, so the last one must be saved, since saving them is done when finding a new one

		definitionDepth = -1;
		definitionCount = 0;
		definitionsBuffer.clear();
	}
	
	private void leaveSection() {
		initSection();
		
		yybegin(MEDIAWIKI);
	}
	
	private void logError(String text) {
		System.err.println("**" + text + "**");
		errorFlag = true;
	}
	
	private void logSyntaxError(String text) {
		System.err.println("!! " + text);
		syntaxErrorFlag = true;
	}
	
	private void saveCurrentDefinition() {
		definitionCount++;
		currentDefinition.setPosition(definitionCount);
		currentWord.addDefinition(currentDefinition);
	}

	private String convertToPlainText(String wikiMedia) {
		return wikiModel.render(converter, wikiMedia);
	}
%}


%public
%class WikimediaDump
%type Void
%unicode
%debug

%init{
	try {
		System.setErr(new PrintStream(new FileOutputStream(new File(ERROR_FILE))));
		System.setOut(new PrintStream(new FileOutputStream(new File(OUTPUT_FILE))));
	} catch (java.io.FileNotFoundException e) {
		System.err.println("Files '" + OUTPUT_FILE + "' and/or '" + ERROR_FILE + "' could not be created!");
		throw new RuntimeException(e);
	}

	initParser();
	
	yybegin(XML);
%init}

%eof{
	logWord();
	
	PREV_ERR.println("Total time: " + ((System.nanoTime() - FIRST_TICK) / 10E9) + "s");
	PREV_ERR.println("Parsed words: " + wordCount);

	// restore outputs
	System.setOut(PREV_OUT);
	System.setErr(PREV_ERR);
%eof}

word = ([:letter:]+)
punct = [,;:.\()/…]
whitespace = [\ \t]
newline = (\r|\n|\r\n)
optionalSpaces = ({whitespace}*)
space = ({whitespace}|{newline})

%state TITLE, MEDIAWIKI, LANG, H2, NATURE, SECTION, PATTERN, PRONUNCIATION, DEFINITION, DEFINITION_DOMAIN, DEFINITION_EXAMPLE, SOURCE, FCHIMIE, SIMPLENYM, SPNM_CONTEXT, SPNM_WORD, TRASH

%xstate XML, PAGE

%%


"</page>"
{
	// fallback for all cases
	logSyntaxError("Out of page, error on word '" + currentWord.getTitle() + "'");
	yybegin(XML);
}


<XML>
{
	"<page>"
	{
		yybegin(PAGE);
	}
	
	([^<]|"<"[^p]|"<p"[^a])+
	{
		// this longer regexp improves performance by getting as long matches as possible
		// in initial state: suppress output
	}
}

<PAGE> 
{
	"<text xml:space=\"preserve\">"
	{
		yybegin(MEDIAWIKI);
	}
	
	"<title>"
	{
		yybegin(TITLE);
	}
	
	"</page>" 
	{ 
		yybegin(XML);
	}
	
	([^<]|"<"[^t/]|"<t"[^ei]|"<ti"[^t]|"</"[^p])+
	{
		// this longer regexp improves performance by getting as long matches as possible
		// in Page: suppress output
	}
}


<TITLE>
{
	[^<:]+"<"
	{
		String title = yytext();
		initWord(title.substring(0, title.length() - 1));
		yybegin(PAGE);
	}
	
	.
	{
		// if it is anything else than expected (i.e. it contains a colon), then it is not a page we're interested in, we can skip it immediately
		yybegin(XML);
	}
}


<MEDIAWIKI>
{
	"== {{="
	{
		yybegin(LANG);
	}
	
	"{{-"
	{
		yybegin(H2);
	}
	
	"</text>"
	{
		yybegin(XML);
	}

	([^={<]|"="[^=]|"=="[^ ]|"== "[^{]|"== {{-"|"{"[^{]|"{{"[^-])+|.
	{
		// "== {{-" is for "== {{-car-}} =="
		// in MediaWiki: suppress output
	}
}

<LANG>
{
	"fr="
	{
		currentWord.set("lang", "fr");
		yybegin(MEDIAWIKI);
	}
	
	"conv="
	{
		currentWord.set("lang", "conv");
		yybegin(MEDIAWIKI);
	}
	
	.
	{
		yybegin(XML);	// this language is not accepted
	}
}


<H2>
{
	"verb"|"nom"|"adj"|"noms-vern"
	{
		// TODO: add all types
		yybegin(NATURE);
	}
	
	("syn"|"ant"|"tropo")"-}}"{newline}
	{
		buffer = yytext();
		currentRelation = relationsMap.get(buffer.substring(0, buffer.length() - 4));
		yybegin(SIMPLENYM);
	}

	.
	{
		yybegin(TRASH);	// this is not an accepted type
	}
}


<NATURE>
{
	-[^}]*"}}"
	{
		// TODO: store type in word
		yybegin(SECTION);
	}
	
	.
	{
		logSyntaxError("Unparsable nature in '" + currentWord.getTitle());
		yybegin(SECTION);
	}
}


<SECTION>
{ // an entrance into that state with a non-consumed newline will switch to <MEDIAWIKI>
	"{{"
	{
		yybegin(PATTERN);
	}

	{newline}#+{optionalSpaces}
	{
		int newDepth = yytext().trim().length() - 1;
		
		if (newDepth <= definitionDepth)
			saveCurrentDefinition();	// we don't want to add definitions that will be specified by inner lists, since we're concatenating the innermost strings with their parents
			
		definitionDepth = newDepth;
		
		currentDefinition = new Definition();
		
		yybegin(DEFINITION);
	}
	
	{newline}#+("*"|":"){optionalSpaces}
	{
		// the colon is not standard, but is used in some words ("siège")
		buffer = "";
		yybegin(DEFINITION_EXAMPLE);
	}
	
	"</text>"
	{
		yybegin(XML);
	}
	
	{newline}{newline}
	{
		leaveSection();
	}

	([^{<\r\n])+|"{"[^{]|.|{newline}
	{
		// in Section: suppress output
	}
}


<PATTERN>
{
	"pron|"
	{
		yybegin(PRONUNCIATION);
	}

	"fchim"
	{
		if (! funcChimEx)
			buffer = "";
		yybegin(FCHIMIE);
	}
	
	"}}"
	{
		yybegin(SECTION);
	}

	[^|}]+|"|"
	{
		logError("Unexpected pattern value: '" + yytext() + "'");
	}
	
	"}"
	{
		logSyntaxError("Unbalanced bracket in pattern in '" + currentWord.getTitle() + "'");
		yybegin(SECTION);
	}
}


<PRONUNCIATION>
{
	[^}|]+
	{
		currentWord.set("pronunciation", yytext());
	}
	
	"|"|"}"
	{
		// not only "}}" in case of missing ending curly bracket
		yybegin(PATTERN);
	}
}


<DEFINITION_DOMAIN>
{
	[^|}]+
	{
		currentDefinition.addDomain(yytext());
	}
	
	(\|[^}]*)?"}}"{optionalSpaces}
	{
		yybegin(DEFINITION);
	}
	
	(\|[^}]*)?"}"{optionalSpaces}
	{
		logSyntaxError("Single bracket in definition domain in '" + currentWord.getTitle() + "'");
		yybegin(DEFINITION);
	}
}

<FCHIMIE>
{
	"|"
	{

	}

	"}}"
	{
		if(funcChimEx){
			funcChimEx = false;
			yybegin(DEFINITION_EXAMPLE);		
		}
		else {
			definitionDepth++;
			definitionsBuffer.add(definitionDepth, buffer);
			definitionDepth++;
			yybegin(DEFINITION);
		}			
	}

	[^\|}]+
	{
		buffer += yytext();
	}
}

<DEFINITION_EXAMPLE>
{
	"{{source|"
	{
		source = "";
		yybegin(SOURCE);
	}

	"{{fchim"
	{
		funcChimEx = true;
		yypushback(7);
		yybegin(SECTION);
	}

	([^\r\n{]|"{"[^{])+|"{{"
	{
		buffer += yytext();
	}

	{newline}#+\*:
	{
		// the colon means we're in the same example, but on another line
		buffer += "\n" + yytext();
	}	

	{newline}
	{
		if(!source.isEmpty())
		{
			buffer +=  "— (" + source + ")";
			source = "";
		}
		String plainStr = convertToPlainText(buffer);
		currentDefinition.addExample(plainStr);
		yypushback(1);	// <SECTION> needs it to match
		yybegin(SECTION);
	}
}

<SOURCE>
{
	"{{"|"w|"|"}}"
	{
	
	}

	([^\r\n}{w]|"}"[^}]|"{"[^{]|"w"[^\|])+
	{
		source += yytext();
	}

	{newline}
	{
		yypushback(1);	// <DEFINITION_EXAMPLE> needs it to match
		yybegin(DEFINITION_EXAMPLE);
	}
}


<DEFINITION>
{
	([^\r\n{]|"{"[^{])+
	{
		try {

			definitionsBuffer.add(definitionDepth, yytext());
			
			String result = "";
			for (int i = definitionDepth; i >= 0; i--)
				result = definitionsBuffer.get(i).trim() + (result.isEmpty() ? "" : (" " + result));

			String plainStr = convertToPlainText(result);
			currentDefinition.setContent(plainStr);

		} catch (ArrayIndexOutOfBoundsException e) {
			// this can happen in very rare cases of malformed nesting (i.e. missing a nesting level, like starting a definition list with `##`)
			logSyntaxError("Definitions nesting error in word '" + currentWord.getTitle() + "': '" + yytext() + "'. Only content at this nesting level will be stored.");
			
			String plainStr = convertToPlainText(yytext());
			currentDefinition.setContent(plainStr); // best recovery we can do: forget about concatenation
		}
				
		yybegin(SECTION);
	}
	
	"{{"
	{
		yybegin(DEFINITION_DOMAIN);
	}
	
	{newline}
	{
		// rare case in which the only content of a definition is a list of domains (see "neuf")
		try {
			definitionsBuffer.add(definitionDepth, ""); // otherwise we'll break the concatenation
		} catch (ArrayIndexOutOfBoundsException e) {
			// this can happen in very rare cases of malformed nesting (i.e. missing a nesting level, like starting a definition list with `##`)
			logSyntaxError("Definitions nesting error in word '" + currentWord.getTitle() + "', with a definition that has no content other than domains."); // damn, that's an edge case… (but an existing one)
			
			// no recovery to do here: more nested definitions will be caught in the usual definition handling case above this one
		}
		
		yypushback(1);
		yybegin(SECTION);
	}
}

<SIMPLENYM>
{
	"{{"[()|]"}}"
	{
		// Wiki syntax for tables
	}

	":"{optionalSpaces}|"'''"|";"
	{
		yybegin(SPNM_CONTEXT);
	}

	"*"([^\[]|"["[^\[])*"[["
	{
		yybegin(SPNM_WORD);
	}

	{newline}{newline}
	{
		leaveSection();
	}

	([^-:;'*\r\n]|"-"[^}])+|.|{newline}
	{
		// in SimpleNym: suppress output
	}
}

<SPNM_CONTEXT>
{
	([^:\n\r]+)
	{
		//TODO: context is not handled yet
	}

	":"|{newline}
	{
		yybegin(SIMPLENYM);
	}
}

<SPNM_WORD>
{
	"]]"
	{
		yybegin(SIMPLENYM);
	}

	([^\]]|"]"[^\]])+
	{
		try {
			currentWord.set(currentRelation, MutableWord.from(yytext()));
		} catch (Exception e) {
			logError("Oh no! Got an exception while trying to add relation " + currentRelation + " to '" + yytext() + "' from word '" + currentWord.getTitle() + "'  :( ");
			e.printStackTrace(System.err);
		}
	}
}

<TRASH>
{
	([^\r\n]|{newline}[^\r\n])*
	{
		// Trash
	}

	{newline}{newline}
	{
		yybegin(MEDIAWIKI);
	}
}
