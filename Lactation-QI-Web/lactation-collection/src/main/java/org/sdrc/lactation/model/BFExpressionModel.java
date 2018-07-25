package org.sdrc.lactation.model;

/**
 * 
 * @author Ratikanta
 *
 */
public class BFExpressionModel {
	private String id;
	private String babyCode;
	private String userId;
	private String syncFailureMessage;
	private Boolean isSynced;
	private String dateOfExpression;
	private String timeOfExpression;
	private Integer methodOfExpression;
	private Integer locationOfExpression;
	private Double volOfMilkExpressedFromLR;// 0-300ml
	private String createdDate;
	private String updatedDate;
	private String uuidNumber;
	private String methodOfExpressionOthers;

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

	public String getDateOfExpression() {
		return dateOfExpression;
	}

	public void setDateOfExpression(String dateOfExpression) {
		this.dateOfExpression = dateOfExpression;
	}

	public String getTimeOfExpression() {
		return timeOfExpression;
	}

	public void setTimeOfExpression(String timeOfExpression) {
		this.timeOfExpression = timeOfExpression;
	}

	public Integer getMethodOfExpression() {
		return methodOfExpression;
	}

	public void setMethodOfExpression(Integer methodOfExpression) {
		this.methodOfExpression = methodOfExpression;
	}

	public Integer getLocationOfExpression() {
		return locationOfExpression;
	}

	public void setLocationOfExpression(Integer locationOfExpression) {
		this.locationOfExpression = locationOfExpression;
	}

	public Double getVolOfMilkExpressedFromLR() {
		return volOfMilkExpressedFromLR;
	}

	public void setVolOfMilkExpressedFromLR(Double volOfMilkExpressedFromLR) {
		this.volOfMilkExpressedFromLR = volOfMilkExpressedFromLR;
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

	public String getMethodOfExpressionOthers() {
		return methodOfExpressionOthers;
	}

	public void setMethodOfExpressionOthers(String methodOfExpressionOthers) {
		this.methodOfExpressionOthers = methodOfExpressionOthers;
	}

}
