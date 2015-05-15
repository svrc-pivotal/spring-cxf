/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package demo.spring.service;

import java.util.List;
import java.security.Principal;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.SamlAssertionValidator;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.apache.cxf.interceptor.security.NamePasswordCallbackHandler;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;


/**
 * This class does some trivial validation of a received SAML Assertion. It checks that it is
 * a SAML 2 Assertion, and checks the issuer name and that it has an Attribute Statement. 
 */
public class CustomSaml2Validator extends SamlAssertionValidator {
  
    @Override
    public Credential validate(Credential credential, RequestData data) throws WSSecurityException {
        Credential validatedCredential = super.validate(credential, data);
        System.out.println("Principal in credential: " + validatedCredential.getPrincipal());
        
        Principal principal = new SAMLTokenPrincipalImpl(validatedCredential.getSamlAssertion());
        validatedCredential.setPrincipal(principal);
        return validatedCredential;
    }

    
}
