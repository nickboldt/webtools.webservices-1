/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.wizard.uddi;

import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;

public class PublicUDDIRegistryIBMTest implements PublicUDDIRegistryType
{

    // Copyright
    public static final String copyright = "(c) Copyright IBM Corporation 2002.";

    public PublicUDDIRegistryIBMTest() {
    }

    public String getName() {
        return WebServiceConsumptionUIPlugin.getMessage("%PUBLICUDDIREGISTRYTYPE_NAME_IBM_TEST");
    }

    public String getInquiryURL() {
        return "http://uddi.ibm.com/testregistry/inquiryapi";
    }

    public String getPublishURL() {
        return "https://uddi.ibm.com/testregistry/publishapi";
    }

    public String getRegistrationURL() {
        return "http://uddi.ibm.com/testregistry/registry.html";
    }

}
