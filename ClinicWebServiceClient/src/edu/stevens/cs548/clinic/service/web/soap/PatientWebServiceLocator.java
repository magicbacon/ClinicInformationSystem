/**
 * PatientWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.stevens.cs548.clinic.service.web.soap;

public class PatientWebServiceLocator extends org.apache.axis.client.Service implements edu.stevens.cs548.clinic.service.web.soap.PatientWebService {

    public PatientWebServiceLocator() {
    }


    public PatientWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PatientWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PatientWebPort
    private java.lang.String PatientWebPort_address = "http://daler-macbookair.local:8080/ClinicWebService/PatientWebService";

    public java.lang.String getPatientWebPortAddress() {
        return PatientWebPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PatientWebPortWSDDServiceName = "PatientWebPort";

    public java.lang.String getPatientWebPortWSDDServiceName() {
        return PatientWebPortWSDDServiceName;
    }

    public void setPatientWebPortWSDDServiceName(java.lang.String name) {
        PatientWebPortWSDDServiceName = name;
    }

    public edu.stevens.cs548.clinic.service.web.soap.IPatientWebPort getPatientWebPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PatientWebPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPatientWebPort(endpoint);
    }

    public edu.stevens.cs548.clinic.service.web.soap.IPatientWebPort getPatientWebPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            edu.stevens.cs548.clinic.service.web.soap.PatientWebPortBindingStub _stub = new edu.stevens.cs548.clinic.service.web.soap.PatientWebPortBindingStub(portAddress, this);
            _stub.setPortName(getPatientWebPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPatientWebPortEndpointAddress(java.lang.String address) {
        PatientWebPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (edu.stevens.cs548.clinic.service.web.soap.IPatientWebPort.class.isAssignableFrom(serviceEndpointInterface)) {
                edu.stevens.cs548.clinic.service.web.soap.PatientWebPortBindingStub _stub = new edu.stevens.cs548.clinic.service.web.soap.PatientWebPortBindingStub(new java.net.URL(PatientWebPort_address), this);
                _stub.setPortName(getPatientWebPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("PatientWebPort".equals(inputPortName)) {
            return getPatientWebPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://cs548.stevens.edu/clinic/service/web/soap", "PatientWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://cs548.stevens.edu/clinic/service/web/soap", "PatientWebPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PatientWebPort".equals(portName)) {
            setPatientWebPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
