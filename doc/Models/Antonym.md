Antonym
====

Two Antonyms are words which has the opposite signification.
Each `Word` object might be linked to other words by an antonym relation, which singificates that all these words have approximately the opposite signification.
There is no class named Antonym. Because an antonym is a word, a "antonym object" is simply a Word object and the antonymy notion is represented by a relation between these words in the database.

Usage
-----

Simply create a new `Word` and pass it the word you're interested in as a `String`.
You'll then get access to his antonyms by calling the getAntonyms method which returns a collection of `Word`.

For creation, modification or deletion of antonyms, you may use the following methods :
* Creation : you may call the addAntonym method is take in parameter the word object to link with as an antonym
* Modification : there is no way to get a specific antonym of the object, so you will have to delete the object and create it again.
* Deletion : you may call the clearAntonyms method which will delete all antonyms.
