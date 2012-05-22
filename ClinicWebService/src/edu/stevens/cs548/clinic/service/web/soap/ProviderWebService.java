package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.IProviderServiceRemote;
import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

@WebService(
		endpointInterface="edu.stevens.cs548.clinic.service.web.soap.IProviderWebService",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap",
		serviceName="ProviderWebService",
		portName="ProviderWebPort")
public class ProviderWebService implements IProviderWebService {

	@EJB(beanName="ProviderServiceBean")
	IProviderServiceRemote service;

	@Override
	public long createProvider(String name, long npi, String specialization)
			throws ProviderServiceExn {
		return service.createProvider(name, npi, specialization);
	}

	@Override
	public ProviderDTO getProviderByNPI(long npi) throws ProviderServiceExn {
		return service.getProviderByNPI(npi);
	}

	@Override
	public ProviderDTO[] getProviderByName(String name) {
		return service.getProviderByName(name);
	}

	@Override
	public void deleteProvider(String name, long npi) throws ProviderServiceExn {
		service.deleteProvider(name, npi);
	}

	@Override
	public long addDrugTreatment(long id, long patientDbId, String diagnosis,
			String drug, float dosage) throws ProviderNotFoundExn {
		return service.addDrugTreatment(id, patientDbId, diagnosis, drug, dosage);
	}

	@Override
	public long addRadiologyTreatment(long id, long patientDbId,
			String diagnosis, List<Date> dates) throws ProviderNotFoundExn {
		return service.addRadiologyTreatment(id, patientDbId, diagnosis, dates);
	}

	@Override
	public long addSurgeryTreatment(long id, long patientDbId,
			String diagnosis, Date date) throws ProviderNotFoundExn {
		return service.addSurgeryTreatment(id, patientDbId, diagnosis, date);
	}

	@Override
	public TreatmentDto[] getTreatments(long id, long[] treatmentIds)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn {
		return service.getTreatments(id, treatmentIds);
	}

	@Override
	public TreatmentDto getTreatment(long id, long treatmentId)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn {
		return service.getTreatment(id, treatmentId);
	}

	@Override
	public void deleteTreatment(long id, long treatmentId)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn {
		service.deleteTreatment(id, treatmentId);
	}

	@Override
	public String siteInfo() {
		return service.siteInfo();
	}

}
