package edu.unice.polytech.kis.semwiktionary.model;


import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.lang.reflect.Constructor;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Transaction;

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
	
	protected NodeMappedObject initNode() {
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			this.node = Database.getDbService().createNode();

			tx.success();
		} finally {
		    tx.finish();
		}

		return this;
	}
	
	protected NodeMappedObject setProperty(String key, String value) {
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			this.node.setProperty(key, value);
			
			tx.success();
		} finally {
		    tx.finish();
		}
		
		return this;
	}
	
	public <T extends NodeMappedObject> Collection<T> get(Relation relType) {
		Class<T> type = relType.getDestinationType();
		List<T> result = new LinkedList<T>();
		Constructor<T> constructor = null;
		try {
			// constructor = T.class.getConstructor(Node.class); // unfortunately, Java can't handle getting a Class from a Type�
			constructor = type.getConstructor(Node.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("The specified generic type does not offer a Node constructor.", e);
		}
		
		try {
			for (Relationship relation : this.node.getRelationships(Direction.OUTGOING, relType)) {
				result.add(constructor.newInstance(relation.getEndNode()));
			}
		} catch (InstantiationException e) {
			throw new RuntimeException("The Node constructor for the specified type failed.", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("The Node constructor for the specified type failed.", e);
		} catch (java.lang.reflect.InvocationTargetException e) {
			throw new RuntimeException("The Node constructor for the specified type failed.", e);
		}
		
	   return result;
	}
	
	public String get(String key) {
		return (String) node.getProperty(key);
	}
	
	public void delete(Relation relType) {
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			for (Relationship relation : this.node.getRelationships(Direction.OUTGOING, relType))
				relation.delete();

			tx.success();
		} finally {
			tx.finish();
		}
	}
}
