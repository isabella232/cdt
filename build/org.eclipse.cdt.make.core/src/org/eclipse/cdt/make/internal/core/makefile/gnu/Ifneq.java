/*******************************************************************************
 * Copyright (c) 2000, 2016 QNX Software Systems and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.make.internal.core.makefile.gnu;

import org.eclipse.cdt.make.internal.core.makefile.Directive;

public class Ifneq extends Conditional {

	public Ifneq(Directive parent, String cond) {
		super(parent, cond);
	}

	@Override
	public boolean isIfneq() {
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(GNUMakefileConstants.CONDITIONAL_IFNEQ);
		sb.append(' ').append(getConditional());
		return sb.toString();
	}

}
