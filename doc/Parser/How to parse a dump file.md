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

If you have already done a parsing earlier, please note that you must reset your database before starting a new one.
It is **necessary** to avoid multiple storage of words in the database that could make the following error : "Inconsistent database."
You can easily delete the database with the following command :

    ant erase_db

Use your favorite editor to modify the parser file to make it fulfill your needs, `src/edu/unice/polytech/kis/semwiktionary/parser/WikimediaDump.jflex`.

Before starting parsing the full database, **remove the `%debug` line** in the parser file (`.jflex`). That will give a significant speedup, and avoid generating several hundreds megabytes of log files. Then:

    ant compile
    ./wiktionary --load path/to/dump/file.xml	# this will take some time (see performance note beneath)
	
If the process ends without error, publish your changes and, if they could be of interest to most other users, send us a pull request!  :)

Error during the parsing process
--------------------------------

Errors might occur while parsing the Wiktionary dump file.
Indeed, we created our parser considering the data that existed in the file and that was the most important to collect, but these informations may change over time. The parser will crash if the file contains a string that is not handled.

In that case, you can correct the parsing rules following these steps :

- The standard output prints the title of a word when it starts to parse it, so the last word displayed is the word that produced the crash.
- Now make a `grep` to obtain this whole word between the `<page>` tags and add it to the test file that contains a subset of words in `test/resources`.
	
	_Example, considering the word "fleur" was responsible for the crash:_
	
	`grep -A 300 -B 1 "<title>fleur</title>" wiktionaryDumpFile.xml`
	
- Add the `%debug` line back in the parser file.
- Try to parse again on this test file, either with `wiktionary --load` or directly with `ant junit`.
- Open the `log/parser-output.txt` and `log/parser-error.txt` files, so you can have explanations on the crash.
- Correct the parser.
- Create JUnit tests that check the validity of the word you added to the test suite, to avoid future regressions.
- Remove the `%debug` in the parser file again.
- Restart the parsing process on the whole file.
- Once the whole file has been processed, publish your changes and make a pull request for them to be integrated in the main repository.
