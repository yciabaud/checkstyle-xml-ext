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

package com.puppycrawl.tools.checkstyle.gui;

import com.puppycrawl.tools.checkstyle.api.XmlTokenTypes;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 * The model that backs the parse tree in the GUI
 * whit the XmlTokenTypes.
 * 
 * @author Yoann Ciabaud<y.ciabaud@gmail.com>
 * @see ParseTreeModel
 */
public class XmlParseTreeModel extends ParseTreeModel {

    public XmlParseTreeModel(DetailAST parseTree) {
        super(parseTree);
    }

    @Override
    public Object getValueAt(Object node, int column) {
        final DetailAST ast = (DetailAST) node;
        switch (column) {
            case 0:
                return null;
            case 1:
                return XmlTokenTypes.getTokenName(ast.getType());
            case 2:
                return ast.getLineNo();
            case 3:
                return ast.getColumnNo();
            case 4:
                return ast.getText();
        }
        return null;
    }
}
