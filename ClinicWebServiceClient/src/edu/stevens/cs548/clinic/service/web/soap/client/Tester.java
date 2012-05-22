package edu.stevens.cs548.clinic.service.web.soap.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;
import javax.xml.rpc.ServiceException;

import edu.stevens.cs548.clinic.service.web.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.web.PatientDTO;
import edu.stevens.cs548.clinic.service.web.ProviderDTO;
import edu.stevens.cs548.clinic.service.web.SurgeryType;
import edu.stevens.cs548.clinic.service.web.soap.GetTreatmentsResponseReturn;
import edu.stevens.cs548.clinic.service.web.soap.IPatientWebPort;
import edu.stevens.cs548.clinic.service.web.soap.IProviderWebPort;
import edu.stevens.cs548.clinic.service.web.soap.PatientServiceExn;
import edu.stevens.cs548.clinic.service.web.soap.PatientWebService;
import edu.stevens.cs548.clinic.service.web.soap.PatientWebServiceLocator;
import edu.stevens.cs548.clinic.service.web.soap.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.web.soap.ProviderWebService;
import edu.stevens.cs548.clinic.service.web.soap.ProviderWebServiceLocator;

public class Tester {

	private class ServiceError extends Exception {
		private static final long serialVersionUID = 1L;

		ServiceError(String s) {
			super(s);
		}
	}

	Logger logger = Logger
			.getLogger("edu.stevens.cs548.clinic.service.web.soap.client.Tester");

	/*
	 * Input file. Defaults to standard input.
	 */
	private String InputFileName = "<stdin>";

	private BufferedReader InputFile = new BufferedReader(
			new InputStreamReader(System.in));

	/*
	 * Output file. Defaults to standard output.
	 */
	private String OutputFileName;

	private PrintWriter OutputFile = new PrintWriter(new OutputStreamWriter(
			System.out));

	/*
	 * Endpoint URL for the Web service.
	 */
	private URL endpointUrl = null;

	/*
	 * Client proxy for the Web service. Generated from the endpoint URL.
	 */
	private IPatientWebPort patient = null;
	
	/*
	 * Client proxy for the web service. Generated from the endpoint URL.
	 */
	private IProviderWebPort provider = null;

	/*
	 * Line number in input file, for error reporting.
	 */
	private int lineNo = 0;

	private String readLine() {
		/*
		 * Read a command from the input.
		 */
		try {
			lineNo++;
			return InputFile.readLine();
		} catch (IOException e) {
			error("Error reading input: " + e);
			return null;
		}
	}

	/**
	 * Write an error.
	 * @param msg
	 */
	private void error(String msg) {
		if (lineNo > 0) {
			System.err.print(lineNo + ": ");
		}
		System.err.println("** Error **");
		System.err.println(msg);
	}
	
	/**
	 * Write an error, and optionally exit application.
	 * @param msg
	 * @param exit
	 */
	private void error(String msg, boolean exit) {
		error(msg);
		if (exit)
			System.exit(-1);
	}

	private void remoteError(Exception e) {
		e.printStackTrace(System.err);
		error("Network error: " + e);
	}

	private void warning(String msg) {
		if (lineNo > 0) {
			System.err.print(lineNo + ": ");
		}
		System.err.println("** Warning **");
		System.err.println(msg);
	}

	private void print(String s) {
		OutputFile.print(s);
	}

	private void println(String s) {
		OutputFile.println(s);
	}
	
	private void newline() {
		OutputFile.println();
	}

	private void displayLong(long n) {
		OutputFile.print(n);
	}

	private void displayTreatment(String prefix, GetTreatmentsResponseReturn t,
			String suffix) {
		print(prefix);
		logger.info("Displaying a treatment.");
		print("Treatment(){");
		print("diagnosis=");
		print(t.getDiagnosis());
		print(",treatment=");
		if (t.getDrugTreatment() != null) {
			DrugTreatmentType dt = t.getDrugTreatment();
			print("DrugTreatment{");
			print("name=");
			print(dt.getName());
			print(",dosage=");
			print(Float.toString(dt.getDosage()));
			print("}");
		} else if (t.getRadiology() != null) {
			Date[] rad = t.getRadiology();
			DateFormat datefmt = DateFormat.getInstance();
			print("Radiology{dates=[");
			for (int i = 0; i < rad.length - 1; i++) {
				print(datefmt.format(rad[i]));
				print(",");
			}
			if (rad.length > 0) {
				print(datefmt.format(rad[rad.length - 1]));
			}
			print("]}");
		} else if (t.getSurgery() != null) {
			SurgeryType st = t.getSurgery();
			print("Surgery{");
			print("date=");
			print(DateFormat.getInstance().format(st.getDate()));
			print("}");
		}
		print("}");
	}

