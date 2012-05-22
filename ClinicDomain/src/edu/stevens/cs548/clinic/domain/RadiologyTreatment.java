package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: RadiologyTreatment
 *
 */
@Entity
@DiscriminatorValue("R")

public class RadiologyTreatment extends Treatment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@ElementCollection
	private List<Date> dates;
	
	public RadiologyTreatment() {
		super();
		this.setTreatmentType("R");
	}

	public List<Date> getDates() {
		return dates;
	}

	public void setDates(List<Date> dates) {
		this.dates = dates;
	}

	@Override
	public void visit(ITreatmentVisitor visitor) {
		visitor.visitRadiology(
			this.getId(), this.getProvider(), this.getPatient(), this.getDiagnosis(), this.getDates());	
	}
   
}
