SemWiktionary
=============

Java API to locally access data from [wiktionary](http://fr.wiktionary.org). Specific target is the French wiktionary.

Goal
----

Offer a complete graph database and a Java API to access it that provides the following information about a word:

1. Definitions.
2. Semantical relations with other words, such as synonymy, antonymy, parnoymy…
3. _[Lexical class](http://en.wikipedia.org/wiki/Part_of_speech) (or “part of speech”)._ **— not available yet**

Example
-------

    import edu.unice.polytech.kis.semwiktionary.model.Word;
	
	
	Word hello = Word.from("bonjour");
	
	for (Word salutation : hello.getSynonyms()) {
		System.out.println(salutation + " word!");
		
		System.out.print("Most usually used in the context of: ");
		System.out.println(hello.getDefinitions().get(0).getDomain());
	}

How to use
----------

### Prerequisites ###

You will need:

- Java 1.6;
- [Ant](http://ant.apache.org) to build this project;
- this project's source files;
- a [Wiktionary dump file](http://dumps.wikimedia.org/frwiktionary/latest/) with all articles (direct link for French: [130MB archive](http://dumps.wikimedia.org/frwiktionary/latest/frwiktionary-latest-pages-articles.xml.bz2));
- around 2 GB of free space (three quarters of it can be reclaimed by deleting the dump file once the database has been populated from it);
- around 20 hours to initialize the database (_unless you use an already parsed database, see below_).

### Database population ###

**Remember that we are currently offering support only for the French wiktionary**. This software has not been tested with any other language. You are most welcome to try and give us feedback, though!

#### Using an already parsed database (recommended) ####

This is clearly the preferred method, as it will allow you to skip the long task of parsing the wiktionary yourself. As long as our servers can handle the load, you can download the [full French wiktionary database](http://dl.mattischneider.fr/semwiktionary/data-v0.1.2.zip) (80 MB ZIP).

#### Parsing yourself ####

You will first need to deflate the dump file you previously downloaded, then:

    git clone git://github.com/MattiSG/SemWiktionary.git # or download the source files manually
    cd SemWiktionary
    ant compile
    ./wiktionary --load path/to/dump/file.xml # this will take some time (see performance note beneath)
	
If you really want to parse a full dumpfile, though, you should first remove the `%debug` line in `src/edu/unice/polytech/kis/semwiktionary/parser/WikimediaDump.jflex`. That will give quite a speedup.

### Lookup ###

For testing or a basic usage, you can simply use the lookup interface this way:

	./wiktionary	# interactive, or:
	./wiktionary [wordToLookUp [anotherWord [...]]]
	
To integrate with your own application, or export the data in any format you wish, use the provided API. To access it, run `ant doc` and read the documentation in the `doc` folder.

Constraints
-----------

This project being academic, some technical constraints were applied to it.

1. Data source: [French Wiktionary](http://fr.wiktionary.org). Internationalization could be thought about, but is not currently aimed at.
2. Technologies: Java. [Graph database](http://en.wikipedia.org/wiki/Graph_database), preferably Neo4j.

Equivalent projects and rationale
---------------------------------

- [JWKTL](http://www.ukp.tu-darmstadt.de/software/jwktl/). Not documented, source code access was not allowed by authors (yet?).

[Several](http://rendering.xwiki.org/xwiki/bin/view/Main/Architecture) [tools](http://dbpedia.hg.sourceforge.net/hgweb/dbpedia/extraction_framework/file/2c1eea7da303/wiktionary) parse MediaWiki markup and create an AST from it. However, most of them are overkill and not specific enough for the Wiktionary dialects (much more strongly structured than Wikipedia, for which most tools are tailored).

License
-------

GNU [General Public License](http://www.gnu.org/licenses/gpl.html).

Credits
-------

### Authors ###
- [Matti Schneider-Ghibaudo](http://mattischneider.fr)
- [Fabien Brossier](http://fabienbrossier.fr)
- Ngoc Nguyen Thinh Dong

### Tutors ###
- Michel Gautero
- Carine Fédèle

### Used projects ###
- [Neo4j](http://neo4j.org/)	(GPL)
- [JFlex](http://jflex.de/) by Gerwin Klein	(GPL)
- [Markdown doclet](http://code.google.com/p/markdown-doclet/) documentation parser by Richard Nichols	(GPLv2)
- [JUnit](http://www.junit.org/)	(CPL)
- [Unitils](http://unitils.org/)	(Apache 2)

### Miscellaneous ###
- Javadoc stylesheet by [Apache Software Foundation](http://click-project-template.googlecode.com/svn-history/r2/trunk/documentation/javadoc-stylesheet.css).
