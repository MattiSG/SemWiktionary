package edu.unice.polytech.kis.semwiktionary.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.AfterClass;

import edu.unice.polytech.kis.semwiktionary.database.DatabaseTest;


@RunWith(Suite.class)
@SuiteClasses({
	edu.unice.polytech.kis.semwiktionary.database.DatabaseTest.class,
	edu.unice.polytech.kis.semwiktionary.model.MutableWordTest.class,
	edu.unice.polytech.kis.semwiktionary.model.DefinitionTest.class,
	
	// test suites above this line don't need the database to be filled
	
	edu.unice.polytech.kis.semwiktionary.parser.ParserTest.class, // after this test suite, data will be stored in database
	edu.unice.polytech.kis.semwiktionary.model.WordTest.class,
	edu.unice.polytech.kis.semwiktionary.model.SynonymsTest.class,
	edu.unice.polytech.kis.semwiktionary.model.AntonymsTest.class,
	edu.unice.polytech.kis.semwiktionary.model.TroponymsTest.class,
	edu.unice.polytech.kis.semwiktionary.model.HyponymsTest.class

	//database cleanup is done in the @AfterClass below
})

public class RunAndPray {
	@AfterClass
	public static void classTearDown() {
		DatabaseTest.deleteDb();
	}
}
