package edu.stevens.cs548.clinic.domain;

import javax.persistence.EntityManager;

public class TreatmentDAO implements ITreatmentDAO {
	
	private EntityManager em;

	@Override
	public Treatment getTreatmentByDbID(long id) throws TreatmentExn {
		Treatment t = em.find(Treatment.class, id);
		if (t == null)
			throw new TreatmentExn("Missing Treatment: id = " + id);
		return t;
	}

	@Override
	public void addTreatment(Treatment t) {
		em.persist(t);
	}

	@Override
	public void deleteTreatment(Treatment t) throws TreatmentExn {
		if (!treatmentExists(t.getId()))
			throw new TreatmentExn("Treatment not found and cannot be deleted. Id = " + t.getId());
		em.remove(t);
	}
	
	private boolean treatmentExists(long tid) {
		return (em.find(Treatment.class, tid) != null);
	}
	
	public TreatmentDAO (EntityManager em) {
		this.em = em;
	}
}
