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


public class HolonymsTest {

	private static final Map<String, List<String>> EXPECTED_HOLONYMS = new HashMap<String, List<String>>();
	
	static {
		List<String> holonymsForRectum = new ArrayList<String>();
		holonymsForRectum.add("gros intestin");
		EXPECTED_HOLONYMS.put("rectum", holonymsForRectum);
		
		List<String> holonymsForGrosIntestin = new ArrayList<String>();
		holonymsForGrosIntestin.add("intestin");
		EXPECTED_HOLONYMS.put("gros intestin", holonymsForGrosIntestin);
		
		List<String> holonymsForIntestin = new ArrayList<String>();
		holonymsForIntestin.add("tube digestif");
		EXPECTED_HOLONYMS.put("intestin", holonymsForIntestin);
		
		List<String> holonymsForSaintClaude = new ArrayList<String>();
		holonymsForSaintClaude.add("France");
		holonymsForSaintClaude.add("Canada");
		EXPECTED_HOLONYMS.put("Saint-Claude", holonymsForSaintClaude);
		
		List<String> holonymsForFrance = new ArrayList<String>();
		holonymsForFrance.add("Franche-Comté");
		holonymsForFrance.add("Guadeloupe");
		EXPECTED_HOLONYMS.put("France", holonymsForFrance);
		
		List<String> holonymsForFrancheComte = new ArrayList<String>();
		holonymsForFrancheComte.add("Jura");
		EXPECTED_HOLONYMS.put("Franche-Comté", holonymsForFrancheComte);
		
		List<String> holonymsForBasseTerre = new ArrayList<String>();
		holonymsForBasseTerre.add("Basse-Terre");
		EXPECTED_HOLONYMS.put("Guadeloupe", holonymsForBasseTerre);
		
		List<String> holonymsForCanada = new ArrayList<String>();
		holonymsForCanada.add("Québec");
		EXPECTED_HOLONYMS.put("Canada", holonymsForCanada);
	}
	
	private static Word subject;
		
	@Test
	public void allHolonymsExist() {
		for (String subjectStr : EXPECTED_HOLONYMS.keySet()) {
			subject = Word.find(subjectStr);
			
			for (String expHol : EXPECTED_HOLONYMS.get(subjectStr)) {
				boolean exists = false;
				for (Word actHol : subject.getHolonyms()) {
					if (actHol.equals(Word.find(expHol))) {
						exists = true;
					}
				}
				assertTrue("The meronym " + expHol + " of word " + subject + " does not exist.", exists);
			}
		}
	}
}