/*******************************************************************************
 * Copyright (c) 2013, 2013 Andrew Gvozdev and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrew Gvozdev - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.errorparsers.xlc.tests;

import junit.framework.TestCase;

import org.eclipse.cdt.core.IMarkerGenerator;

public class TestInformationalMessage_3 extends TestCase {
	String err_msg;
	/**
	 * This function tests parseLine function of the
	 * XlcErrorParser class. A variant of informational message generated by
	 * xlc compiler is given as input for testing.
	 */
	public void testparseLine()
	{
		XlcErrorParserTester aix = new XlcErrorParserTester();
		aix.parseLine(err_msg);
		assertEquals("", aix.getFileName(0));
		assertEquals(0, aix.getLineNumber(0));
		assertEquals(IMarkerGenerator.SEVERITY_INFO, aix.getSeverity(0));
		assertEquals("Global variable \"__td __td__Q2_3std13runtime_error\" is not used.",aix.getMessage(0));
	}
	public TestInformationalMessage_3( String name)
	{
		super(name);
		err_msg = "1540-5336 (I) Global variable \"__td __td__Q2_3std13runtime_error\" is not used.";
	}
}
