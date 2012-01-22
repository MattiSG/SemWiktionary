package edu.unice.polytech.kis.semwiktionary.parser;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import edu.unice.polytech.kis.semwiktionary.model.*;


%%


%{
	MutableWord currentWord;
	
	Definition currentDefinition;
	int definitionCount;
	int definitionDepth;
	
	List<String> definitionsBuffer = new ArrayList<String>(5);
	
	
	
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
	yybegin(NORMAL);
%init}

word = [:letter:]+
punct = [,;:.\()/…]
whitespace = [\ ]
newline = (\r|\n|\r\n)
space = ({whitespace}|{newline})

%state NORMAL, PAGE, TITLE, MEDIAWIKI, LANG, H2, NATURE, SECTION, PATTERN, PRONUNCIATION, DEFINITION, DOMAIN, DEFINITION_BODY, EXAMPLE


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
	[^<:]+
	{
		this.initWord(yytext());
	}
	
	"<"
	{
		yybegin(PAGE);
	}
	
	:
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
	verb|nom|adj
	{
		// TODO: add all types
		// TODO: store type in word
		yybegin(NATURE);
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
	
	{newline}
	{
		yybegin(SECTION);
	}
}



