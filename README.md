XML extension for Checkstyle
============================

This extension provides the ability for checkstyle to check on XML files.
Build the jar, add it to the classpath and run checkstyle over XML!

Mapping
-------

Types are translated from Java to reuse available checks. Here is the current mapping:

* DOCUMENT corresponds to Java CLASS_DEF
* ELEMENT corresponds to Java METHOD_DEF
* ATTRIBUTES corresponds to Java PARAMETERS
* ATTRIBUTE corresponds to Java PARAMETER_DEF
* IDENT corresponds to Java IDENT
* STRING_LITERAL corresponds to Java STRING_LITERAL
* PCDATA corresponds to Java VARIABLE_DEF
* PROCESSING_INSTRUCTION corresponds to Java PACKAGE_DEF
* PROCESSING_TARGET corresponds to Java ANNOTATION
* PROCESSING_DATA corresponds to Java ANNOTATION_DEF
* SKIPPED_ENTITY corresponds to Java SL_COMMENT
* WHITE_SPACE corresponds to Java WILDCARD_TYPE
* PREFIX_MAPPING corresponds to Java IMPORT

Checks
------

### Checkstyle

The following existing checks are supported:

* [TypeName](http://checkstyle.sourceforge.net/config_naming.html#TypeName) -- Checks the XML file name format
* [PackageName](http://checkstyle.sourceforge.net/config_naming.html#PackageName) -- Checks the path to the file format
* [MethodName](http://checkstyle.sourceforge.net/config_naming.html#MethodName) -- Checks an element name format
* [ParameterName](http://checkstyle.sourceforge.net/config_naming.html#ParameterName) -- Checks an attribute name format
* [ParameterNumber](http://checkstyle.sourceforge.net/config_sizes.html#ParameterNumber) -- Checks the number of attributes
* [MethodLength](http://checkstyle.sourceforge.net/config_sizes.html#MethodLength) -- Checks the length of an element body
* [MethodCount](http://checkstyle.sourceforge.net/config_sizes.html#MethodCount) -- Checks the number of child in an element
* [FileLength](http://checkstyle.sourceforge.net/config_sizes.html#FileLength)
* [LineLength](http://checkstyle.sourceforge.net/config_sizes.html#LineLength)

### XML extension specific

* XPathCheck -- Checks the number of occurencies of a XPath expression
* XQueryCheck -- Checks the number of occurencies of a XQuery expression

Contributing
------------

Want to contribute? Great! There are two ways to add checks.


### Checkstyle's guide

[The original guide](http://checkstyle.sourceforge.net/writingchecks.html) remains
available to the XML extension. Just keep in mind that the AST tree for XML is not
the same as the Java one.

You can use the a GUI that displays the structure of a Java source file. To run it type

    java -classpath "checkstyle-5.4-all.jar;" \
        com.puppycrawl.tools.checkstyle.gui.XmlMain
      

on the command line. Click the button at the bottom of the frame and select a syntactically correct XML file. 
The frame will be populated with a tree that corresponds to the structure of the XML document. 


### XML

You can extends existing Checks such as XPathCheck or XQueryChek.
More to come!


Installation
------------

Build the extension jar with maven :

    mvn install

Put this jar and the dependencies in the checkstyle classpath and use checkstyle as usual.

Usage
-----

The configuration XML file of checkstyle have to specify the XmlTreeWalker to activate the XML
extension:

    <?xml version="1.0"?>
    <!DOCTYPE module PUBLIC
        "-//Puppy Crawl//DTD Check Configuration 1.3//EN"
        "http://www.puppycrawl.com/dtds/configuration_1_3.dtd">
    <module name="Checker">
      <module name="XmlTreeWalker">
        <module name="XPathCheck">
          <property name="expression" value="//patient"/>
          <property name="min" value="1"/>
          <property name="max" value="5"/>
        </module>
      </module>
    </module>


Testing
-------

To run the tests:

    mvn test

Contributing
------------

1. Fork it.
2. Create a branch (`git checkout -b my-xml-ext`)
3. Commit your changes (`git commit -am "Added new XML based Ext"`)
4. Push to the branch (`git push origin my-xml-ext`)
5. Create an [Issue][1] with a link to your branch
6. Enjoy a refreshing beer and wait

[1]: https://github.com/yciabaud/checkstyle-xml-ext/issues

