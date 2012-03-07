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
public class MutableLexicalCategory extends LexicalCategory {

	/**
	 * We'll share the same index as `LexicalCategory` but, since the classname is different, we need to override the default "classname" heuristic for finding the index.
	 *@see	NodeMappedObject.getIndexKey
	 */
	public static String INDEX_KEY = "LexicalCategory";
	
// LOOKUP

	/** Finds a lexical category in the database from its pattern, or creates a new one if none is found.
	 *
	 * @param	pattern	The pattern to look for.
	 * @return	A matching LexicalCategory object.
	 */
	public static MutableLexicalCategory obtain(String pattern) {
		LexicalCategory immutableCat = LexicalCategory.find(pattern);
		
		return (immutableCat == null
				? new MutableLexicalCategory(pattern)
				: new MutableLexicalCategory(immutableCat));
	}
	
// CONSTRUCTORS

	private MutableLexicalCategory(String pattern) {
		this.initNode()
			.set("pattern", pattern)
			.indexAs(pattern);
			
		this.pattern = pattern;
	}
	
	protected MutableLexicalCategory(Node node) {
		super(node);
	}
	
	public MutableLexicalCategory(LexicalCategory model) {
		super(model.node);
	}

// SETTERS
	
	public void setDescription(String description) {
		this.set("description", description);
		
		this.description = description;
	}
}
