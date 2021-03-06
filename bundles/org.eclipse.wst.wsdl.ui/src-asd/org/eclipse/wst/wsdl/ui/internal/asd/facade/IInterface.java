/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.facade;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;

public interface IInterface extends INamedObject {
  public List getOperations();
  /*
   * @deprecated.  The method getImage() will be removed in the near future
   */
  public Image getImage();
  public Command getAddOperationCommand();
}
