/*
 * Copyright [2005] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 */

package org.opensaml.saml2.core.impl;

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.AssertionIDRef;
import org.opensaml.xml.XMLObject;

/**
 * A thead-safe Unmarshaller for {@link org.opensaml.saml2.core.AssertionIDRef}
 */
public class AssertionIDRefUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** Constructor */
    public AssertionIDRefUnmarshaller() {
        super(SAMLConstants.SAML20_NS, AssertionIDRef.DEFAULT_ELEMENT_LOCAL_NAME);
    }

    /**
     * Constructor
     * 
     * @param namespaceURI
     * @param elementLocalName
     */
    protected AssertionIDRefUnmarshaller(String namespaceURI, String elementLocalName) {
        super(namespaceURI, elementLocalName);
    }

    /*
     * @see org.opensaml.xml.io.AbstractXMLObjectUnmarshaller#processElementContent(org.opensaml.xml.XMLObject,
     *      java.lang.String)
     */
    protected void processElementContent(XMLObject samlObject, String elementContent) {
        AssertionIDRef assertionIDRef = (AssertionIDRef) samlObject;
        assertionIDRef.setAssertionID(elementContent);
    }
}