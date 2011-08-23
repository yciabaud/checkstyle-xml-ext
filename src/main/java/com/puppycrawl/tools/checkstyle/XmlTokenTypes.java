/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puppycrawl.tools.checkstyle;

import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.grammars.GeneratedJavaTokenTypes;

/**
 *
 * @author vista
 */
public final class XmlTokenTypes {
    
     ///CLOVER:OFF
    /** prevent instantiation */
    private XmlTokenTypes()
    {
    }
    ///CLOVER:ON

   
    /**
     * The end of file token.  This is the root node for the source
     * file.  It's children are an optional package definition, zero
     * or more import statements, and one or more class or interface
     * definitions.
     *
     * @see #PACKAGE_DEF
     * @see #IMPORT
     * @see #CLASS_DEF
     * @see #INTERFACE_DEF
     **/
    public static final int EOF = TokenTypes.EOF;
    
    public static final int DOCUMENT = TokenTypes.CLASS_DEF;
    public static final int PATH = TokenTypes.PACKAGE_DEF;
    public static final int ELEMENT = TokenTypes.METHOD_DEF;
    public static final int ATTRIBUTES = TokenTypes.PARAMETERS;
    public static final int ATTRIBUTE = TokenTypes.PARAMETER_DEF;
    public static final int IDENT = TokenTypes.IDENT;
    public static final int STRING_LITERAL = TokenTypes.STRING_LITERAL;
    public static final int PCDATA = TokenTypes.VARIABLE_DEF;
    public static final int PROCESSING_INSTRUCTION = TokenTypes.ANNOTATIONS;
    public static final int PROCESSING_TARGET = TokenTypes.ANNOTATION;
    public static final int PROCESSING_DATA = TokenTypes.ANNOTATION_DEF;
    public static final int SKIPPED_ENTITY = GeneratedJavaTokenTypes.SL_COMMENT;
    public static final int WHITE_SPACE = TokenTypes.WILDCARD_TYPE;
    public static final int PREFIX_MAPPING = TokenTypes.IMPORT;
    
}
