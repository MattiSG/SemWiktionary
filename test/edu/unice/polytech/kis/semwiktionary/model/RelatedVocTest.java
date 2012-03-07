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


public class RelatedVocTest {
	
	private static final String[] EXPECTED_VOC_FOR_SIEGE = {
		"assiéger",
		"Saint-Siège",
		"sédile",
		"siéger",
		
	};

	private static final String[] EXPECTED_VOC_FOR_BACCALAUREAT = {
		"maîtrise",
		"doctorat",
		"licence",
		"diplôme",
		"certificat"	
	};
	
	private static List<Word> expectedVocForSiege;
	private static List<Word> expectedVocForBaccalaureat;
	private static Word siege;
	private static Word baccalaureat;

	@Before
	public void setUp() throws Exception {
		siege = Word.find("siège");
		baccalaureat = Word.find("baccalauréat");
		expectedVocForSiege = new ArrayList<Word>(EXPECTED_VOC_FOR_SIEGE.length);
		expectedVocForBaccalaureat = new ArrayList<Word>(EXPECTED_VOC_FOR_BACCALAUREAT.length);
		
		for (String relVoc : EXPECTED_VOC_FOR_SIEGE)
			expectedVocForSiege.add(Word.find(relVoc));

		for (String relVoc : EXPECTED_VOC_FOR_BACCALAUREAT)
			expectedVocForBaccalaureat.add(Word.find(relVoc));
	}
		
	@Test
	public void allVocExist() {
		Collection<Word> actualVocForSiege = siege.getRelatedVoc();
		ReflectionAssert.assertReflectionEquals(expectedVocForSiege, actualVocForSiege, ReflectionComparatorMode.LENIENT_ORDER);

		Collection<Word> actualVocForBaccalaureat = baccalaureat.getRelatedVoc();
		ReflectionAssert.assertReflectionEquals(expectedVocForBaccalaureat, actualVocForBaccalaureat, ReflectionComparatorMode.LENIENT_ORDER);
	}
}
