package edu.unice.polytech.kis.semwiktionary.model;


import java.util.Collection;
import java.util.List;
import java.util.LinkedList;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Traverser.Order;

import edu.unice.polytech.kis.semwiktionary.database.Database;
import edu.unice.polytech.kis.semwiktionary.database.Relation;


/** Models a word in the dictionary and abstracts its database storage.
 * Some properties are only loaded when their associated get methods are called.
 * Instances of this class are safe to use, and will never modify the database. See `MutableWord` to modify the database.
 *
 * @author	[Fabien Brossier](http://fabienbrossier.fr)
 * @author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
 */
public class Word extends NodeMappedObject {

// PROPERTIES
	
	/** The Neo4j's index key to be used to index word nodes.
	 * More info on [Neo4j Index doc](http://api.neo4j.org/current/org/neo4j/graphdb/index/Index.html).
	 *
	 *@see	org.neo4j.graphdb.index.IndexManager#forNodes
	 */
	public static final String INDEX_KEY = "words";
	
	/**Index of all words available in the database.
	 * More info on [Neo4j Index doc](http://api.neo4j.org/current/org/neo4j/graphdb/index/Index.html).
	 *
	 *@see	org.neo4j.graphdb.index.IndexManager#forNodes
	 */
	protected static Index index = Database.getIndexForName(INDEX_KEY);
	
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
		return from(word) != null;
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
	}

	/** Constructs a Word object from a Node in the database.
	 * Useful in propagation cases.
	 *
	 * @param node The node object of the database
	 */
	private Word(Node node) {
		this.node = node;
		this.title = this.get("title");
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
		if (definitions == null || definitions.isEmpty())
			this.fetchDefinitions();
		
		return this.definitions;
	}
	
	/** Returns all synonyms of this Word.
	 */
	public Collection<Word> getSynonyms() {
		return this.<Word>get(Relation.SYNONYM);
	}
	
	/** Returns all antonyms of this Word.
	 */
	public Collection<Word> getAntonyms() {
		return this.<Word>get(Relation.ANTONYM);
	}
	
// DATABASE ACCESS
	
	/** Loads the definitions for this Word from the database.
	 */
	protected void fetchDefinitions() {
		this.definitions = new LinkedList<Definition>(this.<Definition>get(Relation.DEFINITION));
	}
}
