/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.design;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * A collection of color and font related constants.
 */
public interface DesignViewGraphicsConstants 
{                          
  public final static Display display = Display.getDefault(); 
                                                 
  public final static Color groupBorderColor        = new Color(null, 118, 134, 164);
  public final static Color groupHeaderColor        = new Color(null, 232, 240, 248);

  public final static Color elementBorderColor      = new Color(null, 120, 152, 184);
  public final static Color elementBackgroundColor  = new Color(null, 232, 240, 248);  
  public final static Color elementLabelColor       = new Color(null,  80, 102, 144);
  public final static Color readOnlyBorderColor     = new Color(null, 164, 164, 164); 
  public final static Color red                     = new Color(null, 255,   0,   0); 

  public final static Color tableOperationHeadingColor = new Color(null, 224, 233, 246);
  public final static Color tableMessageHeadingColor = new Color(null, 230, 240, 245);
  public final static Color tableCellSelectionColor = new Color(null, 238, 232, 170); //new Color(null, 240, 230, 140); //1, 15, 42);

  public final static Color readOnlyBackgroundColor = ColorConstants.white;
  public final static Color readOnlyLabelColor = ColorConstants.gray;
  public final static Color labelColor = ColorConstants.black;

  public final static Font  smallBoldFont           = new Font(Display.getCurrent(), "Tahoma", 8, SWT.BOLD); //$NON-NLS-1$
  public final static Font  mediumFont              = new Font(Display.getCurrent(), "Tahoma", 10, SWT.NONE); //$NON-NLS-1$
  public final static Font  mediumBoldFont          = new Font(Display.getCurrent(), "Tahoma", 10, SWT.BOLD);  //$NON-NLS-1$
}