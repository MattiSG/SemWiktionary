package edu.unice.polytech.kis.semwiktionary.model;

import java.util.List;
import java.util.ArrayList;

public class Definition {

// PROPERTIES
	
	/** The definition of a word.
	 */
	private String definition;
	/** The examples of this definition.
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
	 *
	 * @param	definition	The content for definition of a word to model
	 */
	public Definition(String definition) {
		this.definition = definition;
		listExample = new ArrayList<String>();
		listDomain = new ArrayList<String>();
		position = 1;
	}
	
	/** Models a definition of a word.
	 *
	 * @param	definition	The content for definition of a word to model
	 * @param	defCount	The order of this definition to model
	 */
	public Definition(String definition, int defCount) {
		this.definition = definition;
		listExample = new ArrayList<String>();
		listDomain = new ArrayList<String>();
		position = defCount;
	}
	
// ACCESSORS

		/** Adds the example to this defintion.
	 *
	 * @param	example	The examples to add
	 */
	public void addExample(String example) {
		this.listExample.add(example);
	}
	
	/** Adds the domain to this defintion.
	 *
	 * @param	domain	The domains to add
	 */
	public void addDomain(String domain) {
		this.listDomain.add(domain);
	}

	/** Returns the content of this defintion.
	 */
	public String getContent() {
		return definition;
	}
	
	/** Returns the examples of this defintion.
	 */
	public List<String> getExamples() {
		return listExample;
	}
	
	/** Returns the domains of this defintion.
	 */
	public List<String> getDomains() {
		return listDomain;
	}
	
	/** Returns the position of this defintion.
	 */
	public int getPosition() {
		return position;
	}
	
}
