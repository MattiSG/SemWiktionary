package edu.unice.polytech.kis.semwiktionary.model;


import java.util.List;
import java.util.ArrayList;

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
	
	/** Models a definition of a word.
	 * Default position value is 0.
	 *
	 * @param	definition	The actual definition content
	 */
	public Definition(String definition) { //TODO: remove this constructor (here for backward compatibility at the moment)
		this(definition, 0);
	}
	
	/** Models a definition of a word.
	 *
	 * @param	definition	The actual definition content
	 * @param	position	The position of this definition relatively to other definitions for the same word. Lower is better ranked.
	 */
	public Definition(String definition, int position) {
		this.content = definition;
		this.listExample = new ArrayList<String>();
		this.listDomain = new ArrayList<String>();
		this.position = position;
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
		return listExample;
	}
	
	/** Returns all domains of this defintion.
	 */
	public List<String> getDomains() {
		return listDomain;
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
	 */
	public void addExample(String example) {
		this.listExample.add(example);
	}
	
	/** Adds the given domain to this defintion.
	 *
	 * @param	domain	The domain to add
	 */
	public void addDomain(String domain) {
		this.listDomain.add(domain);
	}

// DESTRUCTORS
	
	/** Deletes this Definition and all attached properties from the database.
	 */
	public void delete() {
		this.node.delete();
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
