package org.sdrc.lactation.repository.springdatajpa;

import org.sdrc.lactation.domain.LogExpressionBreastFeed;
import org.sdrc.lactation.repository.LogExpressionBreastFeedRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = LogExpressionBreastFeed.class, idClass = Integer.class)
public interface SpringDataLogExpressionBreastFeedRepository extends LogExpressionBreastFeedRepository {
}
