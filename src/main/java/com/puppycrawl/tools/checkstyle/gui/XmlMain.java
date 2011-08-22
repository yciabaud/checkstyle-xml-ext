package com.puppycrawl.tools.checkstyle.gui;

import java.io.File;
import javax.swing.JFrame;

/**
 *
 * @author vista
 */
public class XmlMain {
    
    static JFrame frame;

    public static void main(String[] args)
    {
        frame = new JFrame("CheckStyle-xml-ext");
        final XmlParseTreeInfoPanel panel = new XmlParseTreeInfoPanel();
        frame.getContentPane().add(panel);
        if (args.length >= 1) {
            final File f = new File(args[0]);
            panel.openFile(f, frame);
        }
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
}
