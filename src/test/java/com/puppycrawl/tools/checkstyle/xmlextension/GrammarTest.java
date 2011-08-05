package com.puppycrawl.tools.checkstyle.xmlextension;

import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStreamException;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.grammars.GeneratedXmlLexer;
import com.puppycrawl.tools.checkstyle.grammars.GeneratedXmlRecognizer;
import org.junit.Test;

public class GrammarTest {

    @Test
    public void lexerTest() throws TokenStreamException {

        GeneratedXmlLexer lexer = new GeneratedXmlLexer(GrammarTest.class.getResourceAsStream("/simple.xml"));
        Token token;
        while ((token = lexer.nextToken()).getType()!=Token.EOF_TYPE) 
            System.out.println("Token: "+token.getText());

    }
    
    @Test
    public void parserTest() throws TokenStreamException, RecognitionException {

        GeneratedXmlLexer lexer = new GeneratedXmlLexer(GrammarTest.class.getResourceAsStream("/simple.xml"));
        lexer.setTreatAssertAsKeyword(true);
        lexer.setTreatEnumAsKeyword(true);

        final GeneratedXmlRecognizer parser =
            new GeneratedXmlRecognizer(lexer);
        parser.setASTNodeClass(DetailAST.class.getName());
        parser.document();
        parser.getAST();

    }
}
