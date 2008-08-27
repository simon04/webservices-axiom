package org.apache.axiom.c14n.omwrapper;

import org.apache.axiom.c14n.omwrapper.factory.WrapperFactory;
import org.apache.axiom.c14n.omwrapper.interfaces.Document;
import org.apache.axiom.c14n.omwrapper.interfaces.Element;
import org.apache.axiom.c14n.omwrapper.interfaces.NamedNodeMap;
import org.apache.axiom.c14n.omwrapper.interfaces.Attr;
import org.apache.axiom.c14n.DataParser;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import javax.xml.namespace.QName;

/**
 * @author Saliya Ekanayake (esaliya@gmail.com)
 */
public class AttrImplTest extends TestCase {
    private DataParser dp;
    private NamedNodeMap nnm;
    private Attr attr;

    public AttrImplTest(String name){
        super(name);
    }

    public static Test suite() {
        return new TestSuite(AttrImplTest.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public void setUp() throws Exception{
        dp = new DataParser("/sample1.xml");
        dp.init();
        // get e5
        OMElement e5 = dp.omDocEle.getFirstChildWithName(new QName("http://example.org","e5"));
        // get the wrapped element of e5
        Element e = (Element)dp.fac.getNode(e5);
        nnm = e.getAttributes();

    }

    public void testAttrProperties(){
        // e5 has 7 attributes
        assertEquals(7, nnm.getLength());
        //attr is a:attr="out"
        attr = (Attr)nnm.item(0);
        assertEquals("attr", attr.getLocalName());
        assertEquals("a:attr", attr.getName());
        assertEquals("a:attr", attr.getNodeName());
        assertEquals("a", attr.getPrefix());
        assertEquals("http://www.w3.org", attr.getNamespaceURI());

        // attr is attr2="all"
        attr = (Attr)nnm.item(2);
        assertEquals("attr2", attr.getLocalName());
        assertEquals("attr2", attr.getName());
        assertEquals("attr2", attr.getNodeName());
        assertNull("prefix of attr2=\"all\" is null", attr.getPrefix());
        assertNull("Namespace URI of attr2=\"all\" is null", attr.getNamespaceURI());
        assertEquals("all", attr.getValue());
        assertEquals("all", attr.getNodeValue());

        // attr is xmlns:a="http://www.w3.org"
        attr = (Attr)nnm.item(4);
        assertEquals("a", attr.getLocalName());
        assertEquals("xmlns:a", attr.getName());
        assertEquals("xmlns:a", attr.getNodeName());
        assertEquals("xmlns", attr.getPrefix());
        // the namespace URI of xmlns is "http://www.w3.org/2000/xmlns/"
        assertEquals("http://www.w3.org/2000/xmlns/", attr.getNamespaceURI());
        assertEquals("http://www.w3.org", attr.getValue());
        assertEquals("http://www.w3.org", attr.getNodeValue());

        // attr is xmlns="http://example.org"
        attr = (Attr)nnm.item(6);
        assertEquals("xmlns", attr.getLocalName());
        assertEquals("xmlns", attr.getName());
        assertEquals("xmlns", attr.getNodeName());
        assertNull("prefix of xmlns=\"http://example.org\" is null", attr.getPrefix());
        // the namespace URI of xmlns is "http://www.w3.org/2000/xmlns/"
        assertEquals("http://www.w3.org/2000/xmlns/", attr.getNamespaceURI());
        assertEquals("http://example.org", attr.getValue());
        assertEquals("http://example.org", attr.getNodeValue());
    }

    public void testOwnerElement() {
        // get e5
        OMElement e5 = dp.omDocEle.getFirstChildWithName(new QName("http://example.org","e5"));
        // get the wrapped element of e5
        Element e = (Element)dp.fac.getNode(e5);
        // attr is a:attr="out"
        attr = (Attr)e.getAttributes().item(0);
        // the getOwnerElement() should provide a reference to the same object pointed by reference e
        assertEquals(e, attr.getOwnerElement());
    }

    public void testGetNextSibling() {
        // attr is a:attr="out"
        attr = (Attr)nnm.item(0);
        assertNull("getNextSibling() should return null", attr.getNextSibling());
    }

    public void testGetPreviousSibling(){
        // attr is a:attr="out"
        attr = (Attr)nnm.item(0);
        assertNull("getPreviousSibling() should return null", attr.getPreviousSibling());
    }

    public void testGetParentNode(){
        // attr is a:attr="out"
        attr = (Attr)nnm.item(0);
        assertNull("getParentNode() should return null", attr.getParentNode());

    }



}
