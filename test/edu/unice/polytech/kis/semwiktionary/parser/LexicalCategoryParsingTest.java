package edu.unice.polytech.kis.semwiktionary.parser;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.LexicalCategory;


public class LexicalCategoryParsingTest {
	
	protected final static String PATTERN = "-nom-";
	protected final static String DESCRIPTION = "Nom commun";
	protected final static String WORD_TITLE = "accueil";
	
	private static LexicalCategory subject;
	
	
	@Before
	public void setUp() {
		subject = LexicalCategory.find(PATTERN);
	}
	
	@Test
	public void parsedLexicalCategoryExists() {
		assertNotNull("Lexical category '" + PATTERN + "' was not found!", subject);
	}
	
	@Test
	public void parsedLexicalCategoryDefinition() {
		assertEquals("Lexical category '" + PATTERN + "' does not have a proper description!",
					 DESCRIPTION,
					 subject.getDescription());
	}
		
	@Test
	public void atLeastOnelexicalCategoryWasLinked() {
		assertTrue("Word '" + WORD_TITLE + "' has no lexical category!",
				   Word.find(WORD_TITLE).getLexicalCategories().size() > 0);
	}
	
	@Test
	public void properLexicalCategoriesWereLinked() {
		assertTrue("Word '" + WORD_TITLE + "' misses category '" + DESCRIPTION + "'!",
				   Word.find(WORD_TITLE).getLexicalCategories().contains(subject));
	}
}
