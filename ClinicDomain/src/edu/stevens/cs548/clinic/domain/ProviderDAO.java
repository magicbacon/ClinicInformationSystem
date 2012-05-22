package edu.stevens.cs548.clinic.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ProviderDAO implements IProviderDAO {
	
	private EntityManager em;
	private TreatmentDAO treatmentDAO;

	@Override
	public Provider getProviderByNPI(long npi) throws ProviderExn {
		Provider provider = em.find(Provider.class, npi);
		
		if (provider == null)
			throw new ProviderExn("Provider not found. NPI = " + npi);

		provider.setTreatmentDAO(treatmentDAO);
		return provider;
	}

	@Override
	public List<Provider> getProviderByName(String name) {
		TypedQuery<Provider> query =
				em.createNamedQuery("SearchProviderByName", Provider.class).
				setParameter("name", name);
		List<Provider> providers = query.getResultList();
		
		for (Provider p : providers) {
			p.setTreatmentDAO(treatmentDAO);
		}
		return providers;
	}

	@Override
	public void addProvider(Provider provider) throws ProviderExn {
		// Ensure there is not already a provider with this npi.
		if (providerExists(provider.getNpi()))
			throw new ProviderExn("Provider with NPI " + provider.getNpi() +
					" already exists.");
		
		em.persist(provider);
		provider.setTreatmentDAO(treatmentDAO);
	}

	@Override
	public void deleteProvider(Provider provider) throws ProviderExn {
		// Ensure there is a provider with this npi.
		if (!providerExists(provider.getNpi()))
			throw new ProviderExn("Provider with NPI " + provider.getNpi() +
					" does not exist.");
		
		em.remove(provider);
	}
	
	/**
	 * Helper function to check if a provider with a given NPI already exists.
	 * @param npo The provider id.
	 * @return True if provider exists.
	 */
	private boolean providerExists(long npi) {
		return (em.find(Provider.class, npi) != null);
	}
	
	public ProviderDAO (EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

}
