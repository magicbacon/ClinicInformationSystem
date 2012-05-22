package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

/**
 * Entity implementation class for Entity: Provider
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(
		name="SearchProviderByName",
		query="select p from Provider p where p.name = :name"),
	@NamedQuery(
			name="SearchProviderByNPI",
			query="select p from Provider p where p.npi = :npi")
})
@Table(name="PROVIDER")

public class Provider implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private long npi;
	private String name;
	private String specialization;
	
	public long getNpi() {
		return npi;
	}

	public void setNpi(long npi) {
		this.npi = npi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	@OneToMany(mappedBy = "provider", cascade = CascadeType.PERSIST)
	@OrderBy
	private List<Treatment> treatments;
	
	protected List<Treatment> getTreatments() {
		return treatments;
	}

	protected void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO(ITreatmentDAO treatmentDAO) {
		this.treatmentDAO = treatmentDAO;
	}
	
	void addTreatment (Treatment t) {
		this.treatmentDAO.addTreatment(t);
		if (t.getProvider() == null || !t.getProvider().equals(this))
			t.setProvider(this);
		this.getTreatments().add(t);
	}

	public long addDrugTreatment(Patient patient, String diagnosis, String drug, float dosage) {
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDrug(drug);
		treatment.setDosage(dosage);
		treatment.setPatient(patient);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	
	public long addSurgeryTreatment(Patient patient, String diagnosis, Date date) {
		SurgeryTreatment treatment = new SurgeryTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setPatient(patient);
		treatment.setDate(date);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	
	public long addRadiologyTreatment(Patient patient, String diagnosis, List<Date> dates) {
		RadiologyTreatment treatment = new RadiologyTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setPatient(patient);
		treatment.setDates(dates);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	
	public List<Long> getTreatmentIds() {
		List<Long> tids = new ArrayList<Long>();
		for (Treatment t : this.getTreatments()) {
			tids.add(t.getId());
		}
		return tids;
	}
	
	public void visitTreatment(long tid, ITreatmentVisitor visitor) throws TreatmentExn {
		Treatment t = treatmentDAO.getTreatmentByDbID(tid);
		
		// Basic sanity check.
		if (t.getProvider() != this) {
			throw new TreatmentExn("Inappropriate treatment access: Provider = " +
					this.getNpi() + ", treatment = " + tid);
		}
		t.visit(visitor);
	}
	
	public void visitTreatments (ITreatmentVisitor visitor) {
		for (Treatment t : this.getTreatments()) {
			t.visit(visitor);
		}
	}
	
	public void deleteTreatment(long tid) throws TreatmentExn {
		Treatment t = treatmentDAO.getTreatmentByDbID(tid);
		
		// Basic sanity check.
		if (t.getProvider() != this) {
			throw new TreatmentExn("Inappropriate treatment access: Provider = " +
					this.getNpi() + ", treatment = " + tid);
		}
		
		treatmentDAO.deleteTreatment(t);
	}
	
	public Provider() {
		super();
		treatments = new ArrayList<Treatment>();
	}
   
}
