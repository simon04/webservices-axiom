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

package org.apache.axiom.soap.impl.llom;

import org.apache.axiom.om.OMCloneOptions;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPFault;
import org.apache.axiom.soap.SOAPFaultText;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.axiom.soap.impl.common.AxiomSOAPFaultReason;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class SOAPFaultReasonImpl extends SOAPElement implements AxiomSOAPFaultReason {
    public SOAPFaultReasonImpl(OMFactory factory) {
        super(factory);
    }

    protected SOAPFaultReasonImpl(OMNamespace ns, SOAPFactory factory) {
        super(factory.getSOAPVersion().getFaultReasonQName().getLocalPart(), ns, factory);
    }

    /**
     * Constructor OMElementImpl
     *
     * @param parent
     * @param builder
     */
    public SOAPFaultReasonImpl(SOAPFault parent, OMXMLParserWrapper builder,
                               SOAPFactory factory) {
        super(parent, factory.getSOAPVersion().getFaultReasonQName().getLocalPart(), builder,
              factory);
    }

    /** @param parent  */
    public SOAPFaultReasonImpl(OMElement parent,
                               boolean extractNamespaceFromParent, SOAPFactory factory)
            throws SOAPProcessingException {
        super(parent,
              factory.getSOAPVersion().getFaultReasonQName().getLocalPart(),
              extractNamespaceFromParent,
              factory);
    }

    public List getAllSoapTexts() {
        List faultTexts = new ArrayList(1);
        Iterator childrenIter = this.getChildren();
        while (childrenIter.hasNext()) {
            OMNode node = (OMNode) childrenIter.next();
            if (node.getType() == OMNode.ELEMENT_NODE && (node instanceof SOAPFaultText)) {
                faultTexts.add(((SOAPFaultText) node));
            }
        }
        return faultTexts;
    }

    public SOAPFaultText getSOAPFaultText(String language) {
        Iterator childrenIter = this.getChildren();
        while (childrenIter.hasNext()) {
            OMNode node = (OMNode) childrenIter.next();
            if (node.getType() == OMNode.ELEMENT_NODE && (node instanceof SOAPFaultText) &&
                    (language == null || language.equals(((SOAPFaultText) node).getLang()))) {
                return (SOAPFaultText) node;
            }
        }

        return null;
    }

    protected OMElement createClone(OMCloneOptions options, OMContainer targetParent) {
        return ((SOAPFactory)getOMFactory()).createSOAPFaultReason((SOAPFault)targetParent);
    }
}
