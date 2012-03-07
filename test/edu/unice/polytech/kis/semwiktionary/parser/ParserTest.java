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
import edu.unice.polytech.kis.semwiktionary.model.LexicalCategory;
import edu.unice.polytech.kis.semwiktionary.model.Example;


public class ParserTest {
	
	public static final String TEST_FILE = "test/resources/frwiktionary-test-extracts.xml"; // relative to ant build file
	
	private static List<String> unexpectedTitles;
	private static Map<String, String> expectedModels;
	private static Map<String, List<Definition>> expected;
	
	
	@BeforeClass
	public static void classSetUp() throws Exception {
		expected = generateExpectedContent();
		
		expectedModels = new HashMap<String, String>(2);
		expectedModels.put("-nom-", "Nom commun");
		expectedModels.put("-conj-coord-", "Conjonction de coordination");
		
		
		unexpectedTitles = new ArrayList<String>(4);
		unexpectedTitles.add("MediaWiki:Disclaimers");
		unexpectedTitles.add("Discussion utilisateur:Hippietrail");
		for (String modelName : expectedModels.keySet())
			unexpectedTitles.add("Modèle:" + modelName);
		
		FileInputStream fileInputStream = new FileInputStream(new File(TEST_FILE));

		WikimediaDump lexer = new WikimediaDump(fileInputStream);
		try {
			lexer.yylex(); // store in db
		} catch (Exception e) {
			e.printStackTrace();
			fail("Parser failed and threw an exception! (" + e + ")\nSee parser log for details.");
		}
	}
	
	
	@Test
	public void allWordsExist() {
		for (String someWord : expected.keySet())
			assertTrue("'" + someWord + "' pretends not to exist in the database (check with WordTest first)!", Word.exists(someWord));
	}
	
