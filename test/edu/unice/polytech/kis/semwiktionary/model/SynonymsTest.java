package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import edu.unice.polytech.kis.semwiktionary.model.Word;


public class SynonymsTest {
	
	private static final String[] EXPECTED_SYNONYMS = {
		"daron",
		"père",
		"papa",
		"vétuste",
		"ancien",
		"âgé"
	};
	
	private static List<Word> expectedSynonyms;
	private static Word subject;

	@Before
	public void setUp() throws Exception {
		subject = Word.from("vieux");
		expectedSynonyms = new ArrayList<Word>(EXPECTED_SYNONYMS.length);
		
		for (String syn : EXPECTED_SYNONYMS)
			expectedSynonyms.add(Word.from(syn));
	}
		
	@Test
	public void allSynonymsExist() {
		Collection<Word> actualSynonyms = subject.getSynonyms();
		ReflectionAssert.assertReflectionEquals(expectedSynonyms, actualSynonyms, ReflectionComparatorMode.LENIENT_ORDER);
	}
}
