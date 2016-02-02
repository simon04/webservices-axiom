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
package org.apache.axiom.soap.impl.common.builder;

import org.apache.axiom.om.impl.common.builder.PayloadSelector;
import org.apache.axiom.om.impl.intf.AxiomContainer;
import org.apache.axiom.soap.SOAPBody;

public final class SOAPPayloadSelector implements PayloadSelector {
    public static final SOAPPayloadSelector INSTANCE = new SOAPPayloadSelector();

    private SOAPPayloadSelector() {}
    
    @Override
    public boolean isPayload(int elementLevel, AxiomContainer parent) {
        return elementLevel == 3 && parent instanceof SOAPBody;
    }
}
