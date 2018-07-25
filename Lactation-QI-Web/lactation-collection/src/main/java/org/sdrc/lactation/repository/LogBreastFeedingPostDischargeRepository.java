package org.sdrc.lactation.repository;

import java.sql.Timestamp;
import java.util.List;

import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store data of the
 *         LogBreastFeedingPostDischarge form.
 */

public interface LogBreastFeedingPostDischargeRepository {

	void save(Iterable<LogBreastFeedingPostDischarge> logBreastFeedingPostDischargeList);

	List<LogBreastFeedingPostDischarge> findByCreatedByIn(List<String> userNameByInstitution);

	List<LogBreastFeedingPostDischarge> findByUniqueFormIdIsNull();

	List<LogBreastFeedingPostDischarge> findAll();

	List<LogBreastFeedingPostDischarge> findByUpdatedDateBetween(Timestamp startDate, Timestamp endDate);

	List<LogBreastFeedingPostDischarge> findByUniqueFormIdIn(List<String> uniqueIdList);

}
