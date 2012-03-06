package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.LexicalCategory;
import edu.unice.polytech.kis.semwiktionary.database.DatabaseTest;


public class LexicalCategoryTest {
	
	private static LexicalCategory subject;
	
	private final static String DESCRIPTION = "Lexical category test content.";
	private final static String PATTERN = "-test-pattern-";
	
	@Before
	public void setUp() {
		subject = new LexicalCategory(PATTERN);
	}
	
	@Test
	public void lexicalCategoryConstructor() {
		assertNotNull("Lexical Category constructor created a null!", subject);
	}
		
	@Test
	public void deleteTest() {
		try {
			subject.delete();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception raised on deletion! (" + e + ")");
		}
		
		assertNull("Lexical category '" + PATTERN + "' was found after deletion!",
				   LexicalCategory.find(PATTERN));
	}
	
	@Test
	public void getSetLexicalCategoryDescription() {
		subject.setDescription(DESCRIPTION);
		
		assertEquals(DESCRIPTION, subject.getDescription());
	}
}
