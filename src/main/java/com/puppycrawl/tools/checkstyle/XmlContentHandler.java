////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2011  Oliver Burn
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.XmlTokenTypes;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;

/**
 * XML parser used to build an AST tree of a document.
 * 
 * @author Yoann Ciabaud<y.ciabaud@gmail.com>
 */
public class XmlContentHandler implements ContentHandler {

    /** Stores the location of the parser in the file. */
    private Locator locator;
    
    /** Root of the AST tree. */
    private DetailAST root;
    
    /** Pointer on the last parsed node. */
    private DetailAST currentNode;
    
    /** Document beeing parsed. */
    private File file;
    
    /** logger for debug purpose. */
    private static final Log LOG =
        LogFactory.getLog("com.puppycrawl.tools.checkstyle.XmlContentHandler");

    /**
     * Default constructor. 
     */
    public XmlContentHandler(File file) {
        super();
        
        locator = new LocatorImpl();
        root = null;
        this.file = file;
    }

    /** {@inheritDoc} */
    @Override
    public void setDocumentLocator(Locator value) {
        locator = value;
    }

    /** {@inheritDoc} */
    @Override
    public void startDocument() throws SAXException {
        LOG.debug("Starting XML parsing");
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setType(XmlTokenTypes.DOCUMENT);
        token.setText(file.getName());
        
        
        // Node
        root = new DetailAST();
        root.initialize(token);
        
        currentNode = root;
        
        // Path = package
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setType(XmlTokenTypes.PATH);
        token.setText(file.getParent());
        DetailAST path = new DetailAST();
        path.initialize(token);
        root.addChild(path);
        DetailAST pathIdent = new DetailAST();
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setType(XmlTokenTypes.IDENT);
        token.setText(file.getParent());
        pathIdent.initialize(token);
        path.addChild(pathIdent);
        
        // Fake child to match Java grammar structure
        pathIdent = new DetailAST();
        pathIdent.initialize(token);
        path.addChild(pathIdent);
        
        // Name = Type
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setType(XmlTokenTypes.IDENT);
        token.setText(file.getName());
        DetailAST name = new DetailAST();
        name.initialize(token);
        root.addChild(name);
        DetailAST nameIdent = new DetailAST();
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setType(XmlTokenTypes.IDENT);
        token.setText(file.getName());
        nameIdent.initialize(token);
        name.addChild(nameIdent);
    }

    /** {@inheritDoc} */
    public void endDocument() throws SAXException {
        LOG.debug("Ended parsing");
    }

    /** {@inheritDoc} */
    public void startPrefixMapping(String prefix, String URI) throws SAXException {
        LOG.debug("Prefix mapping : " + URI + ", chosen prefix : " + prefix);
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(prefix);
        token.setType(XmlTokenTypes.PREFIX_MAPPING);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        
        // Prefix
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(prefix);
        token.setType(XmlTokenTypes.IDENT);

        DetailAST prefixAST = new DetailAST();
        prefixAST.initialize(token);
        prefixAST.setText(token.getText());
        
        child.addChild(prefixAST);
        
        // URI
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(URI);
        token.setType(XmlTokenTypes.STRING_LITERAL);

        DetailAST uri = new DetailAST();
        uri.initialize(token);
        
        child.addChild(uri);
        
        // Parent
        currentNode.addChild(child);
        
    }

    /** {@inheritDoc} */
    public void endPrefixMapping(String prefix) throws SAXException {
        LOG.debug("End of prefix mapping : " + prefix);
    }

