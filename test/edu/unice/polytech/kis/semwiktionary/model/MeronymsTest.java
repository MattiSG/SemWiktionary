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
		
		List<String> meronymsForIntestinGrêle = new ArrayList<String>();
		meronymsForIntestinGrêle.add("duodénum");
		meronymsForIntestinGrêle.add("jéjunum");
		meronymsForIntestinGrêle.add("iléon");
		EXPECTED_MERONYMS.put("intestin grêle", meronymsForIntestinGrêle);
		
		List<String> meronymsForGrosIntestin = new ArrayList<String>();
		meronymsForGrosIntestin.add("cæcum");
		meronymsForGrosIntestin.add("côlon");
		meronymsForGrosIntestin.add("rectum");
		EXPECTED_MERONYMS.put("gros intestin", meronymsForGrosIntestin);
	}
	
	private static List<Word> expectedMeronyms;
	private static Word subject;
	
	@Test
	public void allMeronymsExist() {
		for (String subjectStr : EXPECTED_MERONYMS.keySet()) {
			subject = Word.find(subjectStr);
			expectedMeronyms = new ArrayList<Word>(EXPECTED_MERONYMS.get(subjectStr).size());
			
			for (String mer : EXPECTED_MERONYMS.get(subjectStr)) {
				expectedMeronyms.add(Word.find(mer));
			}
			
			for (Word expMer : expectedMeronyms) {
				boolean exists = false;
				for (Word actMer : subject.getMeronyms()) {
					if (expMer.equals(actMer)) {
						exists = true;
					}
				}
				assertTrue("The meronym " + expMer + " of word " + subject + " does not exist.", exists);
			}
		}
	}
}

