/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puppycrawl.tools.checkstyle.gui;

import com.puppycrawl.tools.checkstyle.XmlTokenTypes;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

/**
 *
 * @author YCIABAUD
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
