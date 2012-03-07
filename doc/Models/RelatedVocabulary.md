Related vocabulary
==================

Two words which have a related meaning may be considered as related vocabulary.
Each `Word` object might be linked to other words by a `RELATEDVOC` relation, which means that all these words have a related meaning.

There is no class named `RelatedVocabulary`. Indeed, a _“related vocabulary word”_ is simply a `Word` object and the related meaning notion is represented by a relation between these words in the database.

It is preferable to use more accurate relations to link words which have a related meaning. Use the `RELATEDVOC` relation only if none of these relations can by used:
* `Synonym`
* `Antonym`
* `Troponym`
* `Hyponym`
* `Hyperonym`
* `Holonym`
* `Meronym`

Usage
-----

Simply create a new `Word` with `Word.find`.
You'll then get access to its related words by calling the `getRelatedVoc()` method, which returns an iterable collection of `Word`s.

To create, modify or delete related words, you may use the following methods:

* **Creation**: you may call the `addRelatedVoc` method, that takes as parameter the `Word` object to link with as a word with a related meaning.
* **Modification**: there is no way to update a specific related word of the object, so you will have to `clear` all related words and `add` them again.
* **Deletion**: you may call the `clearRelatedWords` method which will delete all related words for this `Word`.
