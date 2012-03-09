package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.LinkedList;

import edu.unice.polytech.kis.semwiktionary.model.Word;
import edu.unice.polytech.kis.semwiktionary.model.Definition;


public class DefinitionTest {
	
	private static Definition subject;
	
	private final static String CONTENT = "First definition MutableWord test content.";
	private final static String DOMAIN = "Biology";
	private final static String EXAMPLE = "Example 1";
	
	@Before
	public void setUp() throws Exception {
		subject = new Definition(CONTENT, 1);
	}
		
	@Test
	public void deleteTest() {
		try {
			subject.delete();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exception raised on deletion! (" + e + ")");
		}
	}
}
