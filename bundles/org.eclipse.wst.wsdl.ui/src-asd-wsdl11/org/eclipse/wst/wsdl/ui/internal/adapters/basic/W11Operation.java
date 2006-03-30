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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddFaultAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddInputAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddOperationAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddOutputAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.asd.editor.outline.ITreeElement;
import org.eclipse.wst.wsdl.asd.facade.IInterface;
import org.eclipse.wst.wsdl.asd.facade.IOperation;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddFaultParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddInputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddOutputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11DeleteCommand;

public class W11Operation extends WSDLBaseAdapter implements IOperation {
	public List getMessages()
	{
		List modelAdapterList = new ArrayList(getOperation().getEFaults().size() + 2);
		if (getOperation().getEInput() != null)
		{
			modelAdapterList.add(createAdapter(getOperation().getEInput()));
		}
		if (getOperation().getEOutput() != null)
		{
			modelAdapterList.add(createAdapter(getOperation().getEOutput()));
		}
		for (Iterator i = getOperation().getEFaults().iterator(); i.hasNext();)
		{
			EObject o = (EObject) i.next();
			modelAdapterList.add(createAdapter(o));
		}
		return modelAdapterList;
	}
	
	// Convienence method
	public Operation getOperation()
	{
		return (Operation) target;
	}
	
	public String getName()
	{
		return getOperation().getName();
	}
	
	public IInterface getOwnerInterface()
	{
		return (IInterface)owner;
	}
	
	public String[] getActions(Object object) {
		List actions = new ArrayList();
		actions.add(ASDAddOperationAction.ID);
		actions.addAll(getValidInputOutpuActions());
		actions.add(ASDAddFaultAction.ID);
		actions.add(ASDDeleteAction.ID);
		
		String[] actionIDs = new String[actions.size()];
		for (int index = 0; index < actions.size(); index++) {
			actionIDs[index] = (String) actions.get(index);
		}
		
		return actionIDs;
	}
	
	public Command getAddInputCommand() {
		return new W11AddInputParameterCommand(getOperation());
	}
	
	public Command getAddOutputCommand() {
		return new W11AddOutputParameterCommand(getOperation());
	}
	
	public Command getAddFaultCommand(Object fault) {
		Fault aFault = null;
		if (fault instanceof Fault) {
			aFault = (Fault) fault;
		}
		return new W11AddFaultParameterCommand(getOperation(), aFault);
	}
	
	public Command getDeleteCommand() {
		return new W11DeleteCommand(this);
	}
	
	public List getValidInputOutpuActions() {
		List list = new ArrayList();
		
		if (getOperation().getEInput() == null) {
			list.add(ASDAddInputAction.ID);
		}
		if (getOperation().getEOutput() == null) {
			list.add(ASDAddOutputAction.ID);
		}
		
		return list;
	}
	
	public Image getImage() {
		return WSDLEditorPlugin.getInstance().getImage("icons/operation_obj.gif");
	}
	
	public String getText() {
		return "operation";
	}
	
	public ITreeElement[] getChildren() {
		List messages = getMessages();
		ITreeElement[] treeElements = new ITreeElement[messages.size()];
		
		for (int index = 0; index < messages.size(); index++) {
			treeElements[index] = (ITreeElement) messages.get(index);
		}
		
		return treeElements;
	}

	public boolean hasChildren() {
		if (getChildren().length > 0) {
			return true;
		}
		
		return false;
	}

	public ITreeElement getParent() {
		return null;
	}
}