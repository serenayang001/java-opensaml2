/*
 * Copyright [2006] [University Corporation for Advanced Internet Development, Inc.]
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

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.metadata.EncryptionMethod;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.security.CredentialUsageTypeEnumeration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.KeyInfo;
import org.w3c.dom.Attr;

/**
 * A thread-safe unmarshaller for {@link org.opensaml.saml2.metadata.KeyDescriptor}s.
 */
public class KeyDescriptorUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /**
     * Constructor
     */
    public KeyDescriptorUnmarshaller() {
        super(SAMLConstants.SAML20MD_NS, KeyDescriptor.DEFAULT_ELEMENT_LOCAL_NAME);
    }

    /**
     * Constructor
     * 
     * @param namespaceURI
     * @param elementLocalName
     */
    protected KeyDescriptorUnmarshaller(String namespaceURI, String elementLocalName) {
        super(namespaceURI, elementLocalName);
    }

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        KeyDescriptor keyDescriptor = (KeyDescriptor) parentSAMLObject;

        if (childSAMLObject instanceof KeyInfo) {
            keyDescriptor.setKeyInfo((KeyInfo) childSAMLObject);
        } else if (childSAMLObject instanceof EncryptionMethod) {
            keyDescriptor.getEncryptionMethods().add((EncryptionMethod) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }

    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        KeyDescriptor keyDescriptor = (KeyDescriptor) samlObject;

        if (attribute.getName().equals(KeyDescriptor.USE_ATTRIB_NAME)) {
            String attributeValue = attribute.getValue();
            if (attributeValue.equals("signing")) {
                keyDescriptor.setUse(CredentialUsageTypeEnumeration.SIGNING);
            } else if (attributeValue.equals("encryption")) {
                keyDescriptor.setUse(CredentialUsageTypeEnumeration.ENCRYPTION);
            } else {
                throw new UnmarshallingException("Unknown key usage type, " + attributeValue);
            }
        }

        super.processAttribute(samlObject, attribute);
    }
}