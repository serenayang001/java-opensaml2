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

import org.opensaml.common.SAMLObject;
import org.opensaml.common.impl.UnknownAttributeException;
import org.opensaml.common.impl.UnknownElementException;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.xml.io.UnmarshallingException;

/**
 * A thread-safe {@link org.opensaml.common.io.Unmarshaller} for {@link org.opensaml.saml2.core.AttributeQuery}.
 */
public class AttributeQueryUnmarshaller extends SubjectQueryUnmarshaller {

    /**
     * Constructor
     *
     */
    public AttributeQueryUnmarshaller() {
        super(SAMLConstants.SAML20P_NS, AttributeQuery.LOCAL_NAME);
    }

    /**
     * @see org.opensaml.saml2.core.impl.SubjectQueryUnmarshaller#processAttribute(org.opensaml.common.SAMLObject, java.lang.String, java.lang.String)
     */
    protected void processAttribute(SAMLObject samlObject, String attributeName, String attributeValue) throws UnmarshallingException, UnknownAttributeException {
        // no attributes of our own
        super.processAttribute(samlObject, attributeName, attributeValue);
    }

    /**
     * @see org.opensaml.saml2.core.impl.SubjectQueryUnmarshaller#processChildElement(org.opensaml.common.SAMLObject, org.opensaml.common.SAMLObject)
     */
    protected void processChildElement(SAMLObject parentSAMLObject, SAMLObject childSAMLObject) throws UnmarshallingException, UnknownElementException {
        AttributeQuery query = (AttributeQuery) parentSAMLObject;
        
        if (childSAMLObject instanceof Attribute)
            query.getAttributes().add((Attribute) childSAMLObject);
        else
            super.processChildElement(parentSAMLObject, childSAMLObject);
    }
    
    

}
