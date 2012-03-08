package edu.unice.polytech.kis.semwiktionary.parser;


import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;


import edu.unice.polytech.kis.semwiktionary.model.Word;


public class RedirectParsingTest {
	
	private static final String EXPECTED_DEF = "Synonyme de noblesse.";
		
	private static final String EXPECTED_EXAMPLE = "Ainsi lui Jacques l'Aumône se trouvait être de sang non seulement bleu mais royal. À sa majorité, il hérite du château d'Amboise et ne tarde pas à se marier avec la fille du roi d'Italie. — (Queneau, Loin de Rueil, 1944, p. 35)";
	
	private static String actualDef, actualExample;
	
	
	@BeforeClass
	public static void init() {
		actualDef = Word.find("avoir le sang bleu")
							.getDefinitions().get(0)
							.getContent();
							
		actualExample = Word.find("avoir le sang bleu")
							.getDefinitions().get(0)
							.getExamples().get(0)
							.getContent();
	}
	
	@Test
	public void redirectedWordIsCorrect() {
		assertEquals(EXPECTED_DEF, actualDef);
		assertEquals(EXPECTED_EXAMPLE, actualExample);
	}
}
