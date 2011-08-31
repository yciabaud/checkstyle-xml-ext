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
package com.puppycrawl.tools.checkstyle.api;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.grammars.GeneratedJavaTokenTypes;
import java.lang.reflect.Field;
import java.util.ResourceBundle;

/**
 * Contains the constants for all the tokens contained in the Abstract
 * Syntax Tree.
 * 
 * This is a mapping between XML tokens and classical checkstyle token types.
 * 
 * @author Yoann Ciabaud<y.ciabaud@gmail.com>
 * @see TokenTypes
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
    
    ////////////////////////////////////////////////////////////////////////
    // The interesting code goes here
    ////////////////////////////////////////////////////////////////////////

    /** Maps from a token name to value */
    private static final ImmutableMap<String, Integer> TOKEN_NAME_TO_VALUE;
    /** Maps from a token value to name */
    private static final String[] TOKEN_VALUE_TO_NAME;

    // Initialise the constants
    static {
        final ImmutableMap.Builder<String, Integer> builder =
            ImmutableMap.builder();
        final Field[] fields = XmlTokenTypes.class.getDeclaredFields();
        String[] tempTokenValueToName = new String[0];
        for (final Field f : fields) {
            // Only process the int declarations.
            if (f.getType() != Integer.TYPE) {
                continue;
            }

            final String name = f.getName();
            try {
                final int tokenValue = f.getInt(name);
                builder.put(name, tokenValue);
                if (tokenValue > tempTokenValueToName.length - 1) {
                    final String[] temp = new String[tokenValue + 1];
                    System.arraycopy(tempTokenValueToName, 0,
                                     temp, 0, tempTokenValueToName.length);
                    tempTokenValueToName = temp;
                }
                tempTokenValueToName[tokenValue] = name;
            }
            catch (final IllegalArgumentException e) {
                e.printStackTrace();
                System.exit(1);
            }
            catch (final IllegalAccessException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        TOKEN_NAME_TO_VALUE = builder.build();
        TOKEN_VALUE_TO_NAME = tempTokenValueToName;
    }

    /**
     * Returns the name of a token for a given ID.
     * @param aID the ID of the token name to get
     * @return a token name
     */
    public static String getTokenName(int aID)
    {
        if (aID > TOKEN_VALUE_TO_NAME.length - 1) {
            throw new IllegalArgumentException("given id " + aID);
        }
        final String name = TOKEN_VALUE_TO_NAME[aID];
        if (name == null) {
            throw new IllegalArgumentException("given id " + aID);
        }
        return name;
    }

    /**
     * Returns the ID of a token for a given name.
     * @param aName the name of the token ID to get
     * @return a token ID
     */
    public static int getTokenId(String aName)
    {
        final Integer id = TOKEN_NAME_TO_VALUE.get(aName);
        if (id == null) {
            throw new IllegalArgumentException("given name " + aName);
        }
        return id.intValue();
    }

    /**
     * Returns the short description of a token for a given name.
     * @param aName the name of the token ID to get
     * @return a short description
     */
    public static String getShortDescription(String aName)
    {
        if (!TOKEN_NAME_TO_VALUE.containsKey(aName)) {
            throw new IllegalArgumentException("given name " + aName);
        }

        final String tokentypes =
            "com.puppycrawl.tools.checkstyle.api.tokentypes";
        final ResourceBundle bundle = ResourceBundle.getBundle(tokentypes);
        return bundle.getString(aName);
    }
}
