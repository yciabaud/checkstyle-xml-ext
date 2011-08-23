package com.puppycrawl.tools.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.XmlTokenTypes;
import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import java.io.StringReader;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.NamespaceConstant;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.tinytree.TinyTree;
import net.sf.saxon.xpath.XPathFactoryImpl;
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
        
        try {
            //évaluation de l'expression XPath
            xPathExpression = xpath.compile(expression);
        } catch (XPathExpressionException ex) {
            log(0, "Invalid XPath expression: " + expression);
            ex.printStackTrace();
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
        if( nbMatches < min || nbMatches > max){
           
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