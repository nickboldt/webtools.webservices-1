/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;

public class NameUtil
{
  /**
   * Return a name which is not used by any other fault in the operation.
   * @return String
   */
  public static String buildUniqueFaultName(Operation operation)
  {
  	return buildUniqueFaultName(operation, "NewFault");
  }
  
  public static String buildUniqueFaultName(Operation operation, String baseName) {
  	if (baseName == null)
  		baseName = "NewFault";
  	
  	List names = getUsedFaultNames(operation);

    // Now search the list until we find an unused name
    return getUniqueNameHelper(baseName, names);
  }

  /**
   * Return a name which is not used by any other input in the portType.  Returned name will be of the form:
   * <operationName> + <ending> [+ unique Integer]
   * @return String
   */
  public static String buildUniqueInputName(PortType portType, String operationName, String ending)
  {
    String name = null;
    String candidate = operationName + ending;

    int i = 0;

    // loop until we find a unique name (the name will consist of the operationName + ending + an integer)
    while (name == null)
    {
      boolean unique = true;

      // determine if this combination is unique within the current porttype
      for (Iterator it = portType.getEOperations().iterator(); it.hasNext() && unique;)
      {
        Operation current = (Operation) it.next();
        // TODO : port check
        // old  if(current.isSetEInput() && current.getEInput().isSetName()) {
        if (current.getEInput() != null && current.getEInput().getName() != null)
        {
          if (current.getEInput().getName().equals(candidate))
            unique = false;
        }
      }
      if (unique)
        name = candidate;
      else
        candidate = operationName + ending + i;
      i++;
    }
    return name;
  }

  /**
   * Return a name which is not used by any other message in the definition.
   * @return String
   */
  public static String buildUniqueMessageName(Definition definition, String baseName)
  {
    if (baseName == null)
    {
      baseName = "NewMessage";
    }

    List names = getUsedMessageNames(definition);

    // Now search the list until we find an unused name
    return getUniqueNameHelper(baseName, names);
  }

  /**
   * Return a name which is not used by any other operation in the port type.
   * @return String
   */
  public static String buildUniqueOperationName(PortType portType)
  {
  	return buildUniqueOperationName(portType, "NewOperation");
  }
  
  public static String buildUniqueOperationName(PortType portType, String baseName)
  {
  	if (baseName == null) {
  		baseName = "NewOperation";
  	}

  	List names = getUsedOperationNames(portType);

    // Now search the list until we find an unused name
    return getUniqueNameHelper(baseName, names);
  }

  /**
   * Return a name which is not used by any other output in the portType.  Returned name will be of the form:
   * <operationName> + <ending> [+ unique Integer]
   * @return String
   */
  public static String buildUniqueOutputName(PortType portType, String operationName, String ending)
  {
    String name = null;
    String candidate = operationName + ending;

    int i = 0;

    // loop until we find a unique name (the name will consist of the operationName + ending + an integer)
    while (name == null)
    {
      boolean unique = true;

      // determine if this combination is unique within the current porttype			
      for (Iterator it = portType.getEOperations().iterator(); it.hasNext() && unique;)
      {
        Operation current = (Operation) it.next();
        // TODO: port check
        // old				if(current.isSetEOutput() && current.getEOutput().isSetName()) {
        if (current.getEOutput() != null && current.getEOutput().getName() != null)
        {
          if (current.getEOutput().getName().equals(candidate))
            unique = false;
        }
      }
      if (unique)
        name = candidate;
      else
        candidate = operationName + ending + i;
      i++;
    }
    return name;
  }

  /**
   * Return a name which is not used by any other part in the message.
   * @return String
   */
  public static String buildUniquePartName(Message message)
  {
  	List names = getUsedPartNames(message);

    // Now search the list until we find an unused name
    return getUniqueNameHelper("NewPart", names);
  }
  
  public static String buildUniquePartName(Message message, String baseName)
  {
  	if (baseName == null)
  	{
  		baseName = "NewPart";
  	}
  	
  	List names = getUsedPartNames(message);

    // Now search the list until we find an unused name
    return getUniqueNameHelper(baseName, names);
  }

  /**
   * Return a name which is not used by any other port type in the definition.
   * @return String
   */
  public static String buildUniquePortTypeName(Definition definition, String baseName)
  {
    if (baseName == null)
    {
      baseName = "NewPortType";
    }

    List names = getUsedPortTypeNames(definition);

    // Now search the list until we find an unused name
    return getUniqueNameHelper(baseName, names);
  }

  public static String getUniqueNameHelper(String baseName, List names)
  {
    int i = 0;

    String name = baseName;
    while (true)
    {
      if (!names.contains(name))
      {
        break;
      }
      i++;
      name = baseName + i;
    }

    return name;
  }

