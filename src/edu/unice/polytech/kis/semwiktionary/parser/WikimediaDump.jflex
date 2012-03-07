package edu.unice.polytech.kis.semwiktionary.parser;

import java.io.*;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

import edu.unice.polytech.kis.semwiktionary.model.*;
import edu.unice.polytech.kis.semwiktionary.database.Relation;
import edu.unice.polytech.kis.semwiktionary.database.LazyPatternsManager;

import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;

%%


%{
	public static final String  OUTPUT_FILE = "log/parser-output.txt",
								ERROR_FILE = "log/parser-error.txt";
								
	public long wordCount = 0,
				modelCount = 0;

	private final PrintStream PREV_OUT = System.out,
							  PREV_ERR = System.err;
							  
	private final static int LOG_FREQUENCY = 100; // a count message will be output to the console on every multiple of this frequency
	
	private Stack<Integer> statesStack = new Stack<Integer>();
	private NodeMappedObject currentNMO;
	private Relation currentRelation;
	
	/**@name	Flags
	*These flags are used to give parsing information for a word to the user. They are reset between each word
	*/
	//@{
	private boolean errorFlag, // a parsing error happened
					syntaxErrorFlag; // the MediaWiki text was malformed
	//@}

	private Definition currentDefinition;
	private int definitionCount = 0;
	private int definitionDepth = -1; // the depth is the number of sharps (#) in front of a definition, minus one (that's optimization to have only one substraction for the 0-based indexed list). So, to trigger comparisons, we need to be negative.
	
	private String buffer = ""; // an all-purpose buffer, to be initialized by groups that need it
	
	private Vector<String> definitionsBuffer;
	private Map<String, Relation> relationsMap;

	// the params are URLs patterns to be used by WikiModel to generate hyperlinks when converting to HTML
	// they are completely useless here, since we only use WikiModel to convert to plaintext, removing links altogether. WikiModel does not offer a default constructor though, so we'll put a meaningful URL.
	private WikiModel wikiModel = new WikiModel("http://fr.wiktionary.org/${image}",
												"http://fr.wiktionary.org/wiki/${title}");
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
	
	/** Pushes the current state to the states stack.
	*/
	private void yypushstate() {
		yypushstate(yystate());
	}
	
	/** Pushes the given state to the states stack.
	*/
	private void yypushstate(int state) {
		statesStack.push(state);
	}
	
	/** Switches the current state to the last one from the states stack.
	* If the stack is empty, the state defaults to <XML>, to avoid unexitable states.
	*/
	private void yypopstate() {
		try {
			yybegin(statesStack.pop());
		} catch (java.util.EmptyStackException e) {
			logError("Unbalanced states! Popping state threw an EmptyStackException. Skipping rest of the word.");
			yybegin(XML);
		}
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
		
		currentNMO = MutableWord.obtain(word);	//TODO: delay until language was accepted? We currently create the word immediately, even though we might not store anything from it if its language is not supported
		resetFlags();
		
		initSection();
		
		wordCount++;
		
		if ((wordCount % LOG_FREQUENCY) == 0)
			PREV_ERR.println("\t\t\t\t\t\t\t" + wordCount + " WORDS PARSED!");
	}
	
	private void initModel(String pattern) {
		logWord();
		
		PREV_OUT.print("Modèle:" + pattern);
		
		currentNMO = MutableLexicalCategory.obtain(pattern);
		
		modelCount++;
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
		
		yybegin(CONTENT_WORD);
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
		((MutableWord) currentNMO).addDefinition(currentDefinition);
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
	PREV_ERR.println("Parsed words:  " + wordCount);
	PREV_ERR.println("Parsed models: " + modelCount);

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


// All states declarations are on their own line to minimize conflicts.

// These states are exclusive, i.e. they may match only with patterns namespaced by them.
//@{
// outermost state
%xstate XML

// in a <page> element of the XML, i.e. an entry in the dictionary
%xstate PAGE

// if a section block is considered useless, we'll switch to this trash state to safely ignore everything
%xstate TRASH
//@}

// These states are inclusive, i.e. they may match with non-state-specific patterns.
//@{
// <title> of a <page>, considered as a word
%state TITLE

// <content> node of a <page>, considered as a word entry
%state CONTENT_WORD

// <content> node of a <page>, considered as a pattern definition entry
%state CONTENT_MODEL

%state LANG

// a third-level header
%state H3

// a third-level header with no "well-known" pattern
%state H3_UNKNOWN

%state SECTION

// any "{{" pattern (template opening)
%state PATTERN

// an {{fchim}} pattern
%state FCHIM_PATTERN

%state PRONUNCIATION
%state DEFINITION
%state DEFINITION_DOMAIN
%state DEFINITION_EXAMPLE
%state SIMPLENYM
%state SPNM_CONTEXT
%state SPNM_WORD

// <title> node of a "Modèle:-***-" page, describing a pattern
%state MODEL

// <content> node of a "Modèle:-***-" page, describing a pattern
%state MODEL_CONTENT

// inside a pattern description, a "{{-déf-|" has been matched, so the model is one of a lexical category
%state LEXICAL_CATEGORY

// inside a {{source}} pattern (origin of a quotation)
%state SOURCE
//@}

%%


"</page>"
{
	// fallback for all cases
	logSyntaxError("Out of page, error on word '" + currentNMO + "'");
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
	"<title>"
	{
		yybegin(TITLE); // this state is reponsible for filling the states stack with the destination state, depending on the type of page determined from the title's pattern
	}
	
	"<text xml:space=\"preserve\">"
	{
		yypopstate();	// the <TITLE> state has filled the stack with the proper state
	}
	
	"</page>" 
	{ 
		yybegin(XML);
	}
	
	([^<]|"<"[^t/]|"<t"[^ei]|"<ti"[^t]|"</"[^p])+
	{
		// everything other than the nodes specified above is useless and must be thrown away
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
		
		yypushstate(CONTENT_WORD); // so that <PAGE> redirects to handling a word
		
		yybegin(PAGE);
	}
	
	 
	"Modèle:-"[^<]+
	{ // for example: "-adj-" describes the "adjective" model. We enter this state with the leading dash removed
		initModel(yytext().substring(8)); // 7 == "Modèle:".length

		yypushstate(CONTENT_MODEL); // so that <PAGE> redirects to handling a model

		yybegin(PAGE);
	}
	
	.
	{
		// if it is anything else than expected, then it is not a page we're interested in, we can skip it immediately
		yybegin(XML);
	}
}

<CONTENT_MODEL>
{	
	"{{-déf-|"
	{
		// in MODEL_CONTENT: matched -déf-, so that is a lexical category
		yybegin(LEXICAL_CATEGORY);
	}
	
	[^{<]+
	{
		// in MODEL_CONTENT: suppress output.
		// some models have markings. For example: "&lt;includeonly&gt;"
	}
	
	.
	{
		// this is not a known pattern type, so we throw it away
		yybegin(XML);
	}
}

<LEXICAL_CATEGORY>
{
	[^|]+
	{
		((MutableLexicalCategory) currentNMO).setDescription(yytext());
		
		LexicalCategory newCategory = (LexicalCategory) currentNMO;
		LazyPatternsManager.transferAll(newCategory.getPattern(), newCategory, Relation.LEXICAL_CATEGORY);
	}
	
	.
	{
		// everything else than the title of the lexical category is useless
		yybegin(XML);
	}
}

<CONTENT_WORD>
{
	"== {{="
	{
		yybegin(LANG);
	}
	
	"{{-"
	{
		yybegin(H3);
	}
	
	"</text>"
	{
		yybegin(XML);
	}

	([^={<]|"="[^=]|"=="[^ ]|"== "[^{]|"== {{-"|"{"[^{]|"{{"[^-])+|.
	{
		// "== {{-" is for "== {{-car-}} =="
		// in CONTENT_WORD: suppress output
	}
}

<LANG>
{
	"fr="
	{
		currentNMO.set("lang", "fr");
		yybegin(CONTENT_WORD);
	}
	
	"conv="
	{
		currentNMO.set("lang", "conv");
		yybegin(CONTENT_WORD);
	}
	
	.
	{
		yybegin(XML);	// this language is not accepted
	}
}


<H3>
{	
	("syn"|"ant"|"tropo")"-}}"{newline}
	{
		buffer = yytext();
		currentRelation = relationsMap.get(buffer.substring(0, buffer.length() - 4));
		yybegin(SIMPLENYM);
	}

	.
	{
		yypushback(1);
		yybegin(H3_UNKNOWN);	// this is not a "well-known" value
	}
}

<H3_UNKNOWN>
{ // we can't put this directly into <H3> because of the "longest match" rule
	[^|}]+
	{
		String pattern = "-" + yytext();

		LexicalCategory category = LexicalCategory.find(pattern);
		
		if (category == null)
			LazyPatternsManager.register(pattern, currentNMO);
		else
			((MutableWord) currentNMO).addLexicalCategory(category);
		
		yybegin(SECTION);
	}
	
	.
	{
		yybegin(SECTION);
	}
}

<SECTION>
{ // an entrance into that state with a non-consumed newline will switch to <CONTENT_WORD>
	"{{"
	{
		yypushstate();
		yybegin(PATTERN);
	}

	{newline}#+{optionalSpaces}
	{
		int newDepth = yytext().trim().length() - 1;
		
		if (newDepth <= definitionDepth)
			saveCurrentDefinition();	// we don't want to add definitions that will be specified by inner lists, since we're concatenating the innermost strings with their parents
			
		definitionDepth = newDepth;
		
		currentDefinition = new Definition();
		
		buffer = "";
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
	
	"source|"
	{
		buffer += "— (";
		yybegin(SOURCE);
	}

	"fchim"
	{
		yybegin(FCHIM_PATTERN);
	}
	
	"}}"
	{
		yypopstate();
	}
	
	"}"
	{
		logSyntaxError("Unbalanced bracket in pattern in '" + currentNMO + "'");
		yypopstate();
	}
	
	[^|}]+
	{
		logError("Unexpected pattern value: '" + yytext() + "'");
	}
	
	"|"
	{
		// in PATTERN: ignore pipes (parameter separators)
	}
}


<PRONUNCIATION>
{
	[^}|]+
	{
		currentNMO.set("pronunciation", yytext());
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
		logSyntaxError("Single bracket in definition domain in '" + currentNMO + "'");
		yybegin(DEFINITION);
	}
}

<FCHIM_PATTERN>
{ // chemical formulas. See http://fr.wiktionary.org/wiki/Modèle:fchim
	"}}"
	{
		yypopstate();
	}

	[^\|}]+
	{
		buffer += yytext();
	}
	
	"|"("lien="[|}]+)?
	{
		// pipes are used as separators for element symbols and their indices, which are all rendered the same in a text-only representation. Therefore, we simply ignore pipes.
		// example: H|2|O => H2O

		// chemical formulas may also specify a link to a complete page with "lien=<link>". We also want to remove this information.
	}
}

<DEFINITION_EXAMPLE>
{
	"{{"
	{
		yypushstate();
		yybegin(PATTERN);
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
		buffer = convertToPlainText(buffer).trim();	// convert before testing for emptiness: `''` is not empty, but the plaintext equivalent is ""
		if (! buffer.isEmpty())
			currentDefinition.addExample(buffer);
		
		yypushback(1);	// <SECTION> needs it to match
		yybegin(SECTION);
	}
}

<SOURCE>
{ // source of a quote. May only be at the end of a line.
	"{{"|"w|"|"}}"
	{
		// in SOURCE: suppress output
	}

	([^\r\n}{w]|"}"[^}]|"{"[^{]|"w"[^\|])+
	{
		buffer += yytext();
	}

	{newline}
	{
		buffer += ")";
		yypushback(1);	// <DEFINITION_EXAMPLE> needs it to match
		yybegin(DEFINITION_EXAMPLE);
	}
}


<DEFINITION>
{
	([^\r\n{]|"{"[^{])+
	{
		buffer += yytext();
	}
	
	"{{"
	{
		yybegin(DEFINITION_DOMAIN);
	}
	
	"{{fchim"
	{ //TODO: this should be generalized to "{{" -> PATTERN, and PATTERN contain all DEFINITION_DOMAIN triggers
		yypushstate();
		yybegin(FCHIM_PATTERN);
	}
	
	{newline}
	{
		String localDefinitionContent = convertToPlainText(buffer).trim();
		try {
			definitionsBuffer.add(definitionDepth, localDefinitionContent);

			String result = "";
			for (int i = definitionDepth; i >= 0; i--)
				result = definitionsBuffer.get(i) + (result.isEmpty() ? "" : (" " + result));

			currentDefinition.setContent(result);

		} catch (ArrayIndexOutOfBoundsException e) {
			// this can happen in cases of malformed nesting (i.e. missing a nesting level, like starting a definition list with `##`)
			logSyntaxError("Definitions nesting error in word '" + currentNMO + "': '" + localDefinitionContent + "'. Only content at this nesting level will be stored.");

			currentDefinition.setContent(localDefinitionContent); // best recovery we can do: forget about concatenation
		}
		
		yypushback(1);	// SECTION needs it to match
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
			currentNMO.set(currentRelation, MutableWord.obtain(yytext()));
		} catch (Exception e) {
			logError("Oh no! Got an exception while trying to add relation " + currentRelation + " to '" + yytext() + "' from word '" + currentNMO + "'  :( ");
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
		yybegin(CONTENT_WORD);
	}
}
