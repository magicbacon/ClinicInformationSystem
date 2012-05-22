package edu.stevens.cs548.clinic.service;

import java.util.Date;
import java.util.List;

import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

public interface IProviderService {

	public class ProviderServiceExn extends Exception {
		private static final long serialVersionUID = 1L;

		public ProviderServiceExn(String m) {
			super(m);
		}
	}
	
	public class ProviderNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;

		public ProviderNotFoundExn(String m) {
			super(m);
		}
	}
	
	public class TreatmentNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;

		public TreatmentNotFoundExn(String m) {
			super(m);
		}
	}
	
	public long createProvider(String name, long npi, String specialization)
			throws ProviderServiceExn;

	public ProviderDTO getProviderByNPI(long npi) throws ProviderServiceExn;
	
	public ProviderDTO[] getProviderByName (String name);

	public void deleteProvider(String name, long npi) throws ProviderServiceExn;
	
	public long addDrugTreatment(long id, long patientDbId, String diagnosis, String drug,
			float dosage) throws ProviderNotFoundExn;
	
	public long addRadiologyTreatment(long id, long patientDbId, String diagnosis, List<Date> dates)
			throws ProviderNotFoundExn;
	
	public long addSurgeryTreatment(long id, long patientDbId, String diagnosis, Date date)
			throws ProviderNotFoundExn;
	
	public TreatmentDto[] getTreatments(long id, long[] treatmentIds)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;
	
	public TreatmentDto getTreatment(long id, long treatmentId)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;
	
	public long[] getTreatmentIds(long id)
			throws ProviderNotFoundExn, ProviderServiceExn;

	public void deleteTreatment(long id, long treatmentId)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;

	public String siteInfo();
}
