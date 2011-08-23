package com.puppycrawl.tools.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.XmlTokenTypes;
import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import java.io.StringReader;
import java.util.List;
import javax.xml.transform.sax.SAXSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.tinytree.TinyTree;
import net.sf.saxon.trans.XPathException;
import org.xml.sax.InputSource;

/**
 *
 * @author YCIABAUD
 */
public class XQueryCheck extends Check {

    private static final int DEFAULT_MAX = 100;
    private int max = DEFAULT_MAX;
    private int min = 0;
    private String expression;
    private XQueryExpression xQueryExpression;
    private StaticQueryContext sqc;
    private DynamicQueryContext env;

    @Override
    public int[] getDefaultTokens() {
        return new int[]{XmlTokenTypes.DOCUMENT};
    }

    @Override
    public void init() {
        super.init();

        Configuration c = new Configuration();
        c.setLineNumbering(true);
        
        try {
            sqc = new StaticQueryContext(c);
            env = new DynamicQueryContext(c);
            xQueryExpression = sqc.compileQuery(expression);
        } catch (XPathException ex) {
            log(0, "Invalid XQuery request: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

    }

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
