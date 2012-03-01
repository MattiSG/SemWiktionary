Synonym
====

Two Synonyms are words which has the same signification.
Each `Word` object might be linked to other words by a synonym relation, which singificates that all these words have approximately the same signification.
There is no class named Synonym. Because a synonym is a word, a "synonym object" is simply a Word object and the synonymy notion is represented by a relation between these words in the database.

Usage
-----

Simply create a new `Word` and pass it the word you're interested in as a `String`.
You'll then get access to his synonyms by calling the getSynonyms method which returns a collection of `Word`.

For creation, modification or deletion of synonyms, you may use the following methods :
* Creation : you may call the addSynonym method is take in parameter the word object to link with as a synonym
* Modification : there is no way to get a specific synonym of the object, so you will have to delete the object and create it again.
* Deletion : you may call the clearSynonyms method which will delete all synonyms.
