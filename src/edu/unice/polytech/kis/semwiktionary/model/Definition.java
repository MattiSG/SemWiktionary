package edu.unice.polytech.kis.semwiktionary.model;


import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import edu.unice.polytech.kis.semwiktionary.database.Database;
import edu.unice.polytech.kis.semwiktionary.database.Relation;
import edu.unice.polytech.kis.semwiktionary.model.Example;


public class Definition extends NodeMappedObject {

	/** PROPERTIES
	 */
	//@{
	
	/** The actual definition content.
	 */
	private String content;
	/** The examples of this content.
	*/
	private List<Example> listExample = new LinkedList<Example>();
	/** The domains of this definition.
	 */
	private List<String> listDomain;
	/** The position of this definition in the list.
	 */
	private int position;
	//@}

	/** CONSTRUCTORS
	 */
	//@{

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
		
		this.listDomain = new LinkedList<String>();
	}
	
	/** Retrieves a stored definition's content from its node.
	 */
	public Definition(Node node) {
		this.node = node;
		
		this.content = this.get("content");
		this.position = new Integer(this.get("position"));
	}
	//@}
	
	/** ACCESSORS
	 */
	//@{
	
	/** Returns the content of this definition.
	 */
	public String getContent() {
		return content;
	}
	
	/** Returns all examples of this definition.
	 */
	public List<Example> getExamples() {
		if (listExample.isEmpty()) {
			this.fetchExamples();
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
	//@}
	
	/** MODIFIERS
	 */
	//@{	

	/** Adds the given example to this definition.
	 * The passed in `String` will be automatically wrapped in an `Example`.
	 *
	 * @param	exampleContent	The content of the example to add.
	 * @return	this	for chainability
	 */
	public Definition addExample(String exampleContent) {
		return this.addExample(new Example(exampleContent));
	}
		
	/** Adds the given example to this definition.
	 *
	 * @param	example	The example to add
	 * @return	this	for chainability
	 */
	public Definition addExample(Example example) {
		Database.link(this.node, example.node, Relation.EXAMPLE);
		this.listExample.add(example);
		
		return this;
	}
	
	public Definition addExamples(Iterable<Example> examples) {
		for (Example example : examples)
			this.addExample(example);

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
	//@}
	
	/** DATABASE ACCESS
	 */
	//@{

	/** Loads the examples for this Definition from the database.
	 */
	protected void fetchExamples() {
		this.listExample.addAll(this.<Example>get(Relation.EXAMPLE));
	}
	//@}
	
	/** DESTRUCTORS
	 */
	//@{
	
	protected void onDelete() {
		// nothing to propagate
	}
	
	public Definition clearExamples() {
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			for (Relationship relation : node.getRelationships(Direction.OUTGOING, Relation.EXAMPLE))
				relation.delete(); // delete the relationship first to be able to delete the linked nodes
			for (Example example : this.listExample)
				example.delete(); // let the Example delete itself
			
			tx.success();
		} finally {
			tx.finish();
		}
		
		this.listExample.clear();
		
		return this;
	}
	//@}
	
	/** OVERRIDES
	 */
	//@{
	
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
	//@}
}
