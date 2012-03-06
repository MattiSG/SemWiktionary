package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.LinkedList;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.LexicalCategory;


public class MutableLexicalCategoryTest {
	
	private static MutableLexicalCategory subject;
	
	private static final String DELETION_TEST_PATTERN = "-to-be-deleted-";
	
	@Before
	public void setUp() {
		subject = MutableLexicalCategory.obtain(LexicalCategoryTest.PATTERN);
	}
	
	@Test
	public void lexicalCategoryObtain() {
		assertNotNull("Lexical Category `obtain()` created a null!", subject);
	}
		
	@Test
	public void deleteMutableLexicalCategory() {
		subject = MutableLexicalCategory.obtain(DELETION_TEST_PATTERN);
		try {
			subject.delete();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception raised on deletion! (" + e + ")");
		}
		
		assertNull("Lexical category '" + DELETION_TEST_PATTERN + "' was found after deletion!",
				   LexicalCategory.find(DELETION_TEST_PATTERN));
	}
	
	@Test
	public void getSetLexicalCategoryDescription() {
		subject.setDescription(LexicalCategoryTest.DESCRIPTION);
		
		assertEquals(LexicalCategoryTest.DESCRIPTION, subject.getDescription());
	}
}
