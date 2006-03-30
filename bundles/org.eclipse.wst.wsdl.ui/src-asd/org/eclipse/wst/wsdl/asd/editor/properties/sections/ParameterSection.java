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
package org.eclipse.wst.wsdl.asd.editor.properties.sections;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.asd.editor.ASDEditorPlugin;
import org.eclipse.wst.wsdl.asd.editor.ASDMultiPageEditor;
import org.eclipse.wst.wsdl.asd.facade.IParameter;
import org.eclipse.wst.xsd.adt.edit.ComponentReferenceEditManager;

public class ParameterSection extends NameSection {
	protected static String NEW_STRING = "New...";
	protected static String BROWSE_STRING = "Browse...";
	protected CCombo typeCombo;
	
	/**
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.internal.provisional.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		FormData data;
		
		typeCombo = getWidgetFactory().createCCombo(composite);
		typeCombo.setBackground(composite.getBackground());
		typeCombo.addListener(SWT.Modify, this);
		typeCombo.addSelectionListener(this);
		
		CLabel componentNameLabel = getWidgetFactory().createCLabel(composite, "Type" + ":"); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(typeCombo, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(typeCombo, 0, SWT.CENTER);
		componentNameLabel.setLayoutData(data);
		
//		Button button = getWidgetFactory().createButton(composite, "", SWT.PUSH); //$NON-NLS-1$
//		button.setImage(InterfaceUIPlugin.getDefault().getImage("icons/obj16/browsebutton.gif")); //$NON-NLS-1$
//		
//		button.addSelectionListener(this);
//		data = new FormData();
//		data.left = new FormAttachment(100, -rightMarginSpace + 2);
//		data.right = new FormAttachment(100, 0);
//		data.top = new FormAttachment(typeCombo, 0, SWT.CENTER);
//		button.setLayoutData(data);
		
		data = new FormData();
		data.left = new FormAttachment(0, 100);
//		data.right = new FormAttachment(button, 0);
		data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(nameText, +ITabbedPropertyConstants.VSPACE);
		typeCombo.setLayoutData(data);
	}
	
	/*
	 * @see org.eclipse.wst.common.ui.properties.internal.provisional.view.ITabbedPropertySection#refresh()
	 */
	public void refresh() {
		super.refresh();
		if (nameText.isFocusControl()) {
			return;
		}
		
		IParameter param = null;
		Object model = getModel();
		setListenerEnabled(false);
		
		if (model instanceof IParameter) {
			param = (IParameter) model;
		}
		
		String name = "";
		String typeName = "ParameterSection.java";
		if (param != null) {
			name = param.getName();
			typeName = param.getComponentName();
		}
		
		nameText.setText(name);
		
		// Populate the type Combo
		typeCombo.removeAll();
		typeCombo.add(BROWSE_STRING);
		typeCombo.add(NEW_STRING);
		
		ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
		if (editManager != null) {
			ComponentSpecification[] specs = editManager.getQuickPicks();
			for (int index = 0; index < specs.length; index++) {
				typeCombo.add((String) specs[index].getName());
			}
		}
		
		
		// Display the type in the Combo
		String[] items = typeCombo.getItems();
		int index;
		for (index = 0; index < items.length; index++) {
			if (items[index].equals(typeName)) {
				break;
			}
		}
		
		if (index < items.length) {
			// Found a match
			typeCombo.select(index);
		}
		else {
			typeCombo.setText(typeName);
		}
		
		setListenerEnabled(true);
	}
	
	public boolean shouldUseExtraSpace()
	{
		return false;
	}
	
	public void doHandleEvent(Event event)
	{
		super.doHandleEvent(event);
		if (event.widget == typeCombo) {
			String value = typeCombo.getItem(typeCombo.getSelectionIndex());
			
			IParameter parameter = (IParameter) this.getModel();
			
			if (value.equals(NEW_STRING)) {
				Command command = parameter.getSetTypeCommand(IParameter.SET_NEW_TYPE_ID);
				command.execute();
			}
			else if (value.equals(BROWSE_STRING)) {
				Command command = parameter.getSetTypeCommand(IParameter.SELECT_EXISTING_TYPE_ID);
				command.execute();
			}
			else {
				ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
				ComponentSpecification spec = getComponentSpecificationForValue((String)value);
				if (spec != null) {
					editManager.modifyComponentReference(parameter, spec);
				}
			}
		}
	}
	
	public ComponentReferenceEditManager getComponentReferenceEditManager() {
		ASDMultiPageEditor editor = (ASDMultiPageEditor) ASDEditorPlugin.getActiveEditor();
		return (ComponentReferenceEditManager) editor.getAdapter(ComponentReferenceEditManager.class);
	}
	
	// TODO: rmah: This code should live in a common place..... This code is also used in other UI scenarios when
	// a similar combo box is used.  For example in Direct Edit...(TypeReferenceDirectEditManager)  Also used in the XSDEditor...
	protected ComponentSpecification getComponentSpecificationForValue(String value)
	{
		ComponentReferenceEditManager editManager = getComponentReferenceEditManager();
		if (editManager != null)
		{  
			ComponentSpecification[] quickPicks = editManager.getQuickPicks();
			if (quickPicks != null)
			{
				for (int i=0; i < quickPicks.length; i++)
				{
					ComponentSpecification componentSpecification = quickPicks[i];
					if (value.equals(componentSpecification.getName()))
					{
						return componentSpecification;
					}                
				}  
			}
			ComponentSpecification[] history = editManager.getHistory();
			if (history != null)
			{
				for (int i=0; i < history.length; i++)
				{
					ComponentSpecification componentSpecification = history[i];
					if (value.equals(componentSpecification.getName()))
					{  
						return componentSpecification;
					}
				}  
			}
		}
		return null;
	}
}