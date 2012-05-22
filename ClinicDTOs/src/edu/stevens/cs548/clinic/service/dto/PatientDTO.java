package edu.stevens.cs548.clinic.service.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.domain.Patient;

@XmlRootElement(name="patient-dto",	namespace="http://cs548.stevens.edu/clinic/service/web")
public class PatientDTO {

	public long id;
	
	@XmlElement(name="patient-id")
	public long patientId;
	
	@XmlElement(required=true)
	public String name;
	
	@XmlElement(name="dob", required=true)
	public Date birthDate;
	
	public long[] treatments;;
	
	public PatientDTO (Patient patient) {
		this.id = patient.getId();
		this.patientId = patient.getPatientId();
		this.name = patient.getName();
		this.birthDate = patient.getBirthDate();
		
		List<Long> tids = patient.getTreatmentIds();
		this.treatments = new long[tids.size()];
		for (int i = 0; i < this.treatments.length; i++) {
			this.treatments[i] = tids.get(i);
		}
	}
	
	public PatientDTO () {}
}
