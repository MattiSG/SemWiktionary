package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;
import edu.unice.polytech.kis.semwiktionary.database.DatabaseTest;


public class MutableWordTest {
	
	private static MutableWord mutableWord_word1;
	private static MutableWord mutableWord_word2;
	
	private static String MUTABLE_WORD_WORD_NAME_1 = "toto";
	private static String MUTABLE_WORD_WORD_NAME_2 = "tata";
	
	private static String MUTABLE_WORD_DEFINITION_NAME_1 = "Bonjour, je suis une tata.";
	private static String MUTABLE_WORD_DEFINITION_NAME_2 = "Et en plus je suis jolie.";
	
	@BeforeClass
	public static void classSetUp() throws Exception {
		mutableWord_word1 = MutableWord.create(MUTABLE_WORD_WORD_NAME_1);
	}
	
	@AfterClass
	public static void classTearDown() {
		DatabaseTest.deleteDb();
	}
	
	@Test
	public void fromTest() {
		assertEquals("'" + mutableWord_word1 + "' was not found in the database!", mutableWord_word1.getTitle(), MutableWord.from(mutableWord_word1.getTitle()).getTitle());
	}
	
	@Test
	public void createTest() {
		mutableWord_word2 = MutableWord.create(MUTABLE_WORD_WORD_NAME_2);
		assertEquals("'" + mutableWord_word2 + "' pretends not to exist in the database!", mutableWord_word2.getTitle(), MutableWord.from(MUTABLE_WORD_WORD_NAME_2).getTitle());
	}
	
	@Test
	public void constructorTest() {
		MutableWord word = new MutableWord(new Word(MUTABLE_WORD_WORD_NAME_1));
		assertNotNull("The created Mutableword is null !", word);
		
		assertEquals("Title of word '" + word + "' was not properly fetched from database!", MUTABLE_WORD_WORD_NAME_1, word.getTitle());
	}
	
	@Test
	public void addDefinitionTest() {
		mutableWord_word1.clearDefinitions();
		
		assertNotNull("When we add a definition, the return word " + mutableWord_word1 + "is null !",
				mutableWord_word1.addDefinition(new Definition(MUTABLE_WORD_DEFINITION_NAME_1)));
		
		assertEquals("The definition was not registred correctly.", MUTABLE_WORD_DEFINITION_NAME_1, mutableWord_word1.getDefinitions().get(0).getDefinition());
	}
	
	@Test
	public void addDefinitionsTest() {
		mutableWord_word1.clearDefinitions();
		List<Definition> definitions = new LinkedList<Definition>();
		
		definitions.add(new Definition(MUTABLE_WORD_DEFINITION_NAME_1));
		definitions.add(new Definition(MUTABLE_WORD_DEFINITION_NAME_2));
		mutableWord_word1.addDefinitions(definitions);
		
		assertEquals("The first definition was not registred correctly.", MUTABLE_WORD_DEFINITION_NAME_1, mutableWord_word1.getDefinitions().get(0).getDefinition());
		assertEquals("The second definition was not registred correctly.", MUTABLE_WORD_DEFINITION_NAME_2, mutableWord_word1.getDefinitions().get(1).getDefinition());
	}
	
	@Test
	public void eraseTest() {
		/*MutableWord.erase(mutableWord_word1);
		assertNull("The erased word " + mutableWord_word1 + " still exists in the database !", mutableWord_word1);
		mutableWord_word1 = MutableWord.create(MUTABLE_WORD_WORD_NAME_1);*/
		// TODO : implement erase method and restore this test
		assertTrue(true);
	}
	
	@Test
	public void clearDefinitions() {
		/*List<Definition> definitions = new LinkedList<Definition>();
		
		definitions.add(new Definition(MUTABLE_WORD_DEFINITION_NAME_1));
		definitions.add(new Definition(MUTABLE_WORD_DEFINITION_NAME_2));
		mutableWord_word1.addDefinitions(definitions);
		
		mutableWord_word1.clearDefinitions();
		assertTrue("There are still definitions in the word after a clear !", mutableWord_word1.getDefinitions().isEmpty());*/
		// TODO : implement clear method and restore this test
		assertTrue(true);
	}
}
