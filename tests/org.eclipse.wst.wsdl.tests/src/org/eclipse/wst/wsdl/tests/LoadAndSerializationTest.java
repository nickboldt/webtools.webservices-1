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
package org.eclipse.wst.wsdl.tests;

import java.io.File;
import java.io.FileFilter;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;

import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.wst.wsdl.tests.util.XMLDiff;

import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.w3c.dom.Element;

/**
 * @author Kihup Boo
 */
public class LoadAndSerializationTest extends TestCase
{ 
  {	    
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl());
    WSDLPackage pkg = WSDLPackage.eINSTANCE;
	    
    // We need this for XSD <import>.
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());
    XSDPackage xsdpkg = XSDPackage.eINSTANCE; 
  }
  
  private String PLUGIN_ABSOLUTE_PATH = WSDLTestsPlugin.getInstallURL();
  static private File[] wsdls;
  static private Definition definition = null;
	
  /**
   * Executes a stand-alone test.
   * @param objects an array of Strings from the command line.
   * @see #run
   */
  public static void main(String args[]) 
  {
    junit.textui.TestRunner.run(suite());
  }
  
  public LoadAndSerializationTest(String name) 
  {
    super(name);
  }
  
  public static Test suite() 
  {
    TestSuite suite = new TestSuite();
    suite.addTest
    (new LoadAndSerializationTest("LoadAndStore") 
      {
        protected void runTest() 
        {
          testLoadAndStore();
        }
      }
    );
    /* suite.addTest // wtp bug 79326
    (new LoadAndSerializationTest("Compare") 
      {
        protected void runTest() 
        {
          testCompare();
        }
      }
    ); */
    return suite;
  } 

  /**
   * Load from the WSDL definitions file and store back to a different file.
   */
  public void testLoadAndStore()
  {
    File dir = new File(PLUGIN_ABSOLUTE_PATH + "samples");
    if (dir.exists() && dir.isDirectory())
    {
      wsdls = dir.listFiles
      (
        new FileFilter()
        {
          public boolean accept(File pathname)
          {
            return pathname.getName().endsWith(".wsdl");
          }
        }
      );

      try 
      {
        for (int i = 0; i < wsdls.length; i++)
        {
          System.out.println(wsdls[i].toURL().toString());
          load(wsdls[i].toString());
          print(wsdls[i].toString() + ".out");
        }
      }
      catch (Exception e)
      {
        Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
      }
    }
    else
      fail(dir.toString());

  }
  
  /*
   * Load from the WSDL definitions file.
   */
  private void load(String filename)
  {	
    try
    {
      definition = DefinitionLoader.load(filename);
	  Assert.assertNotNull(definition);
      Assert.assertTrue(definition.eResource() instanceof WSDLResourceImpl);
	  
      WSDLResourceImpl wsdlResource = (WSDLResourceImpl)definition.eResource();
	  Assert.assertNotNull(wsdlResource);

	  Element element = definition.getElement();
      Assert.assertNotNull(definition.getElement());
	  
      WSDLResourceImpl.serialize(System.out, element);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }  
  }
  
  /*
   * Store the WSDL definitions to a file.
   */  
  private void print(String filename)
  {
    try
    {
      // This removes the associated DOM element, creates a new associated DOM element, and then prints it.
      // This is a good test for how well serialization works for a model created "bottom up".
      //
      definition.setDocument(null);
      definition.setElement(null);
      ((DefinitionImpl)definition).updateElement();

	  DefinitionLoader.store(definition,filename);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }   
  }
  
  /**
   * Compare the output WSDL file to the original.
   */  
  public void testCompare()
  {
    XMLDiff xmldiff = new XMLDiff();
    try
    {
      for (int i = 0; i < wsdls.length; i++)
        Assert.assertTrue(xmldiff.diff(wsdls[i].toString(),wsdls[i].toString() + ".out")); 
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }      
}
