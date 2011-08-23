package com.puppycrawl.tools.checkstyle;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.LocatorImpl;

/**
 * @author smeric
 *
 * Exemple d'implementation extremement simplifiee d'un SAX XML ContentHandler. Le but de cet exemple
 * est purement pedagogique.
 * Very simple implementation sample for XML SAX ContentHandler.
 */
public class XmlContentHandler implements ContentHandler {

    private Locator locator;
    
    private DetailAST root;
    
    private DetailAST currentNode;
    
    private File file;
    
    /** logger for debug purpose */
    private static final Log LOG =
        LogFactory.getLog("com.puppycrawl.tools.checkstyle.XmlContentHandler");

    /**
     * Constructeur par defaut. 
     */
    public XmlContentHandler(File file) {
        super();
        // On definit le locator par defaut.
        locator = new LocatorImpl();
        root = null;
        this.file = file;
    }

    /**
     * Definition du locator qui permet a tout moment pendant l'analyse, de localiser
     * le traitement dans le flux. Le locator par defaut indique, par exemple, le numero
     * de ligne et le numero de caractere sur la ligne.
     * @author smeric
     * @param value le locator a utiliser.
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(Locator value) {
        locator = value;
    }

    /**
     * Evenement envoye au demarrage du parse du flux xml.
     * @throws SAXException en cas de probleme quelquonque ne permettant pas de
     * se lancer dans l'analyse du document.
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException {
        LOG.debug("Debut de l'analyse du document");
        
        // XmlXmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setType(XmlTokenTypes.DOCUMENT);
        token.setText(file.getName());
        
        
        // Node
        root = new DetailAST();
        root.initialize(token);
        
        currentNode = root;
        
        // Path = package
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setType(XmlTokenTypes.PATH);
        token.setText(file.getParent());
        DetailAST path = new DetailAST();
        path.initialize(token);
        root.addChild(path);
        DetailAST pathIdent = new DetailAST();
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setType(XmlTokenTypes.IDENT);
        token.setText(file.getParent());
        pathIdent.initialize(token);
        path.addChild(pathIdent);
        
        // Fake child to match Java grammar structure
        pathIdent = new DetailAST();
        pathIdent.initialize(token);
        path.addChild(pathIdent);
        
        // Name = Type
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setType(XmlTokenTypes.IDENT);
        token.setText(file.getName());
        DetailAST name = new DetailAST();
        name.initialize(token);
        root.addChild(name);
        DetailAST nameIdent = new DetailAST();
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setType(XmlTokenTypes.IDENT);
        token.setText(file.getName());
        nameIdent.initialize(token);
        name.addChild(nameIdent);
    }

    /**
     * Evenement envoye a la fin de l'analyse du flux xml.
     * @throws SAXException en cas de probleme quelquonque ne permettant pas de
     * considerer l'analyse du document comme etant complete.
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        LOG.debug("Fin de l'analyse du document");
    }

    /**
     * Debut de traitement dans un espace de nommage.
     * @param prefixe utilise pour cet espace de nommage dans cette partie de l'arborescence.
     * @param URI de l'espace de nommage.
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
     */
    public void startPrefixMapping(String prefix, String URI) throws SAXException {
        LOG.debug("Traitement de l'espace de nommage : " + URI + ", prefixe choisi : " + prefix);
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(prefix);
        token.setType(XmlTokenTypes.PREFIX_MAPPING);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        
        // Prefix
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(prefix);
        token.setType(XmlTokenTypes.IDENT);

        DetailAST prefixAST = new DetailAST();
        prefixAST.initialize(token);
        prefixAST.setText(token.getText());
        
        child.addChild(prefixAST);
        
        // URI
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(URI);
        token.setType(XmlTokenTypes.STRING_LITERAL);

        DetailAST uri = new DetailAST();
        uri.initialize(token);
        
        child.addChild(uri);
        
        // Parent
        currentNode.addChild(child);
        
    }

