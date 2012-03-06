package edu.unice.polytech.kis.semwiktionary.model;


import java.util.Collection;
import java.util.List;
import java.util.LinkedList;

import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Node;

import edu.unice.polytech.kis.semwiktionary.database.Database;
import edu.unice.polytech.kis.semwiktionary.database.Relation;


/** Models the [part of speech](http://en.wikipedia.org/wiki/Part_of_speech) of a word.
 *
 * @author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
 */
public class LexicalCategory extends NodeMappedObject {

// PROPERTIES

	/** The pattern that represents this lexical category in the Wiktionary.
	 */
	protected String pattern;
	
	/** The human-readable name of this lexical category.
	*/
	protected String description;
	
// LOOKUP

	/** Finds a lexical category in the database from its pattern.
	 *
	 * @param	pattern	The pattern to look for.
	 * @return	The complete LexicalCategory object created or null if no match is found in the database.
	 */
	public static LexicalCategory find(String pattern) {
		return NodeMappedObject.<LexicalCategory>findAndInstanciateSingleOf(LexicalCategory.class, pattern);
	}
	
// CONSTRUCTORS

	public LexicalCategory(String pattern) {
		this.initNode()
			.set("pattern", pattern)
			.indexAs(pattern);
			
		this.pattern = pattern;
	}
	
	public LexicalCategory(Node node) {
		this.node = node;
		
		this.pattern = this.get("pattern");
		this.description = this.get("description");
	}
	
// GETTERS

	public String getDescription() {
		return this.description;
	}
	
// SETTERS
	
	public void setDescription(String description) {
		this.set("description", description);
		
		this.description = description;
	}
	
// STANDARD METHODS

	public String toString() {
		return this.description;
	}
}
