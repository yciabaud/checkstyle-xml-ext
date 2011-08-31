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

package com.puppycrawl.tools.checkstyle.xmlextension;

import com.puppycrawl.tools.checkstyle.XmlTreeWalker;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.Main;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.xml.stream.XMLStreamException;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XML tree walkzer test.
 * 
 * @author Yoann Ciabaud<y.ciabaud@gmail.com>
 */
public class XmlTreeWalkerTest {

    private static final String[] args = new String[]{"-c", "target/test-classes/config.xml", "-r", "target/test-classes/xmltree"};
    
    @Test
    public void walkTest() throws IOException, URISyntaxException, XMLStreamException, SAXException {
        
        File file = new File(XmlTreeWalkerTest.class.getResource("/test.xml").toURI());
        
        DetailAST tree = XmlTreeWalker.parse(new InputSource(new FileInputStream(
                file)), 
                file);

        Assert.assertEquals(2, tree.getNumberOfChildren());
    }

    @Test
    public void configTest() {
        Main.main(args);
    }
}
