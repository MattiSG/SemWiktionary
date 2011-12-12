package edu.unice.polytech.kis.semwiktionary.model;

import org.neo4j.graphdb.Node;

import edu.unice.polytech.kis.semwiktionary.database.Database;
import edu.unice.polytech.kis.semwiktionary.database.Relation;

/** Represents a simple word in the database.
 * The class provides Read methods by his parent, in addition to Create, Update, and Delete methods. 
 * Some properties are only loaded when their associated get methods are called.
 * 
 * @author Fabien BROSSIER
 * @author Matti SCHNEIDER
 * @author Steven SANCHO
 * @author Thinh DONG
 * @version 1.0
 */
public class MutableWord extends Word {
	
// STATIC METHODS
	
	/** Finds a word in the database from his title.
	 * Constructs a MutableWorld object with all his properties (definition, synonyms, ...). 
	 * @param word The title of the world to find in the database
	 * @return The complete Word object created or null if the word is not in the database (unlikely) 
	 */
	public static MutableWord from(String word) {
		if (Word.exists(word))
			return new MutableWord(word);
		
		return null;
	}
	
	/** Creates a new Word in the database from the specified title.
	 * The word is created even if the same word already exists in the database.
	 * You should use the functions "from" or "exists" to check the existence before.
	 * @param word The title of the Word object created
	 * @return The MultableWord object added to the database
	 */
	public static MutableWord create(String word) {
		MutableWord result = new MutableWord(word);
		result.node = Database.createNodeWithProperty("title", word);
		return result;
	}
	
// CONSTRUCTORS

	/** Constructs a MutableWord from his title.
	 * @param title The title of the Word
	 */
	private MutableWord(String word) {
		super(word);
	}
	
	// UPDATE FUNCTIONS
	
	/** Adds the specified definition to the current Word.
	 * The definition is created and linked in the database but also added to the definition list attribute.  
	 * @param definition The Definition object associated to the Word
	 * @return The Word which is attached the Definition.
	 */
	public MutableWord addDefinition(Definition definition) {
		Node defNode = Database.createNodeWithProperty("definition", definition.getDefinition());
		
		if (defNode == null)
			return null;
		
		Database.link(this.node, defNode, Relation.DEFINITION);
		this.definitions.add(definition);
		
		return this;
	}

	/** Remove a definition from the definition list of the current Word.
	 * The definition is removed from the database and from the definition list attribute.
	 * @param definition The definition object to remove
	 * @return The current word
	 */
	public MutableWord removeDefinition(Definition definition) {
		//TODO database
		this.definitions.remove(definition);
		return this;
	}


	// DELETE FUNCTIONS
	
	/** Deletes a word and all his properties from the database.Z 
	 * @param word
	 */
	// TODO define the behavior of the relations 
	public static void erase(MutableWord word) {
		word.clearDefinitions();
		//TODO call other delete functions
	}
	
	/** Deletes all definitions of the current Word.
	 * The definitions are deleted from the database and the attribute list is cleared.
	 * @return The current word after the suppression of definitions.
	 */
	public MutableWord clearDefinitions() {
		//TODO database
		this.definitions.clear();
		return this;
	}

}
