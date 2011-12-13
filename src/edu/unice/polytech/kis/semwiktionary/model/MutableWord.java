package edu.unice.polytech.kis.semwiktionary.model;


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
	
	/** Finds a word in the database from its title.
	 * Constructs a MutableWord object with all its properties (definition, synonyms…).
	 * Such an object _may_ modify the database. See `Word` for a safer use.
	 *
	 * @param	word	The word to model
	 * @return	The complete MutableWord object created or null if the word is not in the database
	 */
	public static MutableWord from(String word) {
		if (Word.exists(word))
			return new MutableWord(word);
		
		return null;
	}
	
	/** Creates a new word in the database.
	 * The word is created even if the same word already exists in the database, possibly leading to duplicates. You should use the `Word.exists` method to check for existence before creating a new word.
	 *
	 * @param	word	The natural language word to add to the database
	 * @return	The MutableWord model for the word added to the database
	 */
	public static MutableWord create(String word) {
		MutableWord result = new MutableWord(word);
		result.node = Database.createNodeWithProperty("title", word);
		return result;
	}
	
// CONSTRUCTORS

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
		Node defNode = Database.createNodeWithProperty("definition", definition.getDefinition());
		
		if (defNode == null)
			return null;
		
		Database.link(this.node, defNode, Relation.DEFINITION);
		this.definitions.add(definition);
		
		return this;
	}

	/** Removes the giben Definition from the definitions for this Word.
	 * The definition is immediately and transparently removed from the database. No need to `commit` modifications.
	 *
	 * @param	definition	The definition object to remove
	 * @return	This MutableWord, for chainability
	 */
	public MutableWord removeDefinition(Definition definition) {
		//TODO database
		this.definitions.remove(definition);
		return this;
	}


	// DELETE FUNCTIONS
	
	/** Deletes a word and all of its properties from the database.
	 *
	 * @param word
	 */
	// TODO define the behavior of the relations 
	public static void erase(MutableWord word) {
		word.clearDefinitions();
		//TODO call other delete functions
	}
	
	/** Deletes all definitions for the current Word.
	 * The definitions are immediately and transparently removed from the database. No need to `commit` modifications.
	 *
	 * @return	This MutableWord, for chainability
	 */
	public MutableWord clearDefinitions() {
		//TODO database
		this.definitions.clear();
		return this;
	}

}
