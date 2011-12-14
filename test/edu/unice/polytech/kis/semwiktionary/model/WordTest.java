package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;
import edu.unice.polytech.kis.semwiktionary.parser.ParserTest;


public class WordTest {
	
	private static Map<String, List<Definition>> expected;
	
	
	@Test
	public void allWordsExistWithFrom() {
		for (String someWord : expected.keySet())
			assertNotNull("'" + someWord + "' was not found in the database!", Word.from(someWord));
	}
	
	@Test
	public void existsTest() {
		for (String someWord : expected.keySet())
			assertTrue("'" + someWord + "' pretends not to exist in the database (compare with allWordsExistWithFrom())!", Word.exists(someWord));
	}
	
	@Test
	public void fromTest() {
		for (String someWord : expected.keySet())
			assertEquals("'" + someWord + "' was not properly fetched from database!", someWord, Word.from(someWord).getTitle());
	}
	
	@Test
	public void titlesDefinitionsMatch() {
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet()) {
			Word myWord = Word.from(currentEntry.getKey());
			assertEquals("Incorrect definitions for word '" + currentEntry.getKey() + "'", currentEntry.getValue(), myWord.getDefinitions());
		}
	}
	
	@BeforeClass
	public static void classSetUp() throws Exception {
		expected = ParserTest.generateExpectedContent();
		
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet())
			MutableWord.create(currentEntry.getKey()).addDefinitions(currentEntry.getValue());
	}
}
