package org.sdrc.lactation.repository.springdatajpa;

import org.sdrc.lactation.domain.Area;
import org.sdrc.lactation.repository.AreaRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Area.class, idClass = Integer.class)
public interface SpringDataAreaRepository extends AreaRepository {

}
