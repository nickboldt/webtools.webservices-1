/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.command.internal.provisional.env.core;

import java.util.Iterator;


/**
 * 
 * This class is used to return a sequence of Commands.
 *
 */
public interface ICommandFactory extends Iterator 
{
  /**
   * 
   * @return returns the next Command in the sequence.
   */
  public EnvironmentalOperation getNextCommand();
}
