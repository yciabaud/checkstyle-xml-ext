XML extension for Checkstyle
============================

This extension provides the ability for checkstyle to check on XML files.
Build the jar, add it to the classpath and run checkstyle over XML!

Checks
------

### Checkstyle

The following existing checks are supported:

* [.markdown](http://daringfireball.net/projects/markdown/) -- `gem install redcarpet`
* [.textile](http://www.textism.com/tools/textile/) -- `gem install RedCloth`
* [.rdoc](http://rdoc.sourceforge.net/) -- `gem install rdoc -v 3.6.1`
* [.org](http://orgmode.org/) -- `gem install org-ruby`
* [.creole](http://wikicreole.org/) -- `gem install creole`
* [.mediawiki](http://www.mediawiki.org/wiki/Help:Formatting) -- `gem install wikicloth`
* [.rst](http://docutils.sourceforge.net/rst.html) -- `easy_install docutils`
* [.asciidoc](http://www.methods.co.nz/asciidoc/) -- `brew install asciidoc`
* [.pod](http://search.cpan.org/dist/perl/pod/perlpod.pod) -- `Pod::Simple::HTML`
  comes with Perl >= 5.10. Lower versions should install Pod::Simple from CPAN.
* .1 - Requires [`groff`](http://www.gnu.org/software/groff/)

### XML extension specific


Contributing
------------

Want to contribute? Great! There are two ways to add checks.


### Checkstyle's guide

[The original guide]http://checkstyle.sourceforge.net/writingchecks.html remains
available to the XML extension. Just keep in mind that the AST tree for XML is not
the same as the Java one.

You can use the a GUI that displays the structure of a Java source file. To run it type

    java -classpath "checkstyle-5.4-all.jar;" \
        com.puppycrawl.tools.checkstyle.gui.XmlMain
      

on the command line. Click the button at the bottom of the frame and select a syntactically correct Java source file. The frame will be populated with a tree that corresponds to the structure of the Java source code. 


### XML

If your markup can be translated using a Ruby library, that's
great. Check out Check `lib/github/markups.rb` for some
examples. Let's look at Markdown:

    markup(:markdown, /md|mkdn?|markdown/) do |content|
      Markdown.new(content).to_html
    end

We give the `markup` method three bits of information: the name of the
file to `require`, a regular expression for extensions to match, and a
block to run with unformatted markup which should return HTML.

If you need to monkeypatch a RubyGem or something, check out the
included RDoc example.

Tests should be added in the same manner as described under the
`Commands` section.


Installation
-----------

    mvn install


Usage
-----

    require 'github/markup'
    GitHub::Markup.render('README.markdown', "* One\n* Two")

Or, more realistically:

    require 'github/markup'
    GitHub::Markup.render(file, File.read(file))


Testing
-------

To run the tests:

    $ rake

To add tests see the `Commands` section earlier in this
README.


Contributing
------------

1. Fork it.
2. Create a branch (`git checkout -b my_markup`)
3. Commit your changes (`git commit -am "Added Snarkdown"`)
4. Push to the branch (`git push origin my_markup`)
5. Create an [Issue][1] with a link to your branch
6. Enjoy a refreshing Diet Coke and wait


[r2h]: http://github.com/github/markup/tree/master/lib/github/commands/rest2html
[r2hc]: http://github.com/github/markup/tree/master/lib/github/markups.rb#L13
[1]: http://github.com/github/markup/issues

