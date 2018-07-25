package org.sdrc.lactation.repository;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.lactation.domain.LogExpressionBreastFeed;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store data coming from
 *         LogExpressionBreastFeed forms.
 */

public interface LogExpressionBreastFeedRepository {

	void save(Iterable<LogExpressionBreastFeed> logExpressionBreastFeedList);

	List<LogExpressionBreastFeed> findByCreatedByIn(List<String> userNameByInstitution);

	List<LogExpressionBreastFeed> findByUniqueFormIdIsNull();

	List<LogExpressionBreastFeed> findAll();

	List<LogExpressionBreastFeed> findByUpdatedDateBetween(Timestamp startDate, Timestamp endDate);

	List<LogExpressionBreastFeed> findByUniqueFormIdIn(List<String> uniqueIdList);

}
