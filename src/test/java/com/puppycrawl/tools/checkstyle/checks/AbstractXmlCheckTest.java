package com.puppycrawl.tools.checkstyle.checks;

import java.io.File;
import java.util.Collections;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AbstractXmlCheckTest {

	public AbstractXmlCheckTest() {
		super();
	}

	/**
	 * In the testfolder is expected:
	 * <ul>
	 * <li>A file called <code>checkstyle.xml</code>, containing the checkstyle
	 * rules.</li>
	 * <li>A file called <code>input.xml</code>, used to test
	 * </ul>
	 * As an {@link AuditListener} you could use a mock
	 * 
	 * <pre>
	 * // prepare
	 * AuditListener listener = mock(AuditListener.class);
	 * ArgumentCaptor&lt;AuditEvent&gt; argument = ArgumentCaptor.forClass(AuditEvent.class);
	 * 
	 * // execute
	 * processChecker(&quot;issues/severity&quot;, listener);
	 * 
	 * // verify
	 * verify(listener).addError(argument.capture());
	 * </pre>
	 * 
	 * Now every <code>addError</code> call is captured. By using <code>argument.getValue()</code> or <code>argument.getAllValues()</code>
	 * you can assert every {@link AuditEvent} 
	 * 
	 * @param testFolder
	 *            subfolder of {@code src/test/java}
	 * @param listener
	 * @throws CheckstyleException
	 */
	protected final void processChecker(String testFolder, AuditListener listener)
			throws CheckstyleException {
				// prepare
				Checker checker = new Checker();
				checker.setModuleClassLoader(XPathCheckTest.class.getClassLoader());
			
				Configuration config = ConfigurationLoader.loadConfiguration(
						"src/test/resources/" + testFolder + "/checkstyle.xml", null);
				checker.configure(config);
				checker.addListener(listener);
			
				// execute
				checker.process(Collections.singletonList(new File(
						"src/test/resources/" + testFolder + "/input.xml")));
			}

}