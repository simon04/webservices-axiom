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
package org.apache.axiom.dom.impl.mixin;

import static org.apache.axiom.dom.DOMExceptionUtil.newDOMException;

import java.util.Iterator;

import javax.xml.XMLConstants;

import org.apache.axiom.core.Axis;
import org.apache.axiom.core.CoreAttribute;
import org.apache.axiom.core.CoreChildNode;
import org.apache.axiom.core.CoreElement;
import org.apache.axiom.core.CoreModelException;
import org.apache.axiom.core.Mappers;
import org.apache.axiom.dom.DOMAttribute;
import org.apache.axiom.dom.DOMCDATASection;
import org.apache.axiom.dom.DOMComment;
import org.apache.axiom.dom.DOMConfigurationImpl;
import org.apache.axiom.dom.DOMDocument;
import org.apache.axiom.dom.DOMDocumentFragment;
import org.apache.axiom.dom.DOMElement;
import org.apache.axiom.dom.DOMEntityReference;
import org.apache.axiom.dom.DOMExceptionUtil;
import org.apache.axiom.dom.DOMNSAwareAttribute;
import org.apache.axiom.dom.DOMNSAwareElement;
import org.apache.axiom.dom.DOMNSAwareNamedNode;
import org.apache.axiom.dom.DOMNSUnawareAttribute;
import org.apache.axiom.dom.DOMNSUnawareElement;
import org.apache.axiom.dom.DOMNamespaceDeclaration;
import org.apache.axiom.dom.DOMNode;
import org.apache.axiom.dom.DOMNodeFactory;
import org.apache.axiom.dom.DOMProcessingInstruction;
import org.apache.axiom.dom.DOMSemantics;
import org.apache.axiom.dom.DOMText;
import org.apache.axiom.dom.ElementsByTagName;
import org.apache.axiom.dom.ElementsByTagNameNS;
import org.apache.axiom.dom.NSUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public aspect DOMDocumentSupport {
    private final DOMConfigurationImpl DOMDocument.domConfig = new DOMConfigurationImpl();

    private String DOMDocument.documentURI;
    private boolean DOMDocument.strictErrorChecking = true;

    public final Document DOMDocument.getOwnerDocument() {
        return null;
    }

    public final String DOMDocument.getNodeName() {
        return "#document";
    }

    public final short DOMDocument.getNodeType() {
        return Node.DOCUMENT_NODE;
    }

    public final String DOMDocument.getNodeValue() {
        return null;
    }

    public final void DOMDocument.setNodeValue(String nodeValue) {
    }

    public final String DOMDocument.getPrefix() {
        return null;
    }

    public final void DOMDocument.setPrefix(String prefix) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
    }

    public final String DOMDocument.getNamespaceURI() {
        return null;
    }

    public final String DOMDocument.getLocalName() {
        return null;
    }

    public final boolean DOMDocument.hasAttributes() {
        return false;
    }

    public final NamedNodeMap DOMDocument.getAttributes() {
        return null;
    }

    public final String DOMDocument.getTextContent() {
        return null;
    }

    public final void DOMDocument.setTextContent(String textContent) {
        // no-op
    }

    public final Element DOMDocument.getDocumentElement() {
        try {
            return (Element)coreGetDocumentElement();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.toUncheckedException(ex);
        }
    }
    
    public final CoreElement DOMDocument.getNamespaceContext() {
        try {
            return coreGetDocumentElement();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.toUncheckedException(ex);
        }
    }

    public final DOMImplementation DOMDocument.getImplementation() {
        return ((DOMNodeFactory)coreGetNodeFactory()).getDOMImplementation();
    }

    public final DOMConfiguration DOMDocument.getDomConfig() {
        return domConfig;
    }

    public final String DOMDocument.getInputEncoding() {
        return coreGetInputEncoding();
    }

    public final String DOMDocument.getXmlVersion() {
        return coreGetXmlVersion();
    }

    public final void DOMDocument.setXmlVersion(String version) {
        coreSetXmlVersion(version);
    }

    public final String DOMDocument.getXmlEncoding() {
        return coreGetXmlEncoding();
    }

    public final boolean DOMDocument.getXmlStandalone() {
        Boolean standalone = coreGetStandalone();
        return standalone != null && standalone;
    }

    public final void DOMDocument.setXmlStandalone(boolean standalone) {
        coreSetStandalone(standalone);
    }

    public final void DOMDocument.normalizeDocument() {
        if (domConfig.isEnabled(DOMConfigurationImpl.SPLIT_CDATA_SECTIONS)
                || domConfig.isEnabled(DOMConfigurationImpl.WELLFORMED)) {
            throw new UnsupportedOperationException("TODO");
        } else {
            normalize(domConfig);
        }
    }
    
    public final Text DOMDocument.createTextNode(String data) {
        DOMText text = coreGetNodeFactory().createNode(DOMText.class);
        text.coreSetOwnerDocument(this);
        text.coreSetCharacterData(data);
        return text;
    }

    public final CDATASection DOMDocument.createCDATASection(String data) {
        try {
            DOMCDATASection cdataSection = coreGetNodeFactory().createNode(DOMCDATASection.class);
            cdataSection.coreSetOwnerDocument(this);
            cdataSection.coreSetCharacterData(data, DOMSemantics.INSTANCE);
            return cdataSection;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.toUncheckedException(ex);
        }
    }
    
    public final Element DOMDocument.createElement(String tagName) {
        DOMNSUnawareElement element = coreGetNodeFactory().createNode(DOMNSUnawareElement.class);
        element.coreSetOwnerDocument(this);
        element.coreSetName(tagName);
        return element;
    }

    public final Attr DOMDocument.createAttribute(String name) {
        NSUtil.validateName(name);
        DOMNSUnawareAttribute attr = coreGetNodeFactory().createNode(DOMNSUnawareAttribute.class);
        attr.coreSetOwnerDocument(this);
        attr.coreSetName(name);
        attr.coreSetType("CDATA");
        return attr;
    }

    public final Element DOMDocument.createElementNS(String namespaceURI, String qualifiedName) {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = "";
            localName = qualifiedName;
        } else {
            prefix = qualifiedName.substring(0, i);
            localName = qualifiedName.substring(i+1);
        }
        namespaceURI = NSUtil.normalizeNamespaceURI(namespaceURI);
        NSUtil.validateNamespace(namespaceURI, prefix);
        DOMNSAwareElement element = coreGetNodeFactory().createNode(DOMNSAwareElement.class);
        element.coreSetOwnerDocument(this);
        element.coreSetName(namespaceURI, localName, prefix);
        return element;
    }

    public final Attr DOMDocument.createAttributeNS(String namespaceURI, String qualifiedName) {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = "";
            localName = qualifiedName;
        } else {
            prefix = qualifiedName.substring(0, i);
            localName = qualifiedName.substring(i+1);
        }
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            DOMNamespaceDeclaration decl = coreGetNodeFactory().createNode(DOMNamespaceDeclaration.class);
            decl.coreSetOwnerDocument(this);
            decl.coreSetDeclaredNamespace(NSUtil.getDeclaredPrefix(localName, prefix), "");
            return decl;
        } else {
            namespaceURI = NSUtil.normalizeNamespaceURI(namespaceURI);
            NSUtil.validateAttributeName(namespaceURI, localName, prefix);
            DOMNSAwareAttribute attr = coreGetNodeFactory().createNode(DOMNSAwareAttribute.class);
            attr.coreSetOwnerDocument(this);
            attr.coreSetName(namespaceURI, localName, prefix);
            // TODO: set type?
            return attr;
        }
    }

    public final ProcessingInstruction DOMDocument.createProcessingInstruction(String target, String data) {
        try {
            DOMProcessingInstruction pi = coreGetNodeFactory().createNode(DOMProcessingInstruction.class);
            pi.coreSetOwnerDocument(this);
            pi.coreSetTarget(target);
            pi.coreSetCharacterData(data, DOMSemantics.INSTANCE);
            return pi;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.toUncheckedException(ex);
        }
    }

    public final EntityReference DOMDocument.createEntityReference(String name) throws DOMException {
        DOMEntityReference node = coreGetNodeFactory().createNode(DOMEntityReference.class);
        node.coreSetOwnerDocument(this);
        node.coreSetName(name);
        return node;
    }

    public final Comment DOMDocument.createComment(String data) {
        try {
            DOMComment node = coreGetNodeFactory().createNode(DOMComment.class);
            node.coreSetOwnerDocument(this);
            node.coreSetCharacterData(data, DOMSemantics.INSTANCE);
            return node;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.toUncheckedException(ex);
        }
    }

    public final DocumentFragment DOMDocument.createDocumentFragment() {
        DOMDocumentFragment fragment = coreGetNodeFactory().createNode(DOMDocumentFragment.class);
        fragment.coreSetOwnerDocument(this);
        return fragment;
    }

    public final NodeList DOMDocument.getElementsByTagName(String tagname) {
        return new ElementsByTagName(this, tagname);
    }

    public final NodeList DOMDocument.getElementsByTagNameNS(String namespaceURI, String localName) {
        return new ElementsByTagNameNS(this, namespaceURI, localName);
    }
    
    // TODO: need unit test to check that this method works as expected on an OMSourcedElement
    public final Node DOMDocument.renameNode(Node node, String namespaceURI, String qualifiedName) {
        if (!(node instanceof DOMNode && ((DOMNode)node).coreHasSameOwnerDocument(this))) {
            throw DOMExceptionUtil.newDOMException(DOMException.WRONG_DOCUMENT_ERR);
        }
        // TODO: what about an attempt to rename a namespace unaware node?
        if (!(node instanceof DOMNSAwareNamedNode)) {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_SUPPORTED_ERR);
        }
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = "";
            localName = qualifiedName;
        } else {
            prefix = qualifiedName.substring(0, i);
            localName = qualifiedName.substring(i+1);
        }
        namespaceURI = NSUtil.normalizeNamespaceURI(namespaceURI);
        switch (((DOMNode)node).coreGetNodeType()) {
            case NS_AWARE_ELEMENT:
                NSUtil.validateNamespace(namespaceURI, prefix);
                ((DOMNSAwareElement)node).coreSetName(namespaceURI, localName, prefix);
                return node;
            case NS_AWARE_ATTRIBUTE:
                if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                    DOMNamespaceDeclaration decl = coreGetNodeFactory().createNode(DOMNamespaceDeclaration.class);
                    decl.coreSetOwnerDocument(this);
                    // TODO: we should have a generic method to move the content over to the new node
                    decl.coreSetDeclaredNamespace(NSUtil.getDeclaredPrefix(localName, prefix), ((DOMNSAwareAttribute)node).getValue());
                    // TODO: what about replacing the node in the tree??
                    return decl;
                } else {
                    NSUtil.validateAttributeName(namespaceURI, localName, prefix);
                    ((DOMNSAwareAttribute)node).coreSetName(namespaceURI, localName, prefix);
                    return node;
                }
            case NAMESPACE_DECLARATION:
                // TODO
                throw new UnsupportedOperationException();
            default:
                throw new IllegalStateException();
        }
    }

    public final DocumentType DOMDocument.getDoctype() {
        try {
            CoreChildNode child = coreGetFirstChild();
            while (child != null) {
                if (child instanceof DocumentType) {
                    return (DocumentType)child;
                } else if (child instanceof Element) {
                    // A doctype declaration can only appear before the root element. Stop here.
                    return null;
                }
                child = child.coreGetNextSibling();
            }
            return null;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.toUncheckedException(ex);
        }
    }

    public final Node DOMDocument.adoptNode(Node node) throws DOMException {
        if (node instanceof DOMNode) {
            DOMNode childNode = (DOMNode)node;
            if (childNode instanceof CoreChildNode) {
                ((CoreChildNode)childNode).coreDetach(this);
            } else {
                childNode.coreSetOwnerDocument(this);
            }
            if (node instanceof DOMAttribute) {
                ((DOMAttribute)node).coreSetSpecified(true);
            }
            return childNode;
        } else {
            return null;
        }
    }

    final void DOMDocument.checkNewChild0(DOMNode newChild) {
        if (newChild instanceof DOMText) {
            throw newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
        }
    }

    public final Element DOMDocument.getElementById(String elementId) {
        try {
            for (Iterator<DOMElement> it = coreGetNodes(Axis.DESCENDANTS, DOMElement.class, Mappers.<DOMElement>identity(), DOMSemantics.INSTANCE); it.hasNext(); ) {
                DOMElement element = it.next();
                for (CoreAttribute attr = element.coreGetFirstAttribute(); attr != null; attr = attr.coreGetNextAttribute()) {
                    if (((DOMAttribute)attr).isId() && elementId.equals(attr.coreGetCharacterData().toString())) {
                        return element;
                    }
                }
            }
            return null;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.toUncheckedException(ex);
        }
    }

    public final String DOMDocument.getDocumentURI() {
        return documentURI;
    }

    public final void DOMDocument.setDocumentURI(String documentURI) {
        this.documentURI = documentURI;
    }

    public final boolean DOMDocument.getStrictErrorChecking() {
        return strictErrorChecking;
    }

    public final void DOMDocument.setStrictErrorChecking(boolean strictErrorChecking) {
        this.strictErrorChecking = strictErrorChecking;
    }
}
