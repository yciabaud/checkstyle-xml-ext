package com.puppycrawl.tools.checkstyle.checks;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

public class XQueryCheckTest extends AbstractXmlCheckTest {

	@Test
	public void testNamespace() throws Exception {
		// prepare
		AuditListener listener = mock(AuditListener.class);
		ArgumentCaptor<AuditEvent> argument = ArgumentCaptor
				.forClass(AuditEvent.class);

		// execute
		processChecker("issues/xquery-namespace", listener);

		// verify
		verify(listener).addError(argument.capture());
		assertEquals(XQueryCheck.class.getName(), argument.getValue()
				.getSourceName());
	}

	@Test
	public void testRequired() throws Exception {
		// prepare
		AuditListener listener = mock(AuditListener.class);
		ArgumentCaptor<AuditEvent> argument = ArgumentCaptor
				.forClass(AuditEvent.class);

		// execute
		processChecker("issues/xquery-required", listener);

		// verify
		verify(listener).addError(argument.capture());
		assertEquals(XQueryCheck.class.getName(), argument.getValue()
				.getSourceName());
		assertEquals(
				"Expected at least 1 match(es) for expression 'for $b in /bookstore/book" +
				"                                             where $b/title = 'XML for Dummies'" +
				"                                             return $b', but found 0.",
				argument.getValue().getMessage());
	}
}
