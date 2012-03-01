Word
====

A `Word` instance models a specific human language word.

It is an abstraction to the database layer, and should be the only way to publicly access data from the Wiktionary database.

Usage
-----

Simply create a new `Word` and pass it the word you're interested in as a `String`. You'll then get access to all necessary methods to get semantic information about that word.

Structure
---------

### Database ###

_Note: In the following, words in italic refer to specific [graph database](http://en.wikipedia.org/wiki/Graph_database) vocabulary. Make sure you understand them._

Since we're using a graph database, a `Word` data is taken from a _node_.

All _properties_ are mapped to primitive types, and nodes connected through _relations_ are modeled either by other `Word` instances (synonyms…) or other classes (definition…).

### Construction ###

A public constructor is offered, that takes as a parameter the natural language word the client is interested in. It is enough to uniquely identify it in the database.

A private constructor that takes a `Node` as parameter is also available, that is used when fetching nodes connected to a `Word` through relations. In such cases, we know the original `Node` but want to avoid the resource waste that the usage of `Strings` would be. However, that's precisely what we want to abstract, so access to such details is private.

Loading and performance
-----------------------

Different levels of database fetching aggressiveness are considered, offering granular control over performance and time/memory tradeoffs. The “levels” referred to are arbitrary and only reflect a step on a lazy-/aggressive-loading scale, _not_ the depth of the fetched graph.

### Level 0 (proxy) ###

The `Word` instance acts as a _proxy_: it does not fetch anything from the database until explicitly asked to do so by the use of a property accessor.

### Level 1 (identification) ###

The `Word` _identifies_ its underlying node upon instanciation, but does not fetch any other information from the database.

_Actual performance gains have yet to be measured._

### Level 2 (properties) ###

Upon instanciation, the `Word` extracts all _properties_ from the node fetched at level 1, and stores them in its members.

_Actual performance gains have yet to be measured._

### Level 3 (relations) ###

The `Word` fetches all neighboring nodes, that is its immediate _relations_. Cascading is forbidden to avoid loading the whole database at once.

_Actual performance gains have yet to be measured._

### Level n (recursive relations) ###

The `Word` fetches all neighboring nodes, that is its immediate _relations_. The loaded nodes then themselves fetch their neighbors, until depth **n** is reached.

_Actual performance gains have yet to be measured._
