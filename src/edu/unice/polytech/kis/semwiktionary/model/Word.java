package edu.unice.polytech.kis.semwiktionary.model;


import java.util.List;
import java.util.LinkedList;

import org.neo4j.graphdb.Node;


public class Word {

	// Attributes
	private Node node;
	private String name;
	private List<Definition> definitions;
	
	// Constructors
	public Word(String name) {
		this.name = name;
		this.definitions = new LinkedList<Definition>();
		//TODO
	}
	
	private Word(Node node) {
		this.node = node;
		//TODO
	}

	// Accessors
	public String getName() {
		return name;
	}

	public List<Definition> getDefinitions() {
		if (definitions.isEmpty())
			this.fetchDefinitions();
		
		return definitions;
	}

	// Modifiers
	public Word addDefinition(Definition definition) {
		this.definitions.add(definition);
		return this;
	}
	
	// Database access
	protected void fetchNode() {
		//TODO
		//this.node = ...
	}
	protected void fetchDefinitions() {
		//TODO
		//this.definitions = ...
	}

}
