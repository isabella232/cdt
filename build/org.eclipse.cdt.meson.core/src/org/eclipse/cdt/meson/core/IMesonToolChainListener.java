/*******************************************************************************
 * Copyright (c) 2016, 2018 QNX Software Systems and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Red Hat Inc. - modified for use with Meson builder
 *******************************************************************************/
package org.eclipse.cdt.meson.core;

/**
 * Listener for toolchain events.
 */
public interface IMesonToolChainListener {

	void handleMesonToolChainEvent(MesonToolChainEvent event);

}
