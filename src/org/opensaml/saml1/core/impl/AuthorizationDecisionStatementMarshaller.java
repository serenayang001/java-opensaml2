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
package org.opensaml.saml1.core.impl;

import org.opensaml.common.SAMLObject;
import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml1.core.AuthorizationDecisionStatement;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

/**
 *A thread safe {@link org.opensaml.common.io.Marshaller} for {@link org.opensaml.saml1.core.AuthorizationDecisionStatement} objects.
 */
public class AuthorizationDecisionStatementMarshaller extends AbstractSAMLObjectMarshaller {

    /**
     * Constructor
     */
    public AuthorizationDecisionStatementMarshaller() {
        super(SAMLConstants.SAML1_NS, AuthorizationDecisionStatement.LOCAL_NAME);
    }

    /*
     * @see org.opensaml.common.impl.AbstractSAMLObjectMarshaller#marshallAttributes(org.opensaml.common.SAMLObject, org.w3c.dom.Element)
     */
    @Override
    protected void marshallAttributes(SAMLObject samlElement, Element domElement) throws MarshallingException {
        AuthorizationDecisionStatement authorizationDecisionStatement;
        
        authorizationDecisionStatement = (AuthorizationDecisionStatement) samlElement;
        
        if (authorizationDecisionStatement.getResource() != null) {
            domElement.setAttribute(AuthorizationDecisionStatement.RESOURCE_ATTRIB_NAME,
                    authorizationDecisionStatement.getResource());
        }
 
        if (authorizationDecisionStatement.getDecision() != null) {
            domElement.setAttribute(AuthorizationDecisionStatement.DECISION_ATTRIB_NAME,
                    authorizationDecisionStatement.getDecision().toString());
        }
    }

}