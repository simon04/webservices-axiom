/*
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
package org.apache.axiom.ts.soap.fault;

import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.soap.SOAPFault;
import org.apache.axiom.ts.soap.SOAPSpec;
import org.apache.axiom.ts.soap.SOAPTestCase;

public class TestGetCode extends SOAPTestCase {
    public TestGetCode(OMMetaFactory metaFactory, SOAPSpec spec) {
        super(metaFactory, spec);
    }

    protected void runTest() throws Throwable {
        SOAPFault soapFault = soapFactory.createSOAPFault();
        assertTrue(
                "Fault Test:- After creating a SOAPFault, it has a code",
                soapFault.getCode() == null);
        soapFault.setCode(soapFactory.createSOAPFaultCode(soapFault));
        assertFalse(
                "Fault Test:- After calling setCode method, Fault has no code",
                soapFault.getCode() == null);
        assertTrue("Fault Test:- Fault code local name mismatch",
                   soapFault.getCode().getLocalName().equals(
                           spec.getFaultCodeLocalName()));
    }
}
