/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.gef.commands.Command;
import org.eclipse.wst.sse.core.internal.encoding.CommonEncodingPreferenceNames;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.core.internal.XMLCorePlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

public class W11TopLevelElementCommand extends Command
{
  private static final String XML = "xml"; //$NON-NLS-1$
  protected Definition definition;

  public W11TopLevelElementCommand(String label, Definition definition)
  {
    super(label);
    this.definition = definition;
  }

  public void execute()
  {
    ensureDefinition();
  }

  private void ensureDefinition()
  {
    Document document = definition.getDocument();

    Element definitionsElement = document.getDocumentElement();

    if (definitionsElement == null)
    {
      String targetNamespace = getDefaultNamespace();
      definition.setQName(new QName(null, getFileName()));
      definition.setTargetNamespace(targetNamespace);
      definition.addNamespace("wsdl", WSDLConstants.WSDL_NAMESPACE_URI); //$NON-NLS-1$
      definition.updateElement();
      // Moving these above updateElement() seems to cause grief with the model.
      definition.addNamespace("tns", targetNamespace); //$NON-NLS-1$
      definition.addNamespace("xsd", WSDLConstants.XSD_NAMESPACE_URI); //$NON-NLS-1$
      definitionsElement = definition.getElement();
    }

    ensureXMLDirective(document);
  }

  private void ensureXMLDirective(Document document)
  {
    if (hasXMLDirective(document))
    {
      return;
    }
    
    Node firstChild = document.getFirstChild();
    ProcessingInstruction xmlDeclaration = getXMLDeclaration(document);
    document.insertBefore(xmlDeclaration, firstChild);
  }
  
  private boolean hasXMLDirective(Document document)
  {
    Node firstChild = document.getFirstChild();
   
    if (firstChild == null)
    {
      return false;
    }
    
    if (firstChild.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE)
    {
      return false;
    }
    
    ProcessingInstruction processingInstruction  = (ProcessingInstruction)firstChild;
    
    if (!processingInstruction.getTarget().equals(XML)) 
    {
      return false;
    }
    
    return true;
  }

  private ProcessingInstruction getXMLDeclaration(Document document)
  {
    Preferences preference = XMLCorePlugin.getDefault().getPluginPreferences();
    String charSet = preference.getString(CommonEncodingPreferenceNames.OUTPUT_CODESET);
    if (charSet == null || charSet.trim().equals(""))
    {
      charSet = "UTF-8";
    }
    ProcessingInstruction xmlDeclaration = document.createProcessingInstruction(XML, "version=\"1.0\" encoding=\"" + charSet + "\"");
    return xmlDeclaration;
    
  }
  private String getDefaultNamespace()
  {
    String namespace = WSDLEditorPlugin.getInstance().getPreferenceStore().getString(Messages.getString("_UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE"));

    if (!namespace.endsWith("/"))
    {
      namespace = namespace.concat("/");
    }

    namespace += getFileName() + "/";

    return namespace;

  }

  private String getFileName()
  {
    String fileLocation = definition.getLocation();
    IPath filePath = new Path(fileLocation);
    return filePath.removeFileExtension().lastSegment().toString();
  }
}
