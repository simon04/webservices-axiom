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
package org.apache.axiom.ts.om.element;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.ts.AxiomTestCase;

public class TestGetXMLStreamReaderGetNamespaceContext extends AxiomTestCase {
    private final boolean cache;
    
    public TestGetXMLStreamReaderGetNamespaceContext(OMMetaFactory metaFactory, boolean cache) {
        super(metaFactory);
        this.cache = cache;
        addTestParameter("cache", Boolean.toString(cache));
    }

    protected void runTest() throws Throwable {
        OMElement element = AXIOMUtil.stringToOM(metaFactory.getOMFactory(),
                "<a xmlns='urn:ns1' xmlns:ns2='urn:ns2'><b xmlns:ns3='urn:ns3'/></a>");
        XMLStreamReader stream = cache ? element.getXMLStreamReader()
                : element.getXMLStreamReaderWithoutCaching();
        stream.next();
        assertEquals(XMLStreamReader.START_ELEMENT, stream.next());
        assertEquals("b", stream.getLocalName());
        NamespaceContext context = stream.getNamespaceContext();
        assertEquals("urn:ns1", context.getNamespaceURI(""));
        assertEquals("urn:ns2", context.getNamespaceURI("ns2"));
        assertEquals("urn:ns3", context.getNamespaceURI("ns3"));
        assertEquals("ns2", context.getPrefix("urn:ns2"));
        element.close(false);
    }
}
