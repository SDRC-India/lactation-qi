package org.sdrc.lactation.repository.springdatajpa;

import org.sdrc.lactation.domain.Patient;
import org.sdrc.lactation.repository.PatientRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Patient.class, idClass = Integer.class)
public interface SpringDataPatientRepository extends PatientRepository {

}
