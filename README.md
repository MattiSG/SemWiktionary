SemWiktionary
=============

Java API to locally access data from [wiktionary](http://fr.wiktionary.org). Specific target is the French wiktionary.

Objectifs
---------

### Objectif primaire ###

Fournir une API Java permettant d'accéder aux informations suivantes sur un mot de la langue française :

1. [nature](http://fr.wikipedia.org/wiki/Classe_lexicale) (ou “classe lexicale”) ;
2. relations avec d'autres mots de la base de données (liste non exhaustive : synonymie, homonymie…) ;
3. définition.

### Objectifs secondaires ###

**Performance.** Durées maximales envisageables : 1 journée pour le chargement de la base de données ; 5 minutes pour l'exécution d'une requête.

How to use
----------

You will need:

- [Ant](http://ant.apache.org) to build this project;
- this project's source files;
- a [Wiktionary dump file](http://dumps.wikimedia.org/frwiktionary/latest/) with all articles (direct link for French: [130MB archive](http://dumps.wikimedia.org/frwiktionary/latest/frwiktionary-latest-pages-articles.xml.bz2));
- around 3 GB of free space.

Currently, no choice is offered regarding the input file. You will first need to replace `test/resources/miniwiki.xml` by the dump file you previously downloaded, then:

    $ cd path/to/project/directory
    $ ant parse
	$ ./wiktionary [wordToLookUp [anotherWord [...]]]

Constraints
-----------

This project being academic, some technical constraints were applied to it.

1. Data source: [French Wiktionary](http://fr.wiktionary.org). Internationalization could be thought about, but is not currently aimed at.
2. Technologies: access API in Java. [Graph database](http://en.wikipedia.org/wiki/Graph_database), preferably Neo4j.

Equivalent projects and rationale
---------------------------------

- [JWKTL](http://www.ukp.tu-darmstadt.de/software/jwktl/). Not documented, source code impossible to get, authors not reachable by email.

Many tools parse MediaWiki markup and create an AST from it. However, most of them are overkill and would require several passes to properly fill the database, therefore leading to poor performance.

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

### Miscellaneous ###
- Javadoc stylesheet by [Apache Software Foundation](http://click-project-template.googlecode.com/svn-history/r2/trunk/documentation/javadoc-stylesheet.css).
