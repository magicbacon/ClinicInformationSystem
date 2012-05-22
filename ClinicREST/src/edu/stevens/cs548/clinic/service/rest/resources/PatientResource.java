package edu.stevens.cs548.clinic.service.rest.resources;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import javax.xml.bind.DatatypeConverter;

import edu.stevens.cs548.clinic.service.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.IPatientServiceRemote;
import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.web.rest.PatientRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.data.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.RadiologyType;
import edu.stevens.cs548.clinic.service.web.rest.data.SurgeryType;

@Path("/patient")
@Stateless
public class PatientResource {
	@Context
	private UriInfo context;
	
	Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.service.rest.resources.PatientResource");

	/**
	 * Default constructor.
	 */
	public PatientResource() {
		logger.info("Calling constructor on PatientResource");
	}

	@EJB(beanName="PatientServiceBean")
	private IPatientServiceRemote patientService;
	
	/**
	 * For testing that a service is up, independent of the beans or database. 
	 * @return
	 */
    @GET
    @Path("hello")
    @Produces("text/plain")
    public String hello() {
        return "hello patient";
    }
    

	/**
	 * Retrieves representation of an instance of PatientResource
	 * 
	 * @return an instance of PatientRepresentation
	 */
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public PatientRepresentation getPatient(@PathParam("id") String id) {
		logger.info("Patient requested for id : " + id);
		
		if (patientService == null) {
			logger.warning("Patient service bean is null!!!");
		}
		
		try {
			PatientDTO patientDTO = patientService.getPatientByDbId(
					Long.parseLong(id));
			
			if (patientDTO == null) {
				logger.info("Patient " + id + " not found");
				throw new WebApplicationException(Status.NOT_FOUND);
			}
			
			logger.info("Patient has " + patientDTO.treatments.length + " treatments");

			// Convert DTO to representation used by REST.
			PatientRepresentation patientRep = new PatientRepresentation(
					patientDTO, context);
			
			logger.info("Patient found for " + id + ": " + patientRep.getName());
			return patientRep;
		} catch (PatientNotFoundExn e) {
			logger.severe("Error retrieving patient: " + e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * Retrieves representation of an instance of PatientResource
	 * 
	 * @return an instance of PatientRepresentations
	 */
	@GET
	@Path("by-ident")
	@Produces("application/xml")
	public PatientRepresentation getPatientByPatientId(
			@QueryParam("id") String patientId) {
		try {
			logger.info("Patient requested for patientId : " + patientId);
			
			PatientDTO patientDTO = patientService.getPatientByPatientId(Long
					.parseLong(patientId));

			// Convert DTO to representation used by REST.
			PatientRepresentation patientRep = new PatientRepresentation(
					patientDTO, context);
			
			return patientRep;
		} catch (PatientNotFoundExn e) {
			logger.severe("Error retrieving patient: " + e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		} catch (PatientServiceExn e) {
			logger.severe("Error retrieving patient: " + e.getMessage());
			throw new WebApplicationException();
		}
	}

	/**
	 * Query patients by Name and DOB.
	 * 
	 * @return List of PatientRepresentations
	 */
	@GET
	@Path("by-name") // Note: omitted in podcasts.
	@Produces("application/xml")
	public PatientRepresentation[] getPatientByNameDOB(
			@QueryParam("name") String name, @QueryParam("dob") String dob) {
		try {
			Date birthDay = DatatypeConverter.parseDate(dob).getTime();
			PatientDTO[] patientDTOs = patientService.getPatientsByNameDob(
					name, birthDay);
			
			if (patientDTOs.length == 0) {
				throw new WebApplicationException(Status.NOT_FOUND);
			}

			// Convert DTO to representation used by REST.
			PatientRepresentation[] patientReps = new PatientRepresentation[patientDTOs.length];
			for (int i = 0; i < patientDTOs.length; i++) {
				patientReps[i] = new PatientRepresentation(patientDTOs[i],
						context);
			}
			return patientReps;
		} catch (PatientServiceExn e) {
			logger.severe("Error retrieving patient: " + e.getMessage());
			throw new WebApplicationException();
		}
	}

	@POST
	@Consumes("application/xml")
	public Response addPatient(PatientRepresentation patient) {
		logger.info("Adding patient with patientId : " + patient.getPatientId());
		try {
			long id = patientService.createPatient(patient.getName(),
					patient.getDob(), patient.getPatientId());
			logger.info("Successfully created patient with id " + id);
			UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
			URI url = ub.build(id);
			return Response.created(url).build();
		} catch (PatientServiceExn e) {
			logger.severe("Error creating patient: " + e.getMessage());
			throw new WebApplicationException();
		}
	}
	
	@DELETE
	@Path("{id}")
	public Response deletePatient(@PathParam("id") String patientId,
			@QueryParam("name") String name) {
		logger.info("Deleting patient with patientId : " + patientId + " and name: " + name);
		try {
			long id = Long.parseLong(patientId);
			patientService.deletePatient(name, id);
			return Response.ok().build();
		} catch (PatientNotFoundExn e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		} catch (PatientServiceExn e) {
			throw new WebApplicationException();
		}
	}
	
	/**
	 * PUT method for updating or creating an instance of TreatmentResource
	 * 
	 * @param content
	 *            representation for the resource
	 * @return an HTTP response with content of the updated or created resource.
	 */
	@POST
	@Path("{id}/treatment")
	@Consumes("application/xml")
	public Response putTreatment(TreatmentRepresentation treatment) {
		
		// Extract patientId and providerId out from the end of the Link path.
		long patientKey = Utils.parseIdFromLink(treatment.getPatient().getUrl());
		long providerKey = Utils.parseIdFromLink(treatment.getProvider().getUrl());
		if (patientKey == 0 || providerKey == 0) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		logger.info("Add treatments for patient with id : " + patientKey);

		long treatmentId = 0;

		try {
			if (treatment.getDrugTreatment() != null) {
				DrugTreatmentType t = treatment.getDrugTreatment();
				treatmentId = patientService.addDrugTreatment(patientKey,
						providerKey, treatment.getDiagnosis(), t.getName(),
						t.getDosage());
			} else if (treatment.getRadiology() != null) {
				RadiologyType t = treatment.getRadiology();
				treatmentId = patientService.addRadiologyTreatment(patientKey,
						providerKey, treatment.getDiagnosis(), t.getDate());
			} else if (treatment.getSurgery() != null) {
				SurgeryType t = treatment.getSurgery();
				treatmentId = patientService.addSurgeryTreatment(patientKey,
						providerKey, treatment.getDiagnosis(), t.getDate());
			} else { // Invalid treatment.
				throw new WebApplicationException();
			}
			
			UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
			URI url = ub.build(treatmentId);
			return Response.created(url).build();
		} catch (PatientNotFoundExn e) {
			logger.severe("Error adding treatment: " + e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	@POST
	@Path("{id}/treatments")
	@Consumes("application/xml")
	@Produces("application/xml")
	public TreatmentRepresentation[] getTreatments(PatientRepresentation patient) {
		logger.info("Getting treatments for patient with id : " + patient.getId());
		
		// Get the list of treatment Ids.
		List<LinkType> treatmentLinks = patient.getTreatments();
		long[] treatmentIds = new long[treatmentLinks.size()];
		for (int i = 0; i < treatmentLinks.size(); i++) {
			long tId = Utils.parseIdFromLink(treatmentLinks.get(i).getUrl());
			if (tId == 0) {
				throw new WebApplicationException();
			}
			treatmentIds[i] = tId;
		}
		
		try {
			// Request list of treatments and process result.
			TreatmentDto[] treatmentDTOs = patientService.getTreatments(
					patient.getId(), treatmentIds);
			TreatmentRepresentation[] treatments = new TreatmentRepresentation[treatmentDTOs.length];
			for (int i = 0; i < treatmentDTOs.length; i++) {
				treatments[i] = new TreatmentRepresentation(treatmentDTOs[i], context);
			}
			return treatments;
			
		} catch (PatientNotFoundExn e) {
			logger.severe("Error getting treatments: " + e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		} catch (TreatmentNotFoundExn e) {
			logger.severe("Error getting treatments: " + e.getMessage());
			throw new WebApplicationException(Status.NOT_FOUND);
		} catch (PatientServiceExn e) {
			logger.severe("Error getting treatments: " + e.getMessage());
			throw new WebApplicationException();
		}
	}
}