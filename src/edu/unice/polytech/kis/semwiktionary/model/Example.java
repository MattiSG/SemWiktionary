package edu.unice.polytech.kis.semwiktionary.model;


import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import edu.unice.polytech.kis.semwiktionary.database.Database;


public class Example extends NodeMappedObject {
// PROPERTIES
	
	/** The actual example content.
	 */
	private String content;

// CONSTRUCTORS

	public Example() {
		this("");
	}
	
	/** Models an example of a definition.
	 *
	 * @param	example		The actual example content
	 */
	public Example(String example) {
		this.initNode();
		
		this.setContent(example);
	}
	
	/** Retrieves a stored example's content from its node.
	 */
	protected Example(Node node) {
		this.node = node;
		
		this.content = this.get("content");
	}

	
// ACCESSORS
	
	/** Returns the content of this example.
	 */
	public String getContent() {
		return content;
	}
	
	
// MODIFIERS
	
	/**
	* @return	this	for chainability
	*/	
	public Example setContent(String content) {
		this.content = content;
		this.set("content", content);
		
		return this;
	}

// DESTRUCTORS
	
	/** Deletes this Example and all attached properties from the database.
	 */
	public void delete() {
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			this.node.delete();
			
			tx.success();
		} finally {
			tx.finish();
		}
	}

// OVERRIDES
	
	@Override
	public String toString() {
		return this.getContent();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this.getClass().isInstance(o))
			return this.content.equals(((Example) o).getContent());
		
		if (String.class.isInstance(o))
			return this.content.equals(o);
		
		return false;
	}	
}
