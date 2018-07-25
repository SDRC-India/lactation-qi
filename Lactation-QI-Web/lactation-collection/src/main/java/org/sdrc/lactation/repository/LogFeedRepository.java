package org.sdrc.lactation.repository;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.lactation.domain.LogFeed;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store data coming from LogFeed form.
 */

public interface LogFeedRepository {

	void save(Iterable<LogFeed> logFeedList);

	List<LogFeed> findByCreatedByIn(List<String> userNameByInstitution);

	List<LogFeed> findByUniqueFormIdIsNull();

	List<LogFeed> findAll();

	List<LogFeed> findByUpdatedDateBetween(Timestamp startDate, Timestamp endDate);

	List<LogFeed> findByUniqueFormIdIn(List<String> uniqueIdList);
}
