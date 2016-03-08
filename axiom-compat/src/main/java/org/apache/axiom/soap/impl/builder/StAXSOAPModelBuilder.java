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
package org.apache.axiom.soap.impl.builder;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPMessage;
import org.apache.axiom.soap.SOAPModelBuilder;
import org.apache.axiom.soap.SOAPProcessingException;

/**
 * @deprecated Please use the {@link OMXMLBuilderFactory} API to create builders.
 */
public class StAXSOAPModelBuilder implements SOAPModelBuilder {
    private final SOAPModelBuilder target;

    protected StAXSOAPModelBuilder(SOAPModelBuilder target) {
        this.target = target;
    }
    
    public StAXSOAPModelBuilder(XMLStreamReader parser, SOAPFactory factory, String soapVersion) {
        this(OMXMLBuilderFactory.createStAXSOAPModelBuilder(factory.getMetaFactory(), parser));
        validateSOAPVersion(factory, soapVersion);
    }
    
    public StAXSOAPModelBuilder(XMLStreamReader parser, String soapVersion) {
        this(OMXMLBuilderFactory.createStAXSOAPModelBuilder(OMAbstractFactory.getMetaFactory(), parser));
        validateSOAPVersion(null, soapVersion);
    }
    
    protected final void validateSOAPVersion(SOAPFactory factory, String soapVersion) {
        SOAPFactory actualFactory = (SOAPFactory)getSOAPMessage().getOMFactory();
        if (factory != null && actualFactory != factory ||
                soapVersion != null && !actualFactory.getSOAPVersion().getEnvelopeURI().equals(soapVersion)) {
            throw new SOAPProcessingException("SOAP version mismatch");
        }
    }
    
    public SOAPEnvelope getSOAPEnvelope() {
        return target.getSOAPEnvelope();
    }

    public int next() throws OMException {
        return target.next();
    }

    public SOAPMessage getSOAPMessage() {
        return target.getSOAPMessage();
    }

    public SOAPMessage getSoapMessage() {
        return target.getSOAPMessage();
    }

    public void setCache(boolean b) throws OMException {
        target.setCache(b);
    }

    public boolean isCache() {
        return target.isCache();
    }

    public boolean isCompleted() {
        return target.isCompleted();
    }

    public OMDocument getDocument() {
        return target.getDocument();
    }

    public OMElement getDocumentElement() {
        return target.getDocumentElement();
    }

    public OMElement getDocumentElement(boolean discardDocument) {
        return target.getDocumentElement(discardDocument);
    }

    public String getCharacterEncoding() {
        return target.getCharacterEncoding();
    }

    public void close() {
        target.close();
    }

    public void detach() {
        target.detach();
    }
}