	private void displayTreatmentList(GetTreatmentsResponseReturn[] ts) {
		if (ts == null) {
			println("No treatments found");
			return;
		}
		println("[");
		for (GetTreatmentsResponseReturn t : ts) {
			displayTreatment("  ", t, ",\n");
		}
		println("]");
	}

	private void displayPatient(String prefix, PatientDTO p, String suffix) {
		print(prefix);
		logger.info("Displaying a patient DTO.");
		print("Patient(");
		print(Long.toString(p.getId()));
		print("){");
		print("name=");
		print(p.getName());
		print(",birthDate=");
		print(DateFormat.getInstance().format(p.getDob().getTime()));
		print(",patientId=");
		print(Long.toString(p.getPatientId()));
		print("}");

	}

	private void displayPatient(PatientDTO p) {
		if (p == null) {
			println("No patient found.");
			return;
		}
		displayPatient("", p, "\n");
	}

	private void displayPatientList(PatientDTO[] ps) {
		if (ps == null) {
			println("No patients found.");
			return;
		}
		
		println("[");
		for (PatientDTO p : ps) {
			displayPatient("  ", p, ",\n");
		}
		println("]");
	}

	private long addPatient(String[] args) throws ServiceError {
		if (args.length != 3) {
			error("Usage: patient addPatient name dob patient-id");
			return 0;
		}
		String name = args[0];
		Calendar dob = DatatypeConverter.parseDate(args[1]);
		long patientId = Long.parseLong(args[2]);
		logger.info("Adding a patient (name=" + name + ").");
		try {
			return patient.create(name, dob, patientId);
		} catch (PatientServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			remoteError(e);
			return -1;
		}
	}

	private PatientDTO getPatientByDbId(String[] args) throws ServiceError {
		if (args.length != 1) {
			error("Usage: patient getPatientByDbId patient-key");
			return null;
		}
		long patientKey = Long.parseLong(args[0]);
		try {
			return patient.getPatientByDbId(patientKey);
		} catch (RemoteException e) {
			error("Network error: " + e);
			return null;
		} catch (Exception e) {
			throw new ServiceError(e.toString());
		}
	}

	private PatientDTO getPatientByPatId(String[] args) throws ServiceError {
		if (args.length != 1) {
			error("Usage: patient getPatientByPatId patient-id");
			return null;
		}
		long patientId = Long.parseLong(args[0]);
		logger.info("Getting a patient by patient id (" + patientId + ").");
		try {
			return patient.getPatientByPatientId(patientId);
		} catch (PatientServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			remoteError(e);
			return null;
		}
	}

	private PatientDTO[] getPatientsByNameDob(String[] args) {
		if (args.length != 2) {
			error("Usage: patient getPatientsByNameDob name dob");
			return null;
		}
		String name = args[0];
		Calendar dob = DatatypeConverter.parseDate(args[1]);
		try {
			return patient.getPatientsByNameDob(name, dob);
		} catch (RemoteException e) {
			remoteError(e);
			return null;
		}
	}

	private void deletePatient(String[] args) throws ServiceError {
		if (args.length != 2) {
			error("Usage: patient deletePatient name key");
			return;
		}
		String name = args[0];
		long id = Long.parseLong(args[1]);
		logger.info("Deleting patient with id (" + id + ").");
		try {
			patient.deletePatient(name, id);
		} catch (PatientServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			remoteError(e);
		}
	}

