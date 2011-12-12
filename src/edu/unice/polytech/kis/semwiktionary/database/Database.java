/**SemWiktionary
*Java API to access data from [wiktionary](http://fr.wiktionary.org). Specific target is the French wiktionary.
*
*@author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
*
*@version 0.0.0
*/

package edu.unice.polytech.kis.semwiktionary.database;


import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import edu.unice.polytech.kis.semwiktionary.model.Word;


/**Singleton class to access the unique SemWiktionary database.
 */
public class Database {
	
	/**Singleton unique instance.
	 */
	private static Database instance;
	
	// TODO : define how to set the database path
	private static final String DB_PATH = "./SemWiktionary/DB";
	
	/** The database object
	 */
	private final GraphDatabaseService graphDb;
	
	/**Singleton constructor, therefore private.
	 */
	private Database() {
		graphDb = new EmbeddedGraphDatabase(DB_PATH);
		registerShutdownHook(graphDb);
	}
	
	/**Method to close the database correctly whatever the close action made by the user.
	 * @param graphDb The 
	 */
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running example before it's completed)
	    Runtime.getRuntime().addShutdownHook(new Thread() {
	        @Override
	        public void run()
	        {
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
	
	public void addWord(Word word) {
		
	}
}
