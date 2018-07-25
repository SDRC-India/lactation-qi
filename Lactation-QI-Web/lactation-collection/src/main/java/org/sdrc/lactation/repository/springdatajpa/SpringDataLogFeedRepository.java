package org.sdrc.lactation.repository.springdatajpa;

import org.sdrc.lactation.domain.LogFeed;
import org.sdrc.lactation.repository.LogFeedRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = LogFeed.class, idClass = Integer.class)
public interface SpringDataLogFeedRepository extends LogFeedRepository {
}