	private long addPatientDrugTreatment(String[] args) throws ServiceError {
		if (args.length != 5) {
			error("Usage: patient addDrugTreatment patient-key provider-key diagnosis drug dosage");
			return 0;
		}
		long id = Long.parseLong(args[0]);
		long providerKey = Long.parseLong(args[1]);
		String diagnosis = args[2];
		String drug = args[3];
		float dosage = Float.parseFloat(args[4]);
		try {
			logger.info("Adding drug treatment: provider=" + providerKey + ", diagnosis=" +
					diagnosis + ", drug=" + drug + ", dosage=" + dosage);
			return patient.addDrugTreatment(id, providerKey, diagnosis, drug, dosage);
		} catch (PatientServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			remoteError(e);
		}
		return 0;
	}
	
	private long addPatientRadiologyTreatment(String[] args) throws ServiceError {
		if (args.length < 4) {
			error("Usage: patient addRadiologyTreatment patient-key provider-key diagnosis date1 date2...");
			return 0;
		}
		long id = Long.parseLong(args[0]);
		long providerKey = Long.parseLong(args[1]);
		String diagnosis = args[2];
		
		Calendar[] dates = new Calendar[args.length - 3];
		String datesStr = "";
		for (int i = 3; i < args.length; i++) {
			dates[i - 3] = DatatypeConverter.parseDate(args[i]);
			datesStr += args[i] + " ";
		}
		
		try {
			logger.info("Adding radiology treatment: provider=" + providerKey + ", diagnosis=" +
					diagnosis + ", dates=" + datesStr);
			return patient.addRadiologyTreatment(id, providerKey, diagnosis, dates);
		} catch (PatientServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			remoteError(e);
		}
		return 0;
	}

	private long addPatientSurgeryTreatment(String[] args) throws ServiceError {
		if (args.length != 4) {
			error("Usage: patient addSurgeryTreatment patient-key provider-key diagnosis date");
			return 0;
		}
		long id = Long.parseLong(args[0]);
		long providerKey = Long.parseLong(args[1]);
		String diagnosis = args[2];	
		Calendar date = DatatypeConverter.parseDate(args[3]);
		
		try {
			logger.info("Adding surgery treatment: provider=" + providerKey + ", diagnosis=" +
					diagnosis + ", date=" + date);
			return patient.addSurgeryTreatment(id, providerKey, diagnosis, date);
		} catch (PatientServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			remoteError(e);
		}
		return 0;
	}

	private GetTreatmentsResponseReturn[] getPatientTreatments(String[] args)
			throws ServiceError {
		if (args.length < 1) {
			error("Usage: patient getTreatments patient-key tid1 tid2 ...");
			return null;
		}
		long id = Long.parseLong(args[0]);
		Long[] tids = new Long[args.length];
		String treamtentIds = "";
		for (int i = 1; i < args.length; i++) {
			tids[i - 1] = Long.parseLong(args[i]);
			treamtentIds += args[i] + " ";
		}
		try {
			logger.info("Getting treatments for patient " + id + ", treatmentIds=" + treamtentIds);
			return patient.getTreatments(id, tids);
		} catch (PatientServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			error("Network error: " + e);
			return null;
		}
	}

	private void deletePatientTreatment(String[] args) throws ServiceError {
		if (args.length != 2) {
			error("Usage: patient getTreatments patient-key tid");
			return;
		}
		long id = Long.parseLong(args[0]);
		long tid = Long.parseLong(args[1]);
		try {
			patient.deleteTreatment(id, tid);
		} catch (RemoteException e) {
			remoteError(e);
		} catch (Exception e) {
			throw new ServiceError(e.toString());
		}
	}

	private String patientSiteInfo(String[] args) {
		if (args.length > 0) {
			error("Usage: patient siteInfo");
			return null;
		}
		try {
			return patient.siteInfo();
		} catch (RemoteException e) {
			remoteError(e);
			return null;
		}
	}

