package edu.stevens.cs548.clinic.service.web.rest;

import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.PatientType;

/**
 * This class allows you to associate behavior with the JAXB representation
 * class by extending it.
 * @author daler
 *
 */
@XmlRootElement(
	name="patient-rep",
	namespace="http://cs548.stevens.edu/clinic/service/web/rest/data")
public class PatientRepresentation extends PatientType {
	
	public PatientRepresentation() {
		super();
	}

	public PatientRepresentation(PatientDTO dto, UriInfo uri) {
		super();
		this.id = dto.id;
		this.patientId = dto.patientId;
		this.dob = dto.birthDate;
		this.name = dto.name;
		
		long[] treatments = dto.treatments;
		
		// Build treatment links from treatment ids.
		// Use getTreatments since treatments field will be null.
		UriBuilder ub = uri.getBaseUriBuilder();
		ub.path("treatment");
		for (long t: treatments) {
			LinkType link = new LinkType();
			link.setRelation(Representation.RELATION_TREATMENT);
			link.setMediaType(Representation.MEDIA_TYPE);
			
			// Generate treatment URI.
			// URL builder is modified in place so need a new clone each time.
			UriBuilder ubTreatment = ub.clone().path("{tid}");
			String treatmentURI = ubTreatment.build(Long.toString(t)).toString();
			link.setUrl(treatmentURI);
			
			this.getTreatments().add(link);
		}
	}
	
	public List<LinkType> getLinksTreatments() {
		return this.getTreatments();
	}
}