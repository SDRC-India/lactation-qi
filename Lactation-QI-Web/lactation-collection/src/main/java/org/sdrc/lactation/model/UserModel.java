package org.sdrc.lactation.model;

public class UserModel {

	private String firstName;
	private String lastName;
	private String email;
	private Integer country;
	private Integer state;
	private Integer institution;
	private Integer district;
	private Boolean isSynced;
	private String syncFailureMessage;
	private String createdDate;
	private String updatedDate;
	private String uuidNumber;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getCountry() {
		return country;
	}

	public void setCountry(Integer country) {
		this.country = country;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getInstitution() {
		return institution;
	}

	public void setInstitution(Integer institution) {
		this.institution = institution;
	}

	public Integer getDistrict() {
		return district;
	}

	public void setDistrict(Integer district) {
		this.district = district;
	}

	public Boolean getIsSynced() {
		return isSynced;
	}

	public void setIsSynced(Boolean isSynced) {
		this.isSynced = isSynced;
	}

	public String getSyncFailureMessage() {
		return syncFailureMessage;
	}

	public void setSyncFailureMessage(String syncFailureMessage) {
		this.syncFailureMessage = syncFailureMessage;
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