	public void invokePatientService(String cmd, String[] args) {
		try {
			if ("addPatient".equals(cmd)) {
				displayLong(addPatient(args));
				newline();
			} else if ("getPatientByDbId".equals(cmd)) {
				displayPatient(getPatientByDbId(args));
				newline();
			} else if ("getPatientByPatId".equals(cmd)) {
				displayPatient(getPatientByPatId(args));
				newline();
			} else if ("getPatientByNameDob".equals(cmd)) {
				displayPatientList(getPatientsByNameDob(args));
				newline();
			} else if ("deletePatient".equals(cmd)) {
				deletePatient(args);
			} else if ("addDrugTreatment".equals(cmd)) {
				displayLong(addPatientDrugTreatment(args));
				newline();
			} else if ("addRadiologyTreatment".equals(cmd)) {
				displayLong(addPatientRadiologyTreatment(args));
				newline();
			} else if ("addSurgeryTreatment".equals(cmd)) {
				displayLong(addPatientSurgeryTreatment(args));
				newline();
			} else if ("getTreatments".equals(cmd)) {
				displayTreatmentList(getPatientTreatments(args));
				newline();
			} else if ("deleteTreatment".equals(cmd)) {
				deletePatientTreatment(args);
			} else if ("siteInfo".equals(cmd)) {
				println(patientSiteInfo(args));
			} else {
				error("Unrecognized patient service command: " + cmd);
			}
		} catch (ServiceError e) {
			error("Service raised exception: " + e);
		}
	}
	
	private void displayProvider(String prefix, ProviderDTO p, String suffix) {
		print(prefix);
		logger.info("Displaying a provider DTO.");
		print("Provider(");
		print(Long.toString(p.getNpi()));
		print("){");
		print("name=");
		print(p.getName());
		print(",specialization=");
		print(p.getSpecialization());
		print("}");

	}

	private void displayProvider(ProviderDTO p) {
		if (p == null) {
			println("No provider found.");
			return;
		}
		displayProvider("", p, "\n");
	}


	private void displayProviderList(ProviderDTO[] ps) {
		if (ps == null) {
			println("No providers found.");
			return;
		}
		
		println("[");
		for (ProviderDTO p : ps) {
			displayProvider("  ", p, ",\n");
		}
		println("]");
	}

	private long addProvider(String[] args) throws ServiceError {
		if (args.length != 3) {
			error("Usage: provider addProvider name npi specialization");
			return 0;
		}
		String name = args[0];
		long npi = Long.parseLong(args[1]);
		String specialization = args[2];
		logger.info("Adding a provider (name=" + name + ").");
		try {
			return provider.create(name, npi, specialization);
		} catch (PatientServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			remoteError(e);
			return -1;
		}
	}


	private ProviderDTO getProviderByNPI(String[] args) throws ServiceError {
		if (args.length != 1) {
			error("Usage: provider getProviderByNPI provider-npi");
			return null;
		}
		long providerKey = Long.parseLong(args[0]);
		try {
			logger.info("Getting provider with NPI=" + providerKey);
			return provider.getProviderByNPI(providerKey);
		} catch (RemoteException e) {
			error("Network error: " + e);
			return null;
		} catch (Exception e) {
			throw new ServiceError(e.toString());
		}
	}
	
	private ProviderDTO[] getProviderByName(String[] args) throws ServiceError {
		if (args.length != 1) {
			error("Usage: provider getProviderByName name");
			return null;
		}
		String name = args[0];
		try {
			logger.info("Searching for providers with name=" + name);
			return provider.getProviderByName(name);
		} catch (RemoteException e) {
			error("Network error: " + e);
			return null;
		} catch (Exception e) {
			throw new ServiceError(e.toString());
		}
	}
	
	private void deleteProvider(String[] args) throws ServiceError {
		if (args.length != 2) {
			error("Usage: provider deleteProvider name provider-npi");
			return;
		}
		String name = args[0];
		long providerkey = Long.parseLong(args[1]);
		try {
			logger.info("Deleting provider name=" + name + ", npi=" + providerkey);
			provider.deleteProvider(name, providerkey);
		} catch (RemoteException e) {
			error("Network error: " + e);
		} catch (Exception e) {
			throw new ServiceError(e.toString());
		}
	}
	
