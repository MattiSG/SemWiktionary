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
otherPunct = [\,\;\:\.]
space = [\ \t\r\n]
whitespace = [\ ]
newline = (\r|\n|\r\n)

%state NORMAL, PAGE, ENDPAGE, DEFINITION, FIRSTWORD, NEXTWORD


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
	"{{-nom-|fr}}" | "{{-adj-|fr}}" | "{{-verb-|fr}}"
	{
		strDef = "";	
		yybegin(DEFINITION);
	}
	"<title>"~"</title>" 
	{
		strTitle = yytext().substring(7, yytext().length()-8);
		currentWord = MutableWord.create(strTitle);
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
{	/* Call the methods to create the Database here */
	.
	{
		li = currentWord.getDefinitions().listIterator(0);
		while (li.hasNext())
		{
			System.out.println (currentWord.getName());
		 	System.out.println("Definition:\n" + ((Definition)li.next()).getDefinition());
		}
		yybegin(NORMAL);
	}
}

<DEFINITION> 
{
	#
	{
		append("*");
		yybegin(FIRSTWORD);
	}
	"#*" 
	{
		append("Ex: ");
		yybegin(FIRSTWORD);
	}
	## 	
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
	"[["{word}\|{word}"]]"
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
		append( yytext() );
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
		append( yytext() );
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
	"{{source"~{newline} 
	{
		append("\n");
		yybegin(DEFINITION);
	}
	{whitespace}{letter}(\'|\’)"'''"{word}"'''"
	{
		append(yytext().substring(0,3) + yytext().substring(6, yytext().length()-3));
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

