/*
 * Copyright [2007] [University Corporation for Advanced Internet Development, Inc.]
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

package org.opensaml.security;

import java.util.List;

import org.opensaml.saml2.metadata.EncryptionMethod;
import org.opensaml.saml2.metadata.KeyDescriptor;
import org.opensaml.saml2.metadata.RoleDescriptor;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialContext;
import org.opensaml.xml.signature.KeyInfo;

/**
 * A credential context for credentials resolved from a {@link KeyInfo} that was found in SAML 2 metadata.
 */
public class SAMLMDCredentialContext extends KeyInfoCredentialContext {

    /** Role containing the KeyInfo resolution context. */
    private RoleDescriptor role;
    
    /** Role containing the KeyInfo resolution context. */
    private List<EncryptionMethod> encMethods;
    
    /** {@inheritDoc} */
    public void setKeyInfo(KeyInfo info) {
        super.setKeyInfo(info);
        if (info != null) {
            // KeyInfo -> KeyDescriptor / EncryptionMethod
            encMethods = ((KeyDescriptor) info.getParent()).getEncryptionMethods();
            // KeyInfo -> KeyDescriptor -> RoleDescriptor
            role = (RoleDescriptor) info.getParent().getParent();
        }
    }
    
    /**
     * Return the list of {@link EncryptionMethod}'s associated with credential context
     * 
     * @return a list of SAML metadata encryption method associated with this context
     */
    public List<EncryptionMethod> getEncryptionMethod() {
        return encMethods;
    }
    
    /**
     * Get the role descriptor context.
     * 
     * @return role descriptor
     */
    public RoleDescriptor getRoleDescriptor() {
        return role;
    }
    
    /**
     * Set the role descriptor context.
     * 
     * @param roleDescriptor the role descriptor context
     */
    public void setRoleDescriptor(RoleDescriptor roleDescriptor) {
        role = roleDescriptor;
    }
}