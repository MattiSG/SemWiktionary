Troponym
========

A troponym of a verb is another verb which describes more accurately the action specified by the first one. In consequence, a troponym relation can only be established between two verbs.

Each `Word` object might be linked to other verbs by a `TROPONYM` relation, which means that all these other verbs have a signification more accurate than the first one.

There is no class named `Troponym`. Indeed, a troponym is a word, a _“troponym object”_ is simply a `Word` object and the troponymy notion is represented by a relation between these words in the database.

Usage
-----

Simply create a new `Word` with `Word.find`.
You'll then get access to its troponyms by calling the `getTroponyms()` method which returns a collection of `Word`s.

To create, modify or delete troponyms, you may use the following methods:

* **Creation**: you may call the `addTroponym` method, that takes as parameter the word object to link with as a troponym.
* **Modification**: there is no way to update a specific troponym of the object, so you will have to `clear` all troponyms links and `add` them again.
* **Deletion**: you may call the `clearTroponyms` method which will delete all troponyms for this `Word`.
