package edu.unice.polytech.kis.semwiktionary.model;


import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;

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
	
	/** Finds a word in the database from its title.
	 * Constructs a MutableWord object with all its properties (definition, synonyms).
	 * Such an object _may_ modify the database. See `Word` for a safer use.
	 *
	 * @param	word	The word to model
	 * @return	The complete MutableWord object created or null if the word is not in the database
	 */
	public static MutableWord from(String word) {
		Word immutableWord = Word.from(word);
		return (immutableWord == null ? null : new MutableWord(immutableWord));
	}
	
	/** Creates a new word in the database.
	 * The word is created even if the same word already exists in the database, possibly leading to duplicates. You should use the `Word.exists` method to check for existence before creating a new word.
	 *
	 * @param	word	The natural language word to add to the database
	 * @return	The MutableWord model for the word added to the database
	 */
	public static MutableWord create(String word) {
		MutableWord result = new MutableWord(word);
		
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			result.createNode();
			result.node.setProperty("title", word);
			
			Word.index.add(result.node, Word.INDEX_KEY, word);
			
			tx.success();
		} finally {
		    tx.finish();
		}

		return result;
	}
	
// CONSTRUCTORS

	/** Models an editable natural-language word.
	 *
	 * @param	model	An immutable version of a word you want to modify.
	 */
	public MutableWord(Word model) {
		super(model.getTitle());
	}
	
	/** Models an editable natural-language word.
	 *
	 * @param	word	The natural language word to model
	 */
	private MutableWord(String word) {
		super(word);
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
		// TODO performance: use a single transaction
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
		
		return this;
	}	
	
}
