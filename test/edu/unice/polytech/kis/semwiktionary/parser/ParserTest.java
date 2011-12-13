package edu.unice.polytech.kis.semwiktionary.parser;


import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import edu.unice.polytech.kis.semwiktionary.model.Word;


public class ParserTest {
	
	private static List<String> EXPECTED_TITLES;
		
	@BeforeClass
	public static void classSetUp() throws Exception {
		EXPECTED_TITLES = new ArrayList<String>(4);
		EXPECTED_TITLES.add("accueil");
		EXPECTED_TITLES.add("dictionnaire");
		EXPECTED_TITLES.add("cercle");
		EXPECTED_TITLES.add("lire");
	}
	
	@Test
	public void allWordsExist() {
		for (String someWord : EXPECTED_TITLES)
			assertNotNull("'" + someWord + "' was not found in the database!", Word.from(someWord));
	}
	
	
}
