package com.puppycrawl.tools.checkstyle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;
import org.xml.sax.Attributes;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.XmlTokenTypes;
import com.puppycrawl.tools.checkstyle.xmlextension.XmlTreeWalkerTest;

public class XmlContentHandlerTest {
	
	@Test
	public void test()  throws Exception {
		File file = new File(XmlTreeWalkerTest.class.getResource("/test.xml").toURI());
		
		XmlContentHandler handler = new XmlContentHandler(file);
		
		Attributes attributes = mock(Attributes.class);
		when(attributes.getLength()).thenReturn(1);
		when(attributes.getLocalName(0)).thenReturn("attrLocalName");
		when(attributes.getValue(0)).thenReturn("attrValue");
		
		handler.startDocument();
		handler.startElement("nameSpaceURI", "localName", "rawName", attributes);
		handler.endDocument();
		
		DetailAST elm = handler.getAST().findFirstToken(XmlTokenTypes.ELEMENT);
		assertEquals("rawName",  elm.getText());
		
		DetailAST attrs = elm.findFirstToken(XmlTokenTypes.ATTRIBUTES);
		assertNotNull(attrs);
		
		DetailAST attr = attrs.findFirstToken(XmlTokenTypes.ATTRIBUTE);
		
		assertEquals("attrLocalName",  attr.findFirstToken(XmlTokenTypes.IDENT).getText());
		assertEquals("attrValue",  attr.findFirstToken(XmlTokenTypes.STRING_LITERAL).getText());
	}

}
