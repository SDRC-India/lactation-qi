package org.sdrc.lactation.repository.springdatajpa;

import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;
import org.sdrc.lactation.repository.LogBreastFeedingSupportivePracticeRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = LogBreastFeedingSupportivePractice.class, idClass = Integer.class)
public interface SpringDataLogBreastFeedingSupportivePracticeRepository
		extends LogBreastFeedingSupportivePracticeRepository {
}
