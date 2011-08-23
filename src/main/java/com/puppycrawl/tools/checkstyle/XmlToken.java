/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puppycrawl.tools.checkstyle;

import antlr.Token;

/**
 *
 * @author YCIABAUD
 */
public class XmlToken extends Token {

    private int column;
    
    private int line;
    
    private String filename;
    
    private String text;

    public XmlToken() {
    }

    public XmlToken(int i, String string) {
        super(i, string);
        text = string;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setColumn(int i) {
        column = i;
    }

    @Override
    public void setFilename(String string) {
        filename = string;
    }

    @Override
    public void setLine(int i) {
        line = i;
    }

    @Override
    public void setText(String string) {
        text = string;
    }
    
}
