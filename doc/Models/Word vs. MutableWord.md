Word vs. MutableWord
====================

Overview
--------

`Word` is a read-only access API to the SemWiktionary database.

`MutableWord`, on the other hand, offers ways to perform all [CRUD](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations on words from the database.

As long as you need read-only access, stick with `Word`. This will make your code safer and a bit faster.

Mutating a Word
---------------

If you irregularly need to transform a `Word` into a `MutableWord`, you can do so with the `MutableWord(Word)` constructor. For example, if you mostly use read-only words but want to be able to modify one under special circumstances, such as an explicit action of your user, mutating on a case-by-case basis is to be preferred.

Mutating comes at a cost: a full new object has to be created, the old one garbage-collected… This is why, if you know you will regularly need CRUD access, you should rather instantiate all your words directly as `MutableWord`s.

Access methods
--------------

To get a read-only `Word`, use `Word.find("your word")`.

To get a CRUD-allowing `MutableWord`, use `MutableWord.obtain("your word")`, or mutate an existing `Word` `w` with `new MutableWord(w)`.
