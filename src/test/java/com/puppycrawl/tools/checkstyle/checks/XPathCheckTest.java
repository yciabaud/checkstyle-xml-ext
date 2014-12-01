package com.puppycrawl.tools.checkstyle.checks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

	@Test
	public void testRequired() throws Exception {
		// prepare
		AuditListener listener = mock(AuditListener.class);
		ArgumentCaptor<AuditEvent> argument = ArgumentCaptor
				.forClass(AuditEvent.class);

		// execute
		processChecker("issues/xpath-required", listener);

		// verify
		verify(listener).addError(argument.capture());
		assertEquals(XPathCheck.class.getName(), argument.getValue()
				.getSourceName());
		assertEquals(
				"Expected at least 1 match(es) for expression '/bookstore/book[title = 'XML for Dummies']', but found 0.",
				argument.getValue().getMessage());
	}
	
	@Test
	public void testItem() throws Exception {
		// prepare
		AuditListener listener = mock(AuditListener.class);
		ArgumentCaptor<AuditEvent> argument = ArgumentCaptor
				.forClass(AuditEvent.class);

		// execute
		processChecker("issues/xpath-item", listener);

		// verify
		verify(listener, times(2)).addError(argument.capture());
		assertEquals(2, argument.getAllValues().size());
		AuditEvent event0 = argument.getAllValues().get(0);
		assertEquals(XPathCheck.class.getName(), event0.getSourceName());
		assertEquals(19, event0.getLine());
		AuditEvent event1 = argument.getAllValues().get(1);
		assertEquals(XPathCheck.class.getName(), event1.getSourceName());
		assertEquals(30, event1.getLine());
	}

}
