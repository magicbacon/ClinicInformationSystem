package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface ITreatmentVisitor {

	public void visitDrugTreatment(
		long tid, Provider provider, Patient patient, String diagnosis, String drug, float dosage);
	
	public void visitRadiology(
		long tid, Provider provider, Patient patient, String diagnosis, List<Date> dates);
	
	public void visitSurgery(
		long tid, Provider provider, Patient patient, String diagnosis, Date date);
}
