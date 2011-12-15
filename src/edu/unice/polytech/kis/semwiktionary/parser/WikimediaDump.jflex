package edu.unice.polytech.kis.semwiktionary.parser;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import edu.unice.polytech.kis.semwiktionary.model.*;


%%


%{
	List<String> listExample = new ArrayList<String>();
	MutableWord currentWord;
	Definition def;
	ListIterator li;
	int flagExam = 0, flagChild = 0, hasChild = 0;
	String	strDef, strTitle, strExam, strChildDef;
	void append(String s) 
	{
		if(flagExam == 0)
		{
			if(flagChild == 0)
				strDef += s;
			else
				strChildDef += s;
		}
		else
			strExam += s;
		
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
otherPunct = [\,\;\:\.\(\)\/\…]
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
	"{{-nom-|fr}}" | "{{-adj-|fr}}" | "{{-verb-|fr}}"
	{
		strDef = "";
		strChildDef = "";
		strExam = "";
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
{	/* To test by display all the words together definitions */
	.
	{
		int count = 1;
		for(Definition def : currentWord.getDefinitions())
		{
			System.out.println(count++);
			System.out.println (currentWord.getTitle());
		 	System.out.println("Definition:\n" + def.getDefinition());
		}
		for(String exam : listExample)
		{
			System.out.println(exam);
		}
		listExample.clear();
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
	{word}[\:]{word}"</title>"
	{
		yybegin(NORMAL);
	}	
	.
	{
		yybegin(NORMAL);
	}
}

<DEFINITION> 
{
	"#"
	{
		if(strExam!="")
		{
			listExample.add(strExam);
			strExam = "";
		}
		if(strDef!="")
		{
			if(hasChild == 0)
			{	
				def = new Definition(strDef);
				currentWord.addDefinition(def);
			}
			else
				hasChild = 0;
		}
		if(strChildDef!="")
		{
			def = new Definition(strChildDef);
			currentWord.addDefinition(def);
			strChildDef = "";
		}
		strDef = "";
		flagExam = 0;
		flagChild = 0;
		append("* ");
		yybegin(FIRSTWORD);
	}
	"#*"
	{
		if(strDef!="")
		{
			def = new Definition(strDef);
			currentWord.addDefinition(def);
			strDef = "";
		}
		flagExam = 1;
		append("Ex(" + strTitle + "):");
		yybegin(FIRSTWORD);
	}
	"##"
	{
		if(strExam!="")
		{
			listExample.add(strExam);
			strExam = "";
		}
		if(strChildDef!="")
		{
			def = new Definition(strChildDef);
			currentWord.addDefinition(def);
			strChildDef = "";
		}
		strChildDef = strDef;
		flagExam = 0;
		flagChild = 1;
		hasChild = 1;
		append("**");
		yybegin(FIRSTWORD);
	}
	"##*"
	{
		if(strChildDef!="")
		{
			def = new Definition(strChildDef);
			currentWord.addDefinition(def);
			strChildDef = "";
		}
		flagExam = 1;
		append("Ex(" + strTitle + "):");
		yybegin(FIRSTWORD);
	}
	"{{-"
	{
		if(strExam!="")
		{
			listExample.add(strExam);
			strExam = "";
		}
		if(strDef!="")
		{
			def = new Definition(strDef);
			currentWord.addDefinition(def);
			strDef = "";
		}
		if(strChildDef!="")
		{
			def = new Definition(strChildDef);
			currentWord.addDefinition(def);
			strChildDef = "";
		}
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
