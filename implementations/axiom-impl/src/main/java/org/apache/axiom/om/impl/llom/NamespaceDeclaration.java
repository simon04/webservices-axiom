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
package org.apache.axiom.om.impl.llom;

import org.apache.axiom.om.OMCloneOptions;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMInformationItem;
import org.apache.axiom.om.impl.common.AxiomNamespaceDeclaration;

public final class NamespaceDeclaration extends Attribute implements AxiomNamespaceDeclaration {
    public NamespaceDeclaration(OMFactory factory) {
        super(factory);
    }

    public String coreGetValue() {
        // Method not used in LLOM
        throw new UnsupportedOperationException();
    }

    public void coreSetValue(String value) {
        // Method not used in LLOM
        throw new UnsupportedOperationException();
    }

    public OMInformationItem clone(OMCloneOptions options) {
        NamespaceDeclaration decl = new NamespaceDeclaration(getOMFactory());
        decl.setDeclaredNamespace(getDeclaredNamespace());
        return decl;
    }
}
