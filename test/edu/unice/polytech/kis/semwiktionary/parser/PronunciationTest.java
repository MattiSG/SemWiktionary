package edu.unice.polytech.kis.semwiktionary.parser;


import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

import edu.unice.polytech.kis.semwiktionary.model.Word;


public class PronunciationTest {
	
	private static Map<String, String> expected;
	
	
	@BeforeClass
	public static void classSetUp() throws Exception {
		expected = new HashMap<String, String>();
		expected.put("accueil", "a.kœj");
		expected.put("dictionnaire", "dik.sjɔ.nɛʁ");
		expected.put("cercle", "sɛʁkl");
		expected.put("lire", "liʁ");
		expected.put("vieux", "vjø");
		expected.put("encyclopédie", "ɑ̃.si.klɔ.pe.di");
		expected.put("siège", "sjɛʒ");
		expected.put("manchot", "mɑ̃.ʃo");
		expected.put("火", "");
		expected.put("moduler", "mɔ.dy.le");
		expected.put("chinois", "ʃi.nwa");
		expected.put("Noël", "nɔ.ɛl");
	}
	
	
	@Test
	public void pronunciationsMatch() {
		for (String currentWord : expected.keySet())
			assertEquals("Pronuciation for '" + currentWord + "' was incorrectly parsed!",
						 expected.get(currentWord),
						 Word.find(currentWord).getPronunciation());
	}
}
