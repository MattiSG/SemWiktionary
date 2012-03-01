Troponym
====

A Troponym of a verb is another verb which describes more accurately the action specified by the first one. In consequence, a troponym relation can only be established between two verbs.
Each `Word` object might be linked to other words by a troponym relation, which singificates that all these other words have a signification more accurate than the first one.
There is no class named Troponym. Because a synonym is a word, a "troponym object" is simply a Word object and the troponymy notion is represented by a relation between these words in the database.

Usage
-----

Simply create a new `Word` and pass it the word you're interested in as a `String`.
You'll then get access to his troponyms by calling the getTroponyms method which returns a collection of `Word`.

For creation, modification or deletion of troponyms, you may use the following methods :
* Creation : you may call the addTroponym method is take in parameter the word object to link with as a troponym
* Modification : there is no way to get a specific troponym of the object, so you will have to delete the object and create it again.
* Deletion : you may call the clearTroponyms method which will delete all troponyms.
