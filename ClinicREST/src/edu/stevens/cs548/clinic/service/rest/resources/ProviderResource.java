package edu.stevens.cs548.clinic.service.rest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.IProviderServiceRemote;
import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.web.rest.ProviderRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;

@Path("/provider")
@Stateless
public class ProviderResource {
    @Context
    private UriInfo context;
	
	Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.service.rest.resources.ProviderResource");

    /**
     * Default constructor. 
     */
    public ProviderResource() {
		logger.info("Calling constructor on ProviderResource");
    }

	@EJB(beanName = "ProviderServiceBean")
	private IProviderServiceRemote providerService;

    /**
     * Retrieves representation of an instance of ProviderResource
     * @return an instance of ProviderRepresentation
     */
    @GET
    @Path("{npi}")
    @Produces("application/xml")
    public ProviderRepresentation getProvider(@PathParam("npi") String npi) {
		try {
			ProviderDTO providerDTO = providerService.getProviderByNPI(
					Long.parseLong(npi));

			// Convert DTO to representation used by REST.
			ProviderRepresentation patientRep = new ProviderRepresentation(
					providerDTO, context);
			return patientRep;
		} catch (ProviderServiceExn e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
    }
    
    /**
     * Retrieves representation of an instance of ProviderResource
     * @return an instance of ProviderRepresentation
     */
    @GET
    @Produces("application/xml")
    public ProviderRepresentation[] getProviderByName(@QueryParam("name") String name) {
		ProviderDTO[] providerDTOs = providerService.getProviderByName(name);
		
		if (providerDTOs.length == 0) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		ProviderRepresentation[] providers = new ProviderRepresentation[providerDTOs.length];
		for (int i = 0; i < providerDTOs.length; i++) {
			providers[i] = new ProviderRepresentation(providerDTOs[i], context);
		}
		return providers;
    }

	@PUT
    @Path("{npi}")
	@Consumes("application/xml")
	public Response addProvider(@PathParam("npi") String npi, 
			ProviderRepresentation provider) {
		try {
			providerService.createProvider(provider.getName(),
					provider.getNpi(), provider.getSpecialization());
			UriBuilder ub = context.getAbsolutePathBuilder().path("{npi}");
			URI url = ub.build(npi);
			return Response.created(url).build();
		} catch (ProviderServiceExn e) {
			throw new WebApplicationException();
		}
	}
	
	@GET
	@Path("{npi}/treatment")
	@Produces("application/xml")
	public TreatmentRepresentation[] getTreatmentsForPatient(@PathParam("npi") String npi,
				@QueryParam("patient") String patientkey) {
		try {
			logger.info("Getting treatment for provider " + npi + " and patient " + patientkey);
			long patientId = Long.parseLong(patientkey);
			long providerId = Long.parseLong(npi);
			long[] allTIds = providerService.getTreatmentIds(providerId);
			
			// Create a list of treatments that are only for the specific patient.
			List<TreatmentRepresentation> treatments = new ArrayList<TreatmentRepresentation>();
			for (long tId : allTIds) {
				TreatmentDto treatmentDto = providerService.getTreatment(providerId, tId);
				if (treatmentDto.getPatientId() == patientId) {
					treatments.add(new TreatmentRepresentation(treatmentDto, context));
				}
			}
			TreatmentRepresentation[] treatmentList = new TreatmentRepresentation[0];
			return treatments.toArray(treatmentList);
		} catch (ProviderNotFoundExn e) {
			logger.severe("Error getting treatments: " + e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		} catch (ProviderServiceExn e) {
			logger.severe("Error getting treatments: " + e.getMessage());
			throw new WebApplicationException();
		}
	}
	
	@GET
    @Path("{npi}/treatment/{tid}")
	@Produces("application/xml")
	public TreatmentRepresentation getTreatment(@PathParam("npi") String npi,
			@PathParam("tid") String treatmentId) {
		try {
			logger.info("Getting treatment with treatmentId: " + treatmentId);
			long tId = Long.parseLong(treatmentId);
			long providerId = Long.parseLong(npi);
			
			return new TreatmentRepresentation(
					providerService.getTreatment(providerId, tId), context);
		} catch (ProviderNotFoundExn e) {
			logger.severe("Error getting treatment: " + e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		} catch (ProviderServiceExn e) {
			logger.severe("Error getting treatment: " + e.getMessage());
			throw new WebApplicationException();
		}
	}
}