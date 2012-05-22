package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

@WebService(
		name="IPatientWebPort",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap")

/**
 * Endpoint interface for the patient web service.
 * @author daler
 *
 */
public interface IPatientWebService {

	@WebMethod(operationName="create")
	public long createPatient(String name, Date dob, long patientId)
			throws PatientServiceExn;

	@WebMethod
	public PatientDTO getPatientByDbId(long id) throws PatientNotFoundExn;

	@WebMethod
	public PatientDTO getPatientByPatientId(long patientId)
			throws PatientServiceExn;

	@WebMethod
	public PatientDTO[] getPatientsByNameDob(String name, Date dob)
			throws PatientServiceExn;

	@WebMethod
	public void deletePatient(String name, long id) throws PatientServiceExn;

	@WebMethod
	public long addDrugTreatment(long id, long providerId, String diagnosis, String drug,
			float dosage) throws PatientNotFoundExn;
	
	@WebMethod
	public long addRadiologyTreatment(long id, long providerId, String diagnosis, List<Date> dates)
			throws PatientNotFoundExn;
	
	@WebMethod
	public long addSurgeryTreatment(long id, long providerId, String diagnosis, Date date) throws PatientNotFoundExn;
	
	@WebMethod
	public TreatmentDto[] getTreatments(long id, long[] treatmentIds)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;

	@WebMethod
	public void deleteTreatment(long id, long treatmentId)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;

	@WebMethod
	public String siteInfo();
}
