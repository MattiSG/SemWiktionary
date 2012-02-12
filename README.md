SemWiktionary
=============

Java API to locally access data from [wiktionary](http://fr.wiktionary.org). Specific target is the French wiktionary.

Goal
----

Offer a complete graph database and a Java API to access it that provides the following information about a word:

1. Definitions.
2. Semantical relations with other words, such as synonymy, antonymy, parnoymy…
3. _[Lexical class](http://en.wikipedia.org/wiki/Part_of_speech) (or “part of speech”)._ **— not available yet**



How to use
----------

You will need:

- [Ant](http://ant.apache.org) to build this project;
- this project's source files;
- a [Wiktionary dump file](http://dumps.wikimedia.org/frwiktionary/latest/) with all articles (direct link for French: [130MB archive](http://dumps.wikimedia.org/frwiktionary/latest/frwiktionary-latest-pages-articles.xml.bz2));
- around 2 GB of free space;
- around an hour to initialize the database.

You will first need to deflate the dump file you previously downloaded, then:

    git clone git://github.com/MattiSG/SemWiktionary.git # or download the source files manually
    cd SemWiktionary
    ant compile
    ./wiktionary --load path/to/dump/file.xml # this will take some time (see performance note beneath)
	./wiktionary [wordToLookUp [anotherWord [...]]]
	
If you really want to parse a full dumpfile, though, you should first remove the `%debug` line in `src/edu/unice/polytech/kis/semwiktionary/parser/WikimediaDump.jflex`. That will give quite a speedup.

Constraints
-----------

This project being academic, some technical constraints were applied to it.

1. Data source: [French Wiktionary](http://fr.wiktionary.org). Internationalization could be thought about, but is not currently aimed at.
2. Technologies: access API in Java. [Graph database](http://en.wikipedia.org/wiki/Graph_database), preferably Neo4j.

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
