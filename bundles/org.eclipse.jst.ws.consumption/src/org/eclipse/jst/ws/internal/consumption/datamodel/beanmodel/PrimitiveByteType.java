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

package org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel;


/**
* objects of this class represent a type
* 
*/
public class PrimitiveByteType extends PrimitiveType 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  /**
  *Constructor
  *
  */
  public PrimitiveByteType()
  {
    super(TypeFactory.PRIM_BYTE_NAME);
  }
 
  /**
  * StringToType gets the string taken by the 
  * user into proxy form
  * @return String convert the string to type.
  */
  public String StringToType(String name)
  {
    String conversion = "Byte.parseByte(" + name + ");";
    return conversion;
  }
  
  
 }

