Wiktionary dump file
====================

A Wiktionary dump file is an XML file.

The following elements of its structure are used by SemWiktionary:

	<mediawiki>
		<page>
			<title>**name of the word**</title>
			<revision>
				<text xml:space="preserve">
				
				**MediaWiki-formatted text containing all data to be extracted**
				
				</text>
			</revision>
		</page>
	</mediawiki>
	
As you can see, the XML is really just a light shell around the actual content. The semantic extraction only takes place in the MediaWiki-formatted text. This is more problematic, since MediaWiki is very loosely formatted, is ambiguous and is written by users, who sometimes make formatting mistakes. The parser therefore has to be quite resilient to unexpected content, and may use some heuristics to correct mistakes, just like the official formatter [does](http://www.mediawiki.org/wiki/Wikitext_parser/Stage_2:_Informal_grammar).
