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
		
		
		
		
		definitions = new ArrayList<Definition>(15);
		
		
		definitions.add(new Definition("D’un certain âge (relatif à un autre).", 1)
						.addExample("Dernièrement, j'avais eu la maladresse de m’enrhumer en pleine chaleur. Voilà pourtant ce que c'est que de devenir vieux : on ne peut résister à rien. — (Émile Thirion, La Politique au village, p. 125, Fischbacher, 1896)")
						.addExample("Il ne rendait visite qu'à sa mère et encore, cette dernière, entourée de vieilles personnes ridicules et sujette elle-même à des radotages, lui agaçait les nerfs […]. — (Francis Carco, L'Homme de Minuit, 1938)")
						.addExample("Je suis le plus vieux de ma classe.")
						.addExample("Un vieil homme, une vieille femme.")
						.addExample("De vieilles gens.")
						.addExample("Se faire vieux : Vieillir, prendre de l’âge : Cet acteur se fait vieux."));
		
		definitions.add(new Definition("Ancien, qui existe depuis longtemps.", 2)
						.addExample("Un vieux manoir.")
						.addExample("Le monde est bien vieux.")
						.addExample("Le bon vieux temps.")
						.addExample("De vieilles rapsodies.")
						.addExample("Les vieilles coutumes.")
						.addExample("Un vieux proverbe.")
						.addExample("De vieux papiers, de vieux parchemins.")
						.addExample("Ce mot, ce terme est vieux : Il a cessé d’être en usage.")
						.addExample("On dit dans le même sens :")
						.addExample("Une vieille locution, le vieux langage."));
		
		definitions.add(new Definition("…", 3)
						.addDomain("Familier")
						.addExample("Vieux comme Hérode, comme Mathusalem : Très vieux."));
		
		definitions.add(new Definition("…", 4)
						.addDomain("Figuré")
						.addDomain("Familier")
						.addExample("Vieux comme les rues. → voir rue"));
		
		definitions.add(new Definition("…", 5)
						.addDomain("Familier")
						.addExample("Cet homme ne fera pas de vieux os. → voir os"));
		
		definitions.add(new Definition("…", 6)
						.addDomain("Dévotion")
						.addExample("Dépouiller le vieil homme. → voir dépouiller"));
		
		definitions.add(new Definition("Apparence de la vétusté, les dehors de la vieillesse.", 7)
						.addExample("Il a un air vieux.")
						.addExample("Être vieux avant l’âge : Présenter prématurément des symptômes de vieillesse."));
		
		definitions.add(new Definition("S’emploie avec les adverbes plus et moins, et autres semblables, pour marquer la différence d’âge entre deux personnes ou choses.", 8)
						.addExample("Il n’a que vingt ans, et vous en avez vingt-cinq, vous êtes plus vieux que lui.")
						.addExample("Il n’est pas si vieux que vous.")
						.addExample("Il est plus vieux que lui de six ans."));
		
		definitions.add(new Definition("Personne qui exerce une profession, un métier, qui mène un certain genre de vie depuis longtemps.", 9)
						.addExample("Vieux magistrat.")
						.addExample("Vieux capitaine.")
						.addExample("Vieux soldat.")
						.addExample("La vieille garde.")
						.addExample("Un vieux garçon, une vieille fille : Un garçon, une fille qui a passé la jeunesse et qui est encore célibataire.")
						.addExample("Un vieil ami : Un ami de longue date.")
						.addExample("Nous sommes de vieux amis.")
						.addExample("De vieux époux : Des époux qui sont mariés depuis longtemps."));
		
		definitions.add(new Definition("Sert aussi à marquer les anciennes habitudes, et surtout les habitudes vicieuses.", 10)
						.addExample("Vieil ivrogne.")
						.addExample("Vieux débauché."));
		
		definitions.add(new Definition("Il s’emploie familièrement dans des phrases de dénigrement.", 11)
						.addExample("Vieux coquin.")
						.addExample("Vieux sorcier, vieille sorcière.")
						.addExample("Vieux fou, Vieille folle.")
						.addExample("Vieux radoteur.")
						.addExample("Vieil avare."));
		
		definitions.add(new Definition("S’emploie pour exprimer la vénération qu’inspire le nom d’un homme célèbre mort depuis longtemps, en laissant une grande renommée.", 12)
						.addExample("Le vieux Corneille.")
						.addExample("Le vieil Homère."));
		
		definitions.add(new Definition("…", 13)
						.addDomain("Figuré")
						.addExample("Un homme de la vieille roche, noblesse de vieille roche. → voir roche"));
		
		definitions.add(new Definition("Par comparaison et opposition à nouveau.", 14)
						.addExample("La vieille ville.")
						.addExample("La vieille cour.")
						.addExample("Du vin vieux.")
						.addExample("La vieille mode.")
						.addExample("Vieux style. → voir style"));
		
		definitions.add(new Definition("Choses qui sont usées, principalement des habits, des meubles, par opposition à neuf.", 15)
						.addExample("Vieil habit.")
						.addExample("Vieux chapeau.")
						.addExample("Vieux linge.")
						.addExample("Vieux coffre.")
						.addExample("Vieille tapisserie."));
						
		result.put("vieux", definitions);

		
		return result;
	}
}
