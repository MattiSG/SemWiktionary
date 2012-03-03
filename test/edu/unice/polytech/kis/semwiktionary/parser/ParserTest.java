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
	
	public static final String TEST_FILE = "test/resources/frwiktionary-test-extracts.xml"; // relative to ant build file
	
	private static List<String> unexpectedTitles;
	private static Map<String, List<Definition>> expected;
	
	
	@BeforeClass
	public static void classSetUp() throws Exception {
		expected = generateExpectedContent();
		
		unexpectedTitles = new ArrayList<String>(2);
		unexpectedTitles.add("MediaWiki:Disclaimers");
		unexpectedTitles.add("Discussion utilisateur:Hippietrail");
		
		FileInputStream fileInputStream = new FileInputStream(new File(TEST_FILE));

		WikimediaDump lexer = new WikimediaDump(fileInputStream);
		lexer.yylex(); // store in db
	}
	
	
	@Test
	public void allWordsExist() {
		for (String someWord : expected.keySet())
			assertTrue("'" + someWord + "' pretends not to exist in the database (check with WordTest first)!", Word.exists(someWord));
	}
	
	@Test
	public void properNumberOfDefinitionsWereParsed() {
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet()) {
			Word currentWord = Word.from(currentEntry.getKey());

			assertEquals("Bad number of definitions for word " + currentWord, currentEntry.getValue().size(), currentWord.getDefinitions().size());
		}
	}
	
	@Test
	public void definitionsWereParsedInCorrectOrder() {
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet()) {
			Word currentWord = Word.from(currentEntry.getKey());
			List<Definition> expectedDefinitions = currentEntry.getValue();
			
			for (int i = 0; i < expectedDefinitions.size(); i++) {
				assertEquals("Bad definition index for word " + currentWord,
							 expectedDefinitions.get(i).getPosition(),
							 currentWord.getDefinitions().get(i).getPosition()
				);
			}
		}
	}
	
	@Test
	public void definitionsContentIsCorrect() {
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet()) {
			Word currentWord = Word.from(currentEntry.getKey());
			List<Definition> expectedDefinitions = currentEntry.getValue();
			
			for (int i = 0; i < expectedDefinitions.size(); i++) {
				assertEquals("Bad definition for word " + currentWord,
							 expectedDefinitions.get(i),
							 currentWord.getDefinitions().get(i)
				);
			}
		}
	}
	
	@Test
	public void examplesWereProperlyParsed() {
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet()) {
			Word currentWord = Word.from(currentEntry.getKey());
			List<Definition> expectedDefinitions = currentEntry.getValue();
			
			//fail("No support for multiple examples yet. Test deactivated to avoid too verbose details."); //TODO
			
			for (int i = 0; i < expectedDefinitions.size(); i++) {
				ReflectionAssert.assertReflectionEquals(
														expectedDefinitions.get(i).getExamples(),
														currentWord.getDefinitions().get(i).getExamples()
														);
			}
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
						.addExample("Le dictionnaire de l’Académie française.", 1)
						.addExample("Les articles d’un dictionnaire.", 2)
						.addExample("Les dictionnaires sont irremplaçables parce qu’ils sont l’expression des connaissances et de l’idéologie dominante à un moment donné de l’histoire. — (Elisabeth Badinter, préface de : Samuel Souffi et Jean Pruvost, La mère, Honoré Champion).", 3));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : un ou plusieurs synonymes, antonymes, etc.", 2)
						.addDomain("Linguistique")
						.addExample("Je te recommande ce dictionnaire des synonymes.", 1)
						.addExample("Dictionnaire des homonymes.", 2));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : une étymologie", 3)
						.addDomain("Linguistique")
						.addExample("Un dictionnaire étymologique.", 1));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : et/ou une traduction.", 4)
						.addDomain("Linguistique")
						.addExample("Le dictionnaire latin-français, français-latin.", 1));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : Il peut se contenter d’attester la simple existence d’un mot de façon normative, sans donner aucune autre information.", 5)
						.addDomain("Linguistique")
						.addExample("Le dictionnaire du Scrabble.", 1));
		
		definitions.add(new Definition("Recueil fait par ordre alphabétique sur des matières de littérature, de sciences ou d’arts.", 6)
						.addDomain("Par extension")
						.addExample("Dictionnaire de la Fable, de médecine, de chimie, de chirurgie.", 1)
						.addExample("Dictionnaire raisonné des arts et des sciences.", 2));
		
		definitions.add(new Definition("Livre, recueil de pensées, d’opinions, d’un auteur, publié dans un ordre alphabétique.", 7)
						.addDomain("Littérature")
						.addExample("Le dictionnaire des idées reçues.", 1)
						.addExample("Le dictionnaire philosophique de Voltaire.", 2));
		
		definitions.add(new Definition("Personne de grande érudition, qui a des connaissances étendues et qui les communique aisément.", 8)
						.addDomain("Figuré")
						.addDomain("Familier")
						.addExample("C’est un dictionnaire vivant !", 1));
		
		definitions.add(new Definition("Vocabulaire, ensemble des mots dont se sert un écrivain.", 9)
						.addDomain("Désuet")
						.addExample("Le dictionnaire de Bossuet est très étendu.", 1));
						
						
		result.put("dictionnaire", definitions);
		
		
		
		
		definitions = new ArrayList<Definition>(15);
		
		
		definitions.add(new Definition("D’un certain âge (relatif à un autre).", 1)
						.addExample("Dernièrement, j'avais eu la maladresse de m’enrhumer en pleine chaleur. Voilà pourtant ce que c'est que de devenir vieux : on ne peut résister à rien. — (Émile Thirion, La Politique au village, p. 125, Fischbacher, 1896)", 1)
						.addExample("Il ne rendait visite qu'à sa mère et encore, cette dernière, entourée de vieilles personnes ridicules et sujette elle-même à des radotages, lui agaçait les nerfs […]. — (Francis Carco, L'Homme de Minuit, 1938)", 2)
						.addExample("Je suis le plus vieux de ma classe.", 3)
						.addExample("Un vieil homme, une vieille femme.", 4)
						.addExample("De vieilles gens.", 5)
						.addExample("Se faire vieux : Vieillir, prendre de l’âge : Cet acteur se fait vieux.", 6));
		
		definitions.add(new Definition("Ancien, qui existe depuis longtemps.", 2)
						.addExample("Un vieux manoir.", 1)
						.addExample("Le monde est bien vieux.", 2)
						.addExample("Le bon vieux temps.", 3)
						.addExample("De vieilles rapsodies.", 4)
						.addExample("Les vieilles coutumes.", 5)
						.addExample("Un vieux proverbe.", 6)
						.addExample("De vieux papiers, de vieux parchemins.", 7)
						.addExample("Ce mot, ce terme est vieux : Il a cessé d’être en usage.", 8)
						.addExample("On dit dans le même sens :", 9)
						.addExample("Une vieille locution, le vieux langage.", 10));
		
		definitions.add(new Definition("…", 3)
						.addDomain("Familier")
						.addExample("Vieux comme Hérode, comme Mathusalem : Très vieux.", 1));
		
		definitions.add(new Definition("…", 4)
						.addDomain("Figuré")
						.addDomain("Familier")
						.addExample("Vieux comme les rues. → voir rue", 1));
		
		definitions.add(new Definition("…", 5)
						.addDomain("Familier")
						.addExample("Cet homme ne fera pas de vieux os. → voir os", 1));
		
		definitions.add(new Definition("…", 6)
						.addDomain("Dévotion")
						.addExample("Dépouiller le vieil homme. → voir dépouiller", 1));
		
		definitions.add(new Definition("Apparence de la vétusté, les dehors de la vieillesse.", 7)
						.addExample("Il a un air vieux.", 1)
						.addExample("Être vieux avant l’âge : Présenter prématurément des symptômes de vieillesse.", 2));
		
		definitions.add(new Definition("S’emploie avec les adverbes plus et moins, et autres semblables, pour marquer la différence d’âge entre deux personnes ou choses.", 8)
						.addExample("Il n’a que vingt ans, et vous en avez vingt-cinq, vous êtes plus vieux que lui.", 1)
						.addExample("Il n’est pas si vieux que vous.", 2)
						.addExample("Il est plus vieux que lui de six ans.", 3));
		
		definitions.add(new Definition("Personne qui exerce une profession, un métier, qui mène un certain genre de vie depuis longtemps.", 9)
						.addExample("Vieux magistrat.", 1)
						.addExample("Vieux capitaine.", 2)
						.addExample("Vieux soldat.", 3)
						.addExample("La vieille garde.", 4)
						.addExample("Un vieux garçon, une vieille fille : Un garçon, une fille qui a passé la jeunesse et qui est encore célibataire.", 5)
						.addExample("Un vieil ami : Un ami de longue date.", 6)
						.addExample("Nous sommes de vieux amis.", 7)
						.addExample("De vieux époux : Des époux qui sont mariés depuis longtemps.", 8));
		
		definitions.add(new Definition("Sert aussi à marquer les anciennes habitudes, et surtout les habitudes vicieuses.", 10)
						.addExample("Vieil ivrogne.", 1)
						.addExample("Vieux débauché.", 2));
		
		definitions.add(new Definition("Il s’emploie familièrement dans des phrases de dénigrement.", 11)
						.addExample("Vieux coquin.", 1)
						.addExample("Vieux sorcier, vieille sorcière.", 2)
						.addExample("Vieux fou, Vieille folle.", 3)
						.addExample("Vieux radoteur.", 4)
						.addExample("Vieil avare.", 5));
		
		definitions.add(new Definition("S’emploie pour exprimer la vénération qu’inspire le nom d’un homme célèbre mort depuis longtemps, en laissant une grande renommée.", 12)
						.addExample("Le vieux Corneille.", 1)
						.addExample("Le vieil Homère.", 2));
		
		definitions.add(new Definition("…", 13)
						.addDomain("Figuré")
						.addExample("Un homme de la vieille roche, noblesse de vieille roche. → voir roche", 1));
		
		definitions.add(new Definition("Par comparaison et opposition à nouveau.", 14)
						.addExample("La vieille ville.", 1)
						.addExample("La vieille cour.", 2)
						.addExample("Du vin vieux.", 3)
						.addExample("La vieille mode.", 4)
						.addExample("Vieux style. → voir style", 5));
		
		definitions.add(new Definition("Choses qui sont usées, principalement des habits, des meubles, par opposition à neuf.", 15)
						.addExample("Vieil habit.", 1)
						.addExample("Vieux chapeau.", 2)
						.addExample("Vieux linge.", 3)
						.addExample("Vieux coffre.", 4)
						.addExample("Vieille tapisserie.", 5));
						
		definitions.add(new Definition("Personne âgée.", 1)
						.addDomain("Péjoratif")
						.addExample("Car enfin il ne m'emballe pas, moi, ce raplati de Karfeck et il est un peu dégoûtant, ce vieux qui guigne tout le temps les mollets de Clotte. — (Paul Margueritte, Jouir, 1918, T.2, p.78)", 1)
						.addExample("Le petit vieux s’est encore perdu.", 2)
						.addExample("Les jeunes et les vieux.", 3)
						.addExample("Faire le vieux : Prendre le ton, les habitudes de la vieillesse.", 4)
						.addExample("Mon vieux : Terme d’affection qui se dit familièrement à un vieil ami, à un ami intime. : J’irai bientôt te voir, mon vieux.", 5));
		
		
		definitions.add(new Definition("Et elliptiquement,", 2)
						   .addDomain("Familier")
						   .addExample("Un vieux de la vieille : Un soldat de la vieille garde et, par extension, un bon vieux compagnon.", 1));
						   
		definitions.add(new Definition("Ce qui est vieux, usé.", 3)
						   .addExample("Coudre du vieux avec du neuf.", 1)
						   .addExample("C’est du vieux qui vaut du neuf.", 2));
			
		definitions.add(new Definition("ou Père ou mère.", 4)
						.addDomain("Argot")
						.addDomain("Populaire")
						.addDomain("Au singulier")
						.addExample("Mon vieux a regardé la télé hier.", 1)
						.addExample("Ma vieille me prend la tête en ce moment...", 2));
		
		definitions.add(new Definition("ou Parents.", 5)
						.addDomain("Argot")
						.addDomain("Populaire")
						.addDomain("Au pluriel")
						.addExample("Mes vieux partent en vacances demain.", 1));
						
						
		result.put("vieux", definitions);
		
		
		definitions = new ArrayList<Definition>(1);
		
		definitions.add(new Definition("Substance phytosanitaire, de formule chimique C6H12NO4PS2 , à usage d'insecticide endothérapique sur de nombreux insectes, particulièrement les pucerons, la mouche de l’olive, la mouche méditerranéenne des fruits, le carpocapse, la mouche de l’endive et la mouche de l’asperge, la mouche de la betterave, la cicadelle de la vigne, les psylles et les thrips ; et avec une bonne action acaricide sur les acariens de la vigne, des arbres fruitiers, des cultures légumières.", 1)
						.addDomain("Agriculture")
						.addDomain("Viticulture"));
		
		
		result.put("formothion", definitions);

		
		return result;
	}
}
