/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.widgets.runtime;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.ui.common.ServerSelectionUtils;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.jst.ws.internal.ui.common.UIUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.command.internal.env.ui.widgets.SimpleWidgetDataContributor;
import org.eclipse.wst.command.internal.env.ui.widgets.WidgetDataEvents;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.provisional.env.core.selection.SelectionListChoices;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IRuntime;

public class ProjectSelectionWidget extends SimpleWidgetDataContributor {

  private final String EAR_PERMITTED_PROJECT_TYPE = "EAR_PERMITTED_PROJECT_TYPE";
  //private final String JAVA_PROJECT_TYPE_ID = "org.eclipse.jst.ws.consumption.ui.clientProjectType.Containerless";
  
  private String pluginId_ = "org.eclipse.jst.ws.consumption.ui";

  private SelectionListChoices projects_;
  
  private boolean needEAR_;
  
  private TypeRuntimeServer trsIds_;
  
  private String j2eeVersion_;
  
  private String projectTypeId_ = EAR_PERMITTED_PROJECT_TYPE;

  private Listener statusListener_;

  private Text messageText_;

  private boolean isClient_ = false;

  private MessageUtils msgUtils;

  private byte CREATE_PROJECT = (byte) 1;

  private byte CREATE_EAR = (byte) 2;

  private byte ADD_EAR_ASSOCIATION = (byte) 4;
  
  private Combo moduleProject_;
  private Combo earProject_;
  private String   componentType_;
  
  private ModifyListener moduleProjectListener_;
  private ModifyListener moduleListener_;
  private ModifyListener earProjectListener_;
  private ModifyListener earModuleListener_;

  private String initialModuleName_;
  
  /*
   * CONTEXT_ID PWRS0006 for the service-side Web project combo box of the
   * runtime selection Page
   */
  private String INFOPOP_PWRS_COMBO_PROJECT = pluginId_ + ".PWRS0006";

  private Combo module_;

  /* CONTEXT_ID PWRS0012 for the EAR combo box of the runtime selection Page */
  private String INFOPOP_PWRS_COMBO_EAR = pluginId_ + ".PWRS0012";

  private Combo earModule_;

  /*
   * Default Constructor
   */
  public ProjectSelectionWidget() {
  }

  /*
   * ProjectSelectionWidget @param isClient
   */
  public ProjectSelectionWidget(boolean isClient) {
    this.isClient_ = isClient;
  }

