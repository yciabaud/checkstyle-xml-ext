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

import antlr.Token;

/**
 * XML token populating the AST tree.
 * 
 * @author Yoann Ciabaud<y.ciabaud@gmail.com>
 */
public class XmlToken extends Token {

    /** Column of the token in the document. */
    private int column;
    
    /** Line of the token in the document. */
    private int line;
    
    /** Filename of the document. */
    private String filename;
    
    /** Text of the node. */
    private String text;

    /** Default constructor. */
    public XmlToken() {
    }

    /**
     * Token constructor.
     * 
     * @param tokenType integer representing the type of the token
     * @param text text value of the node
     */
    public XmlToken(int tokenType, String text) {
        super(tokenType, text);
        this.text = text;
    }

    /** {@inheritDoc} */
    @Override
    public int getColumn() {
        return column;
    }

    /** {@inheritDoc} */
    @Override
    public String getFilename() {
        return filename;
    }

    /** {@inheritDoc} */
    @Override
    public int getLine() {
        return line;
    }

    /** {@inheritDoc} */
    @Override
    public String getText() {
        return text;
    }

    /** {@inheritDoc} */
    @Override
    public void setColumn(int i) {
        column = i;
    }

    /** {@inheritDoc} */
    @Override
    public void setFilename(String string) {
        filename = string;
    }

    /** {@inheritDoc} */
    @Override
    public void setLine(int i) {
        line = i;
    }

    /** {@inheritDoc} */
    @Override
    public void setText(String string) {
        text = string;
    }
    
}
