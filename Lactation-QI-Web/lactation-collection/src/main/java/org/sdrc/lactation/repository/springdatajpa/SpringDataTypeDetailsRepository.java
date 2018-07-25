package org.sdrc.lactation.repository.springdatajpa;

import org.sdrc.lactation.domain.TypeDetails;
import org.sdrc.lactation.repository.TypeDetailsRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = TypeDetails.class, idClass = Integer.class)
public interface SpringDataTypeDetailsRepository extends TypeDetailsRepository{

}
