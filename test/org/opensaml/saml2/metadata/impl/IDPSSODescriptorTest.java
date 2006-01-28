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

package org.opensaml.saml2.metadata.impl;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObjectBaseTestCase;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.metadata.IDPSSODescriptor;

public class IDPSSODescriptorTest extends SAMLObjectBaseTestCase {

    /** expected value for WantAuthnRequestSigned attribute */
    protected Boolean expectedWantAuthnReqSigned;
    
    /**
     * Constructor
     */
    public IDPSSODescriptorTest(){
        singleElementFile = "/data/org/opensaml/saml2/metadata/impl/IDPSSODescriptor.xml";
        singleElementOptionalAttributesFile = "/data/org/opensaml/saml2/metadata/impl/IDPSSODescriptorOptionalAttributes.xml";
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        
        expectedWantAuthnReqSigned = Boolean.TRUE;
    }

    public void testSingleElementUnmarshall() {
        IDPSSODescriptor descriptor = (IDPSSODescriptor) unmarshallElement(singleElementFile);
        
    }

    public void testSingleElementOptionalAttributesUnmarshall() {
        IDPSSODescriptor descriptor = (IDPSSODescriptor) unmarshallElement(singleElementOptionalAttributesFile);
        
        assertEquals("WantAuthnRequestsSigned attribute was not expected value", expectedWantAuthnReqSigned, descriptor.wantAuthnRequestsSigned());
    }

    public void testSingleElementMarshall() {
        QName qname = new QName(SAMLConstants.SAML20MD_NS, IDPSSODescriptor.LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);
        IDPSSODescriptor descriptor = (IDPSSODescriptor) buildSAMLObject(qname);
        
        assertEquals(expectedDOM, descriptor);
    }

    public void testSingleElementOptionalAttributesMarshall() {
        QName qname = new QName(SAMLConstants.SAML20MD_NS, IDPSSODescriptor.LOCAL_NAME, SAMLConstants.SAML20MD_PREFIX);
        IDPSSODescriptor descriptor = (IDPSSODescriptor) buildSAMLObject(qname);
        
        descriptor.setWantAuthnRequestSigned(expectedWantAuthnReqSigned);
        
        assertEquals(expectedOptionalAttributesDOM, descriptor);
    }
}