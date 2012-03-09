/**SemWiktionary
*Java API to access data from [wiktionary](http://fr.wiktionary.org). Specific target is the French wiktionary.
*
*@author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
*@author	[Fabien Brossier](http://fabienbrossier.fr)
*@author	Ngoc Nguyen Thinh Dong
*/

package edu.unice.polytech.kis.semwiktionary;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

import org.neo4j.graphdb.Node;

import edu.unice.polytech.kis.semwiktionary.parser.WikimediaDump;
import edu.unice.polytech.kis.semwiktionary.model.NodeMappedObject;
import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;
import edu.unice.polytech.kis.semwiktionary.model.LexicalCategory;
import edu.unice.polytech.kis.semwiktionary.model.Example;
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
				lookup(arg.replace('_', ' '));
				println("\n");
			}
			return;
		}
			
	
		println("Welcome to the SemWiktionary lookup interface!\n" +
				"♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒♒");
		println("Hit ctrl-C to exit.\n");
		
		BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in));
		
		while (true) {
			println("\nEnter a word and press enter to look it up: ");
			
			String lookedup = "";
			try {
				lookedup = reader.readLine();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			lookup(lookedup);
		}
	}
	
	public static long count() {
		return NodeMappedObject.count(Word.class);
	}
	
	public static void lookup(String input) {
		Word word = Word.find(input);
		
		if (word == null) {
			println("* The word '" + input + "' was not found in the database! *");
			return;
		}
		
		print("(");
		for (LexicalCategory category : word.getLexicalCategories())
			print(category + ", ");
		
		println(")");
		
		println("———————————");
		
		for (Definition def : word.getDefinitions()) {
			println(def.getPosition() + ". " + def);
			
			for (String dom : def.getDomains())
				if (! dom.isEmpty())
					println("(" + dom + ") ");
			
			for (Example ex : def.getExamples())
				println("\t• “" + ex + "”");
			
			println("\t—————");
		}
		
		println("\n-=[ SYNONYMS ]=-  (words with same meaning)");
				
		for (Word synonym : word.getSynonyms())
			println("= " + synonym);
		
		println("\n-=[ ANTONYMS ]=-  (words with opposite meaning)");
		
		for (Word antonym : word.getAntonyms())
			println("≠ " + antonym);
		
		println("\n-=[ TROPONYMS ]=-  (verbs that details this verb’s meaning)");

		for (Word troponym : word.getTroponyms())
			println("∋ " + troponym);
		
		println("\n-=[ HYPONYMS ]=-  (words which meaning is included by this one’s)");
		
		for (Word hyp1 : word.getHyponyms()) {
			println("> " + hyp1.getTitle());
			for (Word hyp2 : hyp1.getHyponyms()) {
				println(" > " + hyp2.getTitle());
				for (Word hyp3 : hyp2.getHyponyms()) {
					println("  > " + hyp3.getTitle());
				}
			}
		}
		
		println("\n-=[ HYPERONYMS ]=-  (words which meaning includes this one’s)");
		
		for (Word hyp1 : word.getHyperonyms()) {
			println("< " + hyp1.getTitle());
			for (Word hyp2 : hyp1.getHyperonyms()) {
				println(" < " + hyp2.getTitle());
				for (Word hyp3 : hyp2.getHyperonyms()) {
					println("  < " + hyp3.getTitle());
				}
			}
		}
		
		println("\n-=[ HOLONYMS ]=-  (more general meaning)");
		
		for (Word hol1 : word.getHolonyms()) {
			println("⋀ " + hol1.getTitle());
			for (Word hol2 : hol1.getHolonyms()) {
				println(" ⋀ " + hol2.getTitle());
				for (Word hol3 : hol2.getHolonyms()) {
					println("  ⋀ " + hol3.getTitle());
				}
			}
		}
		
		println("\n-=[ MERONYMS ]=-  (more precise meaning)");
		
		for (Word mer1 : word.getMeronyms()) {
			println("⋁ " + mer1.getTitle());
			for (Word mer2 : mer1.getMeronyms()) {
				println(" ⋁ " + mer2.getTitle());
				for (Word mer3 : mer2.getMeronyms()) {
					println("  ⋁ " + mer3.getTitle());
				}
			}
		}

		println("\n-=[ RELATED VOCABULARY ]=-  (related meaning)");

		for (Word rel : word.getRelatedVoc())
			println("↔ " + rel.getTitle());

	}
}
