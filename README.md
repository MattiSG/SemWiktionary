SemWiktionary
=============

Java API to locally access data from [Wiktionary](http://fr.wiktionary.org), a collaboratively-edited, free dictionary. Specific target is the French Wiktionary.

Goal
----

Offer a complete graph database and a Java API to access it that provides the following information about a word:

1. Definitions and usage examples.
2. Semantical relations with other words:
	- synonyms & antonyms;
	- [troponyms](http://en.wiktionary.org/wiki/troponym);
	- [hyponyms](http://en.wiktionary.org/wiki/hyponym) & [hyperonyms](http://en.wiktionary.org/wiki/hypernym);
	- [holonyms](http://en.wiktionary.org/wiki/holonym) & [meronyms](http://en.wiktionary.org/wiki/meronym),
	- [meaning-related vocabulary (FR)](http://fr.wiktionary.org/wiki/Modèle:-voc-).
3. [Lexical class](http://en.wikipedia.org/wiki/Part_of_speech) (noun, adjective, verb…).

Example
-------

    import edu.unice.polytech.kis.semwiktionary.model.Word;
	
	
	Word hello = Word.find("bonjour");	// database lookup
	
	for (Word salutation : hello.getSynonyms()) {
		println(salutation + " world!");	// all variants of “hello world!”…
		
		print("Most usually used in the context of: "); // …with the domain (usage context, e.g. “sociology”)…
		println(hello.getDefinitions().get(0).getDomain()); // …of their most common meaning
	}

How to use
----------

**Remember that we are currently offering support only for the French Wiktionary**. This software has not been tested with any other language. You are most welcome to try and contribute support for other languages, though!

### Acquiring the API ###

Download the latest build from the [downloads page](https://github.com/MattiSG/SemWiktionary/downloads).

All necessary dependencies are in the `lib` folder, and the API itself is available as a JAR at the archive's root.

### Acquiring an already parsed database ###

This is clearly the preferred method, as it will allow you to skip the long task of parsing the Wiktionary yourself. As long as our servers can handle the load, you can download the [full French Wiktionary database](http://dl.mattischneider.fr/semwiktionary/) as an archive.
**Make sure to download the same database version as your access library version.**

You will then have to move the contents of the archive in a `data` folder in the deflated API archive, in order to get the following file hierarchy:

    ┲SemWiktionary (deflated API archive)
	├  SemWiktionary.jar
	├  wiktionary
	├┬ lib
	 ┋ (…many jars…)
	├┬ data (deflated database archive)
	 ┋ (…many "neostore" files…)

### Lookup ###

For testing or a basic usage, you can simply use the lookup interface this way:

	./wiktionary	# interactive, or:
	./wiktionary [wordToLookUp [anotherWord [...]]]
	
To integrate SemWiktionary within your own application, or export the data in any format you wish, use the provided API. Its documentation is available in the `doc/javadoc` folder of the archive. You will need to include the SemWiktionary JAR and and all those in the `lib` folder, and provide a `data` folder containing the database at the root of your project folder.

### Tweaking the parser ###

If you are interested in modifying the parser, generate your own database and so on, download the source and read `doc/Parser/How to parse a dump file.md`.

Equivalent projects and rationale
---------------------------------

- [JWKTL](http://www.ukp.tu-darmstadt.de/software/jwktl/). Not documented, source code access was not allowed by authors.

[Several](http://rendering.xwiki.org/xwiki/bin/view/Main/Architecture) [tools](http://dbpedia.hg.sourceforge.net/hgweb/dbpedia/extraction_framework/file/2c1eea7da303/wiktionary) parse MediaWiki markup and create an AST from it. However, most of them are both overkill and not specific enough for the Wiktionary dialects (much more structured than Wikipedia, for which most tools are tailored).

License
-------

GNU [General Public License](http://www.gnu.org/licenses/gpl.html).

Contact authors for a different licensing request.

Credits
-------

### Authors ###
- [Matti Schneider-Ghibaudo](http://mattischneider.fr)
- [Fabien Brossier](http://fabienbrossier.fr)
- Dong Thinh

### Tutors ###
- Michel Gautero
- Carine Fédèle

### Used projects ###
- [Neo4j](http://neo4j.org/) graph database	(GPL)
- [JFlex](http://jflex.de/) Java lexer by Gerwin Klein	(GPL)
- [Markdown doclet](http://code.google.com/p/markdown-doclet/) documentation parser by Richard Nichols	(GPLv2)
- [JUnit](http://www.junit.org/) unit-testing framework	(CPL)
- [Unitils](http://unitils.org/) extensions for JUnit	(Apache 2)
- [Gwtwiki](http://code.google.com/p/gwtwiki/) converter wiki text to plain text	(EPLv1)

### Miscellaneous ###
- Javadoc stylesheet by the [Apache Software Foundation](http://click-project-template.googlecode.com/svn-history/r2/trunk/documentation/javadoc-stylesheet.css).
