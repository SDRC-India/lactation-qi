package org.sdrc.lactation.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 6th February 2018 13:10. This
 *         domain will be used for capturing data from log expression breast
 *         feed form, this form is for a particular baby.
 */

@Entity
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogExpressionBreastFeed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "patient_id", nullable = false)
	private Patient patientId;

	private Timestamp dateAndTimeOfExpression;

	@ManyToOne
	@JoinColumn
	private TypeDetails methodOfExpression;

	@ManyToOne
	@JoinColumn
	private TypeDetails expressionOccuredLocation;

	private Double milkExpressedFromLeftAndRightBreast;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	private String createdBy;

	private String updatedBy;

	private String uniqueFormId;

	private String uuidNumber;

	private String methodOfExpressionOthers;

	public Patient getPatientId() {
		return patientId;
	}

	public void setPatientId(Patient patientId) {
		this.patientId = patientId;
	}

	public Timestamp getDateAndTimeOfExpression() {
		return dateAndTimeOfExpression;
	}

	public void setDateAndTimeOfExpression(Timestamp dateAndTimeOfExpression) {
		this.dateAndTimeOfExpression = dateAndTimeOfExpression;
	}

	public TypeDetails getMethodOfExpression() {
		return methodOfExpression;
	}

	public void setMethodOfExpression(TypeDetails methodOfExpression) {
		this.methodOfExpression = methodOfExpression;
	}

	public TypeDetails getExpressionOccuredLocation() {
		return expressionOccuredLocation;
	}

	public void setExpressionOccuredLocation(TypeDetails expressionOccuredLocation) {
		this.expressionOccuredLocation = expressionOccuredLocation;
	}

	public Double getMilkExpressedFromLeftAndRightBreast() {
		return milkExpressedFromLeftAndRightBreast;
	}

	public void setMilkExpressedFromLeftAndRightBreast(Double milkExpressedFromLeftAndRightBreast) {
		this.milkExpressedFromLeftAndRightBreast = milkExpressedFromLeftAndRightBreast;
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

	public String getMethodOfExpressionOthers() {
		return methodOfExpressionOthers;
	}

	public void setMethodOfExpressionOthers(String methodOfExpressionOthers) {
		this.methodOfExpressionOthers = methodOfExpressionOthers;
	}

}