  public WidgetDataEvents addControls(Composite parent, Listener statusListener) {
    msgUtils = new MessageUtils(pluginId_ + ".plugin", this);
    UIUtils uiUtils = new UIUtils(msgUtils, pluginId_);

    statusListener_ = statusListener;
		
    if (isClient_)
	{
	  moduleProject_ = uiUtils.createCombo(parent, "LABEL_CLIENT_PROJECT", "LABEL_CLIENT_PROJECT", INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER );
      module_ = uiUtils.createCombo(parent, "LABEL_CLIENT_MODULE", "LABEL_CLIENT_MODULE", INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER );
	  earProject_ = uiUtils.createCombo(parent, "LABEL_CLIENT_EAR_PROJECT", "LABEL_CLIENT_EAR_PROJECT", INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER );
	  earModule_ = uiUtils.createCombo(parent, "LABEL_CLIENT_EAR_MODULE", "LABEL_CLIENT_EAR_MODULE", INFOPOP_PWRS_COMBO_EAR, SWT.SINGLE | SWT.BORDER );
    }
    else 
	{
	  moduleProject_ = uiUtils.createCombo(parent, "LABEL_SERVICE_PROJECT", "LABEL_SERVICE_PROJECT", INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER );
      module_ = uiUtils.createCombo(parent, "LABEL_SERVICE_MODULE", "LABEL_SERVICE_MODULE", INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER );
	  earProject_ = uiUtils.createCombo(parent, "LABEL_SERVICE_EAR_PROJECT", "LABEL_SERVICE_EAR_PROJECT", INFOPOP_PWRS_COMBO_PROJECT, SWT.SINGLE | SWT.BORDER );
	  earModule_ = uiUtils.createCombo(parent, "LABEL_SERVICE_EAR_MODULE", "LABEL_SERVICE_EAR_MODULE", INFOPOP_PWRS_COMBO_EAR, SWT.SINGLE | SWT.BORDER );
    }
	
    //module_.addModifyListener(projectListener_);
	
    //earModule_.addModifyListener(earListener_);
    //earModule_.addListener(SWT.Modify, statusListener);
	
	moduleProjectListener_ = new ModifyListener()
	                         {
	                           public void modifyText(ModifyEvent evt) 
					           {
					             handleModuleProjectChanged(null);
					             statusListener_.handleEvent( null );
		                       }
	                         };

	moduleListener_ = new ModifyListener()
                      {
                        public void modifyText(ModifyEvent evt) 
			            {
			              handleModuleChanged();
				          statusListener_.handleEvent( null );
	                    }
                      };
	
	earProjectListener_ = new ModifyListener()
                          {
                            public void modifyText(ModifyEvent evt) 
				            {
				              handleEarProjectChanged();
				              statusListener_.handleEvent( null );
	                        }
                          };
                          
    earModuleListener_ = new ModifyListener()
                             {
                               public void modifyText(ModifyEvent evt) 
				               {
				                 statusListener_.handleEvent( null );
	                           }
                             };
							  
    // message area
    messageText_ = uiUtils.createText(parent, "LABEL_NO_LABEL", "LABEL_NO_LABEL", null, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
  
    return this;
  }
  
  private void listenersOn()
  {
	module_.addModifyListener( moduleListener_ );
	moduleProject_.addModifyListener( moduleProjectListener_ );
	earProject_.addModifyListener( earProjectListener_ );
	earModule_.addModifyListener( earModuleListener_ );
  }
  
  private void listenersOff()
  {
    module_.removeModifyListener( moduleListener_ );
	moduleProject_.removeModifyListener( moduleProjectListener_ );
	earProject_.removeModifyListener( earProjectListener_ );
	earModule_.removeModifyListener( earModuleListener_ );
  }
  
  private void handleModuleProjectChanged(String moduleName)
  {
	String   projectName = moduleProject_.getText(); 
	IProject project     = null;
	
	if( projectName.equals( "" ) ) 
	{
	  module_.setItems( new String[0] );
	  
	  return;
	}
			
	project = ProjectUtilities.getProject( projectName );
	
	
	IVirtualComponent[] components = J2EEUtils.getComponentsByType( project, componentType_ );
	String[] modules = new String[components.length];
	
	for( int index = 0; index < components.length; index++ )
	{
	  modules[index] = components[index].getName();	
	}
	
	module_.setItems( modules );
	
	  if( modules.length > 0 )
	  {
      if (moduleName != null)
      {
       module_.setText(moduleName); 
      }
      else
      {
	      module_.setText( modules[0] );
      }
       
	  }
	  else
	  {
	    module_.setText( projectName );
	  }
  
  }
  
  private void handleModuleChanged()
  {
	IVirtualComponent component = getEarModuleForModule();
	
	if( component != null )
	{
	  String earProject    = component.getProject().getName();
	  String componentName = component.getName();
	
      // This will cause the module list to be update via a listener.
	  earProject_.setText( earProject );  
	  earModule_.setText( componentName );
	}
	else
	{
	  earProject_.setText( "" );  
	  earModule_.setText( "" );
	}
	
	updateEAREnabledState();
  }

  private void handleEarProjectChanged()
  {
	
	String   projectName = earProject_.getText(); 
	
	if( projectName != null && !projectName.equals( "" ))
	{
	  IProject project     = ProjectUtilities.getProject( projectName );
	
	  IVirtualComponent[] components = J2EEUtils.getComponentsByType( project, J2EEUtils.EAR );
	  String[] earModules = new String[components.length];
	
	  for( int index = 0; index < components.length; index++ )
	  {
	    earModules[index] = components[index].getName();	
	  }
	
	  earModule_.setItems( earModules );
	
	  if( earModules.length > 0 )
	  {
	    earModule_.setText( earModules[0] );	
	  }
	  else
	  {
	    earModule_.setText( projectName );	
	  }
	}
	else
	{
	  earModule_.setItems( new String[0] );	
	}
  }
  
  private IVirtualComponent getEarModuleForModule()
  {
	String   projectName = moduleProject_.getText(); 
	
	if( projectName.equals( "" ) )
	{
	  return null;	
	}
	
	IProject project     = ProjectUtilities.getProject( projectName );
	
	IVirtualComponent[] components = J2EEUtils.getReferencingEARComponents( project );
	
	return components.length == 0 ? null : components[0];
  }
    
  public void setProjectChoices(SelectionListChoices projects) 
  {
	listenersOff();
	
    projects_ = projects;
	
	String   selectedModuleProject    = projects.getList().getSelection();
	String   selectedEarModuleProject = projects.getChoice().getList().getSelection();
	String[] projectNames             = getProjects();
	
	moduleProject_.setItems( projectNames );
	moduleProject_.setText( selectedModuleProject );
	earProject_.setItems( projectNames );
	earProject_.setText( selectedEarModuleProject );
		
	handleModuleProjectChanged(initialModuleName_);
  handleModuleChanged();
	updateEAREnabledState();	
    listenersOn();
  }

  public SelectionListChoices getProjectChoices() 
  {
    return projects_;
  }
  
  public String getComponentName()
  {
	return module_.getText();
  }
  
  public void setComponentName( String name )
  {
	listenersOff();
    module_.setText( name );
    initialModuleName_ = name;
	listenersOn();
  }
  
  public String getEarComponentName()
  {
    return earModule_.getText();
  }
  
  public void setEarComponentName( String name )
  { 
	listenersOff();
	earModule_.setText( name );
	listenersOn();
  }
  
  public void setComponentType( String type )
  {
	componentType_ = type;  
  }
  
  public String getComponentType( )
  {
	  return componentType_;
  }
  
  public boolean getNeedEAR()
  {
    return needEAR_;
  }
  
  public String getProjectName()
  {
    return moduleProject_.getText();  
  }
  
  public String getEarProjectName()
  {
	return earProject_.getText();  
  }
  
  public void setNeedEAR(boolean b)
  {
    needEAR_ = b;
  }    

  public void setTypeRuntimeServer(TypeRuntimeServer trs)
  {
    trsIds_ = trs;
	listenersOff();
    updateEAREnabledState();
    listenersOn();
  }
  
  public void setJ2EEVersion(String j2eeVersion)
  {
    j2eeVersion_ = j2eeVersion;
	listenersOff();
    updateEAREnabledState();
	listenersOn();
  }
  
  public void setProjectTypeId(String id)
  {
    projectTypeId_ = id;
  }
  
  private String[] getProjects()
  {
	IProject[] projects     = J2EEUtils.getAllFlexibleProjects();
	String[]   projectNames = new String[projects.length];
	
	for( int index = 0; index < projects.length; index++ )
	{
	  projectNames[index] = projects[index].getName();	
	}
	
	return projectNames;
  }
    
  private void updateEAREnabledState()
  {
    if (projects_ != null)
    {
      if(!projectNeedsEAR(moduleProject_.getText(), module_.getText()))
      {
	    earModule_.setEnabled(false);   
		earProject_.setEnabled(false);
        earModule_.setText("");
		earProject_.setText("");
        needEAR_ = false;
      }
      else
      {
        needEAR_ = true;
        earModule_.setEnabled(true);
		earProject_.setEnabled(true);
        populateEARCombos();
      }
    }
  }

  private void populateEARCombos()
  {
    earModule_.removeAll();
    earProject_.removeAll();
    String moduleName = module_.getText();
    IProject moduleProj = ResourcesPlugin.getWorkspace().getRoot().getProject(moduleProject_.getText());
    if (moduleProj.exists())
    {
      IVirtualComponent[] ears = J2EEUtils.getReferencingEARComponents(moduleProj);
      if (ears != null && ears.length > 0)
      {
        for (int i = 0; i < ears.length; i++)
        {
          earModule_.add(ears[i].getName());
          earProject_.add(ears[i].getProject().getName());
        }
        earModule_.select(0);
        earProject_.select(0);
        return;
      }
    }
    String earName = moduleName + "EAR";
    earModule_.setText(earName);
    earProject_.setText(earName);
  }

  private boolean projectNeedsEAR(String projectName, String componentName)
  {
    if (projectTypeId_.equals(IModuleConstants.JST_UTILITY_MODULE))
      return false;
    
    if (projectName == null || projectName.length()==0)
      return true;
    
    if (componentName == null || componentName.length()==0)
      return true;
    
    
    //IProject project = (IProject)((new StringToIProjectTransformer()).transform(projectName));
	IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectName);
  	if (project != null && project.exists())
  	{
  	  //Get the runtime target on the project
  	  IRuntime target = ServerSelectionUtils.getRuntimeTarget(projectName);
  	  String j2eeVersion = String.valueOf(J2EEVersionConstants.J2EE_1_4_ID);
  	  if (J2EEUtils.exists(project))
  	    j2eeVersion = String.valueOf(J2EEUtils.getJ2EEVersion(project));
  	
  	  
  	  if (target != null)
  	  {
  	  	if (!ServerUtils.isTargetValidForEAR(target.getRuntimeType().getId(),j2eeVersion))
  	  	{
          return false;
  	  	}
  	  		
  	  }
  	}
  	else
  	{
  		//Use the server type
  	    if (trsIds_ != null && trsIds_.getServerId() != null)
  	    {
  		  String targetId = ServerUtils.getRuntimeTargetIdFromFactoryId(trsIds_.getServerId());
  		  if (targetId!=null && targetId.length()>0)
  		  {
  		    if (!ServerUtils.isTargetValidForEAR(targetId,j2eeVersion_))
  		    {
  		      return false;
  	  	    }
  		  }
  	    }
  	}      	
  	
  	return true;    
  }
  
