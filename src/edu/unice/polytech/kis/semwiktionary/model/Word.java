package edu.unice.polytech.kis.semwiktionary.model;

import java.util.List;

public class Word {

	// Attributes
	private Node node;
	private String name;
	private List<Definition> definitions;
	
	// Constructors
	public Word(String name) {
		
	}
	
	private Word(Node node) {
		
	}

	// Accessors
	public String getName() {
		return name;
	}

	public List<Definition> getDefinitions() {
		return definitions;
	}

	// Modifiers
	public void setDefinitions(List<Definition> definitions) {
		this.definitions = definitions;
	}

}
