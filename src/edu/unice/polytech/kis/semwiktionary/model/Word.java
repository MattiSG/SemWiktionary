package edu.unice.polytech.kis.semwiktionary.model;

import java.util.List;
import java.util.LinkedList;

import org.neo4j.graphdb.Node;

/** Represents a simple word in the database.
 * The class provides all access methods to the word.
 * Some properties are only loaded when their associated get methods are called.
 * 
 * @author Fabien BROSSIER
 * @author Matti SCHNEIDER
 * @author Steven SANCHO
 * @author Thinh DONG
 * @version 1.0
 */
public class Word {

// PROPERTIES
	
	/** The node in the Neo4J database
	 */
	protected Node node;
	
	/** The title of the Word
	 */
	protected String title;
	
	/** The definitions of the Wordord 
	 */
	protected List<Definition> definitions;
	
// STATIC METHODS
	
	/** Finds a word in the database from his title.
	 * Constructs a World object with all his properties (definition, synonyms, ...). 
	 * @param word The title of the world to find in the database
	 * @return The complete Word object created or null if the word is not in the database (unlikely)
	 */
	public static Word from(String word) {
		if (Word.exists(word)) //TODO: check if it should not be the other way around, depending on Neo4j getter implementation
			return new Word(word);
		
		return null;
	}
	
	/** Tests if a given Word exists in the database from his title. 
	 * @param word The title of the Word to search in the database
	 * @return True if the Word exists, false otherwise
	 */
	public static boolean exists(String word) {
		//TODO do an actual test
		return true;
	}
	
// CONSTRUCTORS
	
	/** Constructs a Word from his title.
	 * @param title The title of the Word
	 */
	public Word(String title) {
		this.title = title;
		this.definitions = new LinkedList<Definition>();
		//TODO
	}

	/** Constructs a Word object from a Node in the database.
	 * Can be particularly interesting in propagation cases.
	 * @param node The node object of the database
	 */
	private Word(Node node) {
		this.node = node;
		//TODO
	}

// ACCESSORS
	
	/** Returns a String which represents the title of the Word.
	 * @return The title of the Word.
	 */
	public String getTitle() {
		return title;
	}

	/** Returns a list of all the definitions associated to the Word title.
	 * @return The list of Definition objects for the current Word 
	 */
	public List<Definition> getDefinitions() {
		if (definitions.isEmpty())
			this.fetchDefinitions();
		
		return definitions;
	}
	
// DATABASE ACCESS
	
	/** Loads the node of the Word object.
	 * Can be usefull for some methods like from(String title). 
	 */
	// TODO : complete this documentation.
	protected void fetchNode() {
		//TODO
		//this.node = ...
	}
	
	/** Loads the definitions of the Word objects from the database.
	 */
	protected void fetchDefinitions() {
		//TODO
		//this.definitions = ...
	}

}