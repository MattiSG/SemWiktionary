package edu.unice.polytech.kis.semwiktionary.model;


import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Node;

import edu.unice.polytech.kis.semwiktionary.database.Database;
import edu.unice.polytech.kis.semwiktionary.database.Relation;


/** Adds Create, Update, and Delete methods to a Word.
 * Instances of this class can modify the database. For a safe usage, see `Word`.
 * 
 * @author	[Fabien Brossier](http://fabienbrossier.fr)
 * @author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
 */
public class MutableWord extends Word {
	
// STATIC METHODS
	
	/** Finds a word in the database from its title, or creates it if it does not exist yet.
	 * Constructs a MutableWord object and links all of its attached properties (definitions, synonyms…) from the database.
	 * Such an object _may_ modify the database. See `Word` for a safer, immutable use.
	 *
	 * @param	word	The word to model
	 * @return	A MutableWord object, either referencing the current word in database or creating a new one
	 */
	public static MutableWord obtain(String word) {
		Word immutableWord = Word.find(word);
		return (immutableWord == null ? MutableWord.create(word) : new MutableWord(immutableWord));
	}
	
	/** Creates a new word in the database.
	 * The word is created even if the same word already exists in the database, **possibly leading to duplicates**. Such a case would be a violation of the constraints, and render the word unusable.
	 * You should always use the `Word.exists` method to check for existence before creating a new word.
	 *
	 * @param	word	The natural language word to add to the database
	 * @return	The MutableWord model for the word added to the database
	 */
	private static MutableWord create(String word) {
		MutableWord result = new MutableWord();
		result.title = word;
		
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			result.initNode()
				  .set("title", word);
			
			Word.index.add(result.node, Word.INDEX_KEY, word);
			
			tx.success();
		} finally {
		    tx.finish();
		}

		return result;
	}
	
// CONSTRUCTORS
	
	/** Initializes all fields when `create()`ing a new word.
	*
	*@return	An empty shell to be filled by `create()`.
	*/
	private MutableWord() {
		super();
		// nothing else to do
	}
	
	/** Makes an existing natural-language word mutable, that is, able to modify the database.
	 *
	 * @param	model	An immutable version of a word you want to modify.
	 */
	public MutableWord(Word model) {
		super();
		this.title = model.title;
		this.node = model.node;
	}
	
	/** Constructs a MutableWord object from a Node in the database.
	 * Useful in propagation cases.
	 * _Note_: This method is used by the generic `NodeMappedObject.get` method, you should not have to use manually it.
	 *
	 * @param node The Node object storing information about the to-be word in the database.
	 */
	protected MutableWord(Node node) {
		super(node);
	}
	
