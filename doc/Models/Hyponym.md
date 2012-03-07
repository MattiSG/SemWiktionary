Hyponym
=======

An hyponym of a word is another word which have a meaning included in the meaning of the first one. For example, _“apple”_ is an hyponym of _“fruit”_.

Each `Word` object may be linked to other words by an `HYPONYM` relation, which means that all these words have a meaning more accurate than the considered word. A common use of hyponyms is a hierarchy construction. Indeed, you may recursively get the hyponyms of the hyponyms to construct a semantic tree.

There is no class named `Hyponym`, because an hyponym is a `Word`. An _“hyponym object”_ is simply a `Word` object, and the hyponymy notion is represented by a relation between these words in the database.

Usage
-----

Simply create a new `Word` with `Word.find`.
You'll then get access to its hyponyms by calling the `getHyponyms` method, which returns an iterable collection of `Word`s.
Please note the returned hyponyms are the one with the most close meaning of the word.

For example, _“morello cherry”_ is an hyponym of _“cherry”_, and _“cherry”_ is an hyponym of _“fruit”_.
So you must call the `getHyponyms` method on the word _“fruit”_ to obtain the word _“cherry”_.
Then you must call the `getHyponyms` method for the second time on _“cherry”_ to obtain _“morello cherry”_.

If you don't want to get the hyponyms of the considered word but all words which have the considered word as an hyponym, you can call the `getHyperonym` function which is the opposite function of `getHyponyms`. See the **Hyperonym** documentation for more information.

To create, modify or delete hyponyms, you may use the following methods:

* **Creation**: you may call the `addHyponym` method that takes as parameter the `Word` object representing the hyponym.
* **Modification**: there is no way to update a specific hyponym of the object, so you will have to `clear` all hyponyms and `add` them again.
* **Deletion**: you may call the `clearHyponyms` method, which will delete all hyponyms for this `Word`.
