package edu.unice.polytech.kis.semwiktionary.model;


import java.util.List;
import java.util.LinkedList;

import org.neo4j.graphdb.Node;


public class MutableWord extends Word {
	
	// Static constructors
	public static MutableWord from(String word) {
		if (Word.exists(word))
			return new MutableWord(word);
		
		return null;
	}
	
	public static MutableWord create(String word) {
		//TODO database
		return new MutableWord(word);
	}
	
	// Constructor
	private MutableWord(String word) {
		super(word);
	}

	
	// Update
	public MutableWord addDefinition(Definition definition) {
		//TODO database
		this.definitions.add(definition);
		return this;
	}

	public MutableWord removeDefinition(Definition definition) {
		//TODO database
		this.definitions.remove(definition);
		return this;
	}


	// Delete
	public static void erase(MutableWord word) {
		word.clearDefinitions();
		//TODO call other delete functions
	}
	
	public MutableWord clearDefinitions() {
		//TODO database
		this.definitions.clear();
		return this;
	}

}
