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


public class TroponymsTest {
	private static final String[] EXPECTED_TROPONYMS = {
		"avaler",
		"ingérer",
		"ingurgiter",
		"s'empiffrer",
		"se baffrer",
		"se goinfrer",
		"déjeuner",
		"dîner",
		"faire bombance",
		"faire bonne chère",
		"faire ripaille",
		"festoyer",
		"pique-niquer",
		"ripailler",
		"se réfecter",
		"souper"
	};
	
	private static List<Word> expectedTroponyms;
	private static Word subject;

	@Before
	public void setUp() throws Exception {
		subject = Word.from("manger");
		expectedTroponyms = new ArrayList<Word>(EXPECTED_TROPONYMS.length);
		
		for (String trop : EXPECTED_TROPONYMS)
			expectedTroponyms.add(Word.from(trop));
	}
		
	@Test
	public void allTroponymsExist() {
		Collection<Word> actualTroponyms = subject.getTroponyms();
		ReflectionAssert.assertReflectionEquals(expectedTroponyms, actualTroponyms, ReflectionComparatorMode.LENIENT_ORDER);
	}
}
