/**
 * ProviderDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.stevens.cs548.clinic.service.web;

public class ProviderDTO  implements java.io.Serializable {
    private long npi;

    private java.lang.String name;

    private java.lang.String specialization;

    private java.lang.Long[] treatments;

    public ProviderDTO() {
    }

    public ProviderDTO(
           long npi,
           java.lang.String name,
           java.lang.String specialization,
           java.lang.Long[] treatments) {
           this.npi = npi;
           this.name = name;
           this.specialization = specialization;
           this.treatments = treatments;
    }


    /**
     * Gets the npi value for this ProviderDTO.
     * 
     * @return npi
     */
    public long getNpi() {
        return npi;
    }


    /**
     * Sets the npi value for this ProviderDTO.
     * 
     * @param npi
     */
    public void setNpi(long npi) {
        this.npi = npi;
    }


    /**
     * Gets the name value for this ProviderDTO.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ProviderDTO.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the specialization value for this ProviderDTO.
     * 
     * @return specialization
     */
    public java.lang.String getSpecialization() {
        return specialization;
    }


    /**
     * Sets the specialization value for this ProviderDTO.
     * 
     * @param specialization
     */
    public void setSpecialization(java.lang.String specialization) {
        this.specialization = specialization;
    }


    /**
     * Gets the treatments value for this ProviderDTO.
     * 
     * @return treatments
     */
    public java.lang.Long[] getTreatments() {
        return treatments;
    }


    /**
     * Sets the treatments value for this ProviderDTO.
     * 
     * @param treatments
     */
    public void setTreatments(java.lang.Long[] treatments) {
        this.treatments = treatments;
    }

    public java.lang.Long getTreatments(int i) {
        return this.treatments[i];
    }

    public void setTreatments(int i, java.lang.Long _value) {
        this.treatments[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProviderDTO)) return false;
        ProviderDTO other = (ProviderDTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.npi == other.getNpi() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.specialization==null && other.getSpecialization()==null) || 
             (this.specialization!=null &&
              this.specialization.equals(other.getSpecialization()))) &&
            ((this.treatments==null && other.getTreatments()==null) || 
             (this.treatments!=null &&
              java.util.Arrays.equals(this.treatments, other.getTreatments())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += new Long(getNpi()).hashCode();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getSpecialization() != null) {
            _hashCode += getSpecialization().hashCode();
        }
        if (getTreatments() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTreatments());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTreatments(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProviderDTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cs548.stevens.edu/clinic/service/web", "providerDTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("npi");
        elemField.setXmlName(new javax.xml.namespace.QName("http://cs548.stevens.edu/clinic/service/web", "npi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://cs548.stevens.edu/clinic/service/web", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("specialization");
        elemField.setXmlName(new javax.xml.namespace.QName("http://cs548.stevens.edu/clinic/service/web", "specialization"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("treatments");
        elemField.setXmlName(new javax.xml.namespace.QName("http://cs548.stevens.edu/clinic/service/web", "treatments"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