	private long addProviderDrugTreatment(String[] args) throws ServiceError {
		if (args.length != 5) {
			error("Usage: provider addDrugTreatment provider-key patient-key diagnosis drug dosage");
			return 0;
		}
		long npi = Long.parseLong(args[0]);
		long patientDbId = Long.parseLong(args[1]);
		String diagnosis = args[2];
		String drug = args[3];
		float dosage = Float.parseFloat(args[4]);
		try {
			logger.info("Adding drug treatment: patient=" + patientDbId + ", diagnosis=" +
					diagnosis + ", drug=" + drug + ", dosage=" + dosage);
			return provider.addDrugTreatment(npi, patientDbId, diagnosis, drug, dosage);
		} catch (ProviderServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			remoteError(e);
		}
		return 0;
	}
	
	private long addProviderRadiologyTreatment(String[] args) throws ServiceError {
		if (args.length < 4) {
			error("Usage: provider addRadiologyTreatment provider-key patient-key diagnosis date1 date2...");
			return 0;
		}
		long npi = Long.parseLong(args[0]);
		long patientDbId = Long.parseLong(args[1]);
		String diagnosis = args[2];
		
		Calendar[] dates = new Calendar[args.length - 3];
		String datesStr = "";
		for (int i = 3; i < args.length; i++) {
			dates[i - 3] = DatatypeConverter.parseDate(args[i]);
			datesStr += args[i] + " ";
		}
		
		try {
			logger.info("Adding radiology treatment: patient=" + patientDbId + ", diagnosis=" +
					diagnosis + ", dates=" + datesStr);
			return provider.addRadiologyTreatment(npi, patientDbId, diagnosis, dates);
		} catch (ProviderServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			remoteError(e);
		}
		return 0;
	}

	private long addProviderSurgeryTreatment(String[] args) throws ServiceError {
		if (args.length != 4) {
			error("Usage: provider addSurgeryTreatment provider-key patient-key diagnosis date");
			return 0;
		}
		long npi = Long.parseLong(args[0]);
		long patientDbId = Long.parseLong(args[1]);
		String diagnosis = args[2];
		Calendar date = DatatypeConverter.parseDate(args[3]);
		
		try {
			logger.info("Adding surgery treatment: patient=" + patientDbId + ", diagnosis=" +
					diagnosis + ", date=" + date);
			return provider.addSurgeryTreatment(npi, patientDbId, diagnosis, date);
		} catch (ProviderServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			remoteError(e);
		}
		return 0;
	}

	private GetTreatmentsResponseReturn[] getProviderTreatments(String[] args)
			throws ServiceError {
		if (args.length < 1) {
			error("Usage: provider getTreatments provider-key tid1 tid2 ...");
			return null;
		}
		long id = Long.parseLong(args[0]);
		Long[] tids = new Long[args.length];
		String treamtentIds = "";
		for (int i = 1; i < args.length; i++) {
			tids[i - 1] = Long.parseLong(args[i]);
			treamtentIds += args[i] + " ";
		}
		try {
			logger.info("Getting treatments for provider " + id + ", treatmentIds=" + treamtentIds);
			return provider.getTreatments(id, tids);
		} catch (ProviderServiceExn e) {
			throw new ServiceError(e.toString());
		} catch (RemoteException e) {
			error("Network error: " + e);
			return null;
		}
	}

	private void deleteProviderTreatment(String[] args) throws ServiceError {
		if (args.length != 2) {
			error("Usage: provider getTreatments provider-key tid");
			return;
		}
		long id = Long.parseLong(args[0]);
		long tid = Long.parseLong(args[1]);
		try {
			provider.deleteTreatment(id, tid);
		} catch (RemoteException e) {
			remoteError(e);
		} catch (Exception e) {
			throw new ServiceError(e.toString());
		}
	}
	
	private String providerSiteInfo(String[] args) {
		if (args.length > 0) {
			error("Usage: provider siteInfo");
			return null;
		}
		try {
			return provider.siteInfo();
		} catch (RemoteException e) {
			remoteError(e);
			return null;
		}
	}

