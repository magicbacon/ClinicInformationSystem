package edu.stevens.cs548.clinic.service.web.soap;

public class IProviderWebPortProxy implements edu.stevens.cs548.clinic.service.web.soap.IProviderWebPort {
  private String _endpoint = null;
  private edu.stevens.cs548.clinic.service.web.soap.IProviderWebPort iProviderWebPort = null;
  
  public IProviderWebPortProxy() {
    _initIProviderWebPortProxy();
  }
  
  public IProviderWebPortProxy(String endpoint) {
    _endpoint = endpoint;
    _initIProviderWebPortProxy();
  }
  
  private void _initIProviderWebPortProxy() {
    try {
      iProviderWebPort = (new edu.stevens.cs548.clinic.service.web.soap.ProviderWebServiceLocator()).getProviderWebPort();
      if (iProviderWebPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iProviderWebPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iProviderWebPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iProviderWebPort != null)
      ((javax.xml.rpc.Stub)iProviderWebPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public edu.stevens.cs548.clinic.service.web.soap.IProviderWebPort getIProviderWebPort() {
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    return iProviderWebPort;
  }
  
  public long addDrugTreatment(long arg0, long arg1, java.lang.String arg2, java.lang.String arg3, float arg4) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    return iProviderWebPort.addDrugTreatment(arg0, arg1, arg2, arg3, arg4);
  }
  
  public edu.stevens.cs548.clinic.service.web.ProviderDTO getProviderByNPI(long arg0) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderExn{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    return iProviderWebPort.getProviderByNPI(arg0);
  }
  
  public long addRadiologyTreatment(long arg0, long arg1, java.lang.String arg2, java.util.Calendar[] arg3) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    return iProviderWebPort.addRadiologyTreatment(arg0, arg1, arg2, arg3);
  }
  
  public long addSurgeryTreatment(long arg0, long arg1, java.lang.String arg2, java.util.Calendar arg3) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    return iProviderWebPort.addSurgeryTreatment(arg0, arg1, arg2, arg3);
  }
  
  public edu.stevens.cs548.clinic.service.web.soap.GetTreatmentsResponseReturn[] getTreatments(long arg0, java.lang.Long[] arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.TreatmentNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    return iProviderWebPort.getTreatments(arg0, arg1);
  }
  
  public void deleteTreatment(long arg0, long arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.TreatmentNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    iProviderWebPort.deleteTreatment(arg0, arg1);
  }
  
  public java.lang.String siteInfo() throws java.rmi.RemoteException{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    return iProviderWebPort.siteInfo();
  }
  
  public long create(java.lang.String arg0, long arg1, java.lang.String arg2) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    return iProviderWebPort.create(arg0, arg1, arg2);
  }
  
  public edu.stevens.cs548.clinic.service.web.ProviderDTO[] getProviderByName(java.lang.String arg0) throws java.rmi.RemoteException{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    return iProviderWebPort.getProviderByName(arg0);
  }
  
  public void deleteProvider(java.lang.String arg0, long arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    iProviderWebPort.deleteProvider(arg0, arg1);
  }
  
  public edu.stevens.cs548.clinic.service.web.soap.GetTreatmentResponseReturn getTreatment(long arg0, long arg1) throws java.rmi.RemoteException, edu.stevens.cs548.clinic.service.web.soap.ProviderNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.TreatmentNotFoundExn, edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn{
    if (iProviderWebPort == null)
      _initIProviderWebPortProxy();
    return iProviderWebPort.getTreatment(arg0, arg1);
  }
  
  
}