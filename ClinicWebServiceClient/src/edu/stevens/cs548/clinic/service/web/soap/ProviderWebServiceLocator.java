/**
 * ProviderWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.stevens.cs548.clinic.service.web.soap;

public class ProviderWebServiceLocator extends org.apache.axis.client.Service implements edu.stevens.cs548.clinic.service.web.soap.ProviderWebService {

    public ProviderWebServiceLocator() {
    }


    public ProviderWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ProviderWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ProviderWebPort
    private java.lang.String ProviderWebPort_address = "http://daler-macbookair.local:8080/ClinicWebService/ProviderWebService";

    public java.lang.String getProviderWebPortAddress() {
        return ProviderWebPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ProviderWebPortWSDDServiceName = "ProviderWebPort";

    public java.lang.String getProviderWebPortWSDDServiceName() {
        return ProviderWebPortWSDDServiceName;
    }

    public void setProviderWebPortWSDDServiceName(java.lang.String name) {
        ProviderWebPortWSDDServiceName = name;
    }

    public edu.stevens.cs548.clinic.service.web.soap.IProviderWebPort getProviderWebPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ProviderWebPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getProviderWebPort(endpoint);
    }

    public edu.stevens.cs548.clinic.service.web.soap.IProviderWebPort getProviderWebPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            edu.stevens.cs548.clinic.service.web.soap.ProviderWebPortBindingStub _stub = new edu.stevens.cs548.clinic.service.web.soap.ProviderWebPortBindingStub(portAddress, this);
            _stub.setPortName(getProviderWebPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setProviderWebPortEndpointAddress(java.lang.String address) {
        ProviderWebPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (edu.stevens.cs548.clinic.service.web.soap.IProviderWebPort.class.isAssignableFrom(serviceEndpointInterface)) {
                edu.stevens.cs548.clinic.service.web.soap.ProviderWebPortBindingStub _stub = new edu.stevens.cs548.clinic.service.web.soap.ProviderWebPortBindingStub(new java.net.URL(ProviderWebPort_address), this);
                _stub.setPortName(getProviderWebPortWSDDServiceName());
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
        if ("ProviderWebPort".equals(inputPortName)) {
            return getProviderWebPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://cs548.stevens.edu/clinic/service/web/soap", "ProviderWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://cs548.stevens.edu/clinic/service/web/soap", "ProviderWebPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ProviderWebPort".equals(portName)) {
            setProviderWebPortEndpointAddress(address);
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
