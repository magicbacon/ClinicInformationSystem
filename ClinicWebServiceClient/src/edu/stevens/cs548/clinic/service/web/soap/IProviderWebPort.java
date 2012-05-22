/**
 * IProviderWebPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.stevens.cs548.clinic.service.web.soap;

public interface IProviderWebPort extends java.rmi.Remote {
    public long addDrugTreatment(long arg0, long arg1, java.lang.String arg2, java.lang.String arg3, float arg4) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn;
    public edu.stevens.cs548.clinic.service.web.ProviderDTO getProviderByNPI(long arg0) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderExn;
    public long addRadiologyTreatment(long arg0, long arg1, java.lang.String arg2, java.util.Calendar[] arg3) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn;
    public long addSurgeryTreatment(long arg0, long arg1, java.lang.String arg2, java.util.Calendar arg3) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn;
    public edu.stevens.cs548.clinic.service.web.soap.GetTreatmentsResponseReturn[] getTreatments(long arg0, java.lang.Long[] arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.TreatmentNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn;
    public void deleteTreatment(long arg0, long arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.TreatmentNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn;
    public java.lang.String siteInfo() throws java.rmi.RemoteException;
    public long create(java.lang.String arg0, long arg1, java.lang.String arg2) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn;
    public edu.stevens.cs548.clinic.service.web.ProviderDTO[] getProviderByName(java.lang.String arg0) throws java.rmi.RemoteException;
    public void deleteProvider(java.lang.String arg0, long arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn;
    public edu.stevens.cs548.clinic.service.web.soap.GetTreatmentResponseReturn getTreatment(long arg0, long arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.TreatmentNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn;
}
