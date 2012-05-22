package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;

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
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.service.IProviderService;
import edu.stevens.cs548.clinic.service.IProviderServiceLocal;
import edu.stevens.cs548.clinic.service.IProviderServiceRemote;
import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

/**
 * Session Bean implementation class ProviderService
 */
@Stateless(name = "ProviderServiceBean")
public class ProviderService implements IProviderServiceLocal,
		IProviderServiceRemote {

	private ProviderFactory providerFactory;
	private IProviderDAO providerDAO;
	private IPatientDAO patientDAO;

	/**
	 * Default constructor.
	 */
	public ProviderService() {
		providerFactory = new ProviderFactory();
	}

	@PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;

	@SuppressWarnings("unused")
	@PostConstruct
	private void initialize() {
		providerDAO = new ProviderDAO(em);
		patientDAO = new PatientDAO(em);
	}

	/**
	 * @throws ProviderServiceExn
	 * @see IProviderService#createProvider(String, long, Specialization)
	 */
	public long createProvider(String name, long npi, String specialization)
			throws ProviderServiceExn {
		Provider provider = this.providerFactory.createProvider(npi, name, specialization);
		try {
			providerDAO.addProvider(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
		return provider.getNpi();
	}

	/**
	 * @throws ProviderExn 
	 * @see IProviderService#getProviderByNPI(long)
	 */
	public ProviderDTO getProviderByNPI(long npi) throws ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(npi);
			return new ProviderDTO(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	/**
	 * @see IProviderService#getProviderByName(String)
	 */
	public ProviderDTO[] getProviderByName(String name) {
		List<Provider> providers = providerDAO.getProviderByName(name);

		ProviderDTO[] providerDTOs = new ProviderDTO[providers.size()];
		for (int i = 0; i < providerDTOs.length; i++) {
			providerDTOs[i] = new ProviderDTO(providers.get(i));
		}
		return providerDTOs;
	}

	/**
	 * @throws ProviderServiceExn
	 * @see IProviderService#deleteProvider(String, long)
	 */
	public void deleteProvider(String name, long npi) throws ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(npi);
			if (!name.equals(provider.getName())) {
				throw new ProviderServiceExn(
						"Provider name does not match id, " + name + ", " + npi);
			}
			providerDAO.deleteProvider(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	/**
	 * @see IProviderService#getTreatments(long, long[])
	 */
	@Override
	public TreatmentDto[] getTreatments(long id, long[] treatmentIds)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(id);
			TreatmentDto[] treatments = new TreatmentDto[treatmentIds.length];
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
			for (int i = 0; i < treatments.length; i++) {
				provider.visitTreatment(treatmentIds[i], visitor);
				treatments[i] = visitor.getDTO();
			}
			return treatments;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public TreatmentDto getTreatment(long id, long treatmentId)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(id);
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
			provider.visitTreatment(treatmentId, visitor);
			return visitor.getDTO();
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public long[] getTreatmentIds(long id) throws ProviderNotFoundExn,
			ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(id);
			List<Long> tIds = provider.getTreatmentIds();
			long[] treatmentIds = new long[tIds.size()];
			for (int i = 0; i < tIds.size(); i++) {
				treatmentIds[i] =  tIds.get(i).longValue();
			}
			return treatmentIds;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		}
	}

	/**
	 * @throws ProviderNotFoundExn
	 * @see IProviderService#addDrugTreatment(long, String, String, float)
	 */
	@Override
	public long addDrugTreatment(long id, long patientDbId, String diagnosis,
			String drug, float dosage) throws ProviderNotFoundExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(id);
			Patient patient = patientDAO.getPatientByDbid(patientDbId);
			long tid = provider.addDrugTreatment(patient, diagnosis, drug, dosage);
			return tid;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (PatientExn e) {
			throw new ProviderNotFoundExn(e.toString());
		}
	}

	/**
	 * @throws ProviderNotFoundExn
	 * @see IProviderService#addRadiologyTreatment(long, String, List<Date>)
	 */
	@Override
	public long addRadiologyTreatment(long id, long patientDbId,
			String diagnosis, List<Date> dates) throws ProviderNotFoundExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(id);
			Patient patient = patientDAO.getPatientByDbid(patientDbId);
			long tid = provider.addRadiologyTreatment(patient, diagnosis, dates);
			return tid;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (PatientExn e) {
			throw new ProviderNotFoundExn(e.toString());
		}
	}

	/**
	 * @throws ProviderNotFoundExn
	 * @see IProviderService#addSurgeryTreatment(long, String, Date)
	 */
	@Override
	public long addSurgeryTreatment(long id, long patientDbId, String diagnosis,
			Date date) throws ProviderNotFoundExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(id);
			Patient patient = patientDAO.getPatientByDbid(patientDbId);
			long tid = provider.addSurgeryTreatment(patient, diagnosis, date);
			return tid;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (PatientExn e) {
			throw new ProviderNotFoundExn(e.toString());
		}
	}

	/**
	 * @see IProviderService#deleteTreatment(long, long)
	 */
	@Override
	public void deleteTreatment(long id, long treatmentId)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByNPI(id);
			provider.deleteTreatment(treatmentId);
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new TreatmentNotFoundExn(e.toString());
		}
	}

	@Resource(name = "SiteInfo")
	private String siteInformation;

	/**
	 * @see IProviderService#siteInfo()
	 */
	@Override
	public String siteInfo() {
		return siteInformation;
	}

}
