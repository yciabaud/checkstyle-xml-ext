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

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import net.sf.saxon.om.NamespaceConstant;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.xpath.XPathFactoryImpl;

import org.xml.sax.InputSource;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.XmlTokenTypes;

/**
 * <p>
 * Checks for the number of nodes matched by a XPath expression .
 * </p>
 * <p>
 * Business: Implement logical rules to match your needs.
 * </p>
 * <p>
 * An example of how to configure the check so that it accepts file with less
 * than 1500 child for a node:
 * </p>
 * <pre>
 * &lt;module name="XPathCheck"&gt;
 *    &lt;property name="expression" value="//*[count(*)>=100]"/&gt;
 *    &lt;property name="min" value="0"/&gt;
 *    &lt;property name="max" value="1500"/&gt;
 * &lt;/module&gt;
 * </pre>
 * @author Yoann Ciabaud<y.ciabaud@gmail.com>
 */
public class XPathCheck extends Check{

    /** Default value for min and max. */
    private static final int DEFAULT_VALUE = 0;
    
    /** Maximum number occurencies of the expression. */
    private int max = DEFAULT_VALUE;
    
    /** Minimum number occurencies of the expression. */
    private int min = DEFAULT_VALUE;
    
    /** String value of XPath expression*/
    private String expression;
    
    /** XPath expression*/
    private XPathExpression xPathExpression;
    
    /** */
    private String[] namespaces;
        
    /** {@inheritDoc} */
    @Override
    public int[] getDefaultTokens() {
        return new int[]{XmlTokenTypes.DOCUMENT};
    }

    /** {@inheritDoc} */
    @Override
    public void init() {
        super.init();
                
        //création du XPath 
        XPathFactory fabrique = null;
        try {
            System.setProperty("javax.xml.xpath.XPathFactory:"+NamespaceConstant.OBJECT_MODEL_SAXON, "net.sf.saxon.xpath.XPathFactoryImpl");
            fabrique = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
            ((XPathFactoryImpl)fabrique).getConfiguration().setLineNumbering(true);
        } catch (XPathFactoryConfigurationException ex) {
            log(0, "XPath engine error" + ex.getMessage());
            ex.printStackTrace();
            return;
        }
        XPath xpath = fabrique.newXPath();
        
        if( namespaces != null )
        {
        	final Map<String, String> ns = new HashMap<String, String>();
        	
        	for( int index = 0; index < namespaces.length; index+=2)
        	{
        		ns.put(namespaces[index], namespaces[index+1]);
        	}
        	
        	xpath.setNamespaceContext(new NamespaceContext() {
        		
        		private Map<String, String> namespaceMap = ns;
				
				@Override
				public Iterator getPrefixes(String arg0) {
					return null; //not used
				}
				
				@Override
				public String getPrefix(String arg0) {
					return null; //not used
				}
				
				@Override
				public String getNamespaceURI(String prefix) {
					return namespaceMap.get(prefix);
				}
			});
        	
        }
        
        try {
            //évaluation de l'expression XPath
            xPathExpression = xpath.compile(expression);
        } catch (XPathExpressionException ex) {
            log(0, "Invalid XPath expression: " + expression);
            ex.printStackTrace();
        }
        
    }

    /** {@inheritDoc} */
    @Override
    public void visitToken(DetailAST aAST) {

        if(getFileContents() == null){
            log(aAST.getLineNo(),
                "no XML document available in this context");
            
            return;
        }
        
        if(xPathExpression == null){
            log(aAST.getLineNo(),
                "no XPath expression available in this context");
            
            return;
        }
       
        List resultat = null;
        try {
            final String fullText = getFileContents().getText().getFullText().toString();
            InputSource document = new InputSource();
            document.setCharacterStream(new StringReader(fullText));
            resultat = (List) xPathExpression.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            log(aAST.getLineNo(),
                "XPath evaluation failed: " + ex.getMessage());
            ex.printStackTrace();
            
            return;
        }
        
        int nbMatches = resultat != null ? resultat.size() : 0;
        if( nbMatches < min ) {
        	log(0, "xpath.lessMatches", expression, nbMatches, min);
        }
        else if ( nbMatches > max)
        {
            for(int i=max; i<resultat.size(); i++){
                int line = aAST.getLineNo();
                int col = aAST.getColumnNo();

                if(resultat.get(i) instanceof NodeInfo){
                    line = ((NodeInfo)resultat.get(i)).getLineNumber();
                    col = ((NodeInfo)resultat.get(i)).getColumnNumber() - 1;
                }
                log(line,
                    col,
                    "xpath.invalidPath",
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
     * @param expression the XPath expression value
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
