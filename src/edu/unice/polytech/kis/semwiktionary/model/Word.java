package edu.unice.polytech.kis.semwiktionary.model;


import java.util.Collection;
import java.util.List;
import java.util.LinkedList;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
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

	/** The actual natural language word this instance represents.
	 */
	protected String title;
	
	/** Definitions of this Word, in parsing order.
	 */
	protected List<Definition> definitions = new LinkedList<Definition>();
	
	
// STATIC METHODS
	
	/** Finds a word in the database from its title.
	 * Constructs a Word object with all its properties (definition, synonyms).
	 *
	 * @param	word	The word to model.
	 * @return	The complete Word object created or null if the word is not in the database.
	 */
	public static Word find(String word) {
		return NodeMappedObject.<Word>findAndInstanciateSingleOf(Word.class, word);
	}
	
	/** Tests if the given Word exists in the database.
	 *
	 * @param	word	The word to search for in the database
	 * @return	`true` if the word exists in the database, `false` otherwise or if an exception was thrown while trying to access it.
	 */
	public static boolean exists(String word) {
		try {
			return find(word) != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
// CONSTRUCTORS

	/** Initializes all fields when `create()`ing a new word.
	 *
	 *@return	An empty shell to be filled.
	 */
	protected Word() {
		// nothing to do
	}

	/** Constructs a Word object from a Node in the database.
	 * Useful in propagation cases.
	 * _Note_: This method is used by the generic `NodeMappedObject.get` method, you should not have to use manually it.
	 *
	 * @param node The Node object storing information about the to-be word in the database.
	 */
	protected Word(Node node) {
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
		if (definitions.isEmpty())
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
	
	/** Returns all troponyms of this Word.
	 */
	public Collection<Word> getTroponyms() {
		return this.<Word>get(Relation.TROPONYM);
	}
	
// DATABASE ACCESS
	
	/** Loads the definitions for this Word from the database.
	 */
	protected void fetchDefinitions() {
		this.definitions.addAll(this.<Definition>get(Relation.DEFINITION));
	}
	
	
// STANDARD METHODS
	
	public String toString() {
		return this.getTitle();
	}
}
