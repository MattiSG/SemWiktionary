package edu.unice.polytech.kis.semwiktionary.parser;


import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;


public class ParserTest {
	
	public static final String TEST_FILE = "test/resources/miniwiki.xml"; // relative to ant build file
	
	private static List<String> unexpectedTitles;
	private static Map<String, List<Definition>> expected;
	
	
	@BeforeClass
	public static void classSetUp() throws Exception {
		expected = generateExpectedContent();
		
		unexpectedTitles = new ArrayList<String>(2);
		unexpectedTitles.add("MediaWiki:Disclaimers");
		unexpectedTitles.add("Discussion utilisateur:Hippietrail");
		
		FileInputStream fileInputStream = new FileInputStream(new File(TEST_FILE));

		System.setErr(new PrintStream(new FileOutputStream(new File("log/jflex.err"))));
		System.setOut(new PrintStream(new FileOutputStream(new File("log/jflex.out"))));

		WikimediaDump lexer = new WikimediaDump(fileInputStream);
		lexer.yylex(); // store in db

		System.setErr(System.err);
		System.setOut(System.out);
	}
	
	
	@Test
	public void allWordsExist() {
		for (String someWord : expected.keySet())
			assertTrue("'" + someWord + "' pretends not to exist in the database (check with WordTest first)!", Word.exists(someWord));
	}
	
	@Test
	public void definitionsWereProperlyParsed() {
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet()) {
			Word currentWord = Word.from(currentEntry.getKey());

			ReflectionAssert.assertReflectionEquals(currentEntry.getValue(), currentWord.getDefinitions());
		}
	}
	
	@Test
	public void pagesThatAreNotWordsAreNotStored() {
		for (String someWord : unexpectedTitles)
			assertFalse(someWord + " should exist in the database!", Word.exists(someWord));
	}
	
	
	public static Map<String, List<Definition>> generateExpectedContent() {
		Map<String, List<Definition>> result = new HashMap<String, List<Definition>>();
		List<Definition> definitions;
		
		int count = 0;
		
		definitions = new ArrayList<Definition>(9);
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : une définition, un sens", 1)
						.addDomain("Linguistique")
						.addExample("Le dictionnaire de l’Académie française.")
						.addExample("Les articles d’un dictionnaire.")
						.addExample("Les dictionnaires sont irremplaçables parce qu’ils sont l’expression des connaissances et de l’idéologie dominante à un moment donné de l’histoire. — (Elisabeth Badinter, préface de : Samuel Souffi et Jean Pruvost, La mère, Honoré Champion)."));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : un ou plusieurs synonymes, antonymes, etc.", 2)
						.addDomain("Linguistique")
						.addExample("Je te recommande ce dictionnaire des synonymes.")
						.addExample("Dictionnaire des homonymes."));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : une étymologie", 3)
						.addDomain("Linguistique")
						.addExample("Un dictionnaire étymologique."));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : et/ou une traduction.", 4)
						.addDomain("Linguistique")
						.addExample("Le dictionnaire latin-français, français-latin."));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : Il peut se contenter d’attester la simple existence d’un mot de façon normative, sans donner aucune autre information.", 5)
						.addDomain("Linguistique")
						.addExample("Le dictionnaire du Scrabble."));
		
		definitions.add(new Definition("Recueil fait par ordre alphabétique sur des matières de littérature, de sciences ou d’arts.", 6)
						.addDomain("Par extension")
						.addExample("Dictionnaire de la Fable, de médecine, de chimie, de chirurgie.")
						.addExample("Dictionnaire raisonné des arts et des sciences."));
		
		definitions.add(new Definition("Livre, recueil de pensées, d’opinions, d’un auteur, publié dans un ordre alphabétique.", 7)
						.addDomain("Littérature")
						.addExample("Le dictionnaire des idées reçues.")
						.addExample("Le dictionnaire philosophique de Voltaire."));
		
		definitions.add(new Definition("Personne de grande érudition, qui a des connaissances étendues et qui les communique aisément.", 8)
						.addDomain("Figuré")
						.addDomain("Familier")
						.addExample("C’est un dictionnaire vivant !"));
		
		definitions.add(new Definition("Vocabulaire, ensemble des mots dont se sert un écrivain.", 9)
						.addDomain("Désuet")
						.addExample("Le dictionnaire de Bossuet est très étendu."));
						
						
		result.put("dictionnaire", definitions);
		
		return result;
	}
}
