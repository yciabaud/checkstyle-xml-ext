header {
package com.puppycrawl.tools.checkstyle.grammars;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import java.text.MessageFormat;
import antlr.CommonToken;
import org.xml.sax.helpers.*;
}

/** Xml Recognizer
 */
class GeneratedXmlRecognizer extends Parser;

options {
	k = 2;                           // two token lookahead
	exportVocab=GeneratedXml;       // Call its vocabulary "GeneratedJava"
	codeGenMakeSwitchThreshold = 2;  // Some optimizations
	codeGenBitsetTestThreshold = 3;
	defaultErrorHandler = false;     // Don't generate parser error handlers
	buildAST = true;
}

tokens{
    GENERIC_END;
    ELEMENT;
    ATTRIBUTE;
}

{
    /**
     * Counts the number of LT seen in the typeArguments production.
     * It is used in semantic predicates to ensure we have seen
     * enough closing '>' characters; which actually may have been
     * either GT, SR or BSR tokens.
     */
    private int ltCounter = 0;

    /**
     * Counts the number of '>' characters that have been seen but
     * have not yet been associated with the end of a typeParameters or
     * typeArguments production. This is necessary because SR and BSR
     * tokens have significance (the extra '>' characters) not only for the production
     * that sees them but also productions higher in the stack (possibly right up to an outer-most
     * typeParameters production). As the stack of the typeArguments/typeParameters productions unwind,
     * any '>' characters seen prematurely through SRs or BSRs are reconciled.
     */
    private int gtToReconcile = 0;

    /**
     * The most recently seen gt sequence (GT, SR or BSR)
     * encountered in any type argument or type parameter production.
     * We retain this so we can keep manage the synthetic GT tokens/
     * AST nodes we emit to have '<' & '>' balanced trees when encountering
     * SR and BSR tokens.
     */
    private DetailAST currentGtSequence = null;

    /**
     * Consume a sequence of '>' characters (GT, SR or BSR)
     * and match these against the '<' characters seen.
     */
    private void consumeCurrentGtSequence(DetailAST gtSequence)
    {
        currentGtSequence = gtSequence;
        gtToReconcile += currentGtSequence.getText().length();
        ltCounter -= currentGtSequence.getText().length();
    }

    /**
     * Emits a single GT AST node with the line and column correctly
     * set to its position in the source file. This must only
     * ever be called when a typeParameters or typeArguments production
     * is ending and there is at least one GT character to be emitted.
     *
     * @see #areThereGtsToEmit
     */
    private DetailAST emitSingleGt()
    {
        gtToReconcile -= 1;
        CommonToken gtToken = new CommonToken(GENERIC_END, ">");
        gtToken.setLine(currentGtSequence.getLineNo());
        gtToken.setColumn(currentGtSequence.getColumnNo() + (currentGtSequence.getText().length() - gtToReconcile));
        return (DetailAST)astFactory.create(gtToken);
    }

    /**
     * @return true if there is at least one '>' seen but
     * not reconciled with the end of a typeParameters or
     * typeArguments production; returns false otherwise
     */
    private boolean areThereGtsToEmit()
    {
        return (gtToReconcile > 0);
    }

    /**
     * @return true if there is exactly one '>' seen but
     * not reconciled with the end of a typeParameters
     * production; returns false otherwise
     */
    private boolean isThereASingleGtToEmit()
    {
        return (gtToReconcile == 1);
    }

    /**
     * @return true if the '<' and '>' are evenly matched
     * at the current typeParameters/typeArguments nested depth
     */
    private boolean areLtsAndGtsBalanced(int currentLtLevel)
    {
        return ((currentLtLevel != 0) || ltCounter == currentLtLevel);
    }
}

// XML file
document
	:   (PCDATA)? (DOCTYPE (PCDATA)?)?
            (PI)*
            element
            (PCDATA)?
            EOF!
	;

