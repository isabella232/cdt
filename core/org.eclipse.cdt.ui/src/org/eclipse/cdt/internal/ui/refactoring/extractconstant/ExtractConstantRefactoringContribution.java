/*******************************************************************************
 * Copyright (c) 2009, 2012 Institute for Software, HSR Hochschule fuer Technik  
 * Rapperswil, University of applied sciences and others.
 *
 * This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License 2.0 
 * which accompanies this distribution, and is available at 
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0  
 * 
 * Contributors: 
 *     Institute for Software (IFS)- initial API and implementation 
 ******************************************************************************/
package org.eclipse.cdt.internal.ui.refactoring.extractconstant;

import java.util.Map;

import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;

import org.eclipse.cdt.internal.ui.refactoring.CRefactoringContribution;

/**
 * @author Emanuel Graf IFS
 */
public class ExtractConstantRefactoringContribution extends CRefactoringContribution {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public RefactoringDescriptor createDescriptor(String id, String project, String description,
			String comment, Map arguments, int flags) throws IllegalArgumentException {
		if (id.equals(ExtractConstantRefactoring.ID)) {
			return new ExtractConstantRefactoringDescriptor(project, description, comment, arguments);
		}
		return null;
	}
}
