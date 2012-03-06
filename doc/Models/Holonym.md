Holonym
=======

An holonym of a word is another word whose meaning designates a subset of the first one's meaning. For example, _“body”_ is a holonym of _“arm”_.

Each `Word` object may be linked to other words by an `HOLONYM` relation, which means that all these words have a meaning whose designate a subset meaning. A common use of holonyms is a hierarchy construction. Indeed, you may recursively get the holonyms of the holonyms to construct a semantic tree.

There is no class named `Holonym`, because an holonym is a `Word`. An _“holonym object”_ is simply a `Word` object, and the holonymy notion is represented by a relation between these words in the database.

Usage
-----

Simply create a new `Word` with `Word.find`.
You'll then get access to its holonyms by calling the `getHolonyms` method, which returns an iterable collection of `Word`s.
Please note the returned holonyms are the one with the most close meaning of the word.

For example, _“body”_ is an holonym of _“arm”_, and _“arm”_ is an holonym of _“forearm”_.
So you must call the `getHolonyms` method on the word _“forearm”_ to obtain the word _“arm”_.
Then you must call the `getHolonyms` method for the second time on _“arm”_ to obtain _“forearm”_.

If you don't want to get the holonyms of the considered word but all words which have the considered word as an holonym, you can call the `getHolonyms` function which is the opposite function of `getHolonyms`. See the **Meronym** documentation for more information.

To create, modify or delete holonyms, you may use the following methods:

* **Creation**: you may call the `addHolonym` method that takes as parameter the `Word` object representing the holonym.
* **Modification**: there is no way to update a specific holonym of the object, so you will have to `clear` all holonyms and `add` them again.
* **Deletion**: you may call the `clearHolonyms` method, which will delete all holonyms for this `Word`.
