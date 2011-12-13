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
		definitions.add(new Definition("* Cérémonie ou prestation réservée à un nouvel arrivant, consistant généralement à lui souhaiter la bienvenue et à l’aider dans son intégration ou ses démarches.\n"
										+ "Ex:  Nous réservâmes aux nouveaux venus un accueil qui fut cordial et empressé, mais le temps n'était pas aux effusions et d'un commun avis, il fallait agir vite.\n"
										+ "Ex:  Partout elle avait trouvé bon accueil, prompt assentiment, mais elle se propose d'aller plus outre.\n"
										+ "Ex:  Le Maire rappelle au conseil municipal que l’accueil périscolaire aura lieu à la rentrée de septembre dans la salle de réunions et le petit local attenant."));
		definitions.add(new Definition("* Lieu où sont accueillies les personnes.\n"
										+ "Ex:  À l’accueil, ils t’expliqueront comment aller à son bureau."));
		definitions.add(new Definition("*  Fait d’accueillir ou héberger.\n"
										+	"Ex:  Il fait accueil à tous ceux qui vont chez lui.\n"
										+	"Ex:  Je vous remercie de m’avoir fait accueil."));
		
		EXPECTED.put("accueil", definitions);
/*		
		definitions = new ArrayList<Definition>(3);
		
		definitions.add("# {{ling|fr}} [[ouvrage|Ouvrage]] de [[rÃ©fÃ©rence]] qui [[rÃ©pertorier|rÃ©pertorie]] des [[mot]]s dans un ordre convenu ([[alphabÃ©tique]] en gÃ©nÃ©ral) pour leur [[associer]] par exemple :
						## une [[dÃ©finition]], un [[sens]]
						##* ''Le '''dictionnaire''' de lâAcadÃ©mie franÃ§aise.''
						##* ''Les articles dâun '''dictionnaire'''.''
						##*  ''Les '''dictionnaires''' sont irremplaÃ§ables parce quâils sont lâexpression des connaissances et de lâidÃ©ologie dominante Ã  un moment donnÃ© de lâhistoire.'' {{source|Elisabeth Badinter, prÃ©face de : Samuel Souffi et Jean Pruvost, ''La mÃ¨re'', HonorÃ© Champion}}.
						## un ou plusieurs [[synonyme]]s, [[antonyme]]s, etc.
						##* ''Je te recommande ce '''dictionnaire''' des synonymes.''
						##* '''''Dictionnaire''' des homonymes.''
						## une [[Ã©tymologie]]
						##* ''Un '''dictionnaire''' Ã©tymologique.''
						## [[et/ou]] une [[traduction]].
						##* ''Le '''dictionnaire''' latin-franÃ§ais, franÃ§ais-latin.''
						## Il peut se contenter dâattester la simple existence dâun mot de faÃ§on [[normatif|normative]], sans donner aucune autre information.
						##*''Le '''dictionnaire''' du Scrabble.''
						# {{par ext}} [[recueil|Recueil]] fait par [[ordre]] [[alphabÃ©tique]] sur des [[matiÃ¨re]]s de [[littÃ©rature]], de [[science]]s ou dâarts.
						#* '''''Dictionnaire''' de la Fable, de mÃ©decine, de chimie, de chirurgie.''
						#* '''''Dictionnaire''' raisonnÃ© des arts et des sciences.''
						# {{littÃ©rature|fr}} [[livre|Livre]], recueil de pensÃ©es, dâopinions, dâun auteur, publiÃ© dans un ordre alphabÃ©tique.
						#*''Le '''dictionnaire''' des idÃ©es reÃ§ues.''
						#*''Le '''dictionnaire''' philosophique de Voltaire.''
						# {{figurÃ©|fr}} {{familier|fr}} Personne de grande [[Ã©rudition]], qui a des connaissances Ã©tendues et qui les communique [[aisÃ©ment]].
						#* ''Câest un '''dictionnaire''' vivant !''
						# {{dÃ©suet|fr}} [[vocabulaire|Vocabulaire]], ensemble des mots dont se sert un [[Ã©crivain]].
						#* ''Le '''dictionnaire''' de Bossuet est trÃ¨s Ã©tendu.''");
		
		
		EXPECTED.put("dictionnaire", definitions);
		
		definitions = new ArrayList<Definition>(10);
		
		definitions.add("# {{gÃ©omÃ©trie|fr}} [[ellipse|Ellipse]] constituÃ©e de tous les [[point]]s [[Ã©quidistant]]s dâun point donnÃ©, nommÃ© [[centre]].
						#* ''Le '''cercle''' peut Ãªtre considÃ©rÃ© comme un polygone avec un nombre infini de cÃ´tÃ©s.''
						# {{mÃ©ton|fr}} [[ligne|Ligne]] [[circulaire]], [[circonfÃ©rence]].
						#* ''Le '''cercle''' se divise en trois cent soixante degrÃ©s.''
						#* ''Tracer un '''cercle'''.''
						# {{par ext|fr}} Toute [[piÃ¨ce]] de [[mÃ©tal]] ou dâautre [[matiÃ¨re]], formant un '''cercle''', quâon met [[autour]] dâune chose pour la [[serrer]], la [[lier]] ou lâ[[orner]].
						#* ''Mettre un '''cercle''' de fer Ã  une colonne, Ã  une poutre pour lâempÃªcher dâÃ©clater.''
						# {{particulier}} {{cuisine|fr}} [[nonette|Nonette]] de forme [[cylindrique]], servant Ã  [[confectionner]] des [[gÃ¢teau]]x ou des [[entrÃ©e]]s.
						#* ''Un '''cercle''' redimensionnable est indispensable pour confectionner un bavarois.''
						# {{particulier}} [[cerceau|Cerceau]] de [[tonneau]], de [[cuve]].
						#* ''Un '''cercle''' de fer. â Un tonneau qui a rompu ses '''cercles'''.''
						# {{mÃ©ton|fr}} [[tonneau|Tonneau]], [[cuve]]
						#* ''Vin en '''cercles'''.''
						#  Objet ou [[instrument]] de forme [[circulaire]].
						#* '''''Cercle''' dâarpenteur.''
						# {{astro|fr}} Ligne [[circulaire]] [[fictif|fictives]] qui [[servir|sert]] Ã  [[reprÃ©senter]] le [[mouvement]] des [[astre]]s, la [[succession]] des [[saison]]s, les [[division]]s de la sphÃ¨re, etc.
						#* ''Le '''cercle''' polaire arctique.''
						#* ''Le '''cercle''' polaire antarctique.''
						# Toute [[disposition]] dâ[[objet]]s qui [[offre]] Ã  peu prÃ¨s la [[figure]] dâune [[circonfÃ©rence]].
						#* ''Ils formÃ¨rent un '''cercle''' autour de lui.''
						# {{figurÃ©|fr}} [[rÃ©union|RÃ©union]], [[assemblÃ©e]] dâun petit [[nombre]] de personnes ayant les [[mÃªme]]s [[idÃ©e]]s, les [[mÃªme]]s [[goÃ»t]]s ou partageant la mÃªme [[activitÃ©]].
						#* ''Il faut convenir que voilÃ  un '''cercle''' rÃ©publicain qui a tout lâair dâune sacristie ! Ne pas Ãªtre catholique pratiquant, apostolique convaincu et romain de derriÃ¨re les fagots, suffirait-il, en lâan de grÃ¢ce 1881, pour se voir fermer les portes dâ '''cercle''' rÃ©publicain ?'' {{source|L'Univers israÃ©lite: journal des principes conservateurs du judaÃ¯sme, p.185, 1882}}
						# {{particulier}} [[association|Association]] dont les [[membre]]s se [[rÃ©unir|rÃ©unissent]] dans un [[local]] [[louer|louÃ©]] Ã  [[frais]] [[commun]]s pour [[causer]], [[jouer]], [[lire]] les journaux, etc.
						#* ''Il y a cinq ou six '''cercles''' de jeu autorisÃ©s, rien quâÃ  Cavaillon oÃ¹ l'on compte 12000 habitants.'' {{source|{{w|Ludovic Naudeau}}, ''La France se regarde. Le problÃ¨me de la natalitÃ©'' -1931}}
						# {{particulier}} {{Belgique|fr}} [[endroit|Endroit]] oÃ¹ se [[retrouvent]] des [[Ã©tudiant]]s dans un but [[festif]], [[bibitif]] et [[culturel]].
						#* ''On va squetter des pintes au '''cercle''' pour commencer la guindaille.''
						# {{figurÃ©|fr}} [[domaine|Domaine]], [[Ã©tendue]], [[limite]]s.
						#* ''Cet homme nâest jamais sorti du '''cercle''' de ses occupations habituelles.''
						#* ''Agrandir, Ã©tendre le '''cercle''' de ses idÃ©es, de ses connaissances.''
						# {{term|Mali}} [[district|District]] [[administratif]].
						# {{vieilli|fr}} [[cycle|Cycle]].
						#* ''Le '''cercle''' des saisons.''");
		
		EXPECTED.put("cercle", definitions);
		
		definitions = new ArrayList<Definition>(10);
		
		definitions.add("# [[interprÃ©ter|InterprÃ©ter]] des [[information]]s Ã©crites sous forme de [[mot]]s ou de [[dessin]]s sur un [[support]].
								 #* ''[â¦]; je me souviens dâ'''avoir lu''' autrefois, dans un manuel de Paul Bert, que le principe fondamental de la morale s'appuie sur les enseigneÂ­ments de Zoroastre et sur la Constitution de l'an III ; [â¦].''  {{source|{{w|Georges Sorel}}, ''RÃ©flexions sur la violence'', Chap.VII, ''La morale des producteurs'', 1908, p.315}}
								 #* ''[â¦], mais vous comprenez bien quâon ne donne pas une Ã©gale attention Ã  tout ce quâon '''lit''' ou quâon parcourt dans les colonnes des journaux [â¦].'' {{source|{{w|Louis Pergaud}}, ''[[s:Un point dâhistoire|Un point dâhistoire]]'', dans ''{{w|Les Rustiques, nouvelles villageoises}}'', 1921}}
								 #* '' Il Ã©tait cÃ©lÃ¨bre par une obstination admirable Ã  apprendre Ã  Ã©crire et Ã  '''lire''' ; le rÃ©sultat ne fut pas Ã©tonnant ; il faut croire qu'il est bien difficile d'apprendre Ã  '''lire''' ; [â¦].'' {{source|[[w:Alain (philosophe)|Alain]], ''Souvenirs de guerre'', p.75, Hartmann, 1937}}");
		
		definitions.add(new Definition("# [[suivre|Suivre]] des [[yeux]] ce qui est [[Ã©crit]] ou [[imprimer|imprimÃ©]], [[avec]] la [[connaissance]] des sons que les [[lettre]]s [[figurent]]; [[soit]] en ne [[profÃ©rer|profÃ©rant]] pas les mots, [[Ãªtre|soit]] en les profÃ©rant Ã  [[haut]]e [[voix]].
										   #* ''Apprendre Ã  '''lire'''.''
										   #* '''''Lire''' couramment.''
										   #* ''Il ne sait ni '''lire''' ni Ã©crire.''
										   #* ''Il '''lit''' bien le grec, lâhÃ©breu.''
										   #* ''Il sâest fatiguÃ© la vue Ã  '''lire''' de vieux manuscrits.''
										   #* ''Une Ã©criture difficile Ã  '''lire'''.''"));
		
		definitions.add(new Definition("# {{figurÃ©|fr}} [[pÃ©nÃ©trer|PÃ©nÃ©trer]] [[quelque chose]] dâ[[obscur]] ou de [[cacher|cachÃ©]].
										   #* ''L'ivresse se '''lisait''' dans ses yeux, une ivresse crÃ¢ne et satisfaite qui lui arrachait quelquefois de gros rires.'' {{source|{{w|Francis Carco}}, ''Messieurs les vrais de vrai'', 1927}}
										   #* ''A quoi songes-tu donc ? s'informa la maÃ®tresse du logis, surprise de l'inattention qu'elle '''lisait''' dans les yeux de l'artiste. Tu n'as pas de contrariÃ©tÃ©s.'' {{source|{{w|Francis Carco}}, ''L'Homme de Minuit'', 1938}}
										   #* '''''Lire''' dans la pensÃ©e, dans le cÅur, dans les yeux de quelquâun.''
										   #* ''Je '''lis''' sur votre visage que vous Ãªtes mÃ©content.''
										   #* '''''Lire''' dans les astres, dans lâavenir.''"));
		
		definitions.add(new Definition("# [[monnaie|Monnaie]] utilisÃ©e en [[Italie]] avant lâusage de lâ[[euro]].
										   #* ''Un euro vaut environ 1936 '''lires'''.''"));
		definitions.add(new Definition("# [[prendre connaissance|Prendre connaissance]] et [[comprendre]] un texte Ã©crit.
		#* ''Il est de jeunes Ã©crivains qui ne '''lisent''' pas Hugo. C'est pourquoi ils ont tant de certitudes heureuses et atteignent promptement au talent.'' {{source|{{w|Victor MÃ©ric}}, ''Les compagnons de l'Escopette'', 1930, p.67}}
		# [[comprendre|Comprendre]] ce qui est Ã©crit ou imprimÃ© dans une [[langue]] [[Ã©tranger|Ã©trangÃ¨re]].
		#* ''Il ne parle pas lâanglais, mais il le '''lit''' avec assez de facilitÃ©.''
		# {{musique|fr}} {{analogie|fr}} Parcourir des yeux une musique notÃ©e, avec la connaissance des sons que les notes figurent et des diverses modifications que ces sons doivent recevoir.
		#* '''''Lire''' une partition''
		# [[prononcer|Prononcer]] Ã  haute [[voix]], avec lâ[[intonation]] voulue, ce qui est Ã©crit ou imprimÃ©.
		#* ''Il '''lit''' bien, il '''lit''' mal.''
		#* ''Il '''lit''' distinctement.''
		#* ''Il ne sait pas '''lire'''.''
		#* ''Il nous a '''lu''' un long discours.''
		#* ''Je vais vous '''lire''' mes vers.''
		#* ''Ce prince avait lâhabitude de se faire '''lire''' quelque bon livre pendant ses repas.''
		# Sâ[[instruire]], sâ[[amuser]], sâ[[informer]], etc. par la lecture.
		#* '''''Lire''' un volume de vers.''
		#* '''''Lire''' un roman.''
		#* '''''Lire''' un billet, une lettre.''
		#* '''''Lire''' la messe.''
		#* '''''Lire''' une dÃ©pÃªche chiffrÃ©e.''
		#* {{absolument}} ''Il passe son temps Ã  '''lire'''.''
		#* {{figurÃ©|fr}} ''Câest un ouvrage quâon ne peut '''lire''','' se dit dâun Ouvrage ennuyeux, ou mal Ã©crit, ou surtout licencieux.
		#* {{figurÃ©|fr}} et {{familier|fr}} ''Ce livre, cet ouvrage se laisse '''lire''','' On le lit sans fatigue, sans ennui.
		# Se dit encore en parlant de Quelque [[livre]] quâun [[professeur]] [[expliquer|explique]] ou fait expliquer Ã  ses [[auditeur]]s et quâil prend pour [[sujet]] des [[leÃ§on]]s quâil [[leur]] [[donne]].
		#* ''Notre professeur nous '''lisait''' HomÃ¨re.''"));
		
		EXPECTED.put("lire", definitions);
 */
	}
}
