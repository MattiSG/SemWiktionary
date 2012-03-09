package edu.unice.polytech.kis.semwiktionary.model;


import java.util.Collection;
import java.util.List;
import java.util.LinkedList;

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
		// this method does exactly the same as NodeMappedObject.findAndInstanciateSingle, but is less dynamic to improve performance
		Node result;
		try {
			result = Database.getIndexForName("LexicalCategory").get(NodeMappedObject.INDEX_KEY, pattern).getSingle();
		} catch (java.util.NoSuchElementException e) { // there were multiple results for this query
			throw new RuntimeException("Inconsistent database: multiple nodes found for word '" + pattern + "' in index!", e );
		}
		
		return (result == null ? null : new LexicalCategory(result));
	}
	
// CONSTRUCTORS

	/** Initializes all fields when creating a new lexical category.
	 *
	 *@return	An empty shell to be filled.
	 */
	protected LexicalCategory() {
		// nothing to do
	}

	protected LexicalCategory(Node node) {
		this.node = node;
		
		this.pattern = this.get("pattern");
		this.description = this.get("description");
	}
	
// GETTERS

	public String getPattern() {
		return this.pattern;
	}
	
	public String getDescription() {
		return this.description;
	}
	
// STANDARD METHODS

	@Override
	public String toString() {
		return this.description;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this.getClass().isInstance(o))
			return this.pattern.equals(((LexicalCategory) o).getPattern());
		
		if (String.class.isInstance(o))
			return this.pattern.equals(o);
		
		return false;
	}
}
