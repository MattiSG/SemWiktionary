package edu.unice.polytech.kis.semwiktionary.model;


import java.util.List;
import java.util.LinkedList;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Traverser.Order;

import edu.unice.polytech.kis.semwiktionary.database.Database;
import edu.unice.polytech.kis.semwiktionary.database.Relation;


/** Provides basic management for items stored as nodes in a Neo4j graph database.
 *
 * @author	[Matti Schneider-Ghibaudo](http://mattischneider.fr)
 */
public abstract class NodeMappedObject {

// PROPERTIES
		
	/** The database storage for this item.
	 *	See conceptual documentation for database layout.
	 */
	protected Node node;
	
	
	public <T extends NodeMappedObject> Iterable<T> get() {
		List<T> result = new LinkedList<T>();
		return result;
	}
	
	public String get(String key) {
		return (String) node.getProperty(key);
	}
}
