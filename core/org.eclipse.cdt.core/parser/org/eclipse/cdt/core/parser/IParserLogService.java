/*******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Rational Software - Initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.core.parser;

/**
 * @author jcamelon
 */
public interface IParserLogService {
	public void traceLog(String message);
	
	public boolean isTracing();
}
