How to create a new relation
============================

We couldn't create all the relation types of the wiktionary in the database.
You can add a new relation following this process.

We currently handle 2 types of relations in the Wikimedia dump file:
* _“simple relations”_ : a simple relation appears as a list of words; each word is directly linked to the base word with the type of the relation.
* _“complex relations”_ : a complex relation appears as a tree of words; each word is linked to its parent in the tree with the type of the relation.

If you encounter a new type of relation which not follow the same algorithm/rules as these two, you can add a new type of relation.
In that case, you must follow these two additional steps :
* Think about the new algorithm associated to the new relation
* Create a new state corresponding to the syntax to parse

Whatever the relation you want to create, you must follow all these steps described bellow :

PARSER
------

* Add the relation link in the initParser method
* Add the keyword representative of the relation in the H3 state

API
---

* Add the relation in the database.Relation file
* Add the add method in MutableWord
* Add the clear method in MutableWord
* Add the get method in Word
* Add the display of the relation in the lookup method of the SemWiktionary class

DOCUMENTATION
-------------

* Add a file in the doc repertory describing the role of the relation

TESTS
-----

* Add if needed a new word using the current relation to the test file
* Run the program loading the test file and check that the relation was correctly added
	./wiktionary --load /path/to/the/file/test
* Create a test class for the relation
* List this class in the main test class RunAndPray.java
* Validate tests

SCALING
-------

* Run the parser again on the totality of the wikimedia dump file
* When finished, check that all words were properly added to the database and check that the relation were correctly added for random words picked in the base
