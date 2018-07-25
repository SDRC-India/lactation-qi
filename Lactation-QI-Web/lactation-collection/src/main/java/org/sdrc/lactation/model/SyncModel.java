package org.sdrc.lactation.model;

import java.util.List;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 19:10. This
 *         model will be used to receive data from the mobile.
 * @author Ratikanta
 */

public class SyncModel {

	private List<UserModel> users;
	private List<PatientModel> patients;
	private List<BFExpressionModel> bfExpressions;
	private List<FeedExpressionModel> feedExpressions;
	private List<BFSPModel> bfsps;
	private List<BFPDModel> bfpds;
	private Integer instituteId;

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

	public Integer getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(Integer instituteId) {
		this.instituteId = instituteId;
	}

}
