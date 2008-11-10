/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.creation.core.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.resources.JavaResourceChangeListener;
import org.eclipse.jst.ws.internal.cxf.core.resources.WebContentChangeListener;
import org.eclipse.jst.ws.internal.cxf.core.utils.CommandLineUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.FileUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.JDTUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.LaunchUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.SpringUtils;
import org.eclipse.jst.ws.internal.cxf.core.utils.WSDLUtils;
import org.eclipse.jst.ws.internal.cxf.creation.core.CXFCreationCorePlugin;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

/**
 * Provides a wrapper around the <code>org.apache.cxf.tools.java2ws.JavaToWS</code> or the 
 * <code>org.apache.cxf.tools.java2wsdl.JavaToWSDL</code> command depending on the version
 * of CXF used.
 * 
 * @author sclarke
 */
public class Java2WSCommand extends AbstractDataModelOperation {
    private static String JAVA2WSDL_TOOL_CLASS_NAME = "org.apache.cxf.tools.java2wsdl.JavaToWSDL"; //$NON-NLS-1$
    private static String JAVA2WS_TOOL_CLASS_NAME = "org.apache.cxf.tools.java2ws.JavaToWS"; //$NON-NLS-1$
    private String CXF_TOOL_CLASS_NAME;

    private Java2WSDataModel model;
    private String projectName;
    
    private JavaResourceChangeListener javaResourceChangeListener;
    private WebContentChangeListener webContentChangeListener;
    
    public Java2WSCommand(Java2WSDataModel model) {
        this.model = model;
        projectName = model.getProjectName();
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        javaResourceChangeListener = new JavaResourceChangeListener(JDTUtils
                .getJavaProjectSourceDirectoryPath(projectName, model.getJavaStartingPoint()));
        webContentChangeListener = new WebContentChangeListener(projectName);

        ResourcesPlugin.getWorkspace().addResourceChangeListener(javaResourceChangeListener, 
                IResourceChangeEvent.POST_CHANGE);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(webContentChangeListener, 
                IResourceChangeEvent.POST_CHANGE);

        if (model.getCxfRuntimeVersion().compareTo(CXFCorePlugin.CXF_VERSION_2_1) >= 0) {
            CXF_TOOL_CLASS_NAME = JAVA2WS_TOOL_CLASS_NAME;
        } else {
            CXF_TOOL_CLASS_NAME = JAVA2WSDL_TOOL_CLASS_NAME;
        }

        String[] progArgs = CommandLineUtils.getJava2WSProgramArguments(model);

        try {
        	IProject project = FileUtils.getProject(projectName);
            IJavaProject javaProject = JDTUtils.getJavaProject(project);
            LaunchUtils.launch(javaProject, CXF_TOOL_CLASS_NAME, progArgs);
            FileUtils.copyJ2WFilesFromTmp(model);
            
            if (model.isGenerateWSDL()) {
                WSDLUtils.loadSpringConfigInformationFromWSDL((model));
            }
            
            if (model.getFullyQualifiedJavaClassName().trim().length() > 0) {
                SpringUtils.createJAXWSEndpoint(model);
            }
            
            FileUtils.refreshProject(project, monitor);
        } catch (CoreException ce) {
            status = ce.getStatus();
            CXFCreationCorePlugin.log(status);
        } catch (IOException ioe) {
            status = new Status(IStatus.ERROR, CXFCreationCorePlugin.PLUGIN_ID, ioe.getLocalizedMessage());
            CXFCreationCorePlugin.log(status);
        } 
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(javaResourceChangeListener);
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(webContentChangeListener);

        return status;
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        IStatus status = Status.OK_STATUS;
        List<IResource> changedResources = new ArrayList<IResource>();
        changedResources.addAll(javaResourceChangeListener.getChangedResources());
        changedResources.addAll(webContentChangeListener.getChangedResources());
        if (changedResources.size() > 0) {
            for (IResource resource : changedResources) {
                try {
                    resource.delete(true, monitor);
                } catch (CoreException ce) {
                    status = ce.getStatus();
                    CXFCreationCorePlugin.log(status);
                }
            }
        }
        return status;
    }
    
    public CXFDataModel getCXFDataModel() {
        return model;
    }
}
