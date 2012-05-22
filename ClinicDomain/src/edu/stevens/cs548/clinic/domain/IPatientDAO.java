package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface IPatientDAO {
	
	public static class PatientExn extends Exception {
		private static final long serialVersionUID = 1L;
		public PatientExn (String msg) {
			super(msg);
		}
	}
	
	public static class PatientNotFoundExn extends PatientExn {
		private static final long serialVersionUID = 1L;
		public PatientNotFoundExn (String msg) {
			super(msg);
		}
	}

	public Patient getPatientByPatientID(long pid) throws PatientExn;
	
	public Patient getPatientByDbid(long id) throws PatientExn;
	
	public List<Patient> getPatientByNameDob (String name, Date dob);
	
	public void addPatient(Patient patient) throws PatientExn;
	
	public void deletePatient(Patient pat) throws PatientExn;
}
