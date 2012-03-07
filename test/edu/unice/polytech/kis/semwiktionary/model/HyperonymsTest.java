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


public class HyperonymsTest {

	private static final Map<String, List<String>> EXPECTED_HYPERONYMS = new HashMap<String, List<String>>();
	
	static {
		List<String> hyperonymsForCarre = new ArrayList<String>();
		hyperonymsForCarre.add("polygone");
		hyperonymsForCarre.add("puissance");
		EXPECTED_HYPERONYMS.put("carré", hyperonymsForCarre);
		
		List<String> hyperonymsForPolygone = new ArrayList<String>();
		hyperonymsForPolygone.add("quadrilatère");
		EXPECTED_HYPERONYMS.put("polygone", hyperonymsForPolygone);
		
		List<String> hyperonymsForQuadrilatere = new ArrayList<String>();
		hyperonymsForQuadrilatere.add("trapèze");
		EXPECTED_HYPERONYMS.put("quadrilatère", hyperonymsForQuadrilatere);
		
		List<String> hyperonymsForTrapeze = new ArrayList<String>();
		hyperonymsForTrapeze.add("parallélogramme");
		EXPECTED_HYPERONYMS.put("trapèze", hyperonymsForTrapeze);
		
		List<String> hyperonymsForParallelogramme = new ArrayList<String>();
		hyperonymsForParallelogramme.add("losange");
		hyperonymsForParallelogramme.add("rectangle");
		EXPECTED_HYPERONYMS.put("parallélogramme", hyperonymsForParallelogramme);
		
		List<String> hyperonymsForBasSorabe = new ArrayList<String>();
		hyperonymsForBasSorabe.add("sorabe");
		EXPECTED_HYPERONYMS.put("bas-sorabe", hyperonymsForBasSorabe);
		
		List<String> hyperonymsForSorabe = new ArrayList<String>();
		hyperonymsForSorabe.add("slave");
		EXPECTED_HYPERONYMS.put("sorabe", hyperonymsForSorabe);
	}
	
	private static Word subject;
	
	@Test
	public void allHyperonymsExist() {
		for (String subjectStr : EXPECTED_HYPERONYMS.keySet()) {
			subject = Word.find(subjectStr);
			
			for (String expHyp : EXPECTED_HYPERONYMS.get(subjectStr)) {
				assertTrue("The hyperonym " + expHyp + " of word " + subject + " does not exist.",
						   subject.getHyperonyms().contains(Word.find(expHyp)));
			}
		}
	}
}