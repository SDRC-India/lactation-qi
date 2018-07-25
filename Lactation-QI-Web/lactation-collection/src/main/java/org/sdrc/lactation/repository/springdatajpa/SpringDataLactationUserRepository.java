package org.sdrc.lactation.repository.springdatajpa;

import org.sdrc.lactation.domain.LactationUser;
import org.sdrc.lactation.repository.LactationUserRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = LactationUser.class, idClass = Integer.class)
public interface SpringDataLactationUserRepository extends LactationUserRepository {

}
