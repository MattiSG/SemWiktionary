package edu.unice.polytech.kis.semwiktionary.model;


import java.util.List;
import java.util.LinkedList;

import org.neo4j.graphdb.Node;


public class Word {

	// Attributes
	protected Node node;
	protected String name;
	protected List<Definition> definitions;
	
	// Static constructors
	public static Word from(String word) {
		if (Word.exists(word)) //TODO: check if it should not be the other way around, depending on Neo4j getter implementation
			return new Word(word);
		
		return null;
	}
	
	public static boolean exists(String word) {
		//TODO do an actual test
		return true;
	}
	
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
