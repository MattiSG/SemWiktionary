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


public class MeronymsTest {

	private static final Map<String, List<String>> EXPECTED_MERONYMS = new HashMap<String, List<String>>();
	
	static {
		List<String> meronymsForTubeDigestif = new ArrayList<String>();
		meronymsForTubeDigestif.add("bouche");
		meronymsForTubeDigestif.add("œsophage");
		meronymsForTubeDigestif.add("estomac");
		meronymsForTubeDigestif.add("foie");
		meronymsForTubeDigestif.add("intestin");
		meronymsForTubeDigestif.add("anus");
		EXPECTED_MERONYMS.put("tube digestif", meronymsForTubeDigestif);
		
		List<String> meronymsForIntestin = new ArrayList<String>();
		meronymsForIntestin.add("intestin grêle");
		meronymsForIntestin.add("gros intestin");
		EXPECTED_MERONYMS.put("intestin", meronymsForIntestin);
		
		List<String> meronymsForIntestinGrele = new ArrayList<String>();
		meronymsForIntestinGrele.add("duodénum");
		meronymsForIntestinGrele.add("jéjunum");
		meronymsForIntestinGrele.add("iléon");
		EXPECTED_MERONYMS.put("intestin grêle", meronymsForIntestinGrele);
		
		List<String> meronymsForGrosIntestin = new ArrayList<String>();
		meronymsForGrosIntestin.add("cæcum");
		meronymsForGrosIntestin.add("côlon");
		meronymsForGrosIntestin.add("rectum");
		EXPECTED_MERONYMS.put("gros intestin", meronymsForGrosIntestin);
	}
	
	private static Word subject;
	
	@Test
	public void allMeronymsExist() {
		for (String subjectStr : EXPECTED_MERONYMS.keySet()) {
			subject = Word.find(subjectStr);
			
			for (String expMer : EXPECTED_MERONYMS.get(subjectStr)) {
				boolean exists = false;
				for (Word actMer : subject.getMeronyms()) {
					if (actMer.equals(Word.find(expMer))) {
						exists = true;
					}
				}
				assertTrue("The meronym " + expMer + " of word " + subject + " does not exist.", exists);
			}
		}
	}
}