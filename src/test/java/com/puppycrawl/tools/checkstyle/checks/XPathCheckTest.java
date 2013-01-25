package com.puppycrawl.tools.checkstyle.checks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;

public class XPathCheckTest extends AbstractXmlCheckTest {

	@Test
	public void testSeverity() throws Exception {
		// prepare
		AuditListener listener = mock(AuditListener.class);
		ArgumentCaptor<AuditEvent> argument = ArgumentCaptor
				.forClass(AuditEvent.class);

		// execute
		processChecker("issues/severity", listener);

		// verify
		verify(listener).addError(argument.capture());
		assertEquals(XPathCheck.class.getName(), argument.getValue()
				.getSourceName());
		assertEquals(SeverityLevel.WARNING, argument.getValue()
				.getSeverityLevel());
	}

	@Test
	public void testNamespace() throws Exception {
		// prepare
		AuditListener listener = mock(AuditListener.class);
		ArgumentCaptor<AuditEvent> argument = ArgumentCaptor
				.forClass(AuditEvent.class);

		// execute
		processChecker("issues/xpath-namespace", listener);

		// verify
		verify(listener).addError(argument.capture());
		assertEquals(XPathCheck.class.getName(), argument.getValue()
				.getSourceName());
	}

}
