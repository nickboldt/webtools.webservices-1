/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.core.command;


import java.io.File;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.axis.tools.ant.wsdl.Java2WsdlAntTask;
import org.apache.axis.tools.ant.wsdl.NamespaceMapping;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


/**
 * Commands are executable, undoable, redoable objects.
 * Every Command has a name and a description.
 */

public class Java2WSDLCommand extends SimpleCommand {

	private JavaWSDLParameter javaWSDLParam_;
	private ResourceBundle resource = ResourceBundle.getBundle("org.eclipse.jst.ws.axis.consumption.core.consumption"); //$NON-NLS-1$
	
	private String LABEL = "TASK_LABEL_JAVA_WSDL_COMMAND";
	private String DESCRIPTION = "TASK_DESC_JAVA_WSDL_COMMAND";

	public Java2WSDLCommand() {
		super();
	    setName (getMessage(LABEL));
		setDescription(getMessage(DESCRIPTION));
	}	
	/**
	 * Constructor for Java2WSDLCommand.
	 */
	public Java2WSDLCommand(JavaWSDLParameter javaWSDLParam) {
		super();
	    setName (getMessage(LABEL));
		setDescription(getMessage(DESCRIPTION));
		this.javaWSDLParam_ = javaWSDLParam;
	}

	public Status execute(Environment environment) {
		Status status;
		if (javaWSDLParam_ == null) {
			status = new SimpleStatus("Java2WSDLCommand", //$NON-NLS-1$
			getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR);
			environment.getStatusHandler().reportError(status);
			return status;
		}

		if (javaWSDLParam_.getBeanName() == null) {
			status = new SimpleStatus("Java2WSDLCommand", //$NON-NLS-1$
			getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR);
			environment.getStatusHandler().reportError(status);
			return status;
		}

		environment.getProgressMonitor().report(
			getMessage("MSG_GENERATE_WSDL", javaWSDLParam_.getBeanName() ) );

		return executeAntTask(environment);
	}

	protected Status executeAntTask(Environment environment) {

		final class Emitter extends Java2WsdlAntTask {
			public Emitter() {
				project = new Project();
				project.init();
				taskType = "axis"; //$NON-NLS-1$
				taskName = "axis-java2wsdl"; //$NON-NLS-1$
				target = new Target();
			}
		}

		Emitter emitter = new Emitter();
		emitter.createClasspath().setPath(javaWSDLParam_.getClasspath());
		environment.getLog().log(Log.INFO, 5008, this, "executeAntTask", "Class Path = "+ javaWSDLParam_.getClasspath());
		
		emitter.setPortTypeName(javaWSDLParam_.getPortTypeName());
		environment.getLog().log(Log.INFO, 5009, this, "executeAntTask", "Port Type Name = "+ javaWSDLParam_.getPortTypeName());
		
		emitter.setServiceElementName(javaWSDLParam_.getServiceName());
		environment.getLog().log(Log.INFO, 5010, this, "executeAntTask", "Service Name = "+ javaWSDLParam_.getServiceName());
		
		emitter.setLocation(javaWSDLParam_.getUrlLocation());
		environment.getLog().log(Log.INFO, 5011, this, "executeAntTask", "URL Location = "+ javaWSDLParam_.getUrlLocation());
		
		emitter.setMethods(javaWSDLParam_.getMethodString());
		environment.getLog().log(Log.INFO, 5012, this, "executeAntTask", "Methods = "+ javaWSDLParam_.getMethodString());
		
		emitter.setStyle(javaWSDLParam_.getStyle());
		environment.getLog().log(Log.INFO, 5013, this, "executeAntTask", "Style = "+ javaWSDLParam_.getStyle());
		
		emitter.setUse(javaWSDLParam_.getUse());
		environment.getLog().log(Log.INFO, 5014, this, "executeAntTask", "Use = "+ javaWSDLParam_.getUse());
		
		emitter.setOutput(new File(javaWSDLParam_.getOutputWsdlLocation()));
		environment.getLog().log(Log.INFO, 5015, this, "executeAntTask", "WSDL Location = "+ javaWSDLParam_.getOutputWsdlLocation());
		
		emitter.setNamespace(javaWSDLParam_.getNamespace());
		environment.getLog().log(Log.INFO, 5016, this, "executeAntTask", "Name Space = "+ javaWSDLParam_.getNamespace());
		
		emitter.setClassName(javaWSDLParam_.getBeanName());
		environment.getLog().log(Log.INFO, 5017, this, "executeAntTask", "Bean name = "+ javaWSDLParam_.getBeanName());
		
		emitter.setImplClass(javaWSDLParam_.getBeanName());
		
		HashMap mappings = javaWSDLParam_.getMappings();
		if(mappings != null){
			Iterator keys = mappings.keySet().iterator();
			while(keys.hasNext()){
				String pakage = (String)keys.next();
				String namespace = (String)mappings.get(pakage);
				NamespaceMapping map = new NamespaceMapping();
				map.setPackage(pakage);
				map.setNamespace(namespace);
				emitter.addMapping(map);		
			}
		}
		
	
		try {
			emitter.execute();
		} catch (BuildException e) {
			environment.getLog().log(Log.ERROR, 5018, this, "executeAntTask", e);
			Status status = new SimpleStatus("Java2WSDLCommand", //$NON-NLS-1$
			getMessage("MSG_ERROR_JAVA_WSDL_GENERATE") + " " //$NON-NLS-1$
			+e.getCause().toString(), Status.ERROR);
			environment.getStatusHandler().reportError(status);
			return status;
		}
		return new SimpleStatus( "" );

	}
	
	public Status undo(Environment environment) {
		return null;
	}

	public Status redo(Environment environment) {
		return null;
	}

	/**
	 * Returns the javaWSDLParam.
	 * @return JavaWSDLParameter
	 */
	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam_;
	}

	private String getMessage(String messageId, String parm1) {
		String message = resource.getString(messageId);
		return MessageFormat.format(message, new String[] { parm1 });
	}

	/**
	* Returns the message string identified by the given key from
	* plugin.properties.
	* @return The String message.
	*/
	public String getMessage(String key) {
		return resource.getString(key);
	}

	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParm) {
		this.javaWSDLParam_ = javaWSDLParm;
	}
	
	public String getWsdlURI()
	{
	  File file = new File(getJavaWSDLParam().getOutputWsdlLocation()); 
	    String url = "";
		  try {
	    	url = file.toURL().toString();
	    }
		  catch(MalformedURLException mue){}
		return url;
	}
}
