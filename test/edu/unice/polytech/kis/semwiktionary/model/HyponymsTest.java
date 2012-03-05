package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import edu.unice.polytech.kis.semwiktionary.model.Word;


public class HyponymsTest {

	private static final Map<String, List<String>> EXPECTED_HYPONYMS = new HashMap<String, List<String>>();
	
	static {
		List<String> hyponymsForCéréale = new ArrayList<String>();
		hyponymsForCéréale.add("épeautre");
		hyponymsForCéréale.add("escourgeon");
		hyponymsForCéréale.add("froment");
		hyponymsForCéréale.add("seigle");
		hyponymsForCéréale.add("alpiste des canaries");
		hyponymsForCéréale.add("avoine");
		hyponymsForCéréale.add("blé");
		hyponymsForCéréale.add("coix");
		hyponymsForCéréale.add("éleusine cultivée");
		hyponymsForCéréale.add("fonio");
		hyponymsForCéréale.add("kamut");
		hyponymsForCéréale.add("maïs");
		hyponymsForCéréale.add("mil");
		hyponymsForCéréale.add("orge");
		hyponymsForCéréale.add("quinoa");
		hyponymsForCéréale.add("riz");
		hyponymsForCéréale.add("sarrasin");
		hyponymsForCéréale.add("seigle");
		hyponymsForCéréale.add("sésame");
		hyponymsForCéréale.add("sorgho");
		hyponymsForCéréale.add("teff");
		hyponymsForCéréale.add("triticale");
		EXPECTED_HYPONYMS.put("céréale", hyponymsForCéréale);
		
		List<String> hyponymsForBle = new ArrayList<String>();
		hyponymsForBle.add("blé amidonnier");
		hyponymsForBle.add("blé dur");
		hyponymsForBle.add("blé tendre");
		hyponymsForBle.add("blé poulard");
		hyponymsForBle.add("engrain");
		hyponymsForBle.add("épeautre");
		hyponymsForBle.add("pain");
		hyponymsForBle.add("pâte");
		hyponymsForBle.add("semoule");
		EXPECTED_HYPONYMS.put("blé", hyponymsForBle);
		
		List<String> hyponymsForMil = new ArrayList<String>();
		hyponymsForMil.add("millet commun");
		hyponymsForMil.add("millet des oiseaux");
		hyponymsForMil.add("millet japonais");
		hyponymsForMil.add("millet perle");
		EXPECTED_HYPONYMS.put("mil", hyponymsForMil);
		
		List<String> hyponymsForSlave = new ArrayList<String>();
		hyponymsForSlave.add("polonais");
		hyponymsForSlave.add("tchèque");
		hyponymsForSlave.add("slovaque");
		hyponymsForSlave.add("sorabe");
		hyponymsForSlave.add("kachoube");
		hyponymsForSlave.add("polabe");
		hyponymsForSlave.add("slovince");
		hyponymsForSlave.add("serbo-croate");
		hyponymsForSlave.add("bulgare");
		hyponymsForSlave.add("macédonien");
		hyponymsForSlave.add("slovène");
		hyponymsForSlave.add("biélorusse");
		hyponymsForSlave.add("russe");
		hyponymsForSlave.add("rusyn");
		hyponymsForSlave.add("ruthène");
		hyponymsForSlave.add("ukrainien");
		EXPECTED_HYPONYMS.put("slave", hyponymsForSlave);
		
		List<String> hyponymsForSorabe = new ArrayList<String>();
		hyponymsForSorabe.add("haut-sorabe");
		hyponymsForSorabe.add("bas-sorabe");
		EXPECTED_HYPONYMS.put("sorabe", hyponymsForSorabe);
		
		List<String> hyponymsForSerboCroate = new ArrayList<String>();
		hyponymsForSerboCroate.add("bosniaque");
		hyponymsForSerboCroate.add("croate");
		hyponymsForSerboCroate.add("serbe");
		hyponymsForSerboCroate.add("monténégrin");
		EXPECTED_HYPONYMS.put("serbo-croate", hyponymsForSerboCroate);
	}
	
	private static Word subject;
	
	@Test
	public void allHyponymsExist() {
		for (String subjectStr : EXPECTED_HYPONYMS.keySet()) {
			subject = Word.find(subjectStr);
			
			for (String expHyp : EXPECTED_HYPONYMS.get(subjectStr)) {
				boolean exists = false;
				for (Word actHyp : subject.getHyponyms()) {
					if (actHyp.equals(Word.find(expHyp))) {
						exists = true;
					}
				}
				assertTrue("The hyponym " + expHyp + " of word " + subject + " does not exist.", exists);
			}
		}
	}
}