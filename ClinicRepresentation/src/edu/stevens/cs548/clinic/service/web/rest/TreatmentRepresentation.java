package edu.stevens.cs548.clinic.service.web.rest;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.web.rest.data.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.RadiologyType;
import edu.stevens.cs548.clinic.service.web.rest.data.SurgeryType;
import edu.stevens.cs548.clinic.service.web.rest.data.TreatmentType;

@XmlRootElement(name="treatment-rep", namespace="http://cs548.stevens.edu/clinic/service/web/rest/data")
public class TreatmentRepresentation extends TreatmentType {
	
	public TreatmentRepresentation() {
		
	}

	public TreatmentRepresentation(TreatmentDto treatmentDto, UriInfo context) {
		// Copy common attributes.
		this.diagnosis = treatmentDto.getDiagnosis();
		this.id = treatmentDto.getId();
		
		// Create the appropriate treatment object.
		if (treatmentDto.getDrugTreatment() != null) {
			edu.stevens.cs548.clinic.service.dto.DrugTreatmentType type = treatmentDto.getDrugTreatment();
			this.drugTreatment = new DrugTreatmentType();
			this.drugTreatment.setDosage(type.getDosage());
			this.drugTreatment.setName(type.getName());
		} else if (treatmentDto.getRadiology() != null) {
			edu.stevens.cs548.clinic.service.dto.RadiologyType type = treatmentDto.getRadiology();
			this.radiology = new RadiologyType();
			this.radiology.getDate().addAll(type.getDate());
		} else if (treatmentDto.getSurgery() != null) {
			edu.stevens.cs548.clinic.service.dto.SurgeryType type = treatmentDto.getSurgery();
			this.surgery = new SurgeryType();
			this.surgery.setDate(type.getDate());
		}
		
		// Create links.
		UriBuilder ub = context.getBaseUriBuilder();
		ub.path("{resource}").path("{id}");
		
		this.patient = new LinkType();
		this.patient.setMediaType(Representation.MEDIA_TYPE);
		this.patient.setRelation(Representation.RELATION_PATIENT);
		this.patient.setUrl(ub.build("patient", treatmentDto.getPatientId()).toString());

		this.provider = new LinkType();
		this.provider.setMediaType(Representation.MEDIA_TYPE);
		this.provider.setRelation(Representation.RELATION_PROVIDER);
		this.provider.setUrl(ub.build("provider", treatmentDto.getProviderId()).toString());
	}

	public LinkType getLinkPatient() {
		return this.getPatient();
	}
	
	public LinkType getLinkProvider() {
		return this.getProvider();
	}
}

