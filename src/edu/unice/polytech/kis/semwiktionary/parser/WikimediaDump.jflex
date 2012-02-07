package edu.unice.polytech.kis.semwiktionary.parser;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

import edu.unice.polytech.kis.semwiktionary.model.*;
import edu.unice.polytech.kis.semwiktionary.database.Relation;


%%


%{
	public static final String  OUTPUT_FILE = "log/parser-output.txt",
								ERROR_FILE = "log/parser-error.txt";

	private final PrintStream PREV_OUT = System.out,
							  PREV_ERR = System.err;

	private MutableWord currentWord = new MutableWord(new Word("Init & prolog")); // init for logging purposes
	private Relation currentRelation;

	private Definition currentDefinition;
	private int definitionCount;
	private int definitionDepth;
	
	private String buffer = ""; // an all-purpose buffer, to be initialized by groups that need it
	
	private List<String> definitionsBuffer;
	private HashMap<String, Relation> relationsMap;
	
	private long timer = System.nanoTime();
	private static final long FIRST_TICK = System.nanoTime();
	
	
	private void tick(String message) {
		PREV_ERR.println(message + ": " + ((System.nanoTime() - timer) / 10E9) + " s");
		timer = System.nanoTime();
	}

	private void initParser() {
		definitionsBuffer = new LinkedList<String>();
		relationsMap = new HashMap<String, Relation>(2);

		relationsMap.put("syn", Relation.SYNONYM);
		relationsMap.put("ant", Relation.ANTONYM);
	}

	private void initWord(String word) {
		tick(currentWord.toString());
		
		currentWord = MutableWord.create(word);
		initSection();
	}
	
	private void initSection() {
		if (definitionDepth > 0)
			saveCurrentDefinition(); // we matched some definitions, so the last one must be saved, since saving them is done when finding a new one

		definitionDepth = 0;
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
%init}

%eof{
	PREV_ERR.println("Total time: " + ((System.nanoTime() - FIRST_TICK) / 10E9) + "s");

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

%xstate PAGE

%%


"</page>"
{
	// fallback for all cases
	log("**Out of page, error on word '" + currentWord.getTitle() + "'**");
	yybegin(YYINITIAL);
}


<YYINITIAL>
{
	"<page>"
	{
		yybegin(PAGE);
	}
	
	"<"|[^<]+
	{
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
		yybegin(YYINITIAL);
	}
	
	"<"|[^<]+
	{
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
		yybegin(YYINITIAL);
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
		yybegin(YYINITIAL);
	}

	.|{newline}
	{
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
		yybegin(YYINITIAL);	// this language is not accepted
	}
}


<H2>
{
	"verb"|"nom"|"adj"
	{
		// TODO: add all types
		// TODO: store type in word
		yybegin(NATURE);
	}
	
	"syn"|"ant"
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
		int newDepth = yytext().length();
		
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
		yybegin(YYINITIAL);
	}
	
	{newline}{newline}
	{
		leaveSection();
	}

	.|{newline}
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
		log("**unexpected pattern value!** [ " + yytext() + " ]");
	}
}


<PRONUNCIATION>
{
	[^}|]+
	{
		currentWord.set("pronunciation", yytext());
	}
	
	"|"|"}}"
	{
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
	[^{]+
	{
		definitionsBuffer.add(yytext());
		
		String result = "";
		for (int i = definitionsBuffer.size() - 1; i >= 0; i--)
			result = definitionsBuffer.get(i) + (result.isEmpty() ? "" : (" " + result));
		
		currentDefinition.setContent(result);
		
		yybegin(SECTION);
	}
	
	"{{"
	{
		yybegin(DEFINITION_DOMAIN);
	}
}

<SIMPLENYM>
{
	"-}}"
	{
		// end of pattern
	}

	":"{optionalSpaces}
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

	.|{newline}
	{

	}
}

<SPNM_CONTEXT>
{
	[^:]+
	{
		// Context is not handled yet
	}

	":"
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

	[^\]]+
	{
		try {
			currentWord.set(currentRelation, MutableWord.from(yytext()));
		} catch (IllegalArgumentException e) {
			log("**?? Got a null pointer while trying to add synonym '" + yytext() + "' to word '" + currentWord.getTitle() + "'  :( **");
			e.printStackTrace(System.err);
		}
	}
}
