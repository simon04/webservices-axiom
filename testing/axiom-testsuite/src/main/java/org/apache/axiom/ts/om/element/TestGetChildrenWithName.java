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

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.ts.AxiomTestCase;
import org.apache.axiom.ts.xml.XMLSample;

/** Test the element iterator */
public class TestGetChildrenWithName extends AxiomTestCase {
    public TestGetChildrenWithName(OMMetaFactory metaFactory) {
        super(metaFactory);
    }

    protected void runTest() throws Throwable {
        OMElement elt = OMXMLBuilderFactory.createOMBuilder(metaFactory.getOMFactory(),
                XMLSample.SIMPLE.getInputStream()).getDocumentElement().getFirstElement();
        QName qname = new QName("urn:ns2", "subelement");
        Iterator<OMElement> iter = elt.getChildrenWithName(qname);
        int counter = 0;
        while (iter.hasNext()) {
            counter ++;
            Object o = iter.next();
            assertNotNull("Must return not null objects!", o);
            assertTrue("All these should be elements!",
                       ((OMNode) o).getType() == OMNode.ELEMENT_NODE);
        }
        assertEquals("This element should contain only one element with the given QName ", 1,
                     counter);
        elt.close(false);
    }
}
