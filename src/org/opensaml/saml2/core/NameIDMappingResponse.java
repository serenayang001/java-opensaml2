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

package org.opensaml.saml2.core;


/**
 * SAML 2.0 Core NameIDMappingResponse 
 */
public interface NameIDMappingResponse extends StatusResponse {
    
    /** Element local name */
    public static final String LOCAL_NAME = "NameIDMappingResponse";
    
    /**
     * Gets the name identifier of the principal for this response.
     * 
     * @return the name identifier of the principal for this response
     */
    public NameID getNameID();
    
    /**
     * Sets the name identifier of the principal for this response.
     * 
     * @param newNameID the name identifier of the principal for this response
     */
    public void setNameID(NameID newNameID);
 
    // TODO EncryptedID
}