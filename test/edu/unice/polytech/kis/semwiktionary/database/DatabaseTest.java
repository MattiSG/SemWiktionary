package edu.unice.polytech.kis.semwiktionary.database;


import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Direction;

import edu.unice.polytech.kis.semwiktionary.database.Database;


public class DatabaseTest {
	
	private final String EXPECTED_TITLE = "test";
	private final String EXPECTED_RELATIONSHIP_NAME = "testRelation";
	
	private Database db;
	private Node subjectNode1;
	private Node subjectNode2;
	
	
	@Before
	public void setUp() throws Exception {
		db = Database.getInstance();
	}
	
	@Test
	public void instanceExists() {
		assertNotNull(db);
	}
	
	@Test
	public void createNodeWithPropertyTest() {
		subjectNode1 = db.createNodeWithProperty("title", EXPECTED_TITLE);
		assertNotNull(subjectNode1);
		
		Long subjectId = subjectNode1.getId();
		assertEquals(db.graphDb.getNodeById(subjectId), subjectNode1);
	}
	
	@Test
	public void linkTest() {
		subjectNode1 = db.createNodeWithProperty("title", EXPECTED_TITLE);
		subjectNode2 = db.createNodeWithProperty("title", "toto");
		RelationshipType rel = DynamicRelationshipType.withName(EXPECTED_RELATIONSHIP_NAME);
		
		db.link(subjectNode1, subjectNode2, rel);
		
		assertTrue("The first tested node does not have a relationship as expected", subjectNode1.hasRelationship(rel));
		assertTrue("The second tested node does not have a relationship as expected", subjectNode2.hasRelationship(rel));

		assertTrue("The first tested node's relationship is not properly oriented.", subjectNode1.hasRelationship(rel, Direction.OUTGOING));
		assertTrue("The second tested node's relationship is not properly oriented.", subjectNode2.hasRelationship(rel, Direction.INCOMING));
				 
		assertEquals("The first and second node are not properly related (1)", subjectNode1.getSingleRelationship(rel, Direction.OUTGOING).getEndNode(), subjectNode2);
		assertEquals("The first and second node are not properly related (2)", subjectNode2.getSingleRelationship(rel, Direction.INCOMING).getStartNode(), subjectNode1);
	}
	
	public static void deleteDb() {
		Database.getDbService().shutdown();
		
		deleteDirectory(new File(Database.DB_PATH));
	}
	
	private static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
}