element
    : ( t:startTag {#t.setType(ELEMENT);} 
        (TAG_EMPTY_CLOSE
        |(TAG_CLOSE 
            (element
            | PCDATA
            | WS
            )*
            endTag
        ))
        (WS)*
      )
    ;

startTag  : TAG_START_OPEN^ GENERIC_ID (WS)* (attribute)* (WS)* ;

attribute  : a:GENERIC_ID^ {#a.setType(ATTRIBUTE);} ATTR_EQ ATTR_VALUE! (WS)* ;

endTag :  TAG_END_OPEN GENERIC_ID TAG_CLOSE! ;


//----------------------------------------------------------------------------
// The Xml scanner
//----------------------------------------------------------------------------
class GeneratedXmlLexer extends Lexer;
options {
    // needed to tell "<!DOCTYPE..."
    // from "<?..." and "<tag..." and "</tag...>" and "<![CDATA...>"
    // also on exit branch "]]>", "-->"
    k=3;
    charVocabulary = '\3'..'\377'; // extended ASCII (3-255 in octal notation)
    //charVocabulary='\u0000'..'\uFFFE';
    caseSensitive=true;
    exportVocab=GeneratedXml;  // call the vocabulary "Xml"
    testLiterals=false;        // don't automatically test for literals

    // without inlining some bitset tests, couldn't do unicode;
    // I need to make ANTLR generate smaller bitsets; see
    // bottom of JavaLexer.java
    codeGenBitsetTestThreshold=20;
}

// XmlLexer verbatim source code
{

    private CommentListener mCommentListener = null;

    // TODO: Check visibility of this method one parsing is done in central
    // utility method
    public void setCommentListener(CommentListener aCommentListener)
    {
        mCommentListener = aCommentListener;
    }

    private boolean mTreatAssertAsKeyword = true;

    public void setTreatAssertAsKeyword(boolean aTreatAsKeyword)
    {
        mTreatAssertAsKeyword = aTreatAsKeyword;
    }

    private boolean mTreatEnumAsKeyword = true;

    public void setTreatEnumAsKeyword(boolean aTreatAsKeyword)
    {
        mTreatEnumAsKeyword = aTreatAsKeyword;
    }

    private boolean tagMode;

}

TAG_PROLOG_END : ({tagMode}? ("?>")) ;

TAG_CDDATA_OPEN : "<![CDATA[" ;
TAG_CDDATA_CLOSE : ("]]>") ;

TAG_START_OPEN : '<' ;
TAG_END_OPEN : "</" ;
TAG_CLOSE : ('>') ;
TAG_EMPTY_CLOSE : ("/>") ;

ATTR_EQ : ('=') ;

ATTR_VALUE :
        ( '"' (~'"')* '"'
        | '\'' (~'\'')* '\''
        )
    ;

GENERIC_ID : ( LETTER | '_' | ':') (NAMECHAR)* ;

protected STRING_NO_QUOTE
	:	'"'! (~'"')* '"'!
	|	'\''! (~'\'')* '\''!
	;

protected STRING
	:	'"' (~'"')* '"'
	|	'\'' (~'\'')* '\''
	;

protected NAMECHAR
	: LETTER | DIGIT | '.' | '-' | '_' | ':'
	;

protected DIGIT
	:	'0'..'9'
	;

protected LETTER
	: 'a'..'z' 
	| 'A'..'Z'
	;

WS!  :  (' '|'\r'|'\t'|'\u000C'|'\n')
    ;

ESC!
	: ( '\t'
	 	|	NL
		)
	;

// taken from html.g
// Alexander Hinds & Terence Parr
// from antlr 2.5.0: example/html 
//
// '\r' '\n' can be matched in one alternative or by matching
// '\r' in one iteration and '\n' in another.  I am trying to
// handle any flavor of newline that comes in, but the language
// that allows both "\r\n" and "\r" and "\n" to all be valid
// newline is ambiguous.  Consequently, the resulting grammar
// must be ambiguous.  I'm shutting this warning off.
NL!
    : (	options {
	generateAmbigWarnings=false;
	greedy = true;
    }
		: '\n'
		|	"\r\n"
		|	'\r'
		)
		{ newline(); }
	;

DOCTYPE!
    :
        "<!DOCTYPE" WS GENERIC_ID 
        WS
        ( 
            ( "SYSTEM" WS sys1:STRING  
                
            | "PUBLIC" WS pub:STRING WS sys2:STRING 
            )
            ( WS )?
        )?
        ( dtd:INTERNAL_DTD ( WS )? 

        )?
		'>'
	;

protected INTERNAL_DTD
    :
        '['!
        // reports warning, but is absolutely ok (checked generated code)
        // besides this warning was not generated with k=1 which is 
        // enough for this rule...
        ( options {greedy=false;} : NL
        | STRING // handle string specially to avoid to mistake ']' in string for end dtd
        | .
        )*
        ']'!
    ;

PI! 
    :
        "<?" 
        GENERIC_ID
        { tagMode = true; }
	;

// multiple-line comments
protected
COMMENT_DATA
	:	(	/*	'\r' '\n' can be matched in one alternative or by matching
				'\r' in one iteration and '\n' in another.  I am trying to
				handle any flavor of newline that comes in, but the language
				that allows both "\r\n" and "\r" and "\n" to all be valid
				newline is ambiguous.  Consequently, the resulting grammar
				must be ambiguous.  I'm shutting this warning off.
			 */
			options {
				generateAmbigWarnings=false;
			}
		:
			{!(LA(2)=='-' && LA(3)=='>')}? '-' // allow '-' if not "-->"
		|	'\r' '\n'		{newline();}
		|	'\r'			{newline();}
		|	'\n'			{newline();}
		|	~('-'|'\n'|'\r')
		)*
	;


COMMENT!
	:	"<!--" c:COMMENT_DATA "-->" (WS)?
		{ $setType(Token.SKIP); }
	;

PCDATA	: 
        PCDATA_DATA
	;

protected PCDATA_DATA
	: 
        ( options {greedy=true;} : NL
        | ~( '<' | '\n' | '\r' )
        )+
    ;
