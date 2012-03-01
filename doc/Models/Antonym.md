Antonym
=======

Two antonyms are words with opposite meanings.

Each `Word` object may be linked to other words by an `ANTONYM` relation, which means that all these words have an (approximately) opposite signification.

There is no class named `Antonym`, because an antonym is a `Word`. An _“antonym object”_ is simply a `Word` object, and the antonymy notion is represented by a relation between these words in the database.

Usage
-----

Simply create a new `Word` with `Word.find`.
You'll then get access to its antonyms by calling the `getAntonyms()` method, which returns an iterable collection of `Word`s.

To create, modify or delete antonyms, you may use the following methods:

* **Creation**: you may call the `addAntonym` method that takes as parameter the `Word` object representing the antonym.
* **Modification**: there is no way to update a specific antonym of the object, so you will have to `clear` all antonyms and `add` them again.
* **Deletion**: you may call the `clearAntonyms` method, which will delete all antonyms for this `Word`.
