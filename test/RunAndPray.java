package edu.unice.polytech.kis.semwiktionary.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.AfterClass;

import edu.unice.polytech.kis.semwiktionary.database.Database;
import edu.unice.polytech.kis.semwiktionary.database.DatabaseTest;


@RunWith(Suite.class)
@SuiteClasses({
	edu.unice.polytech.kis.semwiktionary.database.DatabaseTest.class,
	edu.unice.polytech.kis.semwiktionary.model.NodeMappedObjectTest.class,
	edu.unice.polytech.kis.semwiktionary.model.MutableWordTest.class,
	edu.unice.polytech.kis.semwiktionary.model.DefinitionTest.class,
	edu.unice.polytech.kis.semwiktionary.model.MutableLexicalCategoryTest.class, // Mutable must be **before** immutable in order to create the LexicalCategory in database
	edu.unice.polytech.kis.semwiktionary.model.LexicalCategoryTest.class,
	
	// test suites above this line don't need the database to be filled
	
	edu.unice.polytech.kis.semwiktionary.parser.ParserTest.class, // after this test suite, data will be stored in database
	edu.unice.polytech.kis.semwiktionary.parser.LexicalCategoryParsingTest.class,
	edu.unice.polytech.kis.semwiktionary.parser.PronunciationTest.class,
	edu.unice.polytech.kis.semwiktionary.parser.HTMLEntitiesParsingTest.class,
	edu.unice.polytech.kis.semwiktionary.model.WordTest.class,
	edu.unice.polytech.kis.semwiktionary.model.WordEntryTest.class,
	edu.unice.polytech.kis.semwiktionary.model.SynonymsTest.class,
	edu.unice.polytech.kis.semwiktionary.model.AntonymsTest.class,
	edu.unice.polytech.kis.semwiktionary.model.TroponymsTest.class,
	edu.unice.polytech.kis.semwiktionary.model.HyponymsTest.class,
	edu.unice.polytech.kis.semwiktionary.model.HyperonymsTest.class,
	edu.unice.polytech.kis.semwiktionary.model.MeronymsTest.class,
	edu.unice.polytech.kis.semwiktionary.model.HolonymsTest.class,
	edu.unice.polytech.kis.semwiktionary.model.RelatedVocTest.class

	//database cleanup is done in the @AfterClass below
})

public class RunAndPray {

	@AfterClass
	public static void classTearDown() {
		DatabaseTest.deleteDb();
	}
}
