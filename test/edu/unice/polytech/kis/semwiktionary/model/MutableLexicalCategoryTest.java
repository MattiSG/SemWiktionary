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
	
	
	@Before
	public void setUp() {
		subject = MutableLexicalCategory.obtain(LexicalCategoryTest.PATTERN);
	}
	
	@Test
	public void lexicalCategoryObtain() {
		assertNotNull("Lexical Category `obtain()` created a null!", subject);
	}
	
	@Test
	public void getSetLexicalCategoryDescription() {
		subject.setDescription(LexicalCategoryTest.DESCRIPTION);
		
		assertEquals(LexicalCategoryTest.DESCRIPTION, subject.getDescription());
	}
}
