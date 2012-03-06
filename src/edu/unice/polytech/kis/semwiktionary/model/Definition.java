package edu.unice.polytech.kis.semwiktionary.model;


import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import edu.unice.polytech.kis.semwiktionary.database.Database;


public class Definition extends NodeMappedObject {

// PROPERTIES
	
	/** The actual definition content.
	 */
	private String content;
	/** The examples of this content.
	 */
	private List<String> listExample;
	/** The domains of this definition.
	 */
	private List<String> listDomain;
	/** The position of this definition in the list.
	 */
	private int position;

// CONSTRUCTORS

	public Definition() {
		this("", 0);
	}
	
	/** Models a definition of a word.
	 *
	 * @param	definition	The actual definition content
	 * @param	position	The position of this definition relatively to other definitions for the same word. Lower is better ranked.
	 */
	public Definition(String definition, int position) {
		this.initNode();
		
		this.setContent(definition)
			.setPosition(position);
		
		this.listExample = new LinkedList<String>();
		this.listDomain = new LinkedList<String>();
	}
	
	/** Retrieves a stored definition's content from its node.
	 */
	public Definition(Node node) {
		this.node = node;
		
		this.content = this.get("content");
		this.position = new Integer(this.get("position"));
	}
	
// ACCESSORS
	
	/** Returns the content of this definition.
	 */
	public String getContent() {
		return content;
	}
	
	/** Returns all examples of this definition.
	 */
	public List<String> getExamples() {
		if (listExample == null) {
			listExample = new ArrayList<String>(1);
			listExample.add(this.get("example"));
		}
		
		return this.listExample;
	}
	
	/** Returns all domains of this defintion.
	 */
	public List<String> getDomains() {
		if (listDomain == null) {
			listDomain = new ArrayList<String>(1);
			listDomain.add(this.get("domain"));
		}
		
		return this.listDomain;
	}
	
	/** Returns the position of this defintion.
	 */
	public int getPosition() {
		return position;
	}

// MODIFIERS
	
	/** Adds the given example to this definition.
	 *
	 * @param	example	The example to add
	 * @return	this	for chainability
	 */
	public Definition addExample(String example) {
		this.listExample.add(example);
		this.set("example", example); // TODO: this stores only the last one
		
		return this;
	}
	
	/** Adds the given domain to this defintion.
	 *
	 * @param	domain	The domain to add
	 * @return	this	for chainability
	 */
	public Definition addDomain(String domain) {
		this.listDomain.add(domain);
		this.set("domain", domain); // TODO: this stores only the last one
		
		return this;
	}

	/**
	* @return	this	for chainability
	*/
	public Definition setPosition(int position) {
		this.position = position;
		this.set("position", "" + position);
		
		return this;
	}
	
	/**
	* @return	this	for chainability
	*/	
	public Definition setContent(String content) {
		this.content = content;
		this.set("content", content);
		
		return this;
	}

// DESTRUCTORS
	
	protected void onDelete() {
		// nothing to propagate
	}

// OVERRIDES
	
	@Override
	public String toString() {
		return this.getContent();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this.getClass().isInstance(o))
			return this.content.equals(((Definition) o).getContent());
		
		if (String.class.isInstance(o))
			return this.content.equals(o);
		
		return false;
	}
}
