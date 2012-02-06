package edu.unice.polytech.kis.semwiktionary.parser;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

import edu.unice.polytech.kis.semwiktionary.model.*;
import edu.unice.polytech.kis.semwiktionary.database.Relation;


%%


%{
	MutableWord currentWord;
	Relation currentRelation;

	Definition currentDefinition;
	int definitionCount;
	int definitionDepth;
	
	String buffer = ""; // an all-purpose buffer, to be initialized by groups that need it
	
	List<String> definitionsBuffer;
	HashMap<String, Relation> relationsMap;

	private void initParser() {
		definitionsBuffer = new LinkedList<String>();
		relationsMap = new HashMap<String, Relation>(2);

		relationsMap.put("syn", Relation.SYNONYM);
		relationsMap.put("ant", Relation.ANTONYM);
	}

	private void initWord(String word) {
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
		System.err.print(text);
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
	initParser();
%init}

word = ([:letter:]+)
punct = [,;:.\()/…]
whitespace = [\ \t]
newline = (\r|\n|\r\n)
optionalSpaces = ({whitespace}*)
space = ({whitespace}|{newline})

%state PAGE, TITLE, MEDIAWIKI, LANG, H2, NATURE, SECTION, PATTERN, PRONUNCIATION, DEFINITION, DEFINITION_DOMAIN, DEFINITION_BODY, DEFINITION_EXAMPLE, SIMPLENYM, SPNM_CONTEXT, SPNM_WORD

%%


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
	-("|"{word})?"}}"
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

	{newline}#+
	{
		int newDepth = yytext().length();
		
		if (newDepth <= definitionDepth)
			saveCurrentDefinition();	// we don't want to add definitions that will be specified by inner lists, since we're concatenating the innermost strings with their parents
			
		definitionDepth = newDepth;
		
		currentDefinition = new Definition();
		
		yybegin(DEFINITION);
	}
	
	{newline}#+"*"{optionalSpaces}
	{
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
		log("**unexpected pattern value!** [ " + yytext() + " ]\n");
	}
}


<PRONUNCIATION>
{
	[^}|]+
	{
		currentWord.set("pronunciation", yytext());
	}
	
	[|}]
	{
		yybegin(PATTERN);
	}
}


<DEFINITION> 
{
	{optionalSpaces}"{{"
	{
		yybegin(DEFINITION_DOMAIN);
	}
	
	{optionalSpaces}
	{
		yybegin(DEFINITION_BODY);
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
		yybegin(DEFINITION_BODY);
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


<DEFINITION_BODY>
{
	.+
	{
		definitionsBuffer.add(yytext());
		
		String result = "";
		for (int i = definitionsBuffer.size() - 1; i >= 0; i--)
			result = definitionsBuffer.get(i) + (result.isEmpty() ? "" : (" " + result));
		
		currentDefinition.setContent(result);
		
		yybegin(SECTION);
	}
}

<SIMPLENYM>
{
	"-}}"
	{
		// end of pattern
	}

	:{whitespace}''
	{
		yybegin(SPNM_CONTEXT);
	}

	\*{whitespace}"[["
	{
		yybegin(SPNM_WORD);
	}

	^{newline}"{{-"
	{
		//TODO: WTF?! This should redirect to <SECTION>
		yybegin(H2);
	}

	.|{newline}
	{

	}
}

<SPNM_CONTEXT>
{
	"[["
	{
		yybegin(SPNM_WORD);
	}

	[^:]+
	{
		// Context is not handled yet
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

	[^\]]+
	{
		currentWord.set(currentRelation, MutableWord.from(yytext()));
	}

	.
	{

	}
}
