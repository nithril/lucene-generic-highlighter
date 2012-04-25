lucene-generic-highlighter
==========================

The current project demonstrates how to create a generic Lucene (http://lucene.apache.org) highlighter.

Original Lucene Highlighter is too much coupled with snippet highlighting and  :
- do not allow easily to highlight a whole text
- handles only text with a formatter strongly coupled to text

I have modified the original Lucene Highlighter to allow highlighting of "anything". The highlighter is now a callback instead of a formatter and it's purpose is just to highlight.

I used this code to highlight XML, PDF, HTML...

Note : This project is an extract of a large project with submodule.

