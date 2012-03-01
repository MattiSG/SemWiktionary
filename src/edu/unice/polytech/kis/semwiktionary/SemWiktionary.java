/**SemWiktionary
*Java API to access data from [wiktionary](http://fr.wiktionary.org). Specific target is the French wiktionary.
*
*@author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
*@author	[Fabien Brossier](http://fabienbrossier.fr)
*@author	Ngoc Nguyen Thinh Dong
*/

package edu.unice.polytech.kis.semwiktionary;


import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;

import org.neo4j.graphdb.Node;

import edu.unice.polytech.kis.semwiktionary.parser.WikimediaDump;
import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;
import edu.unice.polytech.kis.semwiktionary.database.Relation;
import edu.unice.polytech.kis.semwiktionary.database.Database;


public class SemWiktionary {

	public static void print(String message) {
		System.out.print(message);
	}
	
	public static void println(String message) {
		System.out.println(message);
	}
	
	private static void load(String path) {
		try {
			System.err.println("Loading file '" + path + "'…");
			new WikimediaDump(new FileInputStream(new File(path))).yylex();
		} catch (java.io.FileNotFoundException e) {
			System.err.println("File '" + path + "' was not found!");
			System.exit(1);
		} catch (java.io.IOException e) {
			System.err.println("File '" + path + "' could not be read properly  :(");
			throw new RuntimeException(e);
		}
		
		System.err.println("…done!");
	}
	
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].equals("--load")) {
				load(args[1]);
				System.exit(0);
			} else if (args[0].equals("--count")) {
				println(count() + " words in database");
				System.exit(0);
			}
			
			for (String arg: args) {
				lookup(arg);
				println("\n");
			}
			return;
		}
			
	
		println("Welcome to the SemWiktionary lookup interface!\n" +
				"===================================================");
		println("Hit ctrl-C to exit.\n");
		
		while (true) {
			println("\nEnter a word and press enter or ctrl-D to look it up: ");
			
			Scanner sc = new Scanner(System.in);
			String lookedup = sc.next();
			
			lookup(lookedup);
		}
	}
	
	public static long count() {
		print( "Counting… " );
		
		long start = System.nanoTime(),
			 count = 0;
		
		for (Node word : Database.getIndexForName(Word.INDEX_KEY).query(Word.INDEX_KEY, "*")) {
			count++;
			if ((count % 10000) == 0)
				print(".");
		}
		
		println(" (took " + ((System.nanoTime() - start) / 10E9) + "s)");
		return count;
	}
	
	public static void lookup(String input) {
		Word word = Word.from(input);
		
		if (word == null) {
			println("*The word '" + input + "' was not found in the database!*");
			return;
		}
		
		println('"' + word.getTitle() + '"');
		println("--------------------");
		
		for (Word syn : word.getSynonyms())
			println("= " + syn.getTitle());
		
		println("--------------------");
		
		for (Word ant : word.getAntonyms())
			println("≠ " + ant.getTitle());
		
		println("--------------------");

		for (Word tro : word.getTroponyms())
			println("$ " + tro.getTitle());
		
		println("--------------------");
		
		for (Definition def : word.getDefinitions()) {
			println(def.getPosition() + ". \"" + def.getContent() + "\"");

			for (String dom : def.getDomains())
				if (! dom.isEmpty())
					println("(" + dom + ") ");
			
			for (String ex : def.getExamples())
				if (! ex.isEmpty())
					println("\t• \"" + ex + "\"");
			
			println("\t---");
		}
	}
}
