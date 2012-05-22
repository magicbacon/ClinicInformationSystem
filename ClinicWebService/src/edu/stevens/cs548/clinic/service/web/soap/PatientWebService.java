package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.IPatientServiceRemote;
import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

@WebService(
		endpointInterface="edu.stevens.cs548.clinic.service.web.soap.IPatientWebService",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap",
		serviceName="PatientWebService",
		portName="PatientWebPort")
public class PatientWebService implements IPatientWebService {

	@EJB(beanName="PatientServiceBean")
	IPatientServiceRemote service;
	
	@Override
	public long createPatient(String name, Date dob, long patientId)
			throws PatientServiceExn {
		return service.createPatient(name, dob, patientId);
	}

	@Override
	public PatientDTO getPatientByDbId(long id) throws PatientNotFoundExn {
		return service.getPatientByDbId(id);
	}

	@Override
	public PatientDTO getPatientByPatientId(long patientId)
			throws PatientServiceExn {
		return service.getPatientByPatientId(patientId);
	}

	@Override
	public PatientDTO[] getPatientsByNameDob(String name, Date dob)
			throws PatientServiceExn {
		return service.getPatientsByNameDob(name, dob);
	}

	@Override
	public void deletePatient(String name, long id) throws PatientServiceExn {
		service.deletePatient(name, id);
	}

	@Override
	public long addDrugTreatment(long id, long providerId, String diagnosis, String drug,
			float dosage) throws PatientNotFoundExn {
		return service.addDrugTreatment(id, providerId, diagnosis, drug, dosage);
	}

	@Override
	public long addRadiologyTreatment(long id, long providerId, String diagnosis, List<Date> dates)
			throws PatientNotFoundExn {
		return service.addRadiologyTreatment(id, providerId, diagnosis, dates);
	}

	@Override
	public long addSurgeryTreatment(long id, long providerId, String diagnosis, Date date)
			throws PatientNotFoundExn {
		return service.addSurgeryTreatment(id, providerId, diagnosis, date);
	}

	@Override
	public TreatmentDto[] getTreatments(long id, long[] treatmentIds)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		return service.getTreatments(id, treatmentIds);
	}

	@Override
	public void deleteTreatment(long id, long treatmentId)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		service.deleteTreatment(id, treatmentId);
	}

	@Override
	public String siteInfo() {
		return service.siteInfo();
	}

}
