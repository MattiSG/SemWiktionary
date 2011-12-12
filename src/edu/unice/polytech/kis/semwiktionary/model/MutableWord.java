package edu.unice.polytech.kis.semwiktionary.model;

import edu.unice.polytech.kis.semwiktionary.database.Database;

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
		//TODO database
		/*
		 	for (Definition currentDef : word.getDefinitions()) {
			Node definitionNode = graphDb.createNode();
			definitionNode.setProperty("definition", currentDef);
			wordNode.createRelationshipTo(definitionNode, Relations.DEFINITION);
		*/
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
