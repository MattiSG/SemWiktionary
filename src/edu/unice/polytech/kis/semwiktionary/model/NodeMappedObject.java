package edu.unice.polytech.kis.semwiktionary.model;


import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.lang.reflect.Constructor;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
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
	
	public Node getNode() {
		return this.node;
	}
	
	/**Stores the given property on this node.
	*There is a restriction as compared to Neo4j: only strings are accepted.
	*
	*@return	this, for chainability
	*/
	public NodeMappedObject set(String key, String value) {
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			this.node.setProperty(key, value);
			
			tx.success();
		} finally {
		    tx.finish();
		}
		
		return this;
	}

	/**Connects the given object to this one, with the given relation type.
	 *
	 *@return	the created relationship, so you can annotate it if needs be
	 */
	public Relationship set(Relation relType, NodeMappedObject target) {
		return Database.link(this.node, target.node, relType);
	}
	
	/**Fetches all NodeMappedObjects that are connected to this object with the given Relation type.
	 *You'll need to specify the expected output type, so as to avoid casts.
	 *Example: Collection<Word> synonyms = myWord.<Word>get(Relation.SYNONYM);
	 *
	 *This method defaults to ignoring relations' directions. If you want to follow one only,explicitly specify it as a second argument.
	 */
	public <T extends NodeMappedObject> Collection<T> get(Relation relType) {
		return get(relType, Direction.BOTH);
	}	 
	
	/**Fetches all NodeMappedObjects that are connected to this object with the given Relation type.
	*You'll need to specify the expected output type, so as to avoid casts.
	*Example: Collection<Word> synonyms = myWord.<Word>get(Relation.SYNONYM);
	*/
	@SuppressWarnings("unchecked") // otherwise, getDestinationType will warn about <T extends NMO> being different from <? extends NMO>
	public <T extends NodeMappedObject> Collection<T> get(Relation relType, Direction dir) {
		Class<T> type = relType.getDestinationType();
		List<T> result = new LinkedList<T>();
		Constructor<T> constructor = null;
		try {
			// constructor = T.class.getConstructor(Node.class); // unfortunately, Java can't handle getting a Class from a Type
			constructor = type.getDeclaredConstructor(Node.class); // get*Declared*Constructor allows bypassing public-only access
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("The specified generic type (" + type.getName() + ") does not offer a Node constructor.", e);
		}
		
		try {
			for (Relationship relation : this.node.getRelationships(dir, relType)) {
				result.add(constructor.newInstance(relation.getOtherNode(this.node)));
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
	
	/**
	*@return	this	for chainability
	*/
	protected NodeMappedObject indexAs(String key) {
		return indexAsOn(key, getIndexKey());
	}
	
	/** Indexes the current `NodeMappedObject` on the passed key, in the specified index.
	 *
	 *@param	key	the key on which to index the current element
	 *@param	indexKey	the index on which to index the current element
	 *@return	this	for chainability
	 */
	public NodeMappedObject indexAsOn(String key, String indexKey) {
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			getIndex(indexKey).add(this.node, key, true); // we can't index on a key only, so the value we associate to the key is always "true"
			
			tx.success();
		} finally {
		    tx.finish();
		}
		
		return this;
	}
	
	/** Gets the key of the database index for this inheriting type of `NodeMappedObject`.
	*
	*@return either the value of the `static String INDEX_KEY` field or, if not specified, the type's name (ex: `"Word"` for `Word`).
	*@throw	RuntimeException	if the `INDEX_KEY` field is specified, but is not `static` and `public`
	*/
	protected String getIndexKey() {
		return getIndexKey(this.getClass());
	}
	
	
	/** Gets the key of the database index for this inheriting type of `NodeMappedObject`.
	 *
	 *@return either the value of the `static String INDEX_KEY` field or, if not specified, the type's name (ex: `"Word"` for `Word`).
	 *@throw	RuntimeException	if the `INDEX_KEY` field is specified, but is not `static` and `public`
	 */
	private static String getIndexKey(Class type) {
		String indexKey;
		
		// TODO: cache a class=>key map to avoid costly exception-based lookup
		
		try {
			indexKey = (String) (type
								 .getDeclaredField("INDEX_KEY")    // any indexable class should declare a static INDEX_KEY field
								 .get(null)); // since the field is static, we get it for the `null` instance
		} catch (java.lang.NoSuchFieldException e) {
			indexKey = type.getSimpleName(); // unfortunately, there is no way to test for a field presence other than throw a costly exception…
		} catch (java.lang.IllegalAccessException e) {
			throw new RuntimeException("Class '" + type + "' does not specify a public 'INDEX_KEY' static field, and wants to use automatic lookup.", e);
		}
		
		return indexKey;	
	}
	
	/** Gets the index of nodes for the type of this NodeMappedObject-refining.
	 * Uses the default `getIndexKey()` key to obtain the index.
	 *
	 *@see	getIndexKey
	 */
    private Index<Node> getIndex() {
		return getIndex(getIndexKey());
	}
	
	/** Gets the index of nodes for the given type.
	 * Uses the default `getIndexKey(Class)` key to obtain the index.
	 *
	 *@see	getIndexKey
	 */
    private static Index<Node> getIndex(Class type) {
		return getIndex(getIndexKey(type));
	}
	
	/** Gets the index of nodes for the NodeMappedObject-refining type passed in parameter.
	 * The key used for the index is the one passed in parameter.
	 */
	private static Index<Node> getIndex(String indexKey) {
		return Database.getIndexForName(indexKey); // more info on [Neo4j Index doc](http://api.neo4j.org/current/org/neo4j/graphdb/index/Index.html)
	}
	
	/** Counts the number of elements of the given type accessible in the database.
	* This is only the number of elements _indexed_. Even though that fits the definition of _accessible_, this is not a totally accurate way of counting elements, especially with redirections.
	*/
	public static long count(Class<? extends NodeMappedObject> type) {
		return getIndex(type).query("*", true).size();
	}
	
	/** Handles index lookup and instanciation if a matching object is found.
	* The index used is the name of the `resultClass` parameter.
	*
	*@param	resultClass	the destination type of the found object
	*@param	query	the index lookup query
	*@return	an instance of `resultClass` properly initialized from all data in the database, or `null` if no matching result is found
	*@throw	RuntimeException	if the `resultClass` does not have a `Node` constructor
	*@throw	RuntimeException	if several matching nodes were found, as this is a violation of the database structure
	*/
	public static <T extends NodeMappedObject> T findAndInstanciateSingleOf(Class<T> resultClass, String query) {
		Node result;
		String indexKey = getIndexKey(resultClass);
		Constructor<T> constructor = null;
		try {
		
			constructor = resultClass.getDeclaredConstructor(Node.class); // get*Declared*Constructor allows bypassing public-only access
			result = (Node) (getIndex(indexKey)
							 .get(query, true) // we can't index on a key only, so the value we associate to the key is always "true"
							 .getSingle());
							 
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("The specified generic type (" + resultClass + ") does not offer a Node constructor.", e);
		} catch (java.util.NoSuchElementException e) { // there were multiple results for this query
			throw new RuntimeException("Inconsistent database: multiple nodes found for id '" + query + "' with type '" + resultClass + "'!", e);
		}

		try {
		
			return (result == null ? null : constructor.newInstance(result));
			
		} catch (java.lang.reflect.InvocationTargetException e) {
			throw new RuntimeException(e);		
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**Fetches the property for that key, always as a String.
	* If the property is missing, will return an empty string.
	*/
	public String get(String key) {
		return (node.hasProperty(key) ? (String) node.getProperty(key) : "");
	}
	
	/** Calls deletion propagation method, un-indexes the current node, and deletes it.
	* Transactions are handled within this method.
	*/
	public void delete() {
		Transaction tx = Database.getDbService().beginTx();
		
		try {
			this.onDelete(); // hook for inheriting classes
			
			String indexKey = getIndexKey();
			if (Database.getDbService().index().existsForNodes(indexKey)) // we might delete a non-indexed node
				this.getIndex(indexKey).remove(this.node);
			
			this.node.delete();
		} finally {
			tx.finish();
		}
	}
	
	/** This method will be called when `delete`ing a `NodeMappedObject`.
	* Use it as a hook to propagate all necessary deletions.
	* **WARNING**: even if you're not interested in propagating deletion, you _need_ to implement this method to allow for deletion. Otherwise, it will be assumed that the class does not allow deletion, and an exception will be thrown.
	*/
	protected void onDelete() {
		throw new RuntimeException(new IllegalAccessException("Deletion is not allowed on this class (implement `onDelete()` to allow it)."));
	}
	
	/** Deletes all relations of the given type linked to this `NodeMappedObject`.
	*/
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
	
// OVERRIDES	
	@Override
	public boolean equals(Object o) {
		if (! this.getClass().isInstance(o))
			return false;
		
		NodeMappedObject comparedTo = (NodeMappedObject) o;
		
		return comparedTo.node.equals(this.node);
	}
	
	@Override
	public String toString() {
		return "Raw object mapped on node <" + this.node + ">";
	}
}
