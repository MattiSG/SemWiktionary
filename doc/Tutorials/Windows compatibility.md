SemWiktionary for Windows
=========================

SemWiktionary was originally created for UNIX systems.
In particular, the shell script _wiktionary_ at the root project automatically runs the software.
A notification is displayed on Linux and Mac operating systems when the parsing is over.

However, the program can be ran on Windows systems following the next steps:

Constraints
-----------

1. Java JDK6 or higher: [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk-7u3-download-1501626.html).
2. Ant builder: [Ant](http://ant.apache.org/bindownload.cgi).

Compilation
-----------

As specified on the [Ant website](http://ant.apache.org/manual/install.html), you must add the following environment variables to run ant properly:

- ANT\_HOME: ant installation folder

	_Example:_ set ANT_HOME=C:\\ant

- JAVA\_HOME: java installation folder

	_Example:_ set JAVA_HOME=C:\\java\\jdk-1.5.0.05

- PATH: java path to localize the ant program

	set PATH=%PATH%;%ANT_HOME%\\bin

You can check that ant is properly installed on your system by typing `ant` in a prompter anywhere on your disk. The result should be:

`Buildfile: build.xml does not exist!
Build failed`

This message indicates that you are not in a ant compilable project, but now you know that ant is properly installed.

Move to the root project and simple type `ant compile` to compile the project.

Execution
---------

We do not provide any executable script for Windows systems. You are free to develop one in a batch file, and add a notification system if needed.
You can run the program with the `java` command adapted on Windows. To run the interactive mode, you can type (on the root of the project):

	java -cp .\\bin;.\\lib\\*;\\lib\\neo4j_database\\*;\\lib\\parser_build\\*;SemWiktionary_MattiSG.jar edu.unice.polytech.kis.semwiktionary.SemWiktionary

You can add parameters at the end of the line:

* `--load \\path\\to\\the\\dump\\file`: loads a dump file in the database
* `a_word_to_find_in_the_base` : finds the specified word in the database and exits.
