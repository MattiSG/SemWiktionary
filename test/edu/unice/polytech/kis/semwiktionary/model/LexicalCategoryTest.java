package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.LinkedList;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.LexicalCategory;


public class LexicalCategoryTest {
	
	protected final static String DESCRIPTION = "Lexical category test content.";
	protected final static String PATTERN = "-test-pattern-";
	
	private static LexicalCategory subject;
	
	
	@Before
	public void setUp() {
		subject = LexicalCategory.find(PATTERN);
	}
	
	@Test
	public void lexicalCategoryFind() {
		assertNotNull("Lexical Category `find()` did not find an existing element!", subject);
	}
		
	@Test
	public void deleteLexicalCategoryIsForbidden() {
		boolean exceptionWasRaised = false;
		try {
			subject.delete();
		} catch (RuntimeException e) {
			assertEquals("An exception was thrown on LexicalCategory deletion, but not an IllegalAccessException",
						 java.lang.IllegalAccessException.class,
						 e.getCause().getClass());
			
			exceptionWasRaised = true;
		}
		
		assertTrue("Exception was not raised on LexicalCategory '" + subject + "' deletion! (should be forbidden and allowed only on MutableWord)", exceptionWasRaised);
		
		assertEquals("Deletion was forbidden, but the LexicalCategory '" + subject + "' was still deleted :-|",
					 subject.getPattern(),
					 LexicalCategory.find(PATTERN).getPattern());
	}
}
