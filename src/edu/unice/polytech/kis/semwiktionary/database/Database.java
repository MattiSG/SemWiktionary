/**SemWiktionary
*Java API to access data from [wiktionary](http://fr.wiktionary.org). Specific target is the French wiktionary.
*
*@author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
*
*@version 0.0.0
*/

package edu.unice.polytech.kis.semwiktionary.database;


import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

import edu.unice.polytech.kis.semwiktionary.model.Definition;
import edu.unice.polytech.kis.semwiktionary.model.MutableWord;
import edu.unice.polytech.kis.semwiktionary.model.Word;


/**Singleton class to access the unique SemWiktionary database.
 */
public class Database {
	
	/** The underlying database object.
	 */
	protected final GraphDatabaseService graphDb;
	
	/** Folder containing the database files.
	 */
	private static final String DB_PATH = "./data"; // also update ${data} in ant buildfile
	
	/** Singleton unique instance.
	 */
	private static Database instance;
	
	
	/**Singleton constructor, therefore private.
	 */
	private Database() {
		titleIndex = graphDb.index().forNodes("nodes");
		this.graphDb = new EmbeddedGraphDatabase(DB_PATH);
		registerShutdownHook(this.graphDb);
	}
	
	/**Method to close the database correctly whatever the close action made by the user.
	 *Registers a shutdown hook for the Neo4j instance so that it
	 * shuts down nicely when the VM exits (even if you "Ctrl-C" the
	 * running example before it's completed).
	 *
	 * @param graphDb The database object
	 */
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
	
	/**Singleton accessor.
	 */
	public static Database getInstance() {
		if (Database.instance == null)
			Database.instance = new Database();
		
		return Database.instance;
	}
	
	/** Accessor to the main Neo4j manipulating class
	 */
	public static GraphDatabaseService getDbService() {
		return getInstance().graphDb;
	}
	
	/**Create a new node in the database with the given property.
	 * @param property The node property 
	 * @param propValue The property value
	 * @return the created node or null in case of error (unlikely)
	 */
	public static Node createNodeWithProperty(String property, String propValue) {
		Transaction tx = getDbService().beginTx();
		Node node;
		
		try {
			node = getDbService().createNode();
			node.setProperty(property, propValue);
			tx.success();
		} finally {
		    tx.finish();
		}
		
		return node; 
	}
	
	/** Returns the database's nodes' index for the given key.
	 */
	public static Index<Node> getIndexForName(String indexKey) {
		return getDbService().index().forNodes(indexKey);
	}
	
	/** Adds the given relationship between the two given nodes.
	 */
	public static Relationship link(Node from, Node to, RelationshipType relationType) {
		Transaction tx = getDbService().beginTx();
		Relationship relationship;
		
		try {
			relationship = from.createRelationshipTo(to, relationType);
			tx.success();
		} finally {
		    tx.finish();
		}
		
		return relationship;
	}
}
