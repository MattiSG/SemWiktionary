package edu.unice.polytech.kis.semwiktionary.database;


import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.Transaction;

import edu.unice.polytech.kis.semwiktionary.model.NodeMappedObject;


/** This class helps managing so-called “lazy patterns”.
* The Wiktionary uses many patterns, or "Modèles", to reuse common pieces of information, such as lexical categories.
* These are described in some entries, but no guarantee is made that the description entries will be _after_ others that might use them.
* Therefore, to avoid a two-pass parsing, or we use a _lazy_ management of such relations.
* The idea is as follows:
* - When a pattern is encountered in an entry, the `LazyPatternsManager` records it as an entry in an index of name `UNKNOWN`, with the key of the name of the pattern.
* - When the pattern description entry is encountered, a named node is created to store its information.
* - All nodes previously registered as having this pattern in the `UNKNOWN` index get a link added to the new pattern node.
* - All references indexed with the new pattern in the `UNKNOWN` index are removed.
*/
public class LazyPatternsManager {
	
	public static final String INDEX_KEY = "Unknown";
	
	private static Index<Node> index = Database.getIndexForName(INDEX_KEY);
	
	/** Registers the given NodeMappedObject as having the given pattern, without knowing which it is.
	*/
	public static void register(String pattern, NodeMappedObject element) {
//		System.err.println("> LazyPatternsManager registration:\t'" + pattern + "' @ '" + element + "'"); //DEBUG
		
		element.indexAsOn(pattern, INDEX_KEY);
	}

	/** Adds a relation from all nodes previously registered with the given pattern to the given destination, with the given relation type.
	*
	*@param	pattern	The pattern previously unknown.
	*@param	destination	The new NodeMappedObject to which previously-registered elements should be linked to.
	*@param	relType	How the new relations should be typed.
	*/
	public static void transferAll(String pattern, NodeMappedObject destination, Relation relType) {
		Node destinationNode = destination.getNode();
		
		Transaction tx = Database.getDbService().beginTx();

		IndexHits<Node> hits = index.get(NodeMappedObject.INDEX_KEY, pattern);

		try {
			for (Node currentNode : hits) {
				currentNode.createRelationshipTo(destinationNode, relType);
				index.remove(currentNode);
//				System.err.println("> LazyPatternsManager update:\t'" + currentNode.getProperty("title") + "' --[" + relType + "]--> '" +  destination + "'"); //DEBUG
			}
			
			tx.success();
		} finally {
			hits.close();
			
		    tx.finish();
		}
	}
}
