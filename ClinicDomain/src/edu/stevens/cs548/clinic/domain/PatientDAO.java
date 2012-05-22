package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class PatientDAO implements IPatientDAO {

	Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.domain.PatientDAO");
	
	private EntityManager em;
	private TreatmentDAO treatmentDAO;

	@Override
	public Patient getPatientByDbid(long id) throws PatientExn {
		Patient patient = em.find(Patient.class, id);
		
		logger.info(patient.getTreatmentIds().size() + " treatments found.");

		patient.setTreatmentDAO(treatmentDAO);
		return patient;
	}

	@Override
	public Patient getPatientByPatientID(long pid) throws PatientExn {
		TypedQuery<Patient> query =
				em.createNamedQuery("SearchPatientByPatientID", Patient.class).
				setParameter("pid", pid);
		List<Patient> patients = query.getResultList();
		
		// Return NULL instead of throwing an exception so clients can 
		// differentiate for not found exception.
		if (patients.size() == 0)
//			throw new PatientExn("Patient not found. Patient id = " + pid);
			return null;
		if (patients.size() > 1)
			throw new PatientExn("Duplicate patient records found. Patient id = " + pid);
		
		Patient patient = patients.get(0);
		patient.setTreatmentDAO(treatmentDAO);
		return patient;
	}

	@Override
	public List<Patient> getPatientByNameDob(String name, Date dob) {
		TypedQuery<Patient> query =
				em.createNamedQuery("SearchPatientByNameDOB", Patient.class).
				setParameter("name", name).
				setParameter("dob", dob);
		List<Patient> patients = query.getResultList();
		
		for (Patient p : patients) {
			p.setTreatmentDAO(treatmentDAO);
		}
		return patients;
	}

	@Override
	public void addPatient(Patient patient) throws PatientExn {
		// Check if there is already a patient with this pid.
		if (patientExists(patient.getPatientId()))
			throw new PatientExn("Patient with patient ID " + patient.getPatientId() +
					" already exists.");
		
		em.persist(patient);
		patient.setTreatmentDAO(treatmentDAO);
	}

	@Override
	public void deletePatient(Patient patient) throws PatientExn {
		// Ensure there is already a patient with this pid.
		if (!patientExists(patient.getPatientId()))
			throw new PatientExn("Patient with patient ID " + patient.getPatientId() +
					" does not exist.");
		
		em.remove(patient);
	}
	
	/**
	 * Helper function to check if a patient with a given patient Id already exists.
	 * @param pid The patient id.
	 * @return True if patient exists.
	 */
	private boolean patientExists(long pid) {
		TypedQuery<Patient> query =
				em.createNamedQuery("SearchPatientByPatientID", Patient.class).
				setParameter("pid", pid);
		return (query.getResultList().size() > 0);
	}
	
	public PatientDAO (EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

}
