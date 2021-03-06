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


public class AntonymsTest {

	private static final String[] EXPECTED_ANTONYMS = {
		"jeune",
		"neuf",
		"nouveau",
		"récent"
	};
	
	private static List<Word> expectedAntonyms;
	private static Word subject;

	@Before
	public void setUp() throws Exception {
		subject = Word.find("vieux");
		expectedAntonyms = new ArrayList<Word>(EXPECTED_ANTONYMS.length);
		
		for (String ant : EXPECTED_ANTONYMS)
			expectedAntonyms.add(Word.find(ant));
	}
		
	@Test
	public void allAntonymsExist() {
		Collection<Word> actualAntonyms = subject.getAntonyms();
		ReflectionAssert.assertReflectionEquals(expectedAntonyms, actualAntonyms, ReflectionComparatorMode.LENIENT_ORDER);
	}
}
