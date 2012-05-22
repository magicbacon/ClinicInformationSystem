package edu.stevens.cs548.clinic.service.rest.resources;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.IPatientServiceRemote;

@Path("/treatment")
@Stateless
public class TreatmentResource {
	@Context
	private UriInfo context;
	
	Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.service.rest.resources.TreatmentResource");

	/**
	 * Default constructor.
	 */
	public TreatmentResource() {
		logger.info("Calling constructor on ProviderResource");
	}

	@EJB(beanName = "PatientServiceBean")
	private IPatientServiceRemote patientService;
	
	
	@DELETE
	@Path("{id}")
	public Response deleteTreatment(@HeaderParam("Authorization") String patientLink,
			@PathParam("id") String treatmentId) {
		logger.info("Deleting treatment Id " + treatmentId + " for patient " + patientLink);
		long pId = Utils.parseIdFromLink(patientLink);
		long tId = Long.parseLong(treatmentId);
		
		try {
			patientService.deleteTreatment(pId, tId);
		} catch (PatientNotFoundExn e) {
			logger.severe("Error deleting treatment: " + e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		} catch (TreatmentNotFoundExn e) {
			logger.severe("Error deleting treatment: " + e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		} catch (PatientServiceExn e) {
			logger.severe("Error deleting treatment: " + e.getMessage());
			throw new WebApplicationException();
		}
		
		return Response.ok().build();
	}
}