/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puppycrawl.tools.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.XmlTokenTypes;
import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import java.io.StringReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author YCIABAUD
 */
public class XPathCheck extends Check{

    private static final int DEFAULT_MAX = 100;
    private int max = DEFAULT_MAX;
    
    private int min = 0;
    
    private String expression;
    
    private XPathExpression xPathExpression;
    
    @Override
    public int[] getDefaultTokens() {
        return new int[]{XmlTokenTypes.DOCUMENT};
    }

    @Override
    public void init() {
        super.init();
        
        //création du XPath 
        XPathFactory fabrique = XPathFactory.newInstance();
        XPath xpath = fabrique.newXPath();
        try {
            //évaluation de l'expression XPath
            xPathExpression = xpath.compile(expression);
        } catch (XPathExpressionException ex) {
            log(0, ex.getMessage());
        }
        
    }

    
    
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
       
        NodeList resultat = null;
        try {
            final String fullText = getFileContents().getText().getFullText().toString();
            InputSource document = new InputSource();
            document.setCharacterStream(new StringReader(fullText));
            resultat = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            log(aAST.getLineNo(),
                "XPath evaluation failed: " + ex.getMessage());
            
            return;
        }
        
        int nbMatches = resultat != null ? resultat.getLength() : 0;
        if( nbMatches < min || nbMatches > max){
            log(aAST.getLineNo(),
                "incorrect number of matching " + expression + " : " + nbMatches);
        }
        
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
