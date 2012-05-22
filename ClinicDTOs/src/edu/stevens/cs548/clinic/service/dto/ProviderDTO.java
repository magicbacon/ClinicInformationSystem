package edu.stevens.cs548.clinic.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.domain.Provider;

@XmlRootElement(name="provider-dto", namespace="http://cs548.stevens.edu/clinic/service/web")
public class ProviderDTO {

	public long npi;
	
	@XmlElement(required=true)
	public String name;
	
	@XmlElement(required=true)
	public String specialization;
	
	public long[] treatments;;
	
	public ProviderDTO (Provider provider) {
		this.npi = provider.getNpi();
		this.name = provider.getName();
		this.specialization = provider.getSpecialization().toString();
		
		List<Long> tids = provider.getTreatmentIds();
		this.treatments = new long[tids.size()];
		for (int i = 0; i < this.treatments.length; i++) {
			this.treatments[i] = tids.get(i);
		}
	}
	
	public ProviderDTO () {}
}
