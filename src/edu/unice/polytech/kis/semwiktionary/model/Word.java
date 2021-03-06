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
	
	/** Parts of speech, or “Lexical categories”, for this word.
	*/
	protected List<LexicalCategory> lexicalCategories = new LinkedList<LexicalCategory>();
	
	
// STATIC METHODS
	
	/** Finds a word in the database from its title.
	 * Constructs a Word object with all its properties (definition, synonyms).
	 *
	 * @param	word	The word to model.
	 * @return	The complete Word object created or null if the word is not in the database.
	 */
	public static Word find(String word) {
		// this method does exactly the same as NodeMappedObject.findAndInstanciateSingle, but is less dynamic to improve performance
		Node result;
		try {
			result = Database.getIndexForName("Word").get(NodeMappedObject.INDEX_KEY, word).getSingle();
		} catch (java.util.NoSuchElementException e) { // there were multiple results for this query
			throw new RuntimeException("Inconsistent database: multiple nodes found for word '" + word + "' in index!", e );
		}
		
		return (result == null ? null : new Word(result));
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

	/** Initializes all fields when creating a new word.
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
	
	/** Returns the pronunciation for this word.
	 * If no pronunciation was found in the Wiktionary, returns an empty `String`.
	 */
	public String getPronunciation() {
		return this.get("pronunciation");
	}

	/** Returns all available definitions for this Word.
	 */
	public List<Definition> getDefinitions() {
		if (definitions.isEmpty())
			this.fetchDefinitions();
		
		return this.definitions;
	}
	
	public Collection<LexicalCategory> getLexicalCategories() {
		return this.<LexicalCategory>get(Relation.LEXICAL_CATEGORY);
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
	
	/** Returns all hyponyms of this Word.
	 */
	public Collection<Word> getHyponyms() {
		return this.<Word>get(Relation.HYPONYM, Direction.OUTGOING);
	}
	
	/** Returns all hyperonyms of this Word.
	 */
	public Collection<Word> getHyperonyms() {
		return this.<Word>get(Relation.HYPONYM, Direction.INCOMING);
	}
	
	/** Returns all meronyms of this Word.
	 */
	public Collection<Word> getMeronyms() {
		return this.<Word>get(Relation.MERONYM, Direction.OUTGOING);
	}
	
	/** Returns all holonyms of this Word.
	 */
	public Collection<Word> getHolonyms() {
		return this.<Word>get(Relation.MERONYM, Direction.INCOMING);
	}
	
	/** Returns all words with a related meaning of this Word.
	 */
	public Collection<Word> getRelatedVoc() {
		return this.<Word>get(Relation.RELATEDVOC);
	}

// DATABASE ACCESS
	
	/** Loads the definitions for this Word from the database.
	 */
	protected void fetchDefinitions() {
		this.definitions.addAll(this.<Definition>get(Relation.DEFINITION));
	}
	
// STANDARD METHODS
	
	@Override
	public String toString() {
		return this.getTitle();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this.getClass().isInstance(o))
			return this.title.equals(((Word) o).getTitle());
		
		if (String.class.isInstance(o))
			return this.title.equals(o);
		
		return false;
	}
}
