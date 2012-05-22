package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;

import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

/**
 * Class that uses the visitor pattern to set the appropriate type of Treatment DTO.
 * @author daler
 *
 */
class TreatmentPDOtoDTO implements ITreatmentVisitor {

	private TreatmentDto dto;

	public TreatmentDto getDTO() {
		return dto;
	}

	@Override
	public void visitDrugTreatment(long tid, Provider provider,
			Patient patient, String diagnosis, String drug, float dosage) {
		dto = new TreatmentDto();
		dto.setId(tid);
		dto.setDiagnosis(diagnosis);
		dto.setPatientId(patient.getId());
		dto.setProviderId(provider.getNpi());
		DrugTreatmentType t = new DrugTreatmentType();
		t.setDosage(dosage);
		t.setName(drug);
		dto.setDrugTreatment(t);
	}

	@Override
	public void visitRadiology(long tid, Provider provider,
			Patient patient, String diagnosis, List<Date> dates) {
		dto = new TreatmentDto();
		dto.setId(tid);
		dto.setDiagnosis(diagnosis);
		dto.setPatientId(patient.getId());
		dto.setProviderId(provider.getNpi());
		RadiologyType t = new RadiologyType();
		t.getDate().addAll(dates);
		dto.setRadiology(t);
	}

	@Override
	public void visitSurgery(long tid, Provider provider, Patient patient,
			String diagnosis, Date date) {
		dto = new TreatmentDto();
		dto.setId(tid);
		dto.setDiagnosis(diagnosis);
		dto.setPatientId(patient.getId());
		dto.setProviderId(provider.getNpi());
		SurgeryType t = new SurgeryType();
		t.setDate(date);
		dto.setSurgery(t);
	}

}