	public void invokeProviderService(String cmd, String[] args) {
		try {
			if ("addProvider".equals(cmd)) {
				displayLong(addProvider(args));
				newline();
			} else if ("getProviderByNPI".equals(cmd)) {
				displayProvider(getProviderByNPI(args));
				newline();
			} else if ("getProviderByName".equals(cmd)) {
				displayProviderList(getProviderByName(args));
				newline();
			} else if ("deleteProvider".equals(cmd)) {
				deleteProvider(args);
			} else if ("addDrugTreatment".equals(cmd)) {
				displayLong(addProviderDrugTreatment(args));
				newline();
			} else if ("addRadiologyTreatment".equals(cmd)) {
				displayLong(addProviderRadiologyTreatment(args));
				newline();
			} else if ("addSurgeryTreatment".equals(cmd)) {
				displayLong(addProviderSurgeryTreatment(args));
				newline();
			} else if ("getTreatments".equals(cmd)) {
				displayTreatmentList(getProviderTreatments(args));
				newline();
			} else if ("deleteTreatment".equals(cmd)) {
				deleteProviderTreatment(args);
			} else if ("siteInfo".equals(cmd)) {
				println(providerSiteInfo(args));
			} else {
				error("Unrecognized provider service command: " + cmd);
			}
		} catch (ServiceError e) {
			error("Service raised exception: " + e);
		}
	}

	private String currentWorkingDir() {
		return new File(".").getAbsolutePath();
	}

	private Tester processArgs(String[] args) {
		/*
		 * Process the command line arguments:
		 * 
		 * --input filename -i filename File containing a list of commands.
		 * Default is standard input.
		 * 
		 * --output filename -o filename File to write out results of commands.
		 * Default is standard output.
		 * 
		 * --url endpoint-url -u endpoint-url Endpoint URL for the service. No
		 * default.
		 */
		for (int iarg = 0; iarg < args.length; iarg++) {
			if ("--input".equals(args[iarg]) || "-i".equals(args[iarg])) {
				if (iarg + 1 < args.length) {
					InputFileName = args[++iarg];
					try {
						InputFile = new BufferedReader(new FileReader(
								InputFileName));
					} catch (FileNotFoundException e) {
						error("Input file not found: " + InputFileName
								+ "\nDirectory: " + currentWorkingDir());
					}
				} else {
					error("Missing value for --input or -i option.");
				}
			} else if ("--output".equals(args[iarg]) || "-o".equals(args[iarg])) {
				if (iarg + 1 < args.length) {
					OutputFileName = args[++iarg];
					try {
						OutputFile = new PrintWriter(new FileWriter(
								OutputFileName));
					} catch (IOException e) {
						error("Problem opening output file: " + OutputFileName
								+ "\nDirectory: " + currentWorkingDir());
					}
				} else {
					error("Missing value for --output or -o option");
				}
			} else if ("--url".equals(args[iarg]) || "-u".equals(args[iarg])) {
				if (iarg + 1 < args.length) {
					try {
						endpointUrl = new URL(args[++iarg]);
						PatientWebService service = new PatientWebServiceLocator();
						this.patient = service.getPatientWebPort(endpointUrl);
						
						ProviderWebService provService = new ProviderWebServiceLocator();
						this.provider = provService.getProviderWebPort(endpointUrl);
					} catch (MalformedURLException e) {
						error("Bad service URL: " + args[iarg]);
					} catch (ServiceException e) {
						error("Service exception: " + e);
					}
				}
			}
		}

		if (this.patient == null) {
			error("You must specify an endpoint URL with the --url or -u option.");
		}
		
		OutputFile.println("Starting...");
		OutputFile.flush();

		return this;
	}

	private void processLine(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: (patient|provider) command arg1 ... argn");
		} else if ("patient".equals(args[0])) {
			invokePatientService(args[1],
					Arrays.copyOfRange(args, 2, args.length));
		} else if ("provider".equals(args[0])) {
			invokeProviderService(args[1],
					Arrays.copyOfRange(args, 2, args.length));
		} else {
			System.err.println("Service name must be \"patient\" or \"provider\".");
		}
		OutputFile.flush();
	}

	private void processLines() {
		String line;
		while ((line = this.readLine()) != null) {
			String[] args = line.split("\\s");
			processLine(args);
		}
		try {
			InputFile.close();
		} catch (IOException e) {
			warning("Failed to close input: " + e);
		}
		OutputFile.close();
	}

	public Tester() {
	}

	public static void main(String[] args) {
		new Tester().processArgs(args).processLines();
	}

}