    /**
     * Fin de traitement de l'espace de nommage.
     * @param prefixe le prefixe choisi a l'ouverture du traitement de l'espace nommage.
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        LOG.debug("Fin de traitement de l'espace de nommage : " + prefix);
    }

    /**
     * Evenement recu a chaque fois que l'analyseur rencontre une balise xml ouvrante.
     * @param nameSpaceURI l'url de l'espace de nommage.
     * @param localName le nom local de la balise.
     * @param rawName nom de la balise en version 1.0 <code>nameSpaceURI + ":" + localName</code>
     * @throws SAXException si la balise ne correspond pas a ce qui est attendu,
     * comme par exemple non respect d'une dtd.
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String nameSpaceURI, String localName, String rawName, Attributes attributs) throws SAXException {
        LOG.debug("Ouverture de la balise : " + localName);
        
        int col = locator.getColumnNumber();
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(rawName);
        token.setType(XmlTokenTypes.ELEMENT);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        
        
        // Parent
        currentNode.addChild(child);
        
        // Ident
        DetailAST ident = new DetailAST();
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        col++;
        token.setColumn(col);
        token.setText(localName);
        token.setType(XmlTokenTypes.IDENT);
        ident.initialize(token);
        child.addChild(ident);

        if (!"".equals(nameSpaceURI)) { // espace de nommage particulier
            LOG.debug("  appartenant a l'espace de nom : " + nameSpaceURI);
        }

        // Attributes
        LOG.debug("  Attributs de la balise : ");
        DetailAST attrs = new DetailAST();
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        col += rawName.length() + 1;
        token.setColumn( col );
        token.setText(rawName);
        token.setType(XmlTokenTypes.ATTRIBUTES);
        attrs.initialize(token);
        child.addChild(attrs);

        for (int index = 0; index < attributs.getLength(); index++) { // on parcourt la liste des attributs
            
            // Attribut
            DetailAST attr = new DetailAST();
            token = new XmlToken();
            token.setLine(locator.getLineNumber());
            token.setColumn( index );
            token.setText(rawName);
            token.setType(XmlTokenTypes.ATTRIBUTE);
            attr.initialize(token);
            attrs.addChild(attr);
            LOG.debug("     - " + attributs.getLocalName(index) + " = " + attributs.getValue(index));
            
            // Ident
            ident = new DetailAST();
            token = new XmlToken();
            token.setLine(locator.getLineNumber());
            token.setColumn( index );
            token.setText(rawName);
            token.setType(XmlTokenTypes.IDENT);
            ident.initialize(token);
            attr.addChild(ident);
            
            // Value
            DetailAST value  = new DetailAST();
            token = new XmlToken();
            token.setLine(locator.getLineNumber());
            token.setColumn( index + attributs.getLocalName(index).length() + 1 );
            token.setText(rawName);
            token.setType(XmlTokenTypes.STRING_LITERAL);
            value.initialize(token);
            attr.addChild(value);
            
            // Go forward
            col += attributs.getLocalName(index).length() + attributs.getValue(index).length() + 2;
        }
        
        // This node is now he current node
        currentNode = child;
    }

    /**
     * Evenement recu a chaque fermeture de balise.
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void endElement(String nameSpaceURI, String localName, String rawName) throws SAXException {
        LOG.debug("Fermeture de la balise : " + localName);

        if (!"".equals(nameSpaceURI)) { // name space non null
            LOG.debug("appartenant a l'espace de nommage : " + localName);
        }
        
        // Go to the parent
        currentNode = currentNode.getParent();
        
    }

    /**
     * Evenement recu a chaque fois que l'analyseur rencontre des caracteres (entre
     * deux balises).
     * @param ch les caracteres proprement dits.
     * @param start le rang du premier caractere a traiter effectivement.
     * @param end le rang du dernier caractere a traiter effectivement
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int end) throws SAXException {
        
        String value = new String(ch, start, end);
        
        LOG.debug("#PCDATA : " + value);
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber() + start);
        token.setText(value);
        token.setType(XmlTokenTypes.PCDATA);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        child.setText(token.getText());
        
        // Parent
        currentNode.addChild(child);
        
    }

    /**
     * Recu chaque fois que des caracteres d'espacement peuvent etre ignores au sens de
     * XML. C'est a dire que cet evenement est envoye pour plusieurs espaces se succedant,
     * les tabulations, et les retours chariot se succedants ainsi que toute combinaison de ces
     * trois types d'occurrence.
     * @param ch les caracteres proprement dits.
     * @param start le rang du premier caractere a traiter effectivement.
     * @param end le rang du dernier caractere a traiter effectivement
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] ch, int start, int end) throws SAXException {
        
        String value = new String(ch, start, end);
        
        LOG.debug("espaces inutiles rencontres : ..." + value + "...");
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber() + start);
        token.setText(value);
        token.setType(XmlTokenTypes.WHITE_SPACE);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        child.setText(token.getText());
        
        // Parent
        currentNode.addChild(child);
    }

    /**
     * Rencontre une instruction de fonctionnement.
     * @param target la cible de l'instruction de fonctionnement.
     * @param data les valeurs associees a cette cible. En general, elle se presente sous la forme 
     * d'une serie de paires nom/valeur.
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
     */
    public void processingInstruction(String target, String data) throws SAXException {
        LOG.debug("Instruction de fonctionnement : " + target);
        LOG.debug("  dont les arguments sont : " + data);
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(target);
        token.setType(XmlTokenTypes.PROCESSING_INSTRUCTION);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        child.setText(token.getText());
                
        // Target
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(target);
        token.setType(XmlTokenTypes.PROCESSING_TARGET);
        
        DetailAST targetAST = new DetailAST();
        targetAST.initialize(token);
        targetAST.setText(token.getText());
        child.addChild(targetAST);
        
        // Data
        token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber());
        token.setText(data);
        token.setType(XmlTokenTypes.PROCESSING_DATA);
        
        DetailAST dataAST = new DetailAST();
        dataAST.initialize(token);
        dataAST.setText(token.getText());
        child.addChild(dataAST);
        
        
        // Parent
        currentNode.addChild(child);
    }

    /**
     * Recu a chaque fois qu'une balise est evitee dans le traitement a cause d'un
     * probleme non bloque par le parser. Pour ma part je ne pense pas que vous
     * en ayez besoin dans vos traitements.
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(String text) throws SAXException {
        // Je ne fais rien, ce qui se passe n'est pas franchement normal.
        // Pour eviter cet evenement, le mieux est quand meme de specifier une dtd pour vos
        // documents xml et de les faire valider par votre parser.        
        
        // XmlToken
        XmlToken token = new XmlToken();
        token.setLine(locator.getLineNumber());
        token.setColumn(locator.getColumnNumber() );
        token.setText(text);
        token.setType(XmlTokenTypes.SKIPPED_ENTITY);
        
        
        // Node
        DetailAST child = new DetailAST();
        child.initialize(token);
        child.setText(token.getText());
         
        // Parent
        currentNode.addChild(child);
    }

    public DetailAST getAST() {
        return root;
    }
}