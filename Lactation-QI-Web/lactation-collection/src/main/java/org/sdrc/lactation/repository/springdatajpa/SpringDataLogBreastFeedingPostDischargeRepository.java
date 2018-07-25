package org.sdrc.lactation.repository.springdatajpa;

import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;
import org.sdrc.lactation.repository.LogBreastFeedingPostDischargeRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = LogBreastFeedingPostDischarge.class, idClass = Integer.class)
public interface SpringDataLogBreastFeedingPostDischargeRepository extends LogBreastFeedingPostDischargeRepository {
}
