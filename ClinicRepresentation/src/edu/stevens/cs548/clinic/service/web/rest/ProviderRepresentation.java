package edu.stevens.cs548.clinic.service.web.rest;

import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.ProviderType;

@XmlRootElement(name="provider-rep", namespace="http://cs548.stevens.edu/clinic/service/web/rest/data")
public class ProviderRepresentation extends ProviderType {

	public ProviderRepresentation() {
		super();
	}

	public ProviderRepresentation(ProviderDTO dto, UriInfo uri) {
		super();
		this.npi = dto.npi;
		this.name = dto.name;
		this.specialization = dto.specialization;
		
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
			// TODO: since I'm using a param, do I really need to clone each time?
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
