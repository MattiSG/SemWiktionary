Hyperonym
=======

An hyperonym of a word is another word which have a larger meaning than the meaning of the first one. For example, _“fruit”_ is an hyperonym of _“apple”_.

Each `Word` object may be linked to other words by an `HYPERONYM` relation, which means that all these words have a meaning more accurate than the considered word. A common use of hyperonyms is a hierarchy construction. Indeed, you may recursively get the hyperonyms of the hyperonyms to construct a semantic tree.

There is no class named `Hyperonym`, because an hyperonym is a `Word`. An _“hyperonym object”_ is simply a `Word` object, and the hyperonymy notion is represented by a relation between these words in the database.

Usage
-----

Simply create a new `Word` with `Word.find`.
You'll then get access to its hyperonyms by calling the `getHyperonyms` method, which returns an iterable collection of `Word`s.
Please note the returned hyperonyms are the one with the most close meaning of the word.

For example, _“fruit”_ is an hyperonym of _“cherry”_, and _“cherry”_ is an hyperonym of _“morello cherry”_.
So you must call the `getHyperonyms` method on the word _“morello cherry”_ to obtain the word _“cherry”_.
Then you must call the `getHyperonyms` method for the second time on _“cherry”_ to obtain _“fruit”_.

If you don't want to get the hyperonyms of the considered word but all words which have the considered word as an hyperonym, you can call the `getHyperonym` function which is the opposite function of `getHyperonyms`. See the **Hyponym** documentation for more information.

To create, modify or delete hyperonyms, you may use the following methods:

* **Creation**: you may call the `addHyperonym` method that takes as parameter the `Word` object representing the hyperonym.
* **Modification**: there is no way to update a specific hyperonym of the object, so you will have to `clear` all hyperonyms and `add` them again.
* **Deletion**: you may call the `clearHyperonyms` method, which will delete all hyperonyms for this `Word`.
