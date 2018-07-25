package org.sdrc.lactation.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 6th February 2018 13:10. This
 *         domain will be used for capturing data from log feed form, this form
 *         is for a particular baby.
 */

@Entity
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogFeed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;

	@ManyToOne
	@JoinColumn(name="patient_id", nullable = false)
	private Patient patientId;

	private Timestamp dateAndTimeOfFeed;

	@ManyToOne
	@JoinColumn
	private TypeDetails feedMethod;

	private Double ommVolume;

	private Double dhmVolume;

	private Double formulaVolume;

	private Double animalMilkVolume;

	private Double otherVolume;

	@ManyToOne
	@JoinColumn
	private TypeDetails locationOfFeeding;

	private Double weightOfBaby;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	private String createdBy;

	private String updatedBy;

	private String uniqueFormId;

	private String uuidNumber;

	public TypeDetails getLocationOfFeeding() {
		return locationOfFeeding;
	}

	public void setLocationOfFeeding(TypeDetails locationOfFeeding) {
		this.locationOfFeeding = locationOfFeeding;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Patient getPatientId() {
		return patientId;
	}

	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}

	public Timestamp getDateAndTimeOfFeed() {
		return dateAndTimeOfFeed;
	}

	public void setDateAndTimeOfFeed(Timestamp dateAndTimeOfFeed) {
		this.dateAndTimeOfFeed = dateAndTimeOfFeed;
	}

	public TypeDetails getFeedMethod() {
		return feedMethod;
	}

	public void setFeedMethod(TypeDetails feedMethod) {
		this.feedMethod = feedMethod;
	}

	public Double getOmmVolume() {
		return ommVolume;
	}

	public void setOmmVolume(Double ommVolume) {
		this.ommVolume = ommVolume;
	}

	public Double getDhmVolume() {
		return dhmVolume;
	}

	public void setDhmVolume(Double dhmVolume) {
		this.dhmVolume = dhmVolume;
	}

	public Double getFormulaVolume() {
		return formulaVolume;
	}

	public void setFormulaVolume(Double formulaVolume) {
		this.formulaVolume = formulaVolume;
	}

	public Double getOtherVolume() {
		return otherVolume;
	}

	public void setOtherVolume(Double otherVolume) {
		this.otherVolume = otherVolume;
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

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public Double getAnimalMilkVolume() {
		return animalMilkVolume;
	}

	public void setAnimalMilkVolume(Double animalMilkVolume) {
		this.animalMilkVolume = animalMilkVolume;
	}

	public Double getWeightOfBaby() {
		return weightOfBaby;
	}

	public void setWeightOfBaby(Double weightOfBaby) {
		this.weightOfBaby = weightOfBaby;
	}

	public String getUniqueFormId() {
		return uniqueFormId;
	}

	public void setUniqueFormId(String uniqueFormId) {
		this.uniqueFormId = uniqueFormId;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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
