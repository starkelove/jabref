//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.18 at 02:53:35 PM CET 
//


package org.jabref.logic.importer.fileformat.mods;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for issuanceDefinition.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="issuanceDefinition">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="continuing"/>
 *     &lt;enumeration value="monographic"/>
 *     &lt;enumeration value="single unit"/>
 *     &lt;enumeration value="multipart monograph"/>
 *     &lt;enumeration value="serial"/>
 *     &lt;enumeration value="integrating resource"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "issuanceDefinition", namespace = "http://www.loc.gov/mods/v3")
@XmlEnum
public enum IssuanceDefinition {

    @XmlEnumValue("continuing")
    CONTINUING("continuing"),
    @XmlEnumValue("monographic")
    MONOGRAPHIC("monographic"),
    @XmlEnumValue("single unit")
    SINGLE_UNIT("single unit"),
    @XmlEnumValue("multipart monograph")
    MULTIPART_MONOGRAPH("multipart monograph"),
    @XmlEnumValue("serial")
    SERIAL("serial"),
    @XmlEnumValue("integrating resource")
    INTEGRATING_RESOURCE("integrating resource");
    private final String value;

    IssuanceDefinition(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IssuanceDefinition fromValue(String v) {
        for (IssuanceDefinition c: IssuanceDefinition.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
