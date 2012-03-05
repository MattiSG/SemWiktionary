Meronym
=======

A meronym of a word is another word whose meaning designates a subset of the first one's meaning. For example, _“arm”_ is a meronym of _“body”_.

Each `Word` object may be linked to other words by an `MERONYM` relation, which means that all these words have a meaning whose designate a subset meaning. A common use of meronyms is a hierarchy construction. Indeed, you may recursively get the meronyms of the meronyms to construct a semantic tree.

There is no class named `Meronym`, because a meronym is a `Word`. A _“meronym object”_ is simply a `Word` object, and the meronymy notion is represented by a relation between these words in the database.

Usage
-----

Simply create a new `Word` with `Word.find`.
You'll then get access to its meronyms by calling the `getMeronyms` method, which returns an iterable collection of `Word`s.
Please note the returned meronyms are the one with the most close meaning of the word.

For example, _“forearm”_ is a meronym of _“arm”_, and _“arm”_ is a meronym of _“body”_.
So you must call the `getMeronyms` method on the word _“body”_ to obtain the word _“arm”_.
Then you must call the `getMeronyms` method for the second time on _“arm”_ to obtain _“forearm”_.

If you don't want to get the meronyms of the considered word but all words which have the considered word as a meronym, you can call the `getHolonyms` function which is the opposite function of `getMeronyms`. See the **Holonym** documentation for more information.

To create, modify or delete meronyms, you may use the following methods:

* **Creation**: you may call the `addMeronym` method that takes as parameter the `Word` object representing the meronym.
* **Modification**: there is no way to update a specific meronym of the object, so you will have to `clear` all meronyms and `add` them again.
* **Deletion**: you may call the `clearMeronyms` method, which will delete all meronyms for this `Word`.
