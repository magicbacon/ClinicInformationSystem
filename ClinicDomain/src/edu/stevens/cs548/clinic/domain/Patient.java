package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Patient
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(
		name="SearchPatientByNameDOB",
		query="select p from Patient p where p.name = :name and p.birthDate = :dob"),
	@NamedQuery(
			name="SearchPatientByPatientID",
			query="select p from Patient p where p.patientId = :pid")
})
@Table(name="PATIENT")

public class Patient implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	private long patientId;
	private String name;
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@OneToMany(mappedBy = "patient", cascade = REMOVE)
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
		if (t.getPatient() == null || !t.getPatient().equals(this))
			t.setPatient(this);
		this.getTreatments().add(t);
	}
	
	public long addDrugTreatment(Provider provider, String diagnosis, String drug, float dosage) {
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDrug(drug);
		treatment.setDosage(dosage);
		treatment.setProvider(provider);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	
	public long addSurgeryTreatment(Provider provider, String diagnosis, Date date) {
		SurgeryTreatment treatment = new SurgeryTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setProvider(provider);
		treatment.setDate(date);
		this.addTreatment(treatment);
		return treatment.getId();
	}
	
	public long addRadiologyTreatment(Provider provider, String diagnosis, List<Date> dates) {
		RadiologyTreatment treatment = new RadiologyTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setProvider(provider);
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
		if (t.getPatient() != this) {
			throw new TreatmentExn("Inappropriate treatmnet access: patient = " +
					this.getId() + ", treatment = " + tid);
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
		if (t.getPatient() != this) {
			throw new TreatmentExn("Inappropriate treatmnet access: patient = " +
					this.getId() + ", treatment = " + tid);
		}
		
		treatmentDAO.deleteTreatment(t);
	}

	public Patient() {
		super();
		treatments = new ArrayList<Treatment>();
	}
	
}
