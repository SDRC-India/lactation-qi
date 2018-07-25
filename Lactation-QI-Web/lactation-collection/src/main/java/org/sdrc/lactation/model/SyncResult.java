package org.sdrc.lactation.model;

import java.util.List;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 13th February 2018 1601. This
 *         class will help us to send the response back to the mobile after
 *         synchronization process is complete or interrupted. This will send
 *         response about the no. of forms synced, what is the status of the
 *         http request made etc.
 * @author Ratikanta
 */

public class SyncResult {

	/*
	 * private List<FailureUser> failureUsers; private List<FailurePatient>
	 * failurePatients; private List<FailureBFExpression> failureBFExpressions;
	 * private List<FailureFeedExpression> failureFeedExpressions; private
	 * List<FailureBFSP> failureBFSPs; private List<FailureBFPD> failureBFPDs;
	 */

	private List<UserModel> users;
	private List<PatientModel> patients;
	private List<BFExpressionModel> bfExpressions;
	private List<FeedExpressionModel> feedExpressions;
	private List<BFSPModel> bfsps;
	private List<BFPDModel> bfpds;
	private Integer syncStatus;

	public List<UserModel> getUsers() {
		return users;
	}

	public void setUsers(List<UserModel> users) {
		this.users = users;
	}

	public List<PatientModel> getPatients() {
		return patients;
	}

	public void setPatients(List<PatientModel> patients) {
		this.patients = patients;
	}

	public List<BFExpressionModel> getBfExpressions() {
		return bfExpressions;
	}

	public void setBfExpressions(List<BFExpressionModel> bfExpressions) {
		this.bfExpressions = bfExpressions;
	}

	public List<FeedExpressionModel> getFeedExpressions() {
		return feedExpressions;
	}

	public void setFeedExpressions(List<FeedExpressionModel> feedExpressions) {
		this.feedExpressions = feedExpressions;
	}

	public List<BFSPModel> getBfsps() {
		return bfsps;
	}

	public void setBfsps(List<BFSPModel> bfsps) {
		this.bfsps = bfsps;
	}

	public List<BFPDModel> getBfpds() {
		return bfpds;
	}

	public void setBfpds(List<BFPDModel> bfpds) {
		this.bfpds = bfpds;
	}

	public Integer getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
	}

}
