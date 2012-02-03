package edu.unice.polytech.kis.semwiktionary.parser;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
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
	
	List<String> definitionsBuffer;
	HashMap<String, Relation> relationsMap;

	private void initParser() {
		definitionsBuffer = new ArrayList<String>(5);
		relationsMap = new HashMap<String, Relation>(2);

		relationsMap.put("syn", Relation.SYNONYM);
		relationsMap.put("ant", Relation.ANTONYM);
	}

	private void initWord(String word) {
		this.currentWord = MutableWord.create(word);
		this.definitionCount = 0;
		this.definitionsBuffer.clear();
	}
	
	private void log(String text) {
		System.err.print(text);
	}
	
	private void out(String text) {
		System.out.print(text);
	}

%}


%public
%class WikimediaDump
%type Void
%unicode
%debug

%init{

	initParser();
	yybegin(NORMAL);
%init}

word = [:letter:]+
punct = [,;:.\()/…]
whitespace = [\ ]
newline = (\r|\n|\r\n)
space = ({whitespace}|{newline})

%state NORMAL, PAGE, TITLE, MEDIAWIKI, LANG, H2, NATURE, SECTION, PATTERN, PRONUNCIATION, DEFINITION, DOMAIN, DEFINITION_BODY, EXAMPLE, SIMPLENYM, SPNM_CONTEXT, SPNM_WORD


%%


<NORMAL>
{
	"<page>"
	{
		yybegin(PAGE);
	}
	
	"<"|[^<]+
	{
		// suppress output
	}
}

<PAGE> 
{
	"<text"
	{
		yybegin(MEDIAWIKI);
	}
	
	"<title>"
	{
		yybegin(TITLE);
	}
	
	"</page>" 
	{ 
		yybegin(NORMAL); 
	}
	
	"<"|[^<]+
	{
		// suppress output
	}
}


<TITLE>
{
	[^<:]+"<"
	{
		String title = yytext();
		this.initWord(title.substring(0, title.length() - 1));
		yybegin(PAGE);
	}
	
	.
	{
		yybegin(NORMAL);
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
		yybegin(NORMAL);
	}

	.|{newline}
	{
		// suppress output
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
		yybegin(NORMAL);	// this language is not accepted
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
		System.err.println(yytext());
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
	-("|"[a-z]+)?"}}"
	{
		yybegin(SECTION);
	}
}


<SECTION>
{
	"{{"
	{
		yybegin(PATTERN);
	}

	^#+
	{
		this.definitionCount++;
		this.definitionDepth = yytext().length();
		this.currentDefinition = new Definition().setPosition(this.definitionDepth);
		
		yybegin(DEFINITION);
	}
	
	"</text>"
	{
		yybegin(NORMAL);
	}

	.|{newline}
	{
		// suppress output
	}
}


<PATTERN>
{
	"pron|"
	{
		yybegin(PRONUNCIATION);
	}
	
	"}""}"?
	{
		yybegin(SECTION);
	}

	.
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
	"{{"
	{
		yybegin(DOMAIN);
	}
	
	.
	{
		yybegin(DEFINITION_BODY);
	}
}


<DOMAIN>
{
	[^|}]+
	{
	currentDefinition.addDomain(yytext());
	}
	
	(\|[^}]*)?"}}"
	{
		yybegin(DEFINITION_BODY);
	}
}


<DEFINITION_BODY>
{
	[^*\n\r]+
	{
		this.definitionsBuffer.add(this.definitionDepth - 1, yytext());
		
		String result = "";
		for (int i = this.definitionDepth - 1; i >= 0; i--)
			result = this.definitionsBuffer.get(i) + result;
		
		currentDefinition.setContent(result);
		
		currentWord.addDefinition(currentDefinition);
	}
	
	^\*
	{
		yybegin(EXAMPLE);
	}

	{newline}{newline}
	{
		yybegin(MEDIAWIKI);
	}

	{newline}
	{
		yybegin(SECTION);
	}
}


<EXAMPLE>
{
	.+
	{
		this.currentDefinition.addExample(yytext());
	}
	
	{newline}#+\*:
	{
		// don't leave state, the colon means we're in the same example, but on another line
	}	

	{newline}{newline}
	{
		yybegin(MEDIAWIKI);
	}

	{newline}
	{
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
		currentWord.set(currentRelation, MutableWord.create(yytext()));
	}

	.
	{

	}
}


