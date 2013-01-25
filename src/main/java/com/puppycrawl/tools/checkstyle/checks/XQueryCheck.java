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

package com.puppycrawl.tools.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.XmlTokenTypes;
import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.sax.SAXSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;
import org.xml.sax.InputSource;

/**
 * <p>
 * Checks for the number of nodes matched by a XQuery expression .
 * </p>
 * <p>
 * Business: Implement logical rules to match your needs.
 * </p>
 * <p>
 * An example of how to configure the check so that it accepts files with at
 * no results for the following XQuery :
 * </p>
 * <pre>
 * &lt;module name="XQueryCheck"&gt;
 *    &lt;property name="expression" value="
 *              for $x      in //ELT, 
 *                  $name   in $x/NAP_NAME/@value, 
 *                  $server in $x/NET_OND_OUTGOING_NAP_DETAILS/OND_SERVER_ADDRESS/@value, 
 *                  $port   in $x/NET_OND_OUTGOING_NAP_DETAILS/OND_CONNECTION_PORT/@value 
 *              where $server != concat('%NAP_', $name, '_IP_ADDR%')
 *              and   $port   != concat('%NAP_', $name, '_PORT%')
 *              return $x
 *          "/&gt;
 *    &lt;property name="min" value="0"/&gt;
 *    &lt;property name="max" value="0"/&gt;
 * &lt;/module&gt;
 * </pre>
 * @author Yoann Ciabaud<y.ciabaud@gmail.com>
 */
public class XQueryCheck extends Check {

    /** Default value for min and max. */
    private static final int DEFAULT_VALUE = 0;
    
    /** Maximum number occurencies of the expression. */
    private int max = DEFAULT_VALUE;
    
    /** Minimum number occurencies of the expression. */
    private int min = DEFAULT_VALUE;
    
    /** String value of XQuery expression*/
    private String expression;
    
    /** XQuery expression. */
    private XQueryExpression xQueryExpression;
    
    private String[] namespaces;
    
    /** Static query context */
    private StaticQueryContext sqc;
    
    /** Dynamic query context */
    private DynamicQueryContext env;

    /** {@inheritDoc} */
    @Override
    public int[] getDefaultTokens() {
        return new int[]{XmlTokenTypes.DOCUMENT};
    }

    /** {@inheritDoc} */
    @Override
    public void init() {
        super.init();

        Configuration c = new Configuration();
        c.setLineNumbering(true);
        
        try {
            sqc = new StaticQueryContext(c);
            if( namespaces != null )
            {
            	for( int index = 0; index < namespaces.length; index+=2)
            	{
            		sqc.declareNamespace(namespaces[index], namespaces[index+1]);
            	}
            }
            
            env = new DynamicQueryContext(c);
            xQueryExpression = sqc.compileQuery(expression);
        } catch (XPathException ex) {
            log(0, "Invalid XQuery request: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

    }

    /** {@inheritDoc} */
    @Override
    public void visitToken(DetailAST aAST) {

        if (getFileContents() == null) {
            log(aAST.getLineNo(),
                    "no XML document available in this context");

            return;
        }

        if (xQueryExpression == null) {
            log(aAST.getLineNo(),
                    "no XQuery expression available in this context");

            return;
        }

        List resultat = null;
        
        try{
            final String fullText = getFileContents().getText().getFullText().toString();
            InputSource document = new InputSource();
            document.setCharacterStream(new StringReader(fullText));

            DocumentInfo di = sqc.buildDocument(new SAXSource(document));
            env.setContextItem(di.getRoot());

            resultat = xQueryExpression.evaluate(env);   
        } catch (XPathException ex) {
            log(0, "Invalid XQuery expression: " + expression);
            ex.printStackTrace();
        }


        int nbMatches = resultat != null ? resultat.size() : 0;
        if (nbMatches < min || nbMatches > max) {
             for(int i=max; i<resultat.size(); i++){
                int line = aAST.getLineNo();
                int col = aAST.getColumnNo();

                if(resultat.get(i) instanceof NodeInfo){
                    line = ((NodeInfo)resultat.get(i)).getLineNumber();
                    col = ((NodeInfo)resultat.get(i)).getColumnNumber() - 1;
                }
                log(line,
                    col,
                    "xquery.invalidPath",
                    aAST.getText(),
                    expression, nbMatches, min, max);
            }
        }

    }

    /**
     * Setter of max.
     * @param max the max value
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Setter of min.
     * @param min the min value
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Setter of expression.
     * @param expression the XQuery expression value
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    /**
     * Setter of namespaces.
     * For instance: 
     * <pre>
     *   &lt;property name="namespaces" value="bk, urn:xmlns:25hoursaday-com:bookstore, inv, urn:xmlns:25hoursaday-com:inventory-tracking"/&gt;
     * </pre>
     * 
     * @param namespaces an array containing a prefix + namespace pair
     */
    public void setNamespaces(String[] namespaces) {
		this.namespaces = namespaces;
	}
}
