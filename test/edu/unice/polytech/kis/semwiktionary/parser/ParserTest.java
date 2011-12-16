package edu.unice.polytech.kis.semwiktionary.parser;


import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;


public class ParserTest {
	
	private static List<String> unexpectedTitles;
	private static Map<String, List<Definition>> expected;
	
	
	@BeforeClass
	public static void classSetUp() throws Exception {
		expected = generateExpectedContent();
		
		unexpectedTitles = new ArrayList<String>(2);
		unexpectedTitles.add("MediaWiki:Disclaimers");
		unexpectedTitles.add("Discussion utilisateur:Hippietrail");
	}
	
	@Test
	public void allWordsExist() {
		for (String someWord : expected.keySet())
			assertTrue("'" + someWord + "' pretends not to exist in the database (check with WordTest first)!", Word.exists(someWord));
	}
	
	@Test
	public void titlesDefinitionsMatch() {
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet()) {
			Word myWord = Word.from(currentEntry.getKey());
			assertEquals("Incorrect definitions for word '" + currentEntry.getKey() + "'", currentEntry.getValue(), myWord.getDefinitions());
		}
	}
	
	@Test
	public void pagesThatAreNotWordsAreNotStored() {
		for (String someWord : unexpectedTitles)
			assertFalse(someWord + " should exist in the database!", Word.exists(someWord));
	}
	
	
	public static Map<String, List<Definition>> generateExpectedContent() {
		Map<String, List<Definition>> result = new HashMap<String, List<Definition>>();
		
		List<Definition> definitions = new ArrayList<Definition>(3);
		definitions.add(new Definition("* Cérémonie ou prestation réservée à un nouvel arrivant, consistant généralement à lui souhaiter la bienvenue et à l’aider dans son intégration ou ses démarches.\n"
									   + "Ex:  Nous réservâmes aux nouveaux venus un accueil qui fut cordial et empressé, mais le temps n'était pas aux effusions et d'un commun avis, il fallait agir vite.\n"
									   + "Ex:  Partout elle avait trouvé bon accueil, prompt assentiment, mais elle se propose d'aller plus outre.\n"
									   + "Ex:  Le Maire rappelle au conseil municipal que l'accueil périscolaire aura lieu à la rentrée de septembre dans la salle de réunions et le petit local attenant."));
		definitions.add(new Definition("* Lieu où sont accueillies les personnes.\n"
									   + "Ex:  À l�''accueil, ils t’expliqueront comment aller à son bureau."));
		definitions.add(new Definition("*  Fait d�[[accueillir ou héberger.\n"
									   + "Ex:  Il fait accueil à tous ceux qui vont chez lui.\n"
									   + "Ex:  Je vous remercie de m’avoir fait accueil."));
		
		result.put("accueil", definitions);
		
		definitions = new ArrayList<Definition>(5);
		
		definitions.add(new Definition("* Ouvrage de référence qui répertorie des mots dans un ordre convenu( alphabétique en général) pour leur associer par exemple:\n"
									   + "** une définition, un sens\n"
									   + "Ex:  Le dictionnaire de l’Académie française.\n"
									   + "Ex:  Les  d’un dictionnaire.\n"
									   + "Ex:  Les dictionnaires sont irremplaçables parce qu’ils sont l’expression des connaissances et de l’idéologie dominante à un moment donné de l’histoire.\n"
									   + "** un ou plusiearticlesurs synonymes, antonymes, etc.\n"
									   + "Ex:  Je te recommande ce dictionnaire des synonymes.\n"
									   + "Ex:  Dictionnaire des homonymes.\n"
									   + "** une étymologie\n"
									   + "Ex:  Un dictionnaire étymologique.\n"
									   + "**normative, sans donner aucune autre information.\n"
									   + "Ex:  Le dictionnaire du Scrabble."));
		definitions.add(new Definition("* Recueil fait par ordre alphabétique sur des matières de littérature, de sciences ou d’arts.\n"
									   + "Ex:  Dictionnaire de la Fable, de médecine, de chimie, de chirurgie.\n"
									   + "Ex:  Dictionnaire raisonné des arts et des sciences."));
		definitions.add(new Definition("* Livre, recueil de pensées, d’opinions, d’un auteur, publié dans un ordre alphabétique.\n"
									   + "Ex:  Le dictionnaire des idées reçues.\n"
									   + "Ex:  Le dictionnaire philosophique de Voltaire."));
		definitions.add(new Definition("*  Personne de grande érudition, qui a des connaissances étendues et qui les communique aisément.\n"
									   + "Ex: C’est un dictionnaire vivant"));
		definitions.add(new Definition("* Vocabulaire, ensemble des mots dont se sert un écrivain.\n"
									   + "Ex:  Le dictionnaire de Bossuet est très étendu."));
		
		result.put("dictionnaire", definitions);
		
		definitions = new ArrayList<Definition>(16);
		
		definitions.add(new Definition("* Ellipse constituée de tous les points équidistants d’un point donné, nommé centre.\n"
									   + "Ex:  Le cercle peut être considéré comme un polygone avec un nombre infini de côtés."));
		definitions.add(new Definition("* Ligne circulaire, circonférence.\n"
									   + "Ex:  Le cercle se divise en trois cent soixante degrés.\n"
									   + "Ex:  Tracer un cercle."));
		definitions.add(new Definition("*  Toute pièce de métal ou d’autre matière, formant un cercle, qu’on met autour d’une chose pour la serrer, la lier ou l�[[orner.\n"
									   + "Ex:  Mettre un cercle de fer à une colonne, à une poutre pour l’empêcher d’éclater."));
		definitions.add(new Definition("* Nonette de forme cylindrique, servant à confectionner des gâteaux ou des entrées.\n"
									   + "Ex:  Un cercle redimensionnable est indispensable pour confectionner un bavarois."));
		definitions.add(new Definition("* Cerceau de tonneau, de cuve.\n"
									   + "Ex:  Un cercle de fer.� �� Un tonneau qui a rompu ses cercles."));
		definitions.add(new Definition("* Tonneau, cuve\n"
									   + "Ex:  Vin en cercles."));
		definitions.add(new Definition("*  Objet ou instrument de forme circulaire.\n"
									   + "Ex:  Cercle d’arpenteur."));
		definitions.add(new Definition("*  Ligne circulaire fictives qui sert à représenter le mouvement des astres, la succession des saisons, les divisions de la sphère, etc.\n"
									   + "Ex:  Le cercle polaire arctique.\n"
									   + "Ex:  Le cercle polaire antarctique."));
		definitions.add(new Definition("*  Toute disposition d�[[objet s qui offre à peu près la figure d’une circonférence.\n"
									   + "Ex:  Ils formèrent un cercle autour de lui."));
		definitions.add(new Definition("* Réunion, assemblée d’un petit nombre de personnes ayant les mêmes idées, les mêmes goûts ou partageant la même activité.\n"
									   + "Ex:  Il faut convenir que voilà un cercle républicain qui a tout l’air d’une sacristie Ne pas être catholique pratiquant, apostolique convaincu et romain de derrière les fagots, suffirait il, en l’an de grâce, pour se voir fermer les portes d’ cercle républicain"));
		definitions.add(new Definition("* Association dont les membres se réunissent dans un local loué à frais communs pour causer, jouer, lire les journaux, etc.\n"
									   + "Ex:  Il y a cinq ou six cercles de jeu autorisés, rien qu’à Cavaillon où l'on compte habitants."));
		definitions.add(new Definition("* Endroit où se retrouvent des étudiants dans un but festif, bibitif et culturel.\n"
									   + "Ex:  On va squetter des pintes au cercle pour commencer la guindaille."));
		definitions.add(new Definition("* Domaine, étendue, limites.\n"
									   + "Ex:  Cet homme n’est jamais sorti du cercle de ses occupations habituelles.\n"
									   + "Ex:  Agrandir, étendre le cercle de ses idées, de ses connaissances."));
		definitions.add(new Definition("* District administratif."));
		definitions.add(new Definition("* Cycle.\n"
									   + "Ex:  Le cercle des saisons."));
		
		result.put("cercle", definitions);
		
		definitions = new ArrayList<Definition>(11);
		
		definitions.add(new Definition("* Interpréter des informations écrites sous forme de mots ou de dessins sur un support.\n"
									   + "Ex:  ��; je me souviens d’ avoir lu autrefois, dans un manuel de Paul Bert, que le principe fondamental de la morale s'appuie sur les enseigne ments de Zoroastre et sur la Constitution de l'an III;� ��.\n"
									   + "Ex:  ��, mais vous comprenez bien qu’on ne donne pas une égale attention à tout ce qu’on lit ou qu’on parcourt dans les colonnes des journaux� ��.\n"
									   + "Ex:  Il était célèbre par une obstination admirable à apprendre à écrire et à lire; le résultat ne fut pas étonnant; il faut croire qu'il est bien difficile d'apprendre à lire;� ��."));
		definitions.add(new Definition("* Interpréter des informations écrites sous forme de mots ou de dessins sur un support.\n"
									   + "Ex:  ��; je me souviens d’ avoir lu autrefois, dans un manuel de Paul Bert, que le principe fondamental de la morale s'appuie sur les enseigne ments de Zoroastre et sur la Constitution de l'an III;� ��.\n"
									   + "Ex:  ��, mais vous comprenez bien qu’on ne donne pas une égale attention à tout ce qu’on lit ou qu’on parcourt dans les colonnes des journaux� ��.\n"
									   + "Ex:  Il était célèbre par une obstination admirable à apprendre à écrire et à lire; le résultat ne fut pas étonnant; il faut croire qu'il est bien difficile d'apprendre à lire;� ��."));
		definitions.add(new Definition("*  prendre connaissance Prendre connaissance et comprendre un texte écrit.\n"
									   + "Ex:  Il est de jeunes écrivains qui ne lisent pas Hugo. C'est pourquoi ils ont tant de certitudes heureuses et atteignent promptement au talent."));
		definitions.add(new Definition("* Comprendre ce qui est écrit ou imprimé dans une langue étrangère.\n"
									   + "Ex:  Il ne parle pas l’anglais, mais il le lit avec assez de facilité."));
		definitions.add(new Definition("*  Parcourir des yeux une musique notée, avec la connaissance des sons que les notes figurent et des diverses modifications que ces sons doivent recevoir.\n"
									   + "Ex:  Lire une partition"));
		definitions.add(new Definition("* Prononcer à haute voix, avec l�[[intonation voulue, ce qui est écrit ou imprimé.\n"
									   + "Ex:  Il lit bien, il lit mal.\n"
									   + "Ex:  Il lit distinctement.\n"
									   + "Ex:  Il ne sait pas lire.\n"
									   + "Ex:  Il nous a lu un long discours.\n"
									   + "Ex:  Je vais vous lire mes vers.\n"
									   + "Ex:  Ce prince avait l’habitude de se faire lire quelque bon livre pendant ses repas."));
		definitions.add(new Definition("*  S�[[instruire, s�[[amuser, s�[[informer, etc. par la lecture.\n"
									   + "Ex:  Lire un volume de vers.\n"
									   + "Ex:  Lire un roman.\n"
									   + "Ex:  Lire un billet, une lettre.\n"
									   + "Ex:  Lire la messe.\n"
									   + "Ex:  Lire une dépêche chiffrée.\n"
									   + "Ex:  Il passe son temps à lire.\n"
									   + "Ex: C’est un ouvrage qu’on ne peut lire, se dit d’un Ouvrage ennuyeux, ou mal écrit, ou surtout licencieux.\n"
									   + "Ex:  Ce livre, cet ouvrage se laisse lire, On le lit sans fatigue, sans ennui."));
		definitions.add(new Definition("*  Se dit encore en parlant de Quelque livre qu’un professeur explique ou fait expliquer à ses auditeurs et qu’il prend pour sujet des leçons qu’il leur donne.\n"
									   + "Ex:  Notre professeur nous lisait Homère."));
		definitions.add(new Definition("* Pénétrer quelque chose d�[[obscur ou de caché.\n"
									   + "Ex: L'ivresse se lisait dans ses yeux, une ivresse crâne et satisfaite qui lui arrachait quelquefois de gros rires.\n"
									   + "Ex:  A quoi songes tu donc s'informa la maîtresse du logis, surprise de l'inattention qu'elle lisait dans les yeux de l'artiste. Tu n'as pas de contrariétés.\n"
									   + "Ex:  Lire dans la pensée, dans le cœur, dans les yeux de quelqu’un.\n"
									   + "Ex:  Je lis sur votre visage que vous êtes mécontent.\n"
									   + "Ex:  Lire dans les astres, dans l’avenir."));
		definitions.add(new Definition("* Monnaie utilisée en Italie avant l’usage de l�[[euro.\n"
									   + "Ex:  Un euro vaut environ lires."));
		definitions.add(new Definition("* Lyre.\n"
									   + "Ex:  De vièle sot et de rote,\n"
									   + " De lire et de satérion,\n"
									   + " De harpe sot et de choron,\n"
									   + "(� ��)"));
		
		result.put("lire", definitions);
		
		return result;
	}
}
