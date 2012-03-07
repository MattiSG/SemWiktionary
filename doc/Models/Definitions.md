Definitions
===========

Definitions are given as lists.

They are the first list in any type section.

Each definition:

- Starts by one or several dashes (`#`).
- Most often resides on a single line.
- If it spans multiple lines, after the first line, following lines have the same count of dashes, and are followed by a colon (`:`).
- Is followed by zero or (most often) more examples.

Nesting
-------

The number of dashes at the beginning of each line gives the nesting level for that definition.

Nested definition complete, or give precisions about, upper-level definitions. So, they are usually written as complements, and not full sentences. We therefore decided to concatenate nested definitions with all their parents to get meaningful phrasings. Inversely, upper-level definitions that contain nested ones are often not precise enough (they most often don't contain examples themselves, for example). Hence our decision to not integrate them, but only their concatenated version.

Examples
--------

Examples follow the same structure as definitions, that is, they are lists. However, they are bulletted lists instead of numbered lists.

That means each line:

- Starts by the same number of dashes (`#`) as the definition they relate to, immediately followed by an asterisk (`*`).
- Most often resides on a single line.
- If it spans multiple lines, after the first line, following lines have the same count of dashes and asterisk, and are followed by a colon (`:`).