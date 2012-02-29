Parsing your own database
=========================

If you want to tweak the parser to better suit your own needs, you will need to parse the full Wiktionary dump file to generate the database. This process is long (currently around 20 hours), so you should definitely consider using an already-parsed database in all other cases.

Please also **remember that we are currently offering support only for the French wiktionary**. This software has not been tested with any other language. You are most welcome to try and give us feedback, though!

Prerequisites
-------------

You will need:

- Java 1.6;
- [Ant](http://ant.apache.org) to build this project;
- this project's source files;
- a [Wiktionary dump file](http://dumps.wikimedia.org/frwiktionary/latest/) with all articles (direct link for French: [130MB archive](http://dumps.wikimedia.org/frwiktionary/latest/frwiktionary-latest-pages-articles.xml.bz2));
- around 2 GB of free space (three quarters of it can be reclaimed by deleting the dump file once the database has been populated from it);
- around 20 hours to initialize the database.

Database population
-------------------

You will first need to deflate the dump file you previously downloaded, then:

    git clone git://github.com/MattiSG/SemWiktionary.git # or download a zipball
    cd SemWiktionary
    
Use your favorite editor to modify the parser file to make it fulfill your needs, `src/edu/unice/polytech/kis/semwiktionary/parser/WikimediaDump.jflex`.

Before starting parsing the full database, **remove the `%debug` line** in the parser file (`.jflex`). That will give a significant speedup, and avoid generating several hundreds megabytes of log files. Then:

    ant compile
    ./wiktionary --load path/to/dump/file.xml	# this will take some time (see performance note beneath)
	
If the process ends without error, publish your changes and, if they could be of interest to most other users, send us a pull request!  :)
