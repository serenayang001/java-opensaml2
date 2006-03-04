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

package org.opensaml.saml2.metadata.validator;

import org.opensaml.common.SAMLObjectValidatorBaseTestCase;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.xml.validation.ValidationException;

/**
 * Test case for {@link org.opensaml.saml2.metadata.RoleDescriptor}.
 */
public abstract class RoleDescriptorSchemaTest extends SAMLObjectValidatorBaseTestCase {

    /** Constructor */
    public RoleDescriptorSchemaTest() {
 
    }
 
    /*
     * @see org.opensaml.common.SAMLObjectValidatorBaseTestCase#populateRequiredData()
     */
    protected void populateRequiredData() {
        RoleDescriptor roleDescriptor = (RoleDescriptor) target;

        roleDescriptor.addSupportedProtocol("protocol");
    }


    /**
     * Tests for Protocol failure.
     * 
     * @throws ValidationException
     */
    public void testProtocol() throws ValidationException {
        RoleDescriptor roleDescriptor = (RoleDescriptor) target;

        roleDescriptor.removeSupportedProtocol("protocol");
        assertValidationFail("Protocols list was empty, should raise a Validation Exception.");
    }
}