package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Direction;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;
import edu.unice.polytech.kis.semwiktionary.database.Relation;
import edu.unice.polytech.kis.semwiktionary.database.DatabaseTest;


public class MutableWordTest {
	
	private MutableWord subject;
	private int count = 0;
	private String currentTitle;
	
	private static final String MUTABLEWORD_WORD = "MutableWord_test_word";
	private static final String MUTABLEWORD_DEFINITION_1 = "First definition MutableWord test content.";
	private static final String MUTABLEWORD_DEFINITION_2 = "Second definition MutableWord test content.";
	
	@Before
	public void setUp() {
		currentTitle = MUTABLEWORD_WORD + "_" + count;
		subject = MutableWord.create(currentTitle);
		count++;
	}
	
	@AfterClass
	public static void classTearDown() {
		DatabaseTest.deleteDb();
	}
	
	@Test
	public void createTest() {
		assertEquals("'" + subject + "' pretends not to exist in the database!",
					 subject.getTitle(),
					 MutableWord.from(currentTitle).getTitle()
		);
	}
	
	@Test
	public void constructorTest() {
		MutableWord word = new MutableWord(new Word(currentTitle));
		assertNotNull("The created Mutableword is null !", word);
		
		assertEquals("Title of word '" + word + "' was not properly fetched from database!",
					 currentTitle,
					 word.getTitle()
		);
	}
	
	@Test
	public void addDefinitionTest() {
		assertSame("When we add a definition, the returned word '" + subject + "' is not the original one!",
				subject.addDefinition(new Definition(MUTABLEWORD_DEFINITION_1, 1)),
				subject
		);
		
		assertEquals("The first definition was not registered correctly.",
					 MUTABLEWORD_DEFINITION_1,
					 subject.getDefinitions().get(0).getContent()
		);

		subject.addDefinition(new Definition(MUTABLEWORD_DEFINITION_2, 2));
		assertEquals("The second definition was not registered correctly.",
					 MUTABLEWORD_DEFINITION_2,
					 subject.getDefinitions().get(1).getContent()
		);
	}
	
	@Test
	public void addDefinitionsTest() {
		List<Definition> definitions = new LinkedList<Definition>();
		
		definitions.add(new Definition(MUTABLEWORD_DEFINITION_1, 1));
		definitions.add(new Definition(MUTABLEWORD_DEFINITION_2, 2));
		subject.addDefinitions(definitions);
		
		assertEquals("The first definition was not registered correctly.", MUTABLEWORD_DEFINITION_1, subject.getDefinitions().get(0).getContent());
		assertEquals("The second definition was not registered correctly.", MUTABLEWORD_DEFINITION_2, subject.getDefinitions().get(1).getContent());
	}
	
	@Test
	public void deleteTest() {
		subject.delete();
		
		assertNull("The deleted word '" + MUTABLEWORD_WORD + "' still exists in the database!",
				   Word.from(MUTABLEWORD_WORD));
	}
	
	@Test
	public void clearDefinitions() {
		List<Definition> definitions = new LinkedList<Definition>();
		
		definitions.add(new Definition(MUTABLEWORD_DEFINITION_1, 1));
		definitions.add(new Definition(MUTABLEWORD_DEFINITION_2, 2));
		subject.addDefinitions(definitions);
		
		subject.clearDefinitions();
		assertEquals("There are still definitions in the word after a clear!",
					 subject.getDefinitions().size(),
					 0
		);
		
		for (Relationship r : subject.node.getRelationships(Direction.OUTGOING, Relation.DEFINITION))
			fail("At least one relationship was found in the database!");
	}
}
