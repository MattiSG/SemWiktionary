package edu.unice.polytech.kis.semwiktionary.parser;


import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;


public class ParserTest {
	
	private static Map<String, List<Definition>> EXPECTED;
	
	
	@Test
	public void allWordsExist() {
		for (String someWord : EXPECTED.keySet())
			assertNotNull("'" + someWord + "' was not found in the database!", Word.from(someWord));
	}
	
	@Test
	public void titlesDefinitionsMatch() {
		for (Map.Entry<String, List<Definition>> currentEntry : EXPECTED.entrySet()) {
			Word myWord = Word.from(currentEntry.getKey());
			assertEquals(myWord.getDefinitions(), currentEntry.getValue());
		}
	}
	
	@BeforeClass
	public static void classSetUp() throws Exception {
		EXPECTED = new HashMap<String, List<Definition>>();
		
		List<Definition> definitions = new ArrayList<Definition>(3);
		definitions.add(new Definition("# [[c�r�monie|C�r�monie]] ou [[prestation]] r�serv�e � un nouvel [[arrivant]], consistant g�n�ralement � lui [[souhaiter]] la [[bienvenue]] et � l�aider dans son [[int�gration]] ou ses [[d�marche]]s.\n"
									   + "#* ''Nous r�serv�mes aux nouveaux venus un '''accueil''' qui fut cordial et empress�, mais le temps n'�tait pas aux effusions et d'un commun avis, il fallait agir vite.'' {{source|{{w|Jean-Baptiste Charcot}}, ''Dans la mer du Groenland'', 1928}}\n"
									   + "#* ''Partout elle avait trouv� bon '''accueil''', prompt assentiment, mais elle se propose d'aller plus outre.'' {{source|{{w|Jean Rogissart}}, ''Passantes d'Octobre'', 1958}}\n"
									   + "#* ''Le Maire rappelle au conseil municipal que l�'''accueil''' p�riscolaire aura lieu � la rentr�e de septembre 2008-2009 dans la salle de r�unions et le petit local attenant.'' {{source|[[Le Chesne]], Conseil municipal du 27 juin 2008}}"));
		definitions.add(new Definition("# [[lieu|Lieu]] où sont accueillies les [[personne]]s.\n"
									   + "#* ''À l’'''accueil''', ils t’expliqueront comment aller à son bureau.''"));
		definitions.add(new Definition("# {{vieilli|fr}} Fait d’[[accueillir]] ou [[héberger]].\n"
									   + "#* ''Il fait '''accueil''' à tous ceux qui vont chez lui.''"
									   + "#* ''Je vous remercie de m’avoir fait '''accueil'''.''"));
		
		EXPECTED.put("accueil", definitions);
/*		
		definitions = new ArrayList<Definition>(3);
		
		definitions.add("# {{ling|fr}} [[ouvrage|Ouvrage]] de [[référence]] qui [[répertorier|répertorie]] des [[mot]]s dans un ordre convenu ([[alphabétique]] en général) pour leur [[associer]] par exemple :
						## une [[définition]], un [[sens]]
						##* ''Le '''dictionnaire''' de l’Académie française.''
						##* ''Les articles d’un '''dictionnaire'''.''
						##*  ''Les '''dictionnaires''' sont irremplaçables parce qu’ils sont l’expression des connaissances et de l’idéologie dominante à un moment donné de l’histoire.'' {{source|Elisabeth Badinter, préface de : Samuel Souffi et Jean Pruvost, ''La mère'', Honoré Champion}}.
						## un ou plusieurs [[synonyme]]s, [[antonyme]]s, etc.
						##* ''Je te recommande ce '''dictionnaire''' des synonymes.''
						##* '''''Dictionnaire''' des homonymes.''
						## une [[étymologie]]
						##* ''Un '''dictionnaire''' étymologique.''
						## [[et/ou]] une [[traduction]].
						##* ''Le '''dictionnaire''' latin-français, français-latin.''
						## Il peut se contenter d’attester la simple existence d’un mot de façon [[normatif|normative]], sans donner aucune autre information.
						##*''Le '''dictionnaire''' du Scrabble.''
						# {{par ext}} [[recueil|Recueil]] fait par [[ordre]] [[alphabétique]] sur des [[matière]]s de [[littérature]], de [[science]]s ou d’arts.
						#* '''''Dictionnaire''' de la Fable, de médecine, de chimie, de chirurgie.''
						#* '''''Dictionnaire''' raisonné des arts et des sciences.''
						# {{littérature|fr}} [[livre|Livre]], recueil de pensées, d’opinions, d’un auteur, publié dans un ordre alphabétique.
						#*''Le '''dictionnaire''' des idées reçues.''
						#*''Le '''dictionnaire''' philosophique de Voltaire.''
						# {{figuré|fr}} {{familier|fr}} Personne de grande [[érudition]], qui a des connaissances étendues et qui les communique [[aisément]].
						#* ''C’est un '''dictionnaire''' vivant !''
						# {{désuet|fr}} [[vocabulaire|Vocabulaire]], ensemble des mots dont se sert un [[écrivain]].
						#* ''Le '''dictionnaire''' de Bossuet est très étendu.''");
		
		
		EXPECTED.put("dictionnaire", definitions);
		
		definitions = new ArrayList<Definition>(10);
		
		definitions.add("# {{géométrie|fr}} [[ellipse|Ellipse]] constituée de tous les [[point]]s [[équidistant]]s d’un point donné, nommé [[centre]].
						#* ''Le '''cercle''' peut être considéré comme un polygone avec un nombre infini de côtés.''
						# {{méton|fr}} [[ligne|Ligne]] [[circulaire]], [[circonférence]].
						#* ''Le '''cercle''' se divise en trois cent soixante degrés.''
						#* ''Tracer un '''cercle'''.''
						# {{par ext|fr}} Toute [[pièce]] de [[métal]] ou d’autre [[matière]], formant un '''cercle''', qu’on met [[autour]] d’une chose pour la [[serrer]], la [[lier]] ou l’[[orner]].
						#* ''Mettre un '''cercle''' de fer à une colonne, à une poutre pour l’empêcher d’éclater.''
						# {{particulier}} {{cuisine|fr}} [[nonette|Nonette]] de forme [[cylindrique]], servant à [[confectionner]] des [[gâteau]]x ou des [[entrée]]s.
						#* ''Un '''cercle''' redimensionnable est indispensable pour confectionner un bavarois.''
						# {{particulier}} [[cerceau|Cerceau]] de [[tonneau]], de [[cuve]].
						#* ''Un '''cercle''' de fer. — Un tonneau qui a rompu ses '''cercles'''.''
						# {{méton|fr}} [[tonneau|Tonneau]], [[cuve]]
						#* ''Vin en '''cercles'''.''
						#  Objet ou [[instrument]] de forme [[circulaire]].
						#* '''''Cercle''' d’arpenteur.''
						# {{astro|fr}} Ligne [[circulaire]] [[fictif|fictives]] qui [[servir|sert]] à [[représenter]] le [[mouvement]] des [[astre]]s, la [[succession]] des [[saison]]s, les [[division]]s de la sphère, etc.
						#* ''Le '''cercle''' polaire arctique.''
						#* ''Le '''cercle''' polaire antarctique.''
						# Toute [[disposition]] d’[[objet]]s qui [[offre]] à peu près la [[figure]] d’une [[circonférence]].
						#* ''Ils formèrent un '''cercle''' autour de lui.''
						# {{figuré|fr}} [[réunion|Réunion]], [[assemblée]] d’un petit [[nombre]] de personnes ayant les [[même]]s [[idée]]s, les [[même]]s [[goût]]s ou partageant la même [[activité]].
						#* ''Il faut convenir que voilà un '''cercle''' républicain qui a tout l’air d’une sacristie ! Ne pas être catholique pratiquant, apostolique convaincu et romain de derrière les fagots, suffirait-il, en l’an de grâce 1881, pour se voir fermer les portes d’ '''cercle''' républicain ?'' {{source|L'Univers israélite: journal des principes conservateurs du judaïsme, p.185, 1882}}
						# {{particulier}} [[association|Association]] dont les [[membre]]s se [[réunir|réunissent]] dans un [[local]] [[louer|loué]] à [[frais]] [[commun]]s pour [[causer]], [[jouer]], [[lire]] les journaux, etc.
						#* ''Il y a cinq ou six '''cercles''' de jeu autorisés, rien qu’à Cavaillon où l'on compte 12000 habitants.'' {{source|{{w|Ludovic Naudeau}}, ''La France se regarde. Le problème de la natalité'' -1931}}
						# {{particulier}} {{Belgique|fr}} [[endroit|Endroit]] où se [[retrouvent]] des [[étudiant]]s dans un but [[festif]], [[bibitif]] et [[culturel]].
						#* ''On va squetter des pintes au '''cercle''' pour commencer la guindaille.''
						# {{figuré|fr}} [[domaine|Domaine]], [[étendue]], [[limite]]s.
						#* ''Cet homme n’est jamais sorti du '''cercle''' de ses occupations habituelles.''
						#* ''Agrandir, étendre le '''cercle''' de ses idées, de ses connaissances.''
						# {{term|Mali}} [[district|District]] [[administratif]].
						# {{vieilli|fr}} [[cycle|Cycle]].
						#* ''Le '''cercle''' des saisons.''");
		
		EXPECTED.put("cercle", definitions);
		
		definitions = new ArrayList<Definition>(10);
		
		definitions.add("# [[interpréter|Interpréter]] des [[information]]s écrites sous forme de [[mot]]s ou de [[dessin]]s sur un [[support]].
								 #* ''[…]; je me souviens d’'''avoir lu''' autrefois, dans un manuel de Paul Bert, que le principe fondamental de la morale s'appuie sur les enseigne­ments de Zoroastre et sur la Constitution de l'an III ; […].''  {{source|{{w|Georges Sorel}}, ''Réflexions sur la violence'', Chap.VII, ''La morale des producteurs'', 1908, p.315}}
								 #* ''[…], mais vous comprenez bien qu’on ne donne pas une égale attention à tout ce qu’on '''lit''' ou qu’on parcourt dans les colonnes des journaux […].'' {{source|{{w|Louis Pergaud}}, ''[[s:Un point d’histoire|Un point d’histoire]]'', dans ''{{w|Les Rustiques, nouvelles villageoises}}'', 1921}}
								 #* '' Il était célèbre par une obstination admirable à apprendre à écrire et à '''lire''' ; le résultat ne fut pas étonnant ; il faut croire qu'il est bien difficile d'apprendre à '''lire''' ; […].'' {{source|[[w:Alain (philosophe)|Alain]], ''Souvenirs de guerre'', p.75, Hartmann, 1937}}");
		
		definitions.add(new Definition("# [[suivre|Suivre]] des [[yeux]] ce qui est [[écrit]] ou [[imprimer|imprimé]], [[avec]] la [[connaissance]] des sons que les [[lettre]]s [[figurent]]; [[soit]] en ne [[proférer|proférant]] pas les mots, [[être|soit]] en les proférant à [[haut]]e [[voix]].
										   #* ''Apprendre à '''lire'''.''
										   #* '''''Lire''' couramment.''
										   #* ''Il ne sait ni '''lire''' ni écrire.''
										   #* ''Il '''lit''' bien le grec, l’hébreu.''
										   #* ''Il s’est fatigué la vue à '''lire''' de vieux manuscrits.''
										   #* ''Une écriture difficile à '''lire'''.''"));
		
		definitions.add(new Definition("# {{figuré|fr}} [[pénétrer|Pénétrer]] [[quelque chose]] d’[[obscur]] ou de [[cacher|caché]].
										   #* ''L'ivresse se '''lisait''' dans ses yeux, une ivresse crâne et satisfaite qui lui arrachait quelquefois de gros rires.'' {{source|{{w|Francis Carco}}, ''Messieurs les vrais de vrai'', 1927}}
										   #* ''A quoi songes-tu donc ? s'informa la maîtresse du logis, surprise de l'inattention qu'elle '''lisait''' dans les yeux de l'artiste. Tu n'as pas de contrariétés.'' {{source|{{w|Francis Carco}}, ''L'Homme de Minuit'', 1938}}
										   #* '''''Lire''' dans la pensée, dans le cœur, dans les yeux de quelqu’un.''
										   #* ''Je '''lis''' sur votre visage que vous êtes mécontent.''
										   #* '''''Lire''' dans les astres, dans l’avenir.''"));
		
		definitions.add(new Definition("# [[monnaie|Monnaie]] utilisée en [[Italie]] avant l’usage de l’[[euro]].
										   #* ''Un euro vaut environ 1936 '''lires'''.''"));
		definitions.add(new Definition("# [[prendre connaissance|Prendre connaissance]] et [[comprendre]] un texte écrit.
		#* ''Il est de jeunes écrivains qui ne '''lisent''' pas Hugo. C'est pourquoi ils ont tant de certitudes heureuses et atteignent promptement au talent.'' {{source|{{w|Victor Méric}}, ''Les compagnons de l'Escopette'', 1930, p.67}}
		# [[comprendre|Comprendre]] ce qui est écrit ou imprimé dans une [[langue]] [[étranger|étrangère]].
		#* ''Il ne parle pas l’anglais, mais il le '''lit''' avec assez de facilité.''
		# {{musique|fr}} {{analogie|fr}} Parcourir des yeux une musique notée, avec la connaissance des sons que les notes figurent et des diverses modifications que ces sons doivent recevoir.
		#* '''''Lire''' une partition''
		# [[prononcer|Prononcer]] à haute [[voix]], avec l’[[intonation]] voulue, ce qui est écrit ou imprimé.
		#* ''Il '''lit''' bien, il '''lit''' mal.''
		#* ''Il '''lit''' distinctement.''
		#* ''Il ne sait pas '''lire'''.''
		#* ''Il nous a '''lu''' un long discours.''
		#* ''Je vais vous '''lire''' mes vers.''
		#* ''Ce prince avait l’habitude de se faire '''lire''' quelque bon livre pendant ses repas.''
		# S’[[instruire]], s’[[amuser]], s’[[informer]], etc. par la lecture.
		#* '''''Lire''' un volume de vers.''
		#* '''''Lire''' un roman.''
		#* '''''Lire''' un billet, une lettre.''
		#* '''''Lire''' la messe.''
		#* '''''Lire''' une dépêche chiffrée.''
		#* {{absolument}} ''Il passe son temps à '''lire'''.''
		#* {{figuré|fr}} ''C’est un ouvrage qu’on ne peut '''lire''','' se dit d’un Ouvrage ennuyeux, ou mal écrit, ou surtout licencieux.
		#* {{figuré|fr}} et {{familier|fr}} ''Ce livre, cet ouvrage se laisse '''lire''','' On le lit sans fatigue, sans ennui.
		# Se dit encore en parlant de Quelque [[livre]] qu’un [[professeur]] [[expliquer|explique]] ou fait expliquer à ses [[auditeur]]s et qu’il prend pour [[sujet]] des [[leçon]]s qu’il [[leur]] [[donne]].
		#* ''Notre professeur nous '''lisait''' Homère.''"));
		
		EXPECTED.put("lire", definitions);
 */
	}
}
