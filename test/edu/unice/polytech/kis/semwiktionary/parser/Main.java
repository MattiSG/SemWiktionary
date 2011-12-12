package edu.unice.polytech.kis.semwiktionary.parser;

import java.io.*;
import java.util.Iterator;
import edu.unice.polytech.kis.semwiktionary.parser.WikimediaDump;

/**To test JFlex output, issue the following commands:
 *$ mkdir log
 *$ java -classpath bin edu.unice.polytech.kis.semwiktionary.parser.Main
 */
public class Main {
	public static void main(String[] argv) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(new File("test/resources/miniwiki.xml"));
		System.setErr( new PrintStream( new FileOutputStream(
															 new File("log/jflex.err" ) ) ) );
		System.setOut( new PrintStream( new FileOutputStream(
															 new File("log/jflex.out" ) ) ) );
		WikimediaDump lexer = new WikimediaDump( fileInputStream );
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
}
