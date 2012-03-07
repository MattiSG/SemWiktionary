package edu.unice.polytech.kis.semwiktionary.parser;


import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;


import edu.unice.polytech.kis.semwiktionary.model.Word;


public class HTMLEntitiesParsingTest {
		
	private static final String EXPECTED_EXAMPLE =
		"Ne jouerons-nous jamais\n" +
		"Ne serait-ce qu’une heure,\n" +
		"Rien que quelques minutes, \n" +
		"Océan solennel,\n" +
		"Sans que tu aies cet air\n" +
		"De t’occuper ailleurs ?  — (Eugène Guillevic, Carnac, 1961)";
	
	private static String actualExample;
	
	
	@BeforeClass
	public static void init() {
		actualExample = Word.find("océan")
							.getDefinitions().get(0)
							.getExamples().get(0);
	}
	
	@Test
	public void htmlTagsAreProperlyRemoved() {
		assertFalse("An ampersand ('&') has not been removed in '" + actualExample + "'",
					actualExample.contains("&"));
		assertFalse("A <small> HTML tag has not been removed in '" + actualExample + "'",
					actualExample.contains("small"));
	}
	
	@Test
	public void htmlBrTagIsProperlySubstituted() {
		int oldIndex = -1,
			newlineIndex,
			actualIndex;
			
		do {
			newlineIndex = EXPECTED_EXAMPLE.indexOf("\n", oldIndex + 1);
			actualIndex = actualExample.indexOf("\n", oldIndex + 1);
			
			assertEquals("Mismatching newline index (" + newlineIndex + " in expected example, " + actualIndex + " in parsed).",
						 newlineIndex,
						 actualIndex);
			
			oldIndex = newlineIndex;
		} while (newlineIndex > -1);
	}
	
	@Test
	public void finalHtmlTagSubstitutionSanityCheck() {
		assertEquals(EXPECTED_EXAMPLE, actualExample);
	}
}
