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
package org.apache.axiom.ts.strategy;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.testutils.suite.Dimension;
import org.apache.axiom.testutils.suite.MatrixTestCase;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * Defines a way how an {@link OMElement} can be placed inside an object model. An implementation of
 * this interface wraps an {@link OMElement} in a container of a specific type ({@link OMDocument}
 * or {@link OMElement}) in a specific state.
 */
public interface ElementContext extends Dimension {
    ElementContext ORPHAN = new ElementContext() {
        public void addTestParameters(MatrixTestCase testCase) {
            testCase.addTestParameter("container", "none");
        }

        public OMContainer wrap(OMElement element) {
            return null;
        }

        public InputSource getControl(InputSource xml) {
            throw new UnsupportedOperationException();
        }
    };
    
    /**
     * The {@link OMElement} is a child of another (programmatically created) {@link OMElement}.
     */
    ElementContext ELEMENT = new ElementContext() {
        public void addTestParameters(MatrixTestCase testCase) {
            testCase.addTestParameter("container", "element");
            testCase.addTestParameter("complete", "true");
        }

        public OMContainer wrap(OMElement element) {
            OMElement parent = element.getOMFactory().createOMElement("parent", null);
            parent.addChild(element);
            return parent;
        }

        public InputSource getControl(InputSource xml) throws Exception {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document document = dbf.newDocumentBuilder().parse(xml);
            Element parent = document.createElementNS(null, "parent");
            parent.appendChild(document.getDocumentElement());
            StringWriter sw = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(parent), new StreamResult(sw));
            return new InputSource(new StringReader(sw.toString()));
        }
    };
    
    /**
     * The {@link OMElement} is a child of another {@link OMElement} created from a parser and that
     * is incomplete.
     */
    ElementContext INCOMPLETE_ELEMENT = new ElementContext() {
        public void addTestParameters(MatrixTestCase testCase) {
            testCase.addTestParameter("container", "element");
            testCase.addTestParameter("complete", "false");
        }
        
        public OMContainer wrap(OMElement element) {
            OMElement parent = OMXMLBuilderFactory.createOMBuilder(element.getOMFactory(),
                    new StringReader("<parent><sibling/></parent>")).getDocumentElement();
            parent.getFirstOMChild().insertSiblingBefore(element);
            Assert.assertFalse(parent.isComplete());
            return parent;
        }
        
        public InputSource getControl(InputSource xml) throws Exception {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document document = dbf.newDocumentBuilder().parse(xml);
            Element parent = document.createElementNS(null, "parent");
            parent.appendChild(document.getDocumentElement());
            parent.appendChild(document.createElementNS(null, "sibling"));
            StringWriter sw = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(parent), new StreamResult(sw));
            return new InputSource(new StringReader(sw.toString()));
        }
    };
    
    OMContainer wrap(OMElement element);
    
    InputSource getControl(InputSource xml) throws Exception;
}
