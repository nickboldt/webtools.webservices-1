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
package org.eclipse.wst.wsdl.ui.internal.adapters.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.binding.http.HTTPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11DeleteCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11GenerateBindingCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11SetInterfaceCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDGenerateBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetExistingInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetNewInterfaceAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.model.BindingContentPlaceHolder;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

public class W11Binding extends WSDLBaseAdapter implements IBinding {

	public IInterface getInterface() {
        PortType portType = ((Binding) target).getEPortType();
        if (portType != null)
        {  
	      return (IInterface) createAdapter(portType);
		}		
		return null;
	}
    
    public List getBindingContentList()
    {
      List adapterList = new ArrayList();
      List list = new ArrayList();

      List bindingOperations = copyList(((Binding)target).getEBindingOperations()); 
      List operations = copyList(((Binding)target).getEPortType().getEOperations());
      
      // Determine if we need placeholders for Operations
	  List toAdaptList = new ArrayList();
      
	  Iterator bindingOpIt = bindingOperations.iterator();
	  while (bindingOpIt.hasNext()) {
		  BindingOperation item = (BindingOperation) bindingOpIt.next();
		  operations.remove(item.getEOperation());
	  }
	  
      Iterator operationsIt = operations.iterator();
      while (operationsIt.hasNext()) {
    	  Operation op = (Operation) operationsIt.next();
    	  toAdaptList.add(op);
    	  toAdaptList.add(op.getEInput());
    	  toAdaptList.add(op.getEOutput());
    	  toAdaptList.addAll(op.getEFaults());
      }

      Iterator it = toAdaptList.iterator();
      while (it.hasNext()) {
    	  Object adapted = WSDLAdapterFactoryHelper.getInstance().adapt((Notifier) it.next());
    	  BindingContentPlaceHolder placeHolder = new BindingContentPlaceHolder(adapted);
    	  adapterList.add(placeHolder);
      }
      
      for (Iterator i = ((Binding)target).getEBindingOperations().iterator(); i.hasNext(); )
      {
        BindingOperation bindingOperation = (BindingOperation)i.next();
        Operation operation = bindingOperation.getEOperation();
        
        list.add(bindingOperation);
        
        // Handle Input
        if (bindingOperation.getEBindingInput() != null)
        {
          list.add(bindingOperation.getEBindingInput());
        }
        else if (bindingOperation.getEBindingInput() == null && operation.getEInput() != null) {
        	// Need a placeholder
        	Object adaptedObject = createAdapter(operation.getEInput());
        	BindingContentPlaceHolder temp = new BindingContentPlaceHolder(adaptedObject);
        	adapterList.add(temp);
        }
        
        // Handle Output
        if (bindingOperation.getEBindingOutput() != null)
        {
          list.add(bindingOperation.getEBindingOutput());
        }
        else if (bindingOperation.getEBindingOutput() == null && operation.getEOutput() != null) {
        	// Need a placeholder
        	Object adaptedObject = createAdapter(operation.getEOutput());
        	BindingContentPlaceHolder temp = new BindingContentPlaceHolder(adaptedObject);
        	adapterList.add(temp);
        }

        // Handle Faults
        List faults = copyList(operation.getEFaults());
        List bindingFaults = copyList(bindingOperation.getEBindingFaults());
        for (int index = 0; index < bindingFaults.size(); index++) {
        	BindingFault bindingFault = (BindingFault) bindingFaults.get(index);
        	list.add(bindingFault);
        	
        	faults.remove(bindingFault.getEFault());        	
        }

        // Left over faults need placeholders
        Iterator remaningFaults = faults.iterator();
        while (remaningFaults.hasNext()) {
        	Object adaptedObject = createAdapter((Fault) remaningFaults.next());
        	BindingContentPlaceHolder temp = new BindingContentPlaceHolder(adaptedObject);
        	adapterList.add(temp);
        }
      }

      populateAdapterList(list, adapterList);
      return adapterList;
    }
    
    private List copyList(List origList) {
    	List newList = new ArrayList();
    	Iterator it = origList.iterator();
    	while (it.hasNext()) {
    		newList.add(it.next());
    	}
    	
    	return newList;
    }

	public List getExtensiblityObjects() {

		return null;
	}

	public String getName() {
		return ((Binding) target).getQName().getLocalPart();
	}
	
	public String getProtocol() {
		Iterator it = ((Binding) target).getEExtensibilityElements().iterator();
		while (it.hasNext()) {
			Object item = it.next();
			if (item instanceof SOAPBinding) {
				return "SOAP"; //$NON-NLS-1$
			}
			else if (item instanceof HTTPBinding) {
				return "HTTP"; //$NON-NLS-1$
			}
		}
		
		return ""; //$NON-NLS-1$
	}
	
	public IDescription getOwnerDescription() {
		return (IDescription) owner;
	}
	
	public String[] getActions(Object object) {
		String[] actionIDs = new String[6];
		actionIDs[0] = ASDGenerateBindingAction.ID;
		actionIDs[1] = BaseSelectionAction.SUBMENU_START_ID + Messages.getString("_UI_ACTION_SET_PORTTYPE"); //$NON-NLS-1$
		actionIDs[2] = ASDSetNewInterfaceAction.ID;
		actionIDs[3] = ASDSetExistingInterfaceAction.ID;
		actionIDs[4] = BaseSelectionAction.SUBMENU_END_ID;
		actionIDs[5] = ASDDeleteAction.ID;
		
		return actionIDs;
	}
	
	public Command getSetInterfaceCommand(IInterface newInterface) {
		W11Interface w11Interface = (W11Interface) newInterface;
		return new W11SetInterfaceCommand((Binding) target, (PortType) w11Interface.getTarget());
	}
	
	public Command getGenerateBindingCommand() {
		return new W11GenerateBindingCommand((Binding) this.getTarget());
	}
	
	public Command getDeleteCommand() {
		return new W11DeleteCommand(this);
	}

  public Image getImage()
  {
    // TODO (cs) this is evil code, we need an extension driven
    // way to compute this stuff so don't hardcode HTTP or SOAP stuff
    //
    String protocol = getProtocol();
    String imageName = "icons/binding_obj.gif"; //$NON-NLS-1$
    if (protocol != null)
    {  
      if (protocol.equals("HTTP")) //$NON-NLS-1$
      {
        imageName = "icons/httpbinding_obj.gif"; //$NON-NLS-1$
      }  
      else if (protocol.equals("SOAP")) //$NON-NLS-1$
      {
        imageName = "icons/soapbinding_obj.gif"; //$NON-NLS-1$
      }  
    }
    return WSDLEditorPlugin.getInstance().getImage(imageName);    
  }

  public String getText()
  {
    return "binding";
  }
  
  public ITreeElement[] getChildren()
  {
    List list = getBindingOperations();
    ITreeElement[] result = new ITreeElement[list.size()];
    list.toArray(result);
    return result;
  }
  
  public boolean hasChildren() {
	  if (getBindingOperations().size() > 0) {
		  return true;
	  }
	  
	  return false;
  }
  
  public ITreeElement getParent() {
	  return null;
  }
  
  public List getBindingOperations()
  {
    List list = new ArrayList();
    populateAdapterList(((Binding)target).getEBindingOperations(), list);
    return list;
  }
  
  public List getBindingOperations(boolean createMissingOperations, boolean showExtraOperations)
  {
    return getBindingOperations();
  }
}
