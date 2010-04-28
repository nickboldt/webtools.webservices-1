/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100302   304405 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Facet : support JAX-RS 1.1 (JSR 311)
 * 20100428   310905 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet fails to install due to NPE or runtime exception due to duplicate cp entries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal;

/**
 * JAXRS Core framework constants
 * 
 * <p>
 * <b>Provisional API - subject to change</b>
 * </p>
 * 
 * 
 */
public final class IJAXRSCoreConstants {
	/**
	 * The global id for the JAXRS facet 
	 */
	public static final String JAXRS_FACET_ID = "jst.jaxrs"; //$NON-NLS-1$
	/**
	 * The facet version for a JAX-RS 1.0 project 
	 */
	public final static String FACET_VERSION_1_0 = "1.0"; //$NON-NLS-1$
	/**
	 * The constant id for a JAXRS 1.0 project
	 */
	public final static String JAXRS_VERSION_1_0 = FACET_VERSION_1_0;
	/**
	 * The facet version for a JAX-RS 1.1 project 
	 */
	public final static String FACET_VERSION_1_1 = "1.1"; //$NON-NLS-1$
	/**
	 * The constant id for a JAXRS 1.1 project
	 */
	public final static String JAXRS_VERSION_1_1 = FACET_VERSION_1_1;
	public static final String NO_OP_LIBRARY_ID = "jaxrs-no-op-library-provider"; //$NON-NLS-1$

	private IJAXRSCoreConstants() {
		// no instantiation
	}
}
