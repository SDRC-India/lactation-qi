package org.sdrc.lactation.repository;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store data coming from
 *         LogBreastFeedingSupportivePractice form.
 */

public interface LogBreastFeedingSupportivePracticeRepository {

	void save(Iterable<LogBreastFeedingSupportivePractice> logBreastFeedingSupportivePracticeList);

	List<LogBreastFeedingSupportivePractice> findByCreatedByIn(List<String> userNameByInstitution);

	List<LogBreastFeedingSupportivePractice> findByUniqueFormIdIsNull();

	List<LogBreastFeedingSupportivePractice> findAll();

	List<LogBreastFeedingSupportivePractice> findByUpdatedDateBetween(Timestamp startDate, Timestamp endDate);

	List<LogBreastFeedingSupportivePractice> findByUniqueFormIdIn(List<String> uniqueIdList);
}
