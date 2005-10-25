package org.eclipse.jst.ws.internal.consumption.ui.wsrt;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceRuntime;


public class ServiceRuntimeDescriptor
{
  private IConfigurationElement elem;
  private Hashtable allWebServiceImpls;
  private Hashtable allRuntimes;
  private String id;
  private WebServiceImpl serviceImplementationType;
  private RuntimeDescriptor runtime;
  private boolean bottomUp = false;
  private boolean topDown = false;
  private String serviceRuntimeClassName;
  private IWebServiceRuntime webServiceRuntime;
  private RequiredFacetVersion[] requiredFacetVersions;
  private Set projectFacetVersions;
  
  public ServiceRuntimeDescriptor(IConfigurationElement elem, Hashtable allWebServiceImpls, Hashtable allRuntimes)
  {
    this.elem = elem;
    this.allWebServiceImpls = allWebServiceImpls;
    this.allRuntimes = allRuntimes;
    
    System.out.println("bottomUp = "+elem.getAttribute("bottomUp"));
    System.out.println("bottomUp = "+elem.getAttribute("topDown"));

    bottomUp = (Boolean.valueOf(elem.getAttribute("bottomUp"))).booleanValue();
    topDown = (Boolean.valueOf(elem.getAttribute("topDown"))).booleanValue();    
  }
  
  public boolean getBottomUp()
  {    
    return bottomUp;
  }
  
  public String getId()
  {
    if (id == null)
    {
      id = elem.getAttribute("id");
    }
    return id;
  }
  
  
  public RequiredFacetVersion[] getRequiredFacetVersions()
  {
    if (requiredFacetVersions == null)
    {
      ArrayList requiredFacetVersionList = new ArrayList();
      IConfigurationElement[] facetElems = elem.getChildren("required-facet-version");
      for (int i = 0; i < facetElems.length; i++)
      {
        RequiredFacetVersion rfv = new RequiredFacetVersion();
        IProjectFacet projectFacet = ProjectFacetsManager.getProjectFacet(facetElems[i].getAttribute("facet"));        
        IProjectFacetVersion projectFacetVersion = projectFacet.getVersion(facetElems[i].getAttribute("version"));
        rfv.setProjectFacetVersion(projectFacetVersion);
        String allowNewerValue = facetElems[i].getAttribute("allow-newer");
        if (allowNewerValue == null)
        {
          rfv.setAllowNewer(false);
        }
        else
        {
          rfv.setAllowNewer(Boolean.valueOf(allowNewerValue).booleanValue());
        }
        
        requiredFacetVersionList.add(rfv);
      }
      
      requiredFacetVersions = (RequiredFacetVersion[])requiredFacetVersionList.toArray(new RequiredFacetVersion[]{});
    }
    
    return requiredFacetVersions;
  }
  
  public Set getProjectFacetVersions()
  {
    if (projectFacetVersions == null)
    {
     projectFacetVersions = new HashSet();
     RequiredFacetVersion[] rfv = getRequiredFacetVersions();
     for (int i=0; i<rfv.length; i++)
     {
       projectFacetVersions.add(rfv[i].getProjectFacetVersion());
     }
    }
    
    return projectFacetVersions;    
  }
  
  public RuntimeDescriptor getRuntime()
  {
    if (runtime == null)
    {
     String runtimeId = elem.getAttribute("runtimeId");
     runtime = (RuntimeDescriptor)allRuntimes.get(runtimeId);     
    }
    return runtime;
  }
  
  public WebServiceImpl getServiceImplementationType()
  {
    if (serviceImplementationType == null)
    {      
      String serviceImplementationTypeId = elem.getAttribute("serviceImplementationTypeId");
      serviceImplementationType = (WebServiceImpl)allWebServiceImpls.get(serviceImplementationTypeId);
    }
    return serviceImplementationType;
  }
  
  public String getServiceRuntimeClassName()
  {
    if (serviceRuntimeClassName == null)
    {
      serviceRuntimeClassName = elem.getAttribute("class");
    }
    return serviceRuntimeClassName;
  }
  
  public boolean getTopDown()
  {
    return topDown;
  }
  
  public IWebServiceRuntime getWebServiceRuntime()
  {
    if (webServiceRuntime == null)
    {
        try
        {
            webServiceRuntime = (IWebServiceRuntime)elem.createExecutableExtension("class");
        }
        catch(CoreException ce)
        {
            
        }
    }
    
    return webServiceRuntime;
  }  
}