  /**
   * Return a name which is not used by any other service in the definition.
   * @return String
   */
  public static String buildUniqueServiceName(Definition definition)
  {
  	List names = getUsedServiceNames(definition);

    // Now search the list until we find an unused name
    return getUniqueNameHelper("NewService", names);
  }

  /**
   * Return a name which is not used by any other binding in the definition.
   * @return String
   */
  public static String buildUniqueBindingName(Definition definition, String baseName)
  {
    if (baseName == null)
    {
      baseName = "NewBinding";
    }

    List names = getUsedBindingNames(definition);

    return getUniqueNameHelper(baseName, names);
  }

  public static String buildUniquePrefix(Definition definition, String basePrefix)
  {
    String prefix = basePrefix;
    for (int i = 1; definition.getNamespace(prefix) != null; i++)
    {
      prefix = basePrefix + i;
    }
    return prefix;
  }

  public static String buildUniquePortName(Service service, String baseName)
  {
    if (baseName == null)
    {
      baseName = "NewPort";
    }
  
    List names = getUsedPortNames(service);
    
	return getUniqueNameHelper(baseName, names);
  }
	
  public static String buildUniqueMessageName(Definition definition, MessageReference messRef)
  {   
    String name = null;
    if (messRef instanceof Input)
    {
      name = createOperationName(messRef, "Request");    
    }
    else if (messRef instanceof Output)
    {
      name = createOperationName(messRef, "Response"); 
    }
    else if (messRef instanceof Fault)
    {                                
      String faultName = ((Fault) messRef).getName();
      if (faultName == null || faultName.length() == 0)
      {                     
        faultName = "Fault";
      }
      name = createOperationName(messRef, faultName); 
    }                                                                     

    return NameUtil.buildUniqueMessageName(definition, name);
  }
  
  
  public static List getUsedFaultNames(Operation operation) {
    ArrayList names = new ArrayList();
    for (Iterator i = operation.getEFaults().iterator(); i.hasNext();)
    {
      Fault fault = (Fault) i.next();
      names.add(fault.getName());
    }
    
    return names;
  }

  public static List getUsedOperationNames(PortType portType) {
    ArrayList names = new ArrayList();
    for (Iterator i = portType.getEOperations().iterator(); i.hasNext();)
    {
      Operation op = (Operation) i.next();
      names.add(op.getName());
    }
    
    return names;
  }
  
  public static List getUsedPartNames(Message message) {
    ArrayList names = new ArrayList();
    for (Iterator i = message.getEParts().iterator(); i.hasNext();)
    {
      Part part = (Part) i.next();
      names.add(part.getName());
    }    
    return names;
  }
  
  public static List getUsedPortTypeNames(Definition definition) {
    ArrayList names = new ArrayList();
    for (Iterator i = definition.getEPortTypes().iterator(); i.hasNext();)
    {
      PortType portType = (PortType) i.next();
      // TODO: port check
      //			if (portType.isSetQName())
      if (portType.getQName() != null)
      {
        names.add(portType.getQName().getLocalPart());
      }
    }
    
    return names;
    
  }
  public static List getUsedServiceNames(Definition definition) {
    // First build a list of names already used
    ArrayList names = new ArrayList();
    for (Iterator i = definition.getEServices().iterator(); i.hasNext();)
    {
      Service service = (Service) i.next();
      // TODO: port check
      // 		if(service.isSetQName())
      if (service.getQName() != null)
        names.add(service.getQName().getLocalPart());
    }
    
    return names;
  }
  
  public static List getUsedMessageNames(Definition definition) {
    ArrayList names = new ArrayList();
    for (Iterator i = definition.getEMessages().iterator(); i.hasNext();)
    {
      Message msg = (Message) i.next();
      // TODO: port check
      if (msg.getQName() != null)
        //			if(msg.isSetQName())
        names.add(msg.getQName().getLocalPart());
    }
    
    return names;
  }

  public static List getUsedBindingNames(Definition definition) {
    ArrayList names = new ArrayList();
    for (Iterator i = definition.getEBindings().iterator(); i.hasNext();)
    {
      Binding binding = (Binding) i.next();
      // TODO: port check
      //			if (binding.isSetQName())
      if (binding.getQName() != null)
      {
        names.add(binding.getQName().getLocalPart());
      }
    }
    
    return names;
  }

  public static List getUsedPortNames(Service service) {
    // First build a list of names already used
    ArrayList names = new ArrayList();
    for (Iterator i = service.getEPorts().iterator(); i.hasNext();)
    {
      Port port = (Port) i.next();

      if (port.getName() != null)
      {
        names.add(port.getName());
      }
    }
    
    return names;
  }
  
  private static String createOperationName(Object object, String suffix)
  {               
    String result = null;
    if (object instanceof EObject)
    {
      EObject parent = ((EObject)object).eContainer();
      if (parent instanceof Operation)
      {
        result = ((Operation)parent).getName();
      }
    } 
    if (result != null)
    {
      result += suffix;
    }
    return result;
  }
}