    /** {@inheritDoc} */
    public void startElement(String nameSpaceURI, String localName,
            String rawName, Attributes attributs) throws SAXException {
        LOG.debug("Opening element : " + localName);
        
        int col = locator.getColumnNumber();
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(rawName);
        token.setType(XmlTokenTypes.ELEMENT);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        
        
        // Parent
        currentNode.addChild(child);
        
        // Ident
        DetailAST ident = new DetailAST();
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        col++;
        token.setColumn(col);
        token.setText(localName);
        token.setType(XmlTokenTypes.IDENT);
        ident.initialize(token);
        child.addChild(ident);

        // Attributes
        LOG.debug("  Attributes : ");
        DetailAST attrs = new DetailAST();
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        col += rawName.length() + 1;
        token.setColumn( col );
        token.setText(rawName);
        token.setType(XmlTokenTypes.ATTRIBUTES);
        attrs.initialize(token);
        child.addChild(attrs);

        for (int index = 0; index < attributs.getLength(); index++) {
            
            // Attribut
            DetailAST attr = new DetailAST();
            token = new XmlToken();
            token.setLine(locator.getLineNumber());
            token.setColumn( index );
            token.setText(rawName);
            token.setType(XmlTokenTypes.ATTRIBUTE);
            attr.initialize(token);
            attrs.addChild(attr);
            LOG.debug("     - " + attributs.getLocalName(index) + " = "
                    + attributs.getValue(index));
            
            // Ident
            ident = new DetailAST();
            token = new XmlToken();
            token.setLine(locator.getLineNumber());
            token.setColumn( index );
            token.setText(attributs.getLocalName(index));
            token.setType(XmlTokenTypes.IDENT);
            ident.initialize(token);
            attr.addChild(ident);
            
            // Value
            DetailAST value  = new DetailAST();
            token = new XmlToken();
            token.setLine(locator.getLineNumber());
            token.setColumn( index + attributs.getLocalName(index).length() + 1 );
            token.setText(attributs.getValue(index));
            token.setType(XmlTokenTypes.STRING_LITERAL);
            value.initialize(token);
            attr.addChild(value);
            
            // Go forward
            col += attributs.getLocalName(index).length()
                    + attributs.getValue(index).length() + 2;
        }
        
        // This node is now he current node
        currentNode = child;
    }

    /** {@inheritDoc} */
    public void endElement(String nameSpaceURI, String localName,
            String rawName) throws SAXException {
        LOG.debug("End of the element : " + localName);
        
        // Go to the parent
        currentNode = currentNode.getParent();
        
    }

    /** {@inheritDoc} */
    public void characters(char[] ch, int start, int end) throws SAXException {
        
        String value = new String(ch, start, end);
        
        LOG.debug("#PCDATA : " + value);
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber() + start);
        token.setText(value);
        token.setType(XmlTokenTypes.PCDATA);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        child.setText(token.getText());
        
        // Parent
        currentNode.addChild(child);
        
    }

    /** {@inheritDoc} */
    public void ignorableWhitespace(char[] ch, int start, int end) throws SAXException {
        
        String value = new String(ch, start, end);
        
        LOG.debug("ignorable whitespace : ..." + value + "...");
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber() + start);
        token.setText(value);
        token.setType(XmlTokenTypes.WHITE_SPACE);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        child.setText(token.getText());
        
        // Parent
        currentNode.addChild(child);
    }

    /** {@inheritDoc} */
    public void processingInstruction(String target, String data) throws SAXException {
        LOG.debug("Processing instruction : " + target);
        LOG.debug("  Args : " + data);
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(target);
        token.setType(XmlTokenTypes.PROCESSING_INSTRUCTION);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        child.setText(token.getText());
                
        // Target
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(target);
        token.setType(XmlTokenTypes.PROCESSING_TARGET);
        
        DetailAST targetAST = new DetailAST();
        targetAST.initialize(token);
        targetAST.setText(token.getText());
        child.addChild(targetAST);
        
        // Data
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(data);
        token.setType(XmlTokenTypes.PROCESSING_DATA);
        
        DetailAST dataAST = new DetailAST();
        dataAST.initialize(token);
        dataAST.setText(token.getText());
        child.addChild(dataAST);
        
        
        // Parent
        currentNode.addChild(child);
    }

    /** {@inheritDoc} */
    public void skippedEntity(String text) throws SAXException {      
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber() );
        token.setText(text);
        token.setType(XmlTokenTypes.SKIPPED_ENTITY);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        child.setText(token.getText());
         
        // Parent
        currentNode.addChild(child);
    }

    /** {@inheritDoc} */
    public DetailAST getAST() {
        return root;
    }
}