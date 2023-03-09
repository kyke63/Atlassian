package com.nexigroup.spring.alm.infrastructure;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;

/**
 * Java class for anonymous complex type.
 *
 * The following schema fragment specifies the expected content
 * contained within this class.
 *
 * <complexType>
 *   <simpleContent>
 *     <extension base="<http://www.w3.org/2001/XMLSchema>
 *          string">
 *       <attribute name="name"
 *           type="{http://www.w3.org/2001/XMLSchema}string" />
 *     </extension>
 *   </simpleContent>
 * </complexType>
 *
 */


 @XmlAccessorType(XmlAccessType.FIELD)
 @XmlType(name = "", propOrder = { "value" })
 @XmlRootElement(name = "Attribute")
 public class Attribute {

     @XmlValue
     protected String value;
     @XmlAttribute
     protected String name;

     /**
      * Gets the value of the value property.
      *
      * @return possible object is {@link String }
      *
      */
     public String getValue() {
         return value;
     }

     /**
      * Sets the value of the value property.
      *
      * @param value
      *            allowed object is {@link String }
      *
      */
     public void setValue(String value) {
         this.value = value;
     }

     /**
      * Gets the value of the name property.
      *
      * @return possible object is {@link String }
      *
      */
     public String getName() {
         return name;
     }

     /**
      * Sets the value of the name property.
      *
      * @param value
      *            allowed object is {@link String }
      *
      */
     public void setName(String value) {
         this.name = value;
     }

 }