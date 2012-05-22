package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

@WebService(
		name="IProviderWebPort",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap")

/**
 * Endpoint interface for the patient web service.
 * @author daler
 *
 */
public interface IProviderWebService {

	@WebMethod(operationName="create")
	public long createProvider(String name, long npi, String specialization)
			throws ProviderServiceExn;

	@WebMethod
	public ProviderDTO getProviderByNPI(long npi) throws ProviderServiceExn;
	
	@WebMethod
	public ProviderDTO[] getProviderByName (String name);

	@WebMethod
	public void deleteProvider(String name, long npi) throws ProviderServiceExn;
	
	@WebMethod
	public long addDrugTreatment(long id, long patientDbId, String diagnosis, String drug,
			float dosage) throws ProviderNotFoundExn;
	
	@WebMethod
	public long addRadiologyTreatment(long id, long patientDbId, String diagnosis, List<Date> dates)
			throws ProviderNotFoundExn;
	
	@WebMethod
	public long addSurgeryTreatment(long id, long patientDbId, String diagnosis, Date date)
			throws ProviderNotFoundExn;
	
	@WebMethod
	public TreatmentDto[] getTreatments(long id, long[] treatmentIds)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;
	
	public TreatmentDto getTreatment(long id, long treatmentId)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;

	@WebMethod
	public void deleteTreatment(long id, long treatmentId)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;

	@WebMethod
	public String siteInfo();
}
