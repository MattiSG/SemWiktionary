/**SemWiktionary
*Java API to access data from [wiktionary](http://fr.wiktionary.org). Specific target is the French wiktionary.
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

import edu.unice.polytech.kis.semwiktionary.parser.WikimediaDump;


/**Singleton class to access the unique SemWiktionary database.
 */
public class Database {
	
	/** Folder containing the database files.
	 */
	protected static final String DB_PATH = "./data"; // also update ${data} in ant buildfile
	
	/** The underlying database object.
	 */
	protected final GraphDatabaseService graphDb;
	
	/** Singleton unique instance.
	 */
	private static Database instance;
	
	/** Transaction management is abstracted in this class.
	* To improve performance, only one transaction is started by the Database.
	* All calls to the transaction start / stop are managed by this class.
	*/
	private static Transaction transaction = null;
	
	/** Counts the number of transactions that were asked to be started.
	* Since only one transaction is started, we need to count how many of them were asked to start, to be sure to stop the transaction only on the last call to `close`.
	*
	*@see	open
	*@see	close
	*/
	private static int transactionCount = 0;
	
	
	/**Singleton constructor, therefore private.
	 */
	private Database() {
		this.graphDb = new EmbeddedGraphDatabase(DB_PATH);
		registerShutdownHook(this.graphDb);
	}
	
	/** Starts a transaction if necessary.
	 * If a transaction is already currently in use, the current one will be used.
	 *
	 *@return	The current Transaction.
	 *@see	commit
	 */
	public static Transaction open() {
		if (transactionCount == 0)
			transaction = getDbService().beginTx();
			
		transactionCount++;
		
		return transaction;
	} 
	
	/** Validates the current transaction.
	*
	*@return	The current Transaction.
	*@throw	RuntimeException	If no transaction was started.
	*/
	public static Transaction validate() {
		if (transactionCount <= 0)
			throw new RuntimeException("No running transaction!");
		
		transaction.success();
		
		return transaction;
	}
	
	/** Commits the current transaction.
	 * **The transaction needs to be `validate`d to be saved!** Otherwise, it will be rolled back.
	 *
	 *@throw	RuntimeException	If no transaction was started.
	 *@see	open
	 *@see	validate
	 */
	public static void close() {
		if (transactionCount <= 0)
			throw new RuntimeException("No running transaction!");
		
		transactionCount--;
		
		if (transactionCount == 0)
			transaction.finish();
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
		Node node;
		
		open();
		
		try {
			node = getDbService().createNode();
			node.setProperty(property, propValue);
			validate();
		} finally {
			close();
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
		Relationship relationship;
		
		open();
		
		try {
			relationship = from.createRelationshipTo(to, relationType);
			validate();
		} finally {
		    close();
		}
		
		return relationship;
	}
}
