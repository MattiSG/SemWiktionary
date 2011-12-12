package edu.unice.polytech.kis.semwiktionary.parser;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
%%
%{
	List<String> listPage = new ArrayList<String>();
	List<String> listTitle = new ArrayList<String>();
	String	str, strTitle;
	void append( String s ) {
		str += s;
	}

%}
%public
%class MiniwikiPage
%type Void
%unicode

%init{
	yybegin( NORMAL );
%init}

letter = [A-Za-zéàèùâêîôûëïü]
word = {letter}+
space = [\ \t\r\n]
newline = (\r|\n|\r\n)

%state NORMAL, PAGE
%%
<NORMAL> {
	"<page>"
		{
			str = "";
			append ( yytext() );			
			yybegin( PAGE );
		}
	{newline} {}
	.	{}
	}
<PAGE> {
	"</page>"
	{
		append ( yytext() );
		listPage.add(str);
		yybegin( NORMAL );
	}
	"<title>"~"</title>" {
		strTitle = yytext().substring(7, yytext().length()-8);
		listTitle.add( strTitle );
	}
	{newline} {
		append ("\r\n");
	}
	.	{ 
			append ( yytext() );
		}

}
{space} {}
