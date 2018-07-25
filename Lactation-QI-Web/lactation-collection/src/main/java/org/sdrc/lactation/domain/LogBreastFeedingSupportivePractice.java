package org.sdrc.lactation.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 6th February 2018 13:10. This
 *         domain will be used for capturing data filled up in breast feeding
 *         supportive practice form, this is for a particular baby.
 */

@Entity
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogBreastFeedingSupportivePractice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="patient_id", nullable = false)
	private Patient patientId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Timestamp dateAndTimeOfBFSP;

	@ManyToOne
	@JoinColumn
	private TypeDetails bfspPerformed;

	@ManyToOne
	@JoinColumn
	private TypeDetails personWhoPerformedBFSP;

	private Integer bfspDuration;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	private String createdBy;

	private String updatedBy;

	private String uniqueFormId;

	private String uuidNumber;

	public Patient getPatientId() {
		return patientId;
	}

	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}

	public Timestamp getDateAndTimeOfBFSP() {
		return dateAndTimeOfBFSP;
	}

	public void setDateAndTimeOfBFSP(Timestamp dateAndTimeOfBFSP) {
		this.dateAndTimeOfBFSP = dateAndTimeOfBFSP;
	}

	public TypeDetails getBfspPerformed() {
		return bfspPerformed;
	}

	public void setBfspPerformed(TypeDetails bfspPerformed) {
		this.bfspPerformed = bfspPerformed;
	}

	public TypeDetails getPersonWhoPerformedBFSP() {
		return personWhoPerformedBFSP;
	}

	public void setPersonWhoPerformedBFSP(TypeDetails personWhoPerformedBFSP) {
		this.personWhoPerformedBFSP = personWhoPerformedBFSP;
	}

	public Integer getBfspDuration() {
		return bfspDuration;
	}

	public void setBfspDuration(Integer bfspDuration) {
		this.bfspDuration = bfspDuration;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getId() {
		return id;
	}

	public String getUniqueFormId() {
		return uniqueFormId;
	}

	public void setUniqueFormId(String uniqueFormId) {
		this.uniqueFormId = uniqueFormId;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUuidNumber() {
		return uuidNumber;
	}

	public void setUuidNumber(String uuidNumber) {
		this.uuidNumber = uuidNumber;
	}

}
