package org.sdrc.lactation.model;

import java.util.List;

import org.sdrc.lactation.domain.LogBreastFeedingPostDischarge;
import org.sdrc.lactation.domain.LogBreastFeedingSupportivePractice;
import org.sdrc.lactation.domain.LogExpressionBreastFeed;
import org.sdrc.lactation.domain.LogFeed;
import org.sdrc.lactation.domain.Patient;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 15th February 2018 17:10. This
 *         model will be used as a variable for {@link: SynchronizationModel} to
 *         receive patient related data.
 */

public class PatientSynchronizationModel {

	private Patient patient;
	private List<LogExpressionBreastFeed> logExpressionBreastFeedList;
	private List<LogFeed> logFeedList;
	private List<LogBreastFeedingSupportivePractice> logBreastFeedingSupportivePracticeList;
	private List<LogBreastFeedingPostDischarge> logBreastFeedingPostDischargeList;

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public List<LogExpressionBreastFeed> getLogExpressionBreastFeedList() {
		return logExpressionBreastFeedList;
	}

	public void setLogExpressionBreastFeedList(List<LogExpressionBreastFeed> logExpressionBreastFeedList) {
		this.logExpressionBreastFeedList = logExpressionBreastFeedList;
	}

	public List<LogFeed> getLogFeedList() {
		return logFeedList;
	}

	public void setLogFeedList(List<LogFeed> logFeedList) {
		this.logFeedList = logFeedList;
	}

	public List<LogBreastFeedingSupportivePractice> getLogBreastFeedingSupportivePracticeList() {
		return logBreastFeedingSupportivePracticeList;
	}

	public void setLogBreastFeedingSupportivePracticeList(
			List<LogBreastFeedingSupportivePractice> logBreastFeedingSupportivePracticeList) {
		this.logBreastFeedingSupportivePracticeList = logBreastFeedingSupportivePracticeList;
	}

	public List<LogBreastFeedingPostDischarge> getLogBreastFeedingPostDischargeList() {
		return logBreastFeedingPostDischargeList;
	}

	public void setLogBreastFeedingPostDischargeList(
			List<LogBreastFeedingPostDischarge> logBreastFeedingPostDischargeList) {
		this.logBreastFeedingPostDischargeList = logBreastFeedingPostDischargeList;
	}

}
