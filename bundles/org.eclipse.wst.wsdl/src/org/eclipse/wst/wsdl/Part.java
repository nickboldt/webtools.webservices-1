/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.wst.wsdl;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Part</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class represents a WSDL part element. Parts describe the logical abstract content of a message. Each part is associated with a type from some type system. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.Part#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wsdl.Part#getElementName <em>Element Name</em>}</li>
 *   <li>{@link org.eclipse.wsdl.Part#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link org.eclipse.wsdl.Part#getTypeDefinition <em>Type Definition</em>}</li>
 *   <li>{@link org.eclipse.wsdl.Part#getElementDeclaration <em>Element Declaration</em>}</li>
 *   <li>{@link org.eclipse.wsdl.Part#getEMessage <em>EMessage</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wsdl.WSDLPackage#getPart()
 * @model 
 * @generated
 */
public interface Part extends WSDLElement, javax.wsdl.Part{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.wsdl.WSDLPackage#getPart_Name()
   * @model 
   * @generated
   */
	String getName();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.Part#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
	void setName(String value);

  /**
   * Returns the value of the '<em><b>Element Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Element Name</em>' attribute.
   * @see #setElementName(QName)
   * @see org.eclipse.wsdl.WSDLPackage#getPart_ElementName()
   * @model dataType="org.eclipse.wsdl.QName"
   * @generated
   */
	QName getElementName();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.Part#getElementName <em>Element Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Element Name</em>' attribute.
   * @see #getElementName()
   * @generated
   */
	void setElementName(QName value);

  /**
   * Returns the value of the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Type Name</em>' attribute.
   * @see #setTypeName(QName)
   * @see org.eclipse.wsdl.WSDLPackage#getPart_TypeName()
   * @model dataType="org.eclipse.wsdl.QName"
   * @generated
   */
	QName getTypeName();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.Part#getTypeName <em>Type Name</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type Name</em>' attribute.
   * @see #getTypeName()
   * @generated
   */
	void setTypeName(QName value);

  /**
   * Returns the value of the '<em><b>Type Definition</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type Definition</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type Definition</em>' reference.
   * @see #setTypeDefinition(XSDTypeDefinition)
   * @see org.eclipse.wsdl.WSDLPackage#getPart_TypeDefinition()
   * @model 
   * @generated
   */
  XSDTypeDefinition getTypeDefinition();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.Part#getTypeDefinition <em>Type Definition</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type Definition</em>' reference.
   * @see #getTypeDefinition()
   * @generated
   */
  void setTypeDefinition(XSDTypeDefinition value);

  /**
   * Returns the value of the '<em><b>Element Declaration</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Element Declaration</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Element Declaration</em>' reference.
   * @see #setElementDeclaration(XSDElementDeclaration)
   * @see org.eclipse.wsdl.WSDLPackage#getPart_ElementDeclaration()
   * @model 
   * @generated
   */
  XSDElementDeclaration getElementDeclaration();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.Part#getElementDeclaration <em>Element Declaration</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Element Declaration</em>' reference.
   * @see #getElementDeclaration()
   * @generated
   */
  void setElementDeclaration(XSDElementDeclaration value);

  /**
   * Returns the value of the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>EMessage</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>EMessage</em>' reference.
   * @see #setEMessage(Message)
   * @see org.eclipse.wsdl.WSDLPackage#getPart_EMessage()
   * @model 
   * @generated
   */
	Message getEMessage();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.Part#getEMessage <em>EMessage</em>}' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>EMessage</em>' reference.
   * @see #getEMessage()
   * @generated
   */
	void setEMessage(Message value);

} // Part
