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
	public void deletionIsForbidden() {
		for (String someWord : expected.keySet()) {
			Word subject = Word.find(someWord);
			
			boolean exceptionWasRaised = false;
			try {
				subject.delete();
			} catch (RuntimeException e) {
				assertEquals("An exception was thrown on Word deletion, but not an IllegalAccessException",
							 java.lang.IllegalAccessException.class,
							 e.getCause().getClass());
							 
				exceptionWasRaised = true;
			}
			
			assertTrue("Exception was not raised on Word '" + subject + "' deletion! (should be forbidden and allowed only on MutableWord)", exceptionWasRaised);
			
			assertEquals("Deletion was forbidden, but the Word '" + subject + "' was still deleted :-|",
						 subject.getTitle(),
						 Word.find(someWord).getTitle());
		}
	}
}
