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

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObjectBaseTestCase;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.GetComplete;

/**
 * Test case for creating, marshalling, and unmarshalling
 * {@link org.opensaml.saml2.core.impl.GetCompleteImpl}.
 */
public class GetCompleteTest extends SAMLObjectBaseTestCase {
    
    /** Expected element content*/
    private String expectedGetComplete;

    /**
     * Constructor
     */
    public GetCompleteTest() {
        super();
        
       singleElementFile = "/data/org/opensaml/saml2/core/impl/GetComplete.xml";
    }

    /**
     * @see org.opensaml.common.SAMLObjectBaseTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        expectedGetComplete = "http://sp.example.org/idplist.xml";
    }

    /**
     * @see org.opensaml.common.SAMLObjectBaseTestCase#testSingleElementUnmarshall()
     */
    public void testSingleElementUnmarshall() {
        GetComplete gc = (GetComplete) unmarshallElement(singleElementFile);
        
       assertEquals("Unmarshalled GetComplete URI was not the expected value", expectedGetComplete, gc.getGetComplete()); 

    }

    /**
     * @see org.opensaml.common.SAMLObjectBaseTestCase#testSingleElementMarshall()
     */
    public void testSingleElementMarshall() {
        QName qname = new QName(SAMLConstants.SAML20P_NS, GetComplete.LOCAL_NAME);
        GetComplete gc = (GetComplete) buildSAMLObject(qname);

        gc.setGetComplete(expectedGetComplete);
        
        assertEquals(expectedDOM, gc);
    }
}