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

	private MutableWord currentWord = new MutableWord(new Word("Init & prolog")); // init for logging purposes
	private Relation currentRelation;
	
	private boolean errorFlag; // used to warn user about errors for a word without interrupting the process; reset between each word

	private Definition currentDefinition;
	private int definitionCount = 0;
	private int definitionDepth = -1; // the depth is the number of sharps (#) in front of a definition, minus one (that's optimization to have only one substraction for the 0-based indexed list). So, to trigger comparisons, we need to be negative.
	
	private String buffer = ""; // an all-purpose buffer, to be initialized by groups that need it
	
	private Vector<String> definitionsBuffer;
	private Map<String, Relation> relationsMap;
	
	private long timer = System.nanoTime();
	private static final long FIRST_TICK = System.nanoTime();

	WikiModel wikiModel = new WikiModel("http://www.mywiki.com/wiki/${image}", "http://www.mywiki.com/wiki/${title}");
	
	
	private void tick(String message) {
		PREV_OUT.println(message + "\t\t" + ((System.nanoTime() - timer) / 10E9) + "s"
						 + (errorFlag ? "\t\t\t /!\\" : ""));
		timer = System.nanoTime();
	}

	private void initParser() {
		definitionsBuffer = new Vector<String>(8); // maximum level of foreseeable nested definitions
		relationsMap = new HashMap<String, Relation>(3);

		relationsMap.put("syn", Relation.SYNONYM);
		relationsMap.put("ant", Relation.ANTONYM);
		relationsMap.put("tropo", Relation.TROPONYM);
	}

	private void initWord(String word) {
		tick(currentWord.getTitle());
		
		currentWord = MutableWord.create(word);	//TODO: delay until language was accepted? We currently create the word immediately, even though we might not store anything from it if its language is not supported
		errorFlag = false;
		initSection();
		
		wordCount++;
		
		if ((wordCount % 100) == 0)
			PREV_OUT.println("\n\n=============================\n\t" + wordCount + " WORDS PARSED!\n=============================\n\n");
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
	
	private void log(String text) {
		System.err.println(text);
	}
	
	private void logError(String text) {
		System.err.println("**" + text + "**");
		errorFlag = true;
	}
	
	private void logSyntaxError(String text) {
		System.err.println("!! " + text);
		errorFlag = true;
	}
	
	private void out(String text) {
		System.out.print(text);
	}
	
	private void saveCurrentDefinition() {
		definitionCount++;
		currentDefinition.setPosition(definitionCount);
		currentWord.addDefinition(currentDefinition);
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
	tick(currentWord.getTitle());
	
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

%state TITLE, MEDIAWIKI, LANG, H2, NATURE, SECTION, PATTERN, PRONUNCIATION, DEFINITION, DEFINITION_DOMAIN, DEFINITION_EXAMPLE, SIMPLENYM, SPNM_CONTEXT, SPNM_WORD

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
	
	"syn"|"ant"|"tropo"
	{
		currentRelation = relationsMap.get(yytext());
		yybegin(SIMPLENYM);
	}

	.
	{
		yybegin(MEDIAWIKI);	// this is not an accepted type
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
	^"{{"
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
	
	"}}"
	{
		yybegin(SECTION);
	}

	[^|}]+|"|"
	{
		log("Unexpected pattern value: '" + yytext() + "'");
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

<DEFINITION_EXAMPLE>
{
	.+
	{
		buffer = yytext();
	}

	{newline}#+\*:
	{
		// the colon means we're in the same example, but on another line
		buffer += "\n" + yytext();
	}	

	{newline}
	{
		currentDefinition.addExample(buffer);
		yypushback(1);	// <SECTION> needs it to match
		yybegin(SECTION);
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
				result = definitionsBuffer.get(i) + (result.isEmpty() ? "" : (" " + result));

			String plainStr = wikiModel.render(new PlainTextConverter(), result);
			currentDefinition.setContent(result);

		} catch (ArrayIndexOutOfBoundsException e) {
			// this can happen in very rare cases of malformed nesting (i.e. missing a nesting level, like starting a definition list with `##`)
			logSyntaxError("Definitions nesting error in word '" + currentWord.getTitle() + "': '" + yytext() + "'. Only content at this nesting level will be stored.");

			String result = yytext();			
			String plainStr = wikiModel.render(new PlainTextConverter(), result);
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
	"-}}"
	{
		// end of pattern
	}

	(":"{optionalSpaces}|\'\'\')
	{
		yybegin(SPNM_CONTEXT);
	}

	"*"{optionalSpaces}"[["
	{
		yybegin(SPNM_WORD);
	}


	{newline}{newline}
	{
		leaveSection();
	}

	([^-:*\r\n]|"-"[^}])+|.|{newline}
	{
		// in SimpleNym: suppress output
	}
}

<SPNM_CONTEXT>
{
	([^:]+)
	{
		//TODO: context is not handled yet
	}

	":"
	{
		yybegin(SIMPLENYM);
	}

	.
	{

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
