package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.Test;

import edu.unice.polytech.kis.semwiktionary.model.Word;


public class WordEntryTest {
	
	private static Word fauchaisons;
	private static Word federalismes;
	
	@Test
	public void WordEntryTest() { // checks that the trash state does not leak in some edge cases
		assertNotNull("The word \"fauchaisons\" was not properly parsed !", Word.find("fauchaisons"));
		assertNotNull("The word \"fédéralismes\" was not properly parsed !", Word.find("fédéralismes"));
	}
}
