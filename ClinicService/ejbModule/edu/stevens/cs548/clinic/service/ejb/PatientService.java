package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.service.IPatientService;
import edu.stevens.cs548.clinic.service.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.IPatientServiceRemote;
import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name="PatientServiceBean")
public class PatientService implements IPatientServiceLocal,
		IPatientServiceRemote {

	Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.service.ejb.PatientService");

	private PatientFactory patientFactory;
	private IPatientDAO patientDAO;
	private IProviderDAO providerDAO;

	/**
	 * Default constructor.
	 */
	public PatientService() {
		patientFactory = new PatientFactory();
	}

	@PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initialize() {
		patientDAO = new PatientDAO(em);
		providerDAO = new ProviderDAO(em);
	}

	/**
	 * @throws PatientServiceExn
	 * @see IPatientService#getPatientByDbId(long)
	 */
	public PatientDTO getPatientByDbId(long id) throws PatientNotFoundExn {
		try {
			Patient patient = patientDAO.getPatientByDbid(id);
			return new PatientDTO(patient);
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		}
	}

	/**
	 * @throws PatientServiceExn
	 * @see IPatientService#getPatientByPatientId(long)
	 */
	public PatientDTO getPatientByPatientId(long patientId)
			throws PatientServiceExn {
		try {
			Patient patient = patientDAO.getPatientByPatientID(patientId);
			if (patient == null)
				throw new PatientNotFoundExn("Patient with id " + patientId + "not found");
			return new PatientDTO(patient);
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * @see IPatientService#getPatientsByNameDob(String, Date)
	 */
	public PatientDTO[] getPatientsByNameDob(String name, Date dob) {
		List<Patient> patients = patientDAO.getPatientByNameDob(name, dob);

		PatientDTO[] patientDTOs = new PatientDTO[patients.size()];
		for (int i = 0; i < patientDTOs.length; i++) {
			patientDTOs[i] = new PatientDTO(patients.get(i));
		}
		return patientDTOs;
	}

	/**
	 * @throws PatientServiceExn
	 * @see IPatientService#deletePatient(String, long)
	 */
	public void deletePatient(String name, long id) throws PatientServiceExn {
		Patient patient;
		try {
			patient = patientDAO.getPatientByDbid(id);
			if (!name.equals(patient.getName())) {
				throw new PatientServiceExn("Patient name does not match id, "
						+ name + ", " + id);
			}
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		}

		try {
			patientDAO.deletePatient(patient);
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * @throws PatientServiceExn
	 * @see IPatientService#createPatient(String, Date, long)
	 */
	public long createPatient(String name, Date dob, long patientId)
			throws PatientServiceExn {
		Patient patient = this.patientFactory.createPatient(patientId, name,
				dob);
		try {
			patientDAO.addPatient(patient);
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
		return patient.getId();
	}

	@Override
	public long addDrugTreatment(long id, long providerId, String diagnosis, String drug,
			float dosage) throws PatientNotFoundExn {
		try {
			logger.info("Adding drug treatment for patient. id=" + id + ", providerId=" +
					providerId + ", diagnosis=" + diagnosis + "drug=" + drug + ", dosage=" + dosage);
			Patient patient = patientDAO.getPatientByDbid(id);
			Provider provider = providerDAO.getProviderByNPI(providerId);
			long tid = patient.addDrugTreatment(provider, diagnosis, drug, dosage);
			return tid;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			throw new PatientNotFoundExn(e.toString());
		}
	}

	@Override
	public long addRadiologyTreatment(long id, long providerId, String diagnosis, List<Date> dates)
			throws PatientNotFoundExn {
		try {
			Patient patient = patientDAO.getPatientByDbid(id);
			Provider provider = providerDAO.getProviderByNPI(providerId);
			long tid = patient.addRadiologyTreatment(provider, diagnosis, dates);
			return tid;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			throw new PatientNotFoundExn(e.toString());
		}
	}

	@Override
	public long addSurgeryTreatment(long id, long providerId, String diagnosis, Date date)
			throws PatientNotFoundExn {
		try {
			Patient patient = patientDAO.getPatientByDbid(id);
			Provider provider = providerDAO.getProviderByNPI(providerId);
			long tid = patient.addSurgeryTreatment(provider, diagnosis, date);
			return tid;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (ProviderExn e) {
			throw new PatientNotFoundExn(e.toString());
		}
	}

	@Override
	public TreatmentDto[] getTreatments(long id, long[] treatmentIds)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		try {
			logger.info("Getting treatments for patient. id=" + id + " treatmentIds=" + treatmentIds);
			Patient patient = patientDAO.getPatientByDbid(id);
			TreatmentDto[] treatments = new TreatmentDto[treatmentIds.length];
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
			for (int i = 0; i < treatments.length; i++) {
				patient.visitTreatment(treatmentIds[i], visitor);
				treatments[i] = visitor.getDTO();
				logger.info("Getting treatment " + treatments[i].getId());
			}
			logger.info("Done getting treatments");
			return treatments;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public void deleteTreatment(long id, long treatmentId)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		try {
			Patient patient = patientDAO.getPatientByDbid(id);
			patient.deleteTreatment(treatmentId);
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new TreatmentNotFoundExn(e.toString());
		}
	}
	
	@Resource(name="SiteInfo")
	private String siteInformation;

	@Override
	public String siteInfo() {
		return siteInformation;
	}

}
