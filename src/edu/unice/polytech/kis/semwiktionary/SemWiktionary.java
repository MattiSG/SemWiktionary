/**SemWiktionary
*Java API to access data from [wiktionary](http://fr.wiktionary.org). Specific target is the French wiktionary.
*
*@author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
*@author	[Fabien Brossier](http://fabienbrossier.fr)
*@author	Ngoc Nguyen Thinh Dong
*/

package edu.unice.polytech.kis.semwiktionary;


import java.util.Scanner;

import org.neo4j.graphdb.Node;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;
import edu.unice.polytech.kis.semwiktionary.database.Relation;
import edu.unice.polytech.kis.semwiktionary.database.Database;


public class SemWiktionary {
    private static final int MAX_COUNT_TIME = 2 * 1000; // microseconds


	public static void print(String message) {
		System.out.print(message);
	}
	
	public static void println(String message) {
		System.out.println(message);
	}
	
	public static void main(String[] args) {
		if (args.length > 0) {
			for (String arg: args) {
				lookup(arg);
				println("\n");
			}
			return;
		}
			
	
		println("Welcome to the SemWiktionary lookup interface!\n" +
				"===================================================");
		println("Hit ctrl-C to exit.\n");
		
		println(count() + " words in database");
		
		while (true) {
			println("\nEnter a word and press enter or ctrl-D to look it up: ");
			
			Scanner sc = new Scanner(System.in);
			String lookedup = sc.next();
			
			lookup(lookedup);
		}
	}
	
	public static long count() {
		print( "Counting… " );
		
		long start = System.currentTimeMillis(),
			 end,
			 count = 0;
		
		for (Node word : Database.getIndexForName(Word.INDEX_KEY).query(Word.INDEX_KEY, "*")) {
			end = System.currentTimeMillis();
			count++;
			if ((end - start > MAX_COUNT_TIME)) {
				print("(too long " + end + ", stopping now) ");
				return count;
			}
		}
		
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