  private IStatus handleSetMessageText() {
    IStatus status = Status.OK_STATUS;
    try {
      byte result = (byte) 0;
      if (module_.getText().length() != 0 && earModule_.getText().length() != 0) {
        String projectText = moduleProject_.getText();
        String earText = earProject_.getText();
        IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectText);
        IProject ear = ResourceUtils.getWorkspaceRoot().getProject(earText);
        if (project != null) {
          if (!project.exists()) {
            result = (byte) (result | CREATE_PROJECT);
          }
          if (!ear.exists()) {
            result = (byte) (result | CREATE_EAR);
          }

          if (project.exists() && J2EEUtils.exists(project) && ear.exists() && J2EEUtils.exists(ear)) {
            if (!J2EEUtils.isComponentAssociated(ear, project)) result = (byte) (result | ADD_EAR_ASSOCIATION);
          }
        }
      }
      if (isClient_) {
        messageText_.setText(getValidationMessage(result, msgUtils.getMessage("MSG_CLIENT_SUB")));
      }
      else {
        messageText_.setText(getValidationMessage(result, msgUtils.getMessage("MSG_SERVICE_SUB")));
      }
    }
    catch (Exception e) {
      return StatusUtils.errorStatus( msgUtils.getMessage("PAGE_MSG_VALIDATION_INTERNAL_ERROR"), e );
    }
    return status;
  }
  
  private String getValidationMessage(byte result, String serviceOrClient) {
    String msg = null;
    switch (result) {
    case 0:
      return "";
    case 1:
    case 5:
      msg = "MSG_PROJECT_WILL_BE_CREATED";
      break;
    case 2:
    case 6:
      msg = "MSG_EAR_WILL_BE_CREATED";
      break;
    case 3:
    case 7:
      msg = "MSG_PROJECT_AND_EAR_CREATED";
      break;
    case 4:
      msg = "MSG_EAR_WILL_BE_ASSOCIATED";
      break;
    }
    return msg != null ? msgUtils.getMessage(msg, new Object[] { serviceOrClient}) : "";
  }

  public IStatus getStatus() 
  {
    IStatus finalStatus = Status.OK_STATUS;
    handleSetMessageText();
    String projectText = moduleProject_.getText();
    String earText = earProject_.getText();
    String moduleText = msgUtils.getMessage( "MSG_MODULE" );
    
    if (projectText==null || projectText.length()==0)
    {
      if (isClient_)
        return StatusUtils.errorStatus( msgUtils.getMessage("MSG_CLIENT_PROJECT_EMPTY", new String[]{""} ) );
      else
        return StatusUtils.errorStatus( msgUtils.getMessage("MSG_SERVICE_PROJECT_EMPTY", new String[]{""} ) );
    }
    
    if (needEAR_ && (earText==null || earText.length()==0))
    {
      if (isClient_)
        return StatusUtils.errorStatus( msgUtils.getMessage("MSG_CLIENT_EAR_EMPTY", new String[]{""} ) );
      else
        return StatusUtils.errorStatus( msgUtils.getMessage("MSG_SERVICE_EAR_EMPTY", new String[]{""} ) );      
    }
    
    if( module_ == null || module_.getText().length() == 0 ) 
    {
      if( isClient_ )
      {
        return StatusUtils.errorStatus( msgUtils.getMessage("MSG_CLIENT_PROJECT_EMPTY", new String[]{moduleText} ) );
      }
      else
      {
        return StatusUtils.errorStatus( msgUtils.getMessage("MSG_CLIENT_EAR_EMPTY", new String[]{moduleText} ) );        	
      }
    }
      
    if( needEAR_ && ( earModule_ == null || earModule_.getText().length() == 0 ) )
    {
      if( isClient_ )
      {
        return StatusUtils.errorStatus( msgUtils.getMessage("MSG_CLIENT_EAR_EMPTY", new String[]{moduleText} ) );
      }
      else
      {
        return StatusUtils.errorStatus( msgUtils.getMessage("MSG_SERVICE_EAR_EMPTY", new String[]{moduleText} ) );        	
      }  
    }
    
//  TODO:  Defect 107997 - Revisit prject and module creation logic
//    if( !FlexibleJavaProjectPreferenceUtil.getMultipleModulesPerProjectProp() )
//    {
//      IProject project = ResourceUtils.getWorkspaceRoot().getProject(projectText);
//        
//      if( project != null && project.exists() )
//      {
//    	String            moduleName = module_.getText();
//        IVirtualComponent component  = J2EEUtils.getVirtualComponent( project, moduleName );  
//        
//        if( !component.exists() && !moduleName.equals( projectText ) )
//        {
//          finalStatus = new SimpleStatus("",msgUtils.getMessage("MSG_MODULE_NAME_AND_PROJECT_NAME_NOT_THE_SAME" ),Status.ERROR);   	
//        }
//      }
//    }
    
    return finalStatus;
  }
}