package org.sdrc.lactation.model;

/**
 * 
 * @author Ratikanta
 *
 */
public class BFSPModel {
	private String id;
	private String babyCode;
	private String userId;
	private String syncFailureMessage;
	private Boolean isSynced;
	private String dateOfBFSP;
	private String timeOfBFSP;
	private Integer bfspPerformed;
	private Integer personWhoPerformedBFSP;
	private Integer bfspDuration;
	private String createdDate;
	private String updatedDate;
	private String uuidNumber;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBabyCode() {
		return babyCode;
	}

	public void setBabyCode(String babyCode) {
		this.babyCode = babyCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSyncFailureMessage() {
		return syncFailureMessage;
	}

	public void setSyncFailureMessage(String syncFailureMessage) {
		this.syncFailureMessage = syncFailureMessage;
	}

	public Boolean getIsSynced() {
		return isSynced;
	}

	public void setIsSynced(Boolean isSynced) {
		this.isSynced = isSynced;
	}

	public String getDateOfBFSP() {
		return dateOfBFSP;
	}

	public void setDateOfBFSP(String dateOfBFSP) {
		this.dateOfBFSP = dateOfBFSP;
	}

	public String getTimeOfBFSP() {
		return timeOfBFSP;
	}

	public void setTimeOfBFSP(String timeOfBFSP) {
		this.timeOfBFSP = timeOfBFSP;
	}

	public Integer getBfspPerformed() {
		return bfspPerformed;
	}

	public void setBfspPerformed(Integer bfspPerformed) {
		this.bfspPerformed = bfspPerformed;
	}

	public Integer getPersonWhoPerformedBFSP() {
		return personWhoPerformedBFSP;
	}

	public void setPersonWhoPerformedBFSP(Integer personWhoPerformedBFSP) {
		this.personWhoPerformedBFSP = personWhoPerformedBFSP;
	}

	public Integer getBfspDuration() {
		return bfspDuration;
	}

	public void setBfspDuration(Integer bfspDuration) {
		this.bfspDuration = bfspDuration;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUuidNumber() {
		return uuidNumber;
	}

	public void setUuidNumber(String uuidNumber) {
		this.uuidNumber = uuidNumber;
	}

}
