/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.command.data;

import java.util.List;
import java.util.Vector;

import org.eclipse.wst.command.env.core.data.Transformer;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class ServerName2IServerTransformer implements Transformer
{
  public Object transform(Object value)
  {
    Vector serverIds = new Vector();
    {
      String serverName = value.toString();
      List servers = ServerCore.getResourceManager().getServers();
      if (servers != null && !servers.isEmpty())
      {
        for (int i = 0; i < servers.size(); i++)
        {
          IServer server = (IServer)servers.get(i);
          if ((server.getName()).equals(serverName))
            return server;
        }
      }
    }
    return null;
  }
}
