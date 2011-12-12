package edu.unice.polytech.kis.semwiktionary.model;

import org.neo4j.graphdb.Node;

import edu.unice.polytech.kis.semwiktionary.database.Database;
import edu.unice.polytech.kis.semwiktionary.database.Relation;

public class MutableWord extends Word {
	
	// Static constructors
	public static MutableWord from(String word) {
		if (Word.exists(word))
			return new MutableWord(word);
		
		return null;
	}
	
	public static MutableWord create(String word) {
		MutableWord result = new MutableWord(word);
		result.node = Database.createNodeWithProperty("title", word);
		return result;
	}
	
	// Constructor
	private MutableWord(String word) {
		super(word);
	}

	
	// Update
	public MutableWord addDefinition(Definition definition) {
		Node defNode = Database.createNodeWithProperty("definition", definition.getDefinition());
		
		if (defNode == null)
			return null;
		
		Database.link(this.node, defNode, Relation.DEFINITION);
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
