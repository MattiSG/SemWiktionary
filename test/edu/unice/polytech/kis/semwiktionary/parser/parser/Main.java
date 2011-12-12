package edu.unice.polytech.kis.semwiktionary.parser.test;

import java.io.*;
import java.util.Iterator;

import edu.unice.polytech.kis.semwiktionary.parser.MiniwikiPage;


public class Main {
public static void main( String[] argv ) {
String dirName = null;
try {
		for ( int i = 0; i < argv.length; i++ ) {
		if ( argv[ i ].equals( "-dir" ) ) {
			i++;
			if ( i >= argv.length )
			throw new Error( "Missing directory name" );
			dirName = argv[ i ];
		}
		else {
			throw new Error("Usage: java Main -dir directory" );
		}
		}
		if ( dirName == null )
			throw new Error( "Directory not specified" );
		FileInputStream fileInputStream = new FileInputStream(
		new File( dirName, "miniwiki.xml" ) );
		System.setErr( new PrintStream( new FileOutputStream(
		new File( dirName, "program.err" ) ) ) );
		System.setOut( new PrintStream( new FileOutputStream(
		new File( dirName, "program.out" ) ) ) );
		MiniwikiPage lexer = new MiniwikiPage( fileInputStream );
		lexer.yylex();
		for (Iterator it = lexer.listTitle.iterator (); it.hasNext ();) {
			String s = (String)it.next ();
			System.out.println (s);
		}
		
		for (Iterator it = lexer.listPage.iterator (); it.hasNext ();) {
			String s = (String)it.next ();
			System.out.println (s);
		}
	}
	catch ( Exception exception ) {
		System.err.println( "Exception in Main " + exception.toString() );
		exception.printStackTrace();
	}
}
}
