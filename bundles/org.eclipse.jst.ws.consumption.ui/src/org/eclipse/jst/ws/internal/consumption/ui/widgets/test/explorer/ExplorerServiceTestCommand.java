/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test.explorer;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.StartProjectCommand;
import org.eclipse.jst.ws.internal.ext.test.WSDLTestFinishCommand;
import org.eclipse.jst.ws.internal.ui.wse.LaunchOption;
import org.eclipse.jst.ws.internal.ui.wse.LaunchWebServicesExplorerCommand;
import org.eclipse.jst.ws.internal.ui.wse.WSExplorerType;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;

public class ExplorerServiceTestCommand extends SimpleCommand implements WSDLTestFinishCommand
{

  private String LABEL = "ExplorerServiceTestCommand";
  private String DESCRIPTION = "Launch the Explorer";
  private boolean externalBrowser = true;
  private String wsdlServiceURL;
  private String serviceServerTypeID;
  private IServer serviceExistingServer;
  private String serviceProject;
  private List endpoints;
  
  /**
  * Constructs a new WebServiceExplorerLaunch object with the given label and description.
  */
  public ExplorerServiceTestCommand ()
  {
  	setDescription(DESCRIPTION);
  	setName(LABEL);
  }

  public Status execute(Environment env)
  {
  	Status status = new SimpleStatus( "" );
    
    StartProjectCommand spc = new StartProjectCommand( true );
    spc.setServiceServerTypeID(serviceServerTypeID);
    spc.setServiceExistingServer(serviceExistingServer);
    IProject project = (IProject) ResourceUtils.findResource(serviceProject);
    spc.setServiceProject(project);
    spc.setIsWebProjectStartupRequested(true);
    
    status = spc.execute(env);
    if (status.getSeverity() == Status.ERROR)
    	return status;

    LaunchWebServicesExplorerCommand launchCommand = new LaunchWebServicesExplorerCommand();
    launchCommand.setForceLaunchOutsideIDE(externalBrowser);
    Vector launchOptionVector = new Vector();
    launchOptionVector.add(new LaunchOption(WSExplorerType.WSDL_URL,wsdlServiceURL));
    if (endpoints != null)
      for (Iterator it = endpoints.iterator(); it.hasNext();)
        launchOptionVector.add(new LaunchOption(WSExplorerType.WEB_SERVICE_ENDPOINT, it.next().toString()));
    launchCommand.setLaunchOptions((LaunchOption[])launchOptionVector.toArray(new LaunchOption[0]));
    status = launchCommand.execute(env);
    return status;
  }

  public void setExternalBrowser(boolean externalBrowser)
  {
  	this.externalBrowser = externalBrowser;
  }
  
  public void setWsdlServiceURL(String wsdlServiceURL)
  {
  	this.wsdlServiceURL = wsdlServiceURL;
  }
  
  public void setServerTypeID(String serviceServerTypeID)
  {
  	this.serviceServerTypeID = serviceServerTypeID;
  }
  
  public void setExistingServer(IServer serviceExistingServer)
  {
  	this.serviceExistingServer = serviceExistingServer;
  }
  
  public void setServiceProject(String serviceProject)
  {
    this.serviceProject = serviceProject;
  }
  
  public void setEndpoint(List endpoints)
  {
    this.endpoints = endpoints;
  }
}