package org.sdrc.lactation.model;

/**
 * 
 * @author Ratikanta
 *
 */
public class BFPDModel {

	private String id;
	private String babyCode;
	private String userId;
	private String syncFailureMessage;
	private Boolean isSynced;
	private String dateOfBreastFeeding;
	private Integer timeOfBreastFeeding;
	private Integer breastFeedingStatus;
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

	public String getDateOfBreastFeeding() {
		return dateOfBreastFeeding;
	}

	public void setDateOfBreastFeeding(String dateOfBreastFeeding) {
		this.dateOfBreastFeeding = dateOfBreastFeeding;
	}

	public Integer getTimeOfBreastFeeding() {
		return timeOfBreastFeeding;
	}

	public void setTimeOfBreastFeeding(Integer timeOfBreastFeeding) {
		this.timeOfBreastFeeding = timeOfBreastFeeding;
	}

	public Integer getBreastFeedingStatus() {
		return breastFeedingStatus;
	}

	public void setBreastFeedingStatus(Integer breastFeedingStatus) {
		this.breastFeedingStatus = breastFeedingStatus;
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
