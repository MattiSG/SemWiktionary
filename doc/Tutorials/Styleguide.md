Coding Style, Philosophy & Implemented standards
================================================

Repo management
---------------

- [SemVer](http://semver.org), Semantic Versioning.
- [README-Driven Development](http://tom.preston-werner.com/2010/08/23/readme-driven-development.html). Definitely applies to branches too.
- [GitHub-flow](http://scottchacon.com/2011/08/31/github-flow.html)-like, that is:
	- `master` branch should always be deployable.
	- one branch per functionality, with explicit branch naming. Once functionality is implemented and tested, it is merged into `master`, and the branch is **deleted**.
- code is considered valid only once it has been **documented** and **tested**. Automated tests are not mandatory for UI code (maintenance cost too high).
- atomic commits: a commit is **one** change. It may be a documentation change, an API change, an implementation change, it may be split across several files or stand in one line, but it changes only **one aspect** of the application.

File hierarchy
--------------

Versioned:

- `src` contains all source files.
- `test` contains all test source files.
- `doc` contains all documentation, except this README. [Markdown](http://daringfireball.net/projects/markdown) is to be used for documentation formatting.
- `lib` contains all third-party libraries.

Ignored:

- `build` contains intermediate build products.
- `bin` contains `class` files.
- `jar` contains build products as executable libraries.
- `data` contains program-generated data.
- `log` contains program-generated log files.
- `dist` contains deliverables.
- `doc/vendor` contains included libraries documentation, and is not to be distributed nor versioned, it is just a convenient shortcut.

Coding style
------------

OOP. DRY. Dynamicity. TDD with [JUnit](http://junit.sourceforge.net/).

### Writing ###

- Follow Oracle's [Java Code Conventions](http://www.oracle.com/technetwork/java/codeconvtoc-136057.html).
- Scope opening brackets are **on the same line** as the control element that opens the scope; scope closing brackets are on their own line, except when in an `if / else if / else` construct, where we want to achieve a `… } else { …` look.
- **tabs**. Spaces allowed in very specific contexts **only**, such as aligning multi-line arguments. For converters, tab:space ratio is set to 1:4.
- **[Javadoc](http://www.oracle.com/technetwork/java/javase/documentation/index-137868.html)**-style comments **with Markdown** instead of HTML. That seems to be [Markdown-doclet](http://www.richardnichols.net/2009/06/markdown-doclet-for-javadoc/)-parsable, but the main goal is to have the most usable documentation in the code itself. Public documentation elements (i.e. parts that provide details about variables, methods rather than algorithmic details) should be in double-star comments (`/**`).
- inline comments (`//`) and single-star comments (`/*`) comment a specific part of the implementation, and do not give any public-interest information.

### Evils ###

- code duplication;
- hardcoded stuff;
- coupling;
- bad documentation;
- non-explicit function names.

Basically, everything that will end up biting you bad later on.