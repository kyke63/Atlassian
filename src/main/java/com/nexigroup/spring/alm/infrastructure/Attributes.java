package com.nexigroup.spring.alm.infrastructure;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**

 * Java class for anonymous complex type.
 *

 * The following schema fragment specifies the expected content
 * contained within this class.
 *
 * <complexType>
 *   <complexContent>
 *     <restriction base=
 *         "{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element ref=
 *             "{}Attribute" maxOccurs="unbounded"
 *             minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 *
 *

 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "attribute" })
@XmlRootElement(name = "Attributes")
public class Attributes {

    @XmlElement(name = "Attribute")
    protected List<Attribute> attribute;

    /**
     * Gets the value of the attribute property.
     *
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the attribute property.
     *
     * For example, to add a new item, do as follows:
     *
     * getAttribute().add(newItem);
      *
     * Objects of the following type(s) are allowed in the
     * list {@link Attribute }
     *
     *
     */
    public List<Attribute> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<Attribute>();
        }
        return this.attribute;
    }

}
