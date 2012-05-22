package edu.stevens.cs548.clinic.service.rest.resources;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/clinic/rest")

public class ClinicApp extends Application {

  public Set<Class<?>> getClasses() {
    Set<Class<?>> s = new HashSet<Class<?>>();
    s.add(PatientResource.class);
    s.add(TreatmentResource.class);
    s.add(ProviderResource.class);
    return s;
  }
}