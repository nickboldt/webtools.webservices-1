/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20071024   196997 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.wst.ws.service.policy;

import java.util.List;


public class PolicyRelationshipImpl implements IPolicyRelationship
{
  private IPolicyEnumerationList       policyEnumerationList;
  private List<IPolicyEnumerationList> relatedPolices;
  
  public PolicyRelationshipImpl( IPolicyEnumerationList       policyEnumerationList,
                                 List<IPolicyEnumerationList> relatedPolicies )
  {
    this.policyEnumerationList = policyEnumerationList;
    this.relatedPolices        = relatedPolicies;
  }
  
  public IPolicyEnumerationList getPolicyEnumerationList()
  {
    return policyEnumerationList;
  }

  public List<IPolicyEnumerationList> getRelatedPolicies()
  {
    return relatedPolices;
  } 
}