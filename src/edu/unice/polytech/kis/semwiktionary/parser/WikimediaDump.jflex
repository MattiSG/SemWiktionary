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
	int flagExam = 0, flagChild = 0, hasChild = 0, defCount = 1;
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
		defCount=1;
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
		for(Definition def : currentWord.getDefinitions())
		{
			System.out.println(def.getPosition());
			System.out.println (currentWord.getTitle());
		 	System.out.println("Definition:\n" + def.getContent());
		 	for(String str : def.getExamples())
		 	{
				System.out.println(str);
		 	}
		}
		
		yybegin(NORMAL);
	}
}

<TITLE>
{
	{word}("_"{word})*"</title>"
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
			def.addExample(strExam);
			strExam = "";
		}
		if(strDef!="")
		{
			if(hasChild == 0)
			{	
				def = new Definition(strDef, defCount);
				currentWord.addDefinition(def);
			}
			else
				hasChild = 0;
				
			defCount++;
		}
		if(strChildDef!="")
		{
			def = new Definition(strChildDef, defCount);
			currentWord.addDefinition(def);
			strChildDef = "";
			defCount++;
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
			def = new Definition(strDef, defCount);
			currentWord.addDefinition(def);
			strDef = "";
			defCount++;
		}
		flagExam = 1;
		append("Ex(" + strTitle + "):");
		yybegin(FIRSTWORD);
	}
	"##"
	{
		if(strExam!="")
		{
			def.addExample(strExam);
			strExam = "";
		}
		if(strChildDef!="")
		{
			def = new Definition(strChildDef, defCount);
			currentWord.addDefinition(def);
			strChildDef = "";
			defCount++;
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
			def = new Definition(strChildDef, defCount);
			currentWord.addDefinition(def);
			strChildDef = "";
			defCount++;
		}
		flagExam = 1;
		append("Ex(" + strTitle + "):");
		yybegin(FIRSTWORD);
	}
	"{{-"
	{
		if(strExam!="")
		{
			def.addExample(strExam);
			strExam = "";
		}
		if(strDef!="")
		{
			def = new Definition(strDef, defCount);
			currentWord.addDefinition(def);
			strDef = "";
			defCount++;
		}
		if(strChildDef!="")
		{
			def = new Definition(strChildDef, defCount);
			currentWord.addDefinition(def);
			strChildDef = "";
			defCount++;
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
