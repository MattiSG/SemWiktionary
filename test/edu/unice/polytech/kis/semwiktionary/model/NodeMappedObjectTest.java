package edu.unice.polytech.kis.semwiktionary.model;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class NodeMappedObjectTest {
	
	private static String EXPECTED_WORD_INDEX_KEY = "Word";
	private static String TEST_WORD_NAME = "NodeMappedObjectTest_instance";

	private MutableWord mutableSubject;
	
	@Before
	public void createMutableWordInstance() {
		mutableSubject = MutableWord.obtain(TEST_WORD_NAME);
	}

	@Test
	public void wordIndexKeyIsCorrect() {
		assertEquals(EXPECTED_WORD_INDEX_KEY, new Word().getIndexKey());
	}
		
	@Test
	public void mutableWordIndexKeyIsCorrect() {
		assertEquals(EXPECTED_WORD_INDEX_KEY, mutableSubject.getIndexKey());
	}
}