	@Test
	public void properNumberOfDefinitionsWereParsed() {
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet()) {
			Word currentWord = Word.find(currentEntry.getKey());

			assertEquals("Bad number of definitions for word " + currentWord, currentEntry.getValue().size(), currentWord.getDefinitions().size());
		}
	}
	
	@Test
	public void definitionsWereParsedInCorrectOrder() {
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet()) {
			Word currentWord = Word.find(currentEntry.getKey());
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
			Word currentWord = Word.find(currentEntry.getKey());
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
			Word currentWord = Word.find(currentEntry.getKey());
			List<Definition> expectedDefinitions = currentEntry.getValue();
						
			for (int i = 0; i < expectedDefinitions.size(); i++) {
				for (int j = 0; j < expectedDefinitions.get(i).getExamples().size(); j++) {
					assertEquals("Bad example for word " + currentWord + " at example ",
								 expectedDefinitions.get(i).getExamples().get(j),
								 currentWord.getDefinitions().get(i).getExamples().get(j)
					);
				}
			}
		}
	}
	

	@Test
	public void pagesThatAreNotWordsAreNotIndexedAsWords() {
		for (String someWord : unexpectedTitles)
			assertFalse(someWord + " should not exist as a word!", Word.exists(someWord));
	}
	
	
	@Test
	public void modelsAreStoredAsModels() {
		for (Map.Entry currentModel : expectedModels.entrySet()) {
			LexicalCategory cat = LexicalCategory.find((String) (currentModel.getKey()));
			
			assertNotNull("Missing lexical category for '" + currentModel.getKey() + "'", cat);
			assertEquals(currentModel.getValue(), cat.getDescription());
		}
	}
	
	@Test
	public void modelsDescriptionIsProperlyStored() {
		for (Map.Entry currentModel : expectedModels.entrySet()) {
			LexicalCategory cat = LexicalCategory.find((String) (currentModel.getKey()));
			
			assertEquals(currentModel.getValue(), cat.getDescription());
		}
	}
	
	
	public static Map<String, List<Definition>> generateExpectedContent() {
		Map<String, List<Definition>> result = new HashMap<String, List<Definition>>();
		List<Definition> definitions;
				
		int count = 0;
		
		definitions = new ArrayList<Definition>(9);
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : une définition, un sens", 1)
						.addDomain("Linguistique")
						.addExample(new Example("Le dictionnaire de l’Académie française."))
						.addExample(new Example("Les articles d’un dictionnaire."))
						.addExample(new Example("Les dictionnaires sont irremplaçables parce qu’ils sont l’expression des connaissances et de l’idéologie dominante à un moment donné de l’histoire. — (Elisabeth Badinter, préface de : Samuel Souffi et Jean Pruvost, La mère, Honoré Champion).")));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : un ou plusieurs synonymes, antonymes, etc.", 2)
						.addDomain("Linguistique")
						.addExample(new Example("Je te recommande ce dictionnaire des synonymes."))
						.addExample(new Example("Dictionnaire des homonymes.")));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : une étymologie", 3)
						.addDomain("Linguistique")
						.addExample(new Example("Un dictionnaire étymologique.")));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : et/ou une traduction.", 4)
						.addDomain("Linguistique")
						.addExample(new Example("Le dictionnaire latin-français, français-latin.")));
		
		definitions.add(new Definition("Ouvrage de référence qui répertorie des mots dans un ordre convenu (alphabétique en général) pour leur associer par exemple : Il peut se contenter d’attester la simple existence d’un mot de façon normative, sans donner aucune autre information.", 5)
						.addDomain("Linguistique")
						.addExample(new Example("Le dictionnaire du Scrabble.")));
		
		definitions.add(new Definition("Recueil fait par ordre alphabétique sur des matières de littérature, de sciences ou d’arts.", 6)
						.addDomain("Par extension")
						.addExample(new Example("Dictionnaire de la Fable, de médecine, de chimie, de chirurgie."))
						.addExample(new Example("Dictionnaire raisonné des arts et des sciences.")));
		
		definitions.add(new Definition("Livre, recueil de pensées, d’opinions, d’un auteur, publié dans un ordre alphabétique.", 7)
						.addDomain("Littérature")
						.addExample(new Example("Le dictionnaire des idées reçues."))
						.addExample(new Example("Le dictionnaire philosophique de Voltaire.")));
		
		definitions.add(new Definition("Personne de grande érudition, qui a des connaissances étendues et qui les communique aisément.", 8)
						.addDomain("Figuré")
						.addDomain("Familier")
						.addExample(new Example("C’est un dictionnaire vivant !")));
		
		definitions.add(new Definition("Vocabulaire, ensemble des mots dont se sert un écrivain.", 9)
						.addDomain("Désuet")
						.addExample(new Example("Le dictionnaire de Bossuet est très étendu.")));
						
						
		result.put("dictionnaire", definitions);
		
		
		
		
		definitions = new ArrayList<Definition>(15);
		
		
		definitions.add(new Definition("D’un certain âge (relatif à un autre).", 1)
						.addExample(new Example("Dernièrement, j'avais eu la maladresse de m’enrhumer en pleine chaleur. Voilà pourtant ce que c'est que de devenir vieux : on ne peut résister à rien. — (Émile Thirion, La Politique au village, p. 125, Fischbacher, 1896)"))
						.addExample(new Example("Il ne rendait visite qu'à sa mère et encore, cette dernière, entourée de vieilles personnes ridicules et sujette elle-même à des radotages, lui agaçait les nerfs […]. — (Francis Carco, L'Homme de Minuit, 1938)"))
						.addExample(new Example("Je suis le plus vieux de ma classe."))
						.addExample(new Example("Un vieil homme, une vieille femme."))
						.addExample(new Example("De vieilles gens."))
						.addExample(new Example("Se faire vieux : Vieillir, prendre de l’âge : Cet acteur se fait vieux.")));
		
		definitions.add(new Definition("Ancien, qui existe depuis longtemps.", 2)
						.addExample(new Example("Un vieux manoir."))
						.addExample(new Example("Le monde est bien vieux."))
						.addExample(new Example("Le bon vieux temps."))
						.addExample(new Example("De vieilles rapsodies."))
						.addExample(new Example("Les vieilles coutumes."))
						.addExample(new Example("Un vieux proverbe."))
						.addExample(new Example("De vieux papiers, de vieux parchemins."))
						.addExample(new Example("Ce mot, ce terme est vieux : Il a cessé d’être en usage."))
						.addExample(new Example("On dit dans le même sens :"))
						.addExample(new Example("Une vieille locution, le vieux langage.")));
		
		definitions.add(new Definition("…", 3)
						.addDomain("Familier")
						.addExample(new Example("Vieux comme Hérode, comme Mathusalem : Très vieux.")));
		
		definitions.add(new Definition("…", 4)
						.addDomain("Figuré")
						.addDomain("Familier")
						.addExample(new Example("Vieux comme les rues.")));
		
		definitions.add(new Definition("…", 5)
						.addDomain("Familier")
						.addExample(new Example("Cet homme ne fera pas de vieux os.")));
		
		definitions.add(new Definition("…", 6)
						.addDomain("Dévotion")
						.addExample(new Example("Dépouiller le vieil homme.")));
		
		definitions.add(new Definition("Apparence de la vétusté, les dehors de la vieillesse.", 7)
						.addExample(new Example("Il a un air vieux."))
						.addExample(new Example("Être vieux avant l’âge : Présenter prématurément des symptômes de vieillesse.")));
		
		definitions.add(new Definition("S’emploie avec les adverbes plus et moins, et autres semblables, pour marquer la différence d’âge entre deux personnes ou choses.", 8)
						.addExample(new Example("Il n’a que vingt ans, et vous en avez vingt-cinq, vous êtes plus vieux que lui."))
						.addExample(new Example("Il n’est pas si vieux que vous."))
						.addExample(new Example("Il est plus vieux que lui de six ans.")));
		
		definitions.add(new Definition("Personne qui exerce une profession, un métier, qui mène un certain genre de vie depuis longtemps.", 9)
						.addExample(new Example("Vieux magistrat."))
						.addExample(new Example("Vieux capitaine."))
						.addExample(new Example("Vieux soldat."))
						.addExample(new Example("La vieille garde."))
						.addExample(new Example("Un vieux garçon, une vieille fille : Un garçon, une fille qui a passé la jeunesse et qui est encore célibataire."))
						.addExample(new Example("Un vieil ami : Un ami de longue date."))
						.addExample(new Example("Nous sommes de vieux amis."))
						.addExample(new Example("De vieux époux : Des époux qui sont mariés depuis longtemps.")));
		
		definitions.add(new Definition("Sert aussi à marquer les anciennes habitudes, et surtout les habitudes vicieuses.", 10)
						.addExample(new Example("Vieil ivrogne."))
						.addExample(new Example("Vieux débauché.")));
		
		definitions.add(new Definition("Il s’emploie familièrement dans des phrases de dénigrement.", 11)
						.addExample(new Example("Vieux coquin."))
						.addExample(new Example("Vieux sorcier, vieille sorcière."))
						.addExample(new Example("Vieux fou, Vieille folle."))
						.addExample(new Example("Vieux radoteur."))
						.addExample(new Example("Vieil avare.")));
		
		definitions.add(new Definition("S’emploie pour exprimer la vénération qu’inspire le nom d’un homme célèbre mort depuis longtemps, en laissant une grande renommée.", 12)
						.addExample(new Example("Le vieux Corneille."))
						.addExample(new Example("Le vieil Homère.")));
		
		definitions.add(new Definition("…", 13)
						.addDomain("Figuré")
						.addExample(new Example("Un homme de la vieille roche, noblesse de vieille roche.")));
		
		definitions.add(new Definition("Par comparaison et opposition à nouveau.", 14)
						.addExample(new Example("La vieille ville."))
						.addExample(new Example("La vieille cour."))
						.addExample(new Example("Du vin vieux."))
						.addExample(new Example("La vieille mode."))
						.addExample(new Example("Vieux style.")));
		
		definitions.add(new Definition("Choses qui sont usées, principalement des habits, des meubles, par opposition à neuf.", 15)
						.addExample(new Example("Vieil habit."))
						.addExample(new Example("Vieux chapeau."))
						.addExample(new Example("Vieux linge."))
						.addExample(new Example("Vieux coffre."))
						.addExample(new Example("Vieille tapisserie.")));
						
		definitions.add(new Definition("Personne âgée.", 1)
						.addDomain("Péjoratif")
						.addExample(new Example("Car enfin il ne m'emballe pas, moi, ce raplati de Karfeck et il est un peu dégoûtant, ce vieux qui guigne tout le temps les mollets de Clotte. — (Paul Margueritte, Jouir, 1918, T.2, p.78)"))
						.addExample(new Example("Le petit vieux s’est encore perdu."))
						.addExample(new Example("Les jeunes et les vieux."))
						.addExample(new Example("Faire le vieux : Prendre le ton, les habitudes de la vieillesse."))
						.addExample(new Example("Mon vieux : Terme d’affection qui se dit familièrement à un vieil ami, à un ami intime. : J’irai bientôt te voir, mon vieux.")));
		
		
		definitions.add(new Definition("Et elliptiquement,", 2)
						   .addDomain("Familier")
						   .addExample(new Example("Un vieux de la vieille : Un soldat de la vieille garde et, par extension, un bon vieux compagnon.")));
						   
		definitions.add(new Definition("Ce qui est vieux, usé.", 3)
						   .addExample(new Example("Coudre du vieux avec du neuf."))
						   .addExample(new Example("C’est du vieux qui vaut du neuf.")));
			
		definitions.add(new Definition("ou Père ou mère.", 4)
						.addDomain("Argot")
						.addDomain("Populaire")
						.addDomain("Au singulier")
						.addExample(new Example("Mon vieux a regardé la télé hier."))
						.addExample(new Example("Ma vieille me prend la tête en ce moment...")));
		
		definitions.add(new Definition("ou Parents.", 5)
						.addDomain("Argot")
						.addDomain("Populaire")
						.addDomain("Au pluriel")
						.addExample(new Example("Mes vieux partent en vacances demain.")));
						
						
		result.put("vieux", definitions);
		
		
		definitions = new ArrayList<Definition>(1);
		
		definitions.add(new Definition("Substance phytosanitaire, de formule chimique C6H12NO4PS2 , à usage d'insecticide endothérapique sur de nombreux insectes, particulièrement les pucerons, la mouche de l’olive, la mouche méditerranéenne des fruits, le carpocapse, la mouche de l’endive et la mouche de l’asperge, la mouche de la betterave, la cicadelle de la vigne, les psylles et les thrips ; et avec une bonne action acaricide sur les acariens de la vigne, des arbres fruitiers, des cultures légumières.", 1)
						.addDomain("Agriculture")
						.addDomain("Viticulture"));
		
		
		result.put("formothion", definitions);


		definitions = new ArrayList<Definition>(6);
		
		definitions.add(new Definition("Action, charge, ou manière de gouverner, de régir, d’administrer quelque chose, en particulier dans le domaine politique.", 1)
						.addDomain("vieilli")
						.addExample("Elle jouit avec un si tranquille orgueil de son autorité domestique, que je ne me sens pas le courage de tenter un coup d'État contre le gouvernement de mes armoires. — (Anatole France, Le crime de Sylvestre Bonnard)")
						.addExample("Le régime municipal, devenu un mode d'administration, fut réduit au gouvernement des affaires locales, des intérêts civils de la cité. — (Guizot, Histoire générale de la civilisation en Europe, Leçon 7, 1828)")
						.addExample("Peut-être parce que, depuis le XI siècle, la seule théorie politique de l'islam a été celle de l'obéissance passive à toute autorité de facto, le gouvernement par consentement reste un concept inconnu : l'autocratie a été la véritable et, pour l'essentiel, l'unique expérience. — (P.J. Vatikiotis, L'Islam et l'État, 1987, traduction de Odette Guitard, 1992, p.38)"));
						
		definitions.add(new Definition("Pouvoir qui gouverne un État.", 2)
						.addExample("La France démocratique, dans un accès d'indignation, renversa le gouvernement de la France bourgeoise et se proclama libre sous un gouvernement républicain. — (Daniel Stern, Histoire de la Révolution de 1848)")
						.addExample("Sumner Maine fait observer que les rapports des gouvernements et des citoyens ont été bouleversés de fond en comble depuis la fin du XVIII siècle ; […]. — (Georges Sorel,  Réflexions sur la violence, Chap.III, Les préjugés contre  la violence, 1908, p.142)")
						.addExample("Ainsi vous voyez que j'aurais pu réussir et, comme tant d'autres, vivre du budget; mais je n'ai jamais voulu rien accepter d'aucun gouvernement, si ce n'est du suffrage universel. — (Réponse de M. Raspail père à l'avocat général, lors du procès de François-Vincent Raspail le 12 février 1874)")
						.addExample("Une société ne saurait subsister sans un gouvernement. — (Montesquieu)")
						.addExample("Le gouvernement ordonne une répression sanglante. Arrestations, pendaisons, exécutions sommaire plongent Tripoli dans un bain de sang. — (Tewfik Farès, 1911 : la Libye en guerre, déjà, dans Libération (journal) du 18 mars 2011, p.S12)"));
						
		definitions.add(new Definition("Organisation, structure politique de l’État.", 3)
						.addExample("Toute nation a le gouvernement qu'elle mérite. Jean de Maistre"));
						
		definitions.add(new Definition("Ceux qui gouvernent un état et particulièrement le pouvoir exécutif.", 4)
						.addExample("Le gouvernement a pris des mesures impopulaires.")
						.addExample("Adresser une demande au gouvernement par l'entremise de son député.")
						.addExample("Ces deux gouvernements étaient d’accord pour signer ce traité."));
						
		definitions.add(new Definition("Charge de gouverneur dans une province, dans une ville, dans une place forte, dans une maison royale.", 5)
						.addDomain("histoire") 
						.addExample("Le roi lui donna le gouvernement de Normandie."));
						
		definitions.add(new Definition("La ville et le pays qui sont sous le pouvoir de ce gouverneur.", 6)
						.addDomain("par ext")
						.addExample("\"Le roi de Navarre et le duc d'Anjou ont fui la cour et se sont retirés, l'un dans son royaume, l'autre dans son gouvernement.\" — (Alexandre Dumas père, Henri III)"));
		
		result.put("gouvernement", definitions);
						
		return result;
	}
}
