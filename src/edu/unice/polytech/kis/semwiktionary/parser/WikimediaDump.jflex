package edu.unice.polytech.kis.semwiktionary.parser;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import edu.unice.polytech.kis.semwiktionary.model.*;


%%


%{
	MutableWord currentWord;
	Definition def;
	ListIterator li;
	String	strDef, strTitle;
	void append(String s) 
	{
		strDef += s;
	}

%}


%public
%class WikimediaDump
%type Void
%unicode

%init{
	yybegin(NORMAL);
%init}

letter = [A-ZÀÉÈÂÊÔÙa-zéàèùâêîôûëïüçœ]
word = {letter}+
otherPunct = [\,\;\:\.\(\)\…]
space = [\ \t\r\n]
whitespace = [\ ]
newline = (\r|\n|\r\n)

%state NORMAL, PAGE, ENDPAGE, TITLE, DEFINITION, FIRSTWORD, NEXTWORD


%%


<NORMAL> 
{
	"<page>"
	{			
		yybegin(PAGE);
	}
	{newline} 
	{
	}
	.	
	{
	}
}

<PAGE> 
{
	"{{-nom-|fr}}" | "{{-adj-|fr}}" | "{{-verb-|fr}}" | "{{-nom-|fro}}"
	{
		strDef = "";	
		yybegin(DEFINITION);
	}
	"<title>"
	{
		strTitle = "";
		yybegin(TITLE);
	}
	"</page>" 
	{ 
		yybegin(ENDPAGE); 
	}
	{newline} 
	{
	}
	.	
	{
	}
}

<ENDPAGE>
{	/* To test by display all the words together defines */
	.
	{
		for(Definition def : currentWord.getDefinitions())
		{
			System.out.println (currentWord.getTitle());
		 	System.out.println("Definition:\n" + def.getDefinition());
		}
		yybegin(NORMAL);
	}
}

<TITLE>
{
	{word}"</title>"
	{
		strTitle = yytext().substring(0, yytext().length()-8);
		currentWord = MutableWord.create(strTitle);
		yybegin(PAGE);
	}
	.
	{
		yybegin(ENDPAGE);
	}
}

<DEFINITION> 
{
	"#"
	{
		if(strDef!="")
		{
			def = new Definition(strDef);
			currentWord.addDefinition(def);
			strDef = "";
		}
		append("* ");
		yybegin(FIRSTWORD);
	}
	"#*"
	{
		append("Ex: ");
		yybegin(FIRSTWORD);
	}
	"##"
	{
		append("**");
		yybegin(FIRSTWORD);
	}
	"##*"
	{
		append("Ex: ");
		yybegin(FIRSTWORD);
	}
	"{{-"
	{
		def = new Definition(strDef);
		currentWord.addDefinition(def);
		yypushback(3);
		yybegin(PAGE);
	}
	{newline} 
	{
	}
	.	
	{
	}
}

<FIRSTWORD> 
{
	"{{"~"}}"
	{
	}
	"[["~\|{word}"]]"
	{
		append(yytext().substring(yytext().indexOf("|")+1, yytext().length()-2));
		yybegin(NEXTWORD);
	}
	{word} 
	{
		append( " " + yytext() );
		yybegin(NEXTWORD);
	}
	{word}{whitespace}"{{"~"}}"
	{
	}
	{letter}(\'|\’){word}
	{
		append(yytext());
		yybegin(NEXTWORD);
	}
	{whitespace}{letter}(\'|\’)"[["{word}
	{
		append(yytext().substring(0,3) + yytext().substring(5));
		yybegin(NEXTWORD);
	}
	{newline} 
	{
		append("\n");
		yybegin(DEFINITION);
	}
	. 
	{
	}
}

<NEXTWORD> 
{
	{word} 
	{
		append(" "+yytext());
	}
	{whitespace}{word}(\'|\’){word}
	{
		append(yytext());
	}
	{whitespace}{letter}(\'|\’)|{whitespace}{letter}(\'|\’){word}
	{
		append( yytext() );
	}
	{whitespace}{letter}(\'|\’)"[["{word}
	{
		append(yytext().substring(0,3) + yytext().substring(5));
	}	
	"[["{word}"]]"({word})+
	{
		append(" " + yytext().substring(2, yytext().indexOf("]]")) + yytext().substring(yytext().indexOf("]]")+2));
	}
	"[["{word}\|{word}"]]"
	{
		append(" " + yytext().substring(yytext().indexOf("|")+1, yytext().length()-2));
	}
	"{{source"~{newline} 
	{
		append("\n");
		yybegin(DEFINITION);
	}
	{whitespace}{letter}(\'|\’)"'''"{word}"'''"
	{
		append(yytext().substring(0,3) + yytext().substring(6, yytext().length()-3));
	}
	"&lt;br&gt;"
	{
		append("\n");
	}
	"&lt;"~"&gt;"
	{
	}
	{otherPunct}
	{
		append(yytext());
	}
	{newline} 
	{
		append("\n");
		yybegin(DEFINITION);
	}
	
	.
	{
	}
}

{space} 
{
}
