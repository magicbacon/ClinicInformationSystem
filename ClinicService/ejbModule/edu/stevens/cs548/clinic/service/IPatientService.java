package edu.stevens.cs548.clinic.service;

import java.util.Date;
import java.util.List;

import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

public interface IPatientService {

	public class PatientServiceExn extends Exception {
		private static final long serialVersionUID = 1L;

		public PatientServiceExn(String m) {
			super(m);
		}
	}
	
	public class PatientNotFoundExn extends PatientServiceExn {
		private static final long serialVersionUID = 1L;

		public PatientNotFoundExn(String m) {
			super(m);
		}
	}
	
	public class TreatmentNotFoundExn extends PatientServiceExn {
		private static final long serialVersionUID = 1L;

		public TreatmentNotFoundExn(String m) {
			super(m);
		}
	}

	public long createPatient(String name, Date dob, long patientId)
			throws PatientServiceExn;

	public PatientDTO getPatientByDbId(long id) throws PatientNotFoundExn;

	public PatientDTO getPatientByPatientId(long patientId)
			throws PatientServiceExn;

	public PatientDTO[] getPatientsByNameDob(String name, Date dob)
			throws PatientServiceExn;

	public void deletePatient(String name, long id) throws PatientNotFoundExn, PatientServiceExn;

	public long addDrugTreatment(long id, long providerId, String diagnosis, String drug,
			float dosage) throws PatientNotFoundExn;
	
	public long addRadiologyTreatment(long id, long providerId, String diagnosis, List<Date> dates)
			throws PatientNotFoundExn;
	
	public long addSurgeryTreatment(long id, long providerId, String diagnosis, Date date)
			throws PatientNotFoundExn;
	
	public TreatmentDto[] getTreatments(long id, long[] treatmentIds)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;

	public void deleteTreatment(long id, long treatmentId)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;

	public String siteInfo();
}
