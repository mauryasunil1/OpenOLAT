/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 *
 * Content got modified for OpenOlat Context
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="client://java.sun.com/xml/jaxb">client://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.06.27 at 01:16:00 PM WEST 
//

package org.olat.modules.oaipmh.common.oaioo;


import org.olat.modules.oaipmh.common.exceptions.XmlWriteException;
import org.olat.modules.oaipmh.common.xml.XSISchema;
import org.olat.modules.oaipmh.common.xml.XmlWritable;
import org.olat.modules.oaipmh.common.xml.XmlWriter;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sumit Kapoor, sumit.kapoor@frentix.com, <a href="https://www.frentix.com">https://www.frentix.com</a>
 */
public class OAIOOMetadata implements XmlWritable {

    public static final String NAMESPACE_URI = "http://www.openolat.org/OAI/oai_oo";
    public static final String SCHEMA_LOCATION = "http://www.openolat.org/xsd/oai_oo.xsd";
    public static final String NAMESPACE_DC = "http://purl.org/dc/elements/1.1/";

    protected List<OOElement> OOElements = new ArrayList<>();


    public List<OOElement> getElements() {
        return this.OOElements;
    }

    public OAIOOMetadata withElement(OOElement OOElement) {
        this.OOElements.add(OOElement);
        return this;
    }

    public String toString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            this.write(new XmlWriter(out));
        } catch (XmlWriteException e) {
            // don't do anything
        } catch (XMLStreamException e) {
            // don't do anything
        }
        return out.toString();
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        try {
            writer.writeStartElement("oai_oo" + ":oo");
            writer.writeNamespace("oai_oo", NAMESPACE_URI);
            writer.writeNamespace("oo", NAMESPACE_DC);
            writer.writeNamespace(XSISchema.PREFIX, XSISchema.NAMESPACE_URI);
            writer.writeAttribute(XSISchema.PREFIX, XSISchema.NAMESPACE_URI, "schemaLocation",
                    NAMESPACE_URI + " " + SCHEMA_LOCATION);

            for (OOElement OOElement : getElements()) {
                writer.writeStartElement("oo", OOElement.getName(), NAMESPACE_URI);
                OOElement.write(writer);
                writer.writeEndElement();
            }
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }
}
