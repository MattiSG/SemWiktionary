SemWiktionary database introduction
===================================

Our database engine is a NoSQL, graph-oriented database: Neo4j.

Basic introduction
------------------
http://docs.neo4j.org/chunked/1.5/what-is-a-graphdb.html

IDE inclusion
-------------
http://docs.neo4j.org/chunked/1.5/tutorials-java-embedded-setup.html

Performance notes
-----------------
Note that starting a database server is an expensive operation, so don’t start up a new instance every time you need to interact with the database! The instance can be shared by multiple threads. Transactions are thread confined.

Traversers are lazy loading, so it’s performant even when dealing with thousands of statuses - they are not loaded until we actually consume them.