// UPDATE FUNCTIONS
	
	/** Adds the given Definition to this Word.
	 * The definition is immediately and transparently stored in the database. No need to `commit` modifications.
	 * 
	 * @param	definition	The Definition to add to this Word
	 * @return	This MutableWord, for chainability
	 */
	public MutableWord addDefinition(Definition definition) {
		Database.link(this.node, definition.node, Relation.DEFINITION);
		this.definitions.add(definition);
		
		return this;
	}

	/** Adds the given set of Definitions to this Word.
	 * The definitions are immediately and transparently stored in the database. No need to `commit` modifications.
	 * 
	 * @param	definitions	The set of Definitions to add to this Word
	 * @return	This MutableWord, for chainability
	 * @see		MutableWord#addDefinition
	 */
	public MutableWord addDefinitions(Iterable<Definition> definitions) {
		for (Definition definition : definitions)
			this.addDefinition(definition);

		return this;
	}

	/** Adds the word given in parameter to the current word object as a synonym.
	 * @param synonym The synonym to add to this Word
	 * @return This MutableWord, for chainability
	 */
	public MutableWord addSynonym(Word synonym) {
		Database.link(this.node, synonym.node, Relation.SYNONYM);
		
		return this;
	}
	
	/** Adds the word given in parameter to the current word object as an antonym.
	 * @param antonym The antonym to add to this Word
	 * @return This MutableWord, for chainability
	 */
	public MutableWord addAntonym(Word antonym) {
		Database.link(this.node, antonym.node, Relation.ANTONYM);
		
		return this;
	}
	
	/** Adds the word given in parameter to the current word object as a troponym.
	 * @param troponym The troponym to add to this Word
	 * @return This MutableWord, for chainability
	 */
	public MutableWord addTroponym(Word troponym) {
		Database.link(this.node, troponym.node, Relation.TROPONYM);
		
		return this;
	}

	/** Adds the word given in parameter to the current word object as a word with a related meaning.
	 * @param relatedVoc The related word to add to this Word
	 * @return This MutableWord, for chainability
	 */
	public MutableWord addRelatedVoc(Word relatedVoc) {
		Database.link(this.node, relatedVoc.node, Relation.RELATEDVOC);
		
		return this;
	}	
	
	/** Adds the word given in parameter to the current word object as an hyponym.
	 * @param hyponym The hyponym to add to this Word
	 * @return This MutableWord, for chainability
	 * @see #addHyperonym(Word hyperonym) (opposite method)
	 */
	public MutableWord addHyponym(Word hyponym) {
		Database.link(this.node, hyponym.node, Relation.HYPONYM);
		
		return this;
	}
	
	/** Adds the word given in parameter to the current word object as an hyperonym.
	 * @param hyperonym The hyperonym to add to this Word
	 * @return This MutableWord, for chainability
	 * @see #addHyponym(Word hyponym) (opposite method)
	 */
	public MutableWord addHyperonym(Word hyperonym) {
		Database.link(hyperonym.node, this.node, Relation.HYPONYM);
		
		return this;
	}

	/** Adds the word given in parameter to the current word object as a meronym.
	 * @param meronym The meronym to add to this Word
	 * @return This MutableWord, for chainability
	 * @see #addHolonym(Word holonym) (opposite method)
	 */
	public MutableWord addMeronym(Word meronym) {
		Database.link(this.node, meronym.node, Relation.MERONYM);
		
		return this;
	}

	/** Adds the word given in parameter to the current word object as an holonym.
	 * @param holonym The holonym to add to this Word
	 * @return This MutableWord, for chainability
	 * @see #addMeronym(Word meronym) (opposite method)
	 */
	public MutableWord addHolonym(Word holonym) {
		Database.link(holonym.node, this.node, Relation.MERONYM);
		
		return this;
	}
// DELETE FUNCTIONS
	
	/** Deletes this word and all of its properties from the database.
	 */
	public void delete() {
		Transaction tx = Database.getDbService().beginTx();
		try {
			this.clearDefinitions();
			Word.index.remove(this.node);
			this.node.delete();
		} finally {
			tx.finish();
		}
	}
	
	/** Deletes all definitions for the current Word.
	 * The definitions are immediately and transparently removed from the database. No need to `commit` modifications.
	 * If you want to delete one single definition, get them with `getDefinitions`, remove it from within the set, `clearDefinitions` and then `addDefinitions` with your copied set.
	 *
	 * @return	This MutableWord, for chainability
	 */
	public MutableWord clearDefinitions() {
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			for (Relationship relation : node.getRelationships(Direction.OUTGOING, Relation.DEFINITION))
				relation.delete(); // delete the relationship first to be able to delete the linked nodes
			for (Definition definition : this.definitions)
				definition.delete(); // let the Definition delete itself
			
			tx.success();
		} finally {
			tx.finish();
		}
		
		this.definitions.clear();
		
		return this;
	}	
	
	/** Deletes all synonyms associated to this Word.
	 * @return This MutableWord, for chainability
	 */
	public MutableWord clearSynonyms() {
		this.delete(Relation.SYNONYM);
		
		return this;
	}
	
	/** Deletes all antonyms associated to this Word.
	 * @return This MutableWord, for chainability
	 */
	public MutableWord clearAntonyms() {
		this.delete(Relation.ANTONYM);
		
		return this;
	}
	
	/** Deletes all troponyms associated to this Word.
	 * @return This MutableWord, for chainability
	 */
	public MutableWord clearTroponyms() {
		this.delete(Relation.TROPONYM);
		
		return this;
	}

	/** Deletes all related vocabulary words associated to this Word.
	 * @return This MutableWord, for chainability
	 */
	public MutableWord clearRelatedVoc() {
		this.delete(Relation.RELATEDVOC);
		
		return this;
	}
	
	/** Deletes all hyponyms associated to this Word.
	 * @return This MutableWord, for chainability
	 */
	public MutableWord clearHyponyms() {
		this.delete(Relation.HYPONYM);
		
		return this;
	}
	
	/** Deletes all meronyms associated to this Word.
	 * @return This MutableWord, for chainability
	 */
	public MutableWord clearMeronyms() {
		this.delete(Relation.MERONYM);
		
		return this;
	}
	
// STANDARD METHODS
	public String toString() {
		return super.toString() + "*";
	}
}
