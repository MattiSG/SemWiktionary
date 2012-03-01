Synonym
=======

Two synonyms are words with the same meaning.

Each `Word` object might be linked to other words by a `SYNONYM` relation, which means that all these words have (approximately) the same signification.

There is no class named `Synonym`. Indeed, a synonym is a `Word`, a _“synonym object”_ is simply a `Word` object and the synonymy notion is represented by a relation between these words in the database.

Usage
-----

Simply create a new `Word` with `Word.find`.
You'll then get access to its synonyms by calling the `getSynonyms()` method, which returns an iterable collection of `Word`s.

To create, modify or delete synonyms, you may use the following methods:

* **Creation**: you may call the `addSynonym` method, that takes as parameter the `Word` object to link with as a synonym.
* **Modification**: there is no way to update a specific synonym of the object, so you will have to `clear` all synonyms and `add` them again.
* **Deletion**: you may call the `clearSynonyms` method which will delete all synonyms for this `Word`.
