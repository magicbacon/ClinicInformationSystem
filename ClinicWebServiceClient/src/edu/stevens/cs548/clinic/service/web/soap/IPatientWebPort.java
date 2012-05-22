/**
 * IPatientWebPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.stevens.cs548.clinic.service.web.soap;

public interface IPatientWebPort extends java.rmi.Remote {
    public edu.stevens.cs548.clinic.service.web.PatientDTO getPatientByDbId(long arg0) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.PatientNotFoundExn;
    public edu.stevens.cs548.clinic.service.web.PatientDTO getPatientByPatientId(long arg0) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.PatientServiceExn;
    public edu.stevens.cs548.clinic.service.web.PatientDTO[] getPatientsByNameDob(java.lang.String arg0, java.util.Calendar arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.PatientServiceExn;
    public void deletePatient(java.lang.String arg0, long arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.PatientServiceExn;
    public long create(java.lang.String arg0, java.util.Calendar arg1, long arg2) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.PatientServiceExn;
    public long addDrugTreatment(long arg0, long arg1, java.lang.String arg2, java.lang.String arg3, float arg4) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.PatientNotFoundExn;
    public long addRadiologyTreatment(long arg0, long arg1, java.lang.String arg2, java.util.Calendar[] arg3) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.PatientNotFoundExn;
    public long addSurgeryTreatment(long arg0, long arg1, java.lang.String arg2, java.util.Calendar arg3) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.PatientNotFoundExn;
    public edu.stevens.cs548.clinic.service.web.soap.GetTreatmentsResponseReturn[] getTreatments(long arg0, java.lang.Long[] arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.PatientServiceExn, edu.stevens.cs548.clinic.service.web.soap.TreatmentNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.PatientNotFoundExn;
    public void deleteTreatment(long arg0, long arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.PatientServiceExn, edu.stevens.cs548.clinic.service.web.soap.TreatmentNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.PatientNotFoundExn;
    public java.lang.String siteInfo() throws java.rmi.RemoteException;
}
