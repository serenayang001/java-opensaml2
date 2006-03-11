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

package org.opensaml.saml1.core.validator;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObjectValidatorBaseTestCase;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml1.core.Status;
import org.opensaml.saml1.core.StatusCode;
import org.opensaml.saml2.core.validator.StatusSchemaValidator;

/**
 * Test case for {@link org.opensaml.saml1.core.validator.StatusSchemaValidator}.
 */
public class StatusSchemaTest extends SAMLObjectValidatorBaseTestCase {

    /** Constructor */
    public StatusSchemaTest() {
        super();
        targetQName = new QName(SAMLConstants.SAML1P_NS, Status.LOCAL_NAME, SAMLConstants.SAML1P_PREFIX);
        validator = new StatusSchemaValidator();
    }

    /*
     * @see org.opensaml.common.SAMLObjectValidatorBaseTestCase#populateRequiredData()
     */
    protected void populateRequiredData() {
        super.populateRequiredData();

        Status status = (Status) target;
        QName qname = new QName(SAMLConstants.SAML1P_NS, StatusCode.LOCAL_NAME, SAMLConstants.SAML1P_PREFIX);
        status.setStatusCode((StatusCode)buildXMLObject(qname));
    }
    
    public void testMissingStatusCode(){
        Status status = (Status) target;
        status.setStatusCode(null);
        assertValidationFail("No StatusCode, should raise a Validation Exception");
    }
}