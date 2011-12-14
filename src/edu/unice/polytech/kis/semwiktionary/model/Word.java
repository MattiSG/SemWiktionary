package edu.unice.polytech.kis.semwiktionary.model;


import java.util.List;
import java.util.LinkedList;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import  edu.unice.polytech.kis.semwiktionary.database.Database;


/** Models a word in the dictionary and abstracts its database storage.
 * Some properties are only loaded when their associated get methods are called.
 * Instances of this class are safe to use, and will never modify the database. See `MutableWord` to modify the database.
 *
 * @author	[Fabien Brossier](http://fabienbrossier.fr)
 * @author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
 */
public class Word {

// PROPERTIES
	
	/** The Neo4j's index key to be used to index word nodes.
	 *
	 *@see	http://api.neo4j.org/current/org/neo4j/graphdb/index/Index.html
	 */
	public static final String INDEX_KEY = "words";
	
	/**Index of all words available in the database.
	 *
	 *@see	http://api.neo4j.org/current/org/neo4j/graphdb/index/Index.html
	 */
	protected static Index index = Database.getIndexForName(INDEX_KEY);
	
	/** The database storage for this Word.
	 *	See conceptual documentation for database layout.
	 */
	protected Node node;
	
	/** The actual natural language word this instance represents.
	 */
	protected String title;
	
	/** Definitions of this Word, in parsing order.
	 */
	protected List<Definition> definitions;
	
	
// STATIC METHODS
	
	/** Finds a word in the database from its title.
	 * Constructs a Word object with all its properties (definition, synonyms).
	 *
	 * @param	word	The word to model
	 * @return	The complete Word object created or null if the word is not in the database
	 */
	public static Word from(String word) {
		Node result;
		try {
			 result = (Node) index.get(INDEX_KEY, word).getSingle();
		} catch (java.util.NoSuchElementException e) { // there were multiple results for this query
			throw new RuntimeException("Inconsistent database: multiple nodes found for word '" + word + "' in index!", e );
		}
		
		return (result == null ? null : new Word(result));
	}
	
	/** Tests if the given Word exists in the database.
	 *
	 * @param	word	The word to search for in the database
	 * @return	`true` if the word exists in the database, `false` otherwise
	 */
	public static boolean exists(String word) {
		return Word.from(word) == null;
	}
	
// CONSTRUCTORS
	
	/** Models a natural-language word.
	 * Does not store it in the database. See `MutableWord.create` to create a word in the database.
	 *
	 * @param	word	The natural language word to model
	 */
	public Word(String word) {
		this.title = word;
		this.definitions = new LinkedList<Definition>();
		//TODO
	}

	/** Constructs a Word object from a Node in the database.
	 * Useful in propagation cases.
	 *
	 * @param node The node object of the database
	 */
	private Word(Node node) {
		this.node = node;
		//TODO
	}

// ACCESSORS
	
	/** Returns the natural language word this instance models.
	 */
	public String getTitle() {
		return title;
	}

	/** Returns all available definitions for this Word.
	 */
	public List<Definition> getDefinitions() {
		if (definitions.isEmpty())
			this.fetchDefinitions();
		
		return definitions;
	}
	
// DATABASE ACCESS
	
	/** Loads the node for this Word from the database.
	 */
	protected void fetchNode() {
		//TODO
		//this.node = ...
	}
	
	/** Loads the definitions for this Word from the database.
	 */
	protected void fetchDefinitions() {
		//TODO
		//this.definitions = ...
	}

}
