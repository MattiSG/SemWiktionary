package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.util.List;
import java.util.Map;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;
import edu.unice.polytech.kis.semwiktionary.parser.ParserTest;
import edu.unice.polytech.kis.semwiktionary.database.DatabaseTest;


public class WordTest {
	
	private static Map<String, List<Definition>> expected;
	
	@BeforeClass
	public static void classSetUp() throws Exception {
		expected = ParserTest.generateExpectedContent();
	}
		
	@Test
	public void findTest() {
		for (String someWord : expected.keySet())
			assertNotNull("'" + someWord + "' was not found in the database!", Word.find(someWord));
	}
	
	@Test
	public void existsTest() {
		for (String someWord : expected.keySet())
			assertTrue("'" + someWord + "' pretends not to exist in the database (compare with allWordsExistWithFind())!", Word.exists(someWord));
	}
	
	@Test
	public void constructorTest() {
		for (String someWord : expected.keySet())
			assertEquals("Title of word '" + someWord + "' was not properly fetched from database!", someWord, Word.find(someWord).getTitle());
	}
	
	@Test
	public void definitionsMatch() {
		for (Map.Entry<String, List<Definition>> currentEntry : expected.entrySet()) {
			Word currentWord = Word.find(currentEntry.getKey());
			List<Definition> expectedDefinitions = currentEntry.getValue();
			
			fail("No MediaWiki content parser. Test deactivated to avoid too verbose details."); //TODO
			
			ReflectionAssert.assertReflectionEquals(expectedDefinitions, currentWord.getDefinitions());
		}
	}
	
}
