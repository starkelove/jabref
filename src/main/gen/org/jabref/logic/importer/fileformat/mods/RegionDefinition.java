//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.15 at 02:24:28 PM CET 
//


package org.jabref.logic.importer.fileformat.mods;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for regionDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="regionDefinition">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.loc.gov/mods/v3>hierarchicalPart">
 *       &lt;attribute name="regionType" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "regionDefinition", namespace = "http://www.loc.gov/mods/v3")
public class RegionDefinition
    extends HierarchicalPart
{

    @XmlAttribute(name = "regionType")
    @XmlSchemaType(name = "anySimpleType")
    protected String regionType;

    /**
     * Gets the value of the regionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionType() {
        return regionType;
    }

    /**
     * Sets the value of the regionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionType(String value) {
        this.regionType = value;
    }

}