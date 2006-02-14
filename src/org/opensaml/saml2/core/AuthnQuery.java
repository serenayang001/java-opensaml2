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
package org.opensaml.saml2.core;

/**
 * SAML 2.0 AuthnQuery
 */
public interface AuthnQuery extends SubjectQuery {
    
    /** Element local name */
    public final static String LOCAL_NAME = "AuthnQuery";
    
    /** SessionIndex attribute name */
    public final static String SESSION_INDEX_ATTRIB_NAME = "SessionIndex";
    
    /**
     * Gets the SessionIndex of this request.
     * 
     * @return the SessionIndex of this request
     */
    public String getSessionIndex();
    
    /**
     * Sets the SessionIndex of this request.
     * 
     * @param newSessionIndex the SessionIndex of this request
     */
    public void setSessionIndex(String newSessionIndex);
    
    /**
     * Gets the RequestedAuthnContext of this request.
     * 
     * @return the RequestedAuthnContext of this request
     */
    public RequestedAuthnContext getRequestedAuthnContext();
    
    /**
     * Sets the RequestedAuthnContext of this request.
     * 
     * @param newRequestedAuthnContext the RequestedAuthnContext of this request
     */
    public void setRequestedAuthnContext(RequestedAuthnContext newRequestedAuthnContext);

}