/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puppycrawl.tools.checkstyle.xmlextension;

import com.puppycrawl.tools.checkstyle.Main;
import org.junit.Test;

/**
 *
 * @author YCIABAUD
 */
public class CliTest {
    
    private static final String[] args = new String[]{"-c", "target/test-classes/config.xml", "-f", "xml", "-o", "target/checkstyle-errors.xml", "-r", "O:\\JWARE_AUTH\\config\\import\\JWARE_AUTH_EXPERIAN\\CFG_MXT"};
    
   
    @Test
    public void configTest() {
        Main.main(args);
    }
}
