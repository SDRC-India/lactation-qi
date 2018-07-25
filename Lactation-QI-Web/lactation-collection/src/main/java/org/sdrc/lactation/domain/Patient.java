package org.sdrc.lactation.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 5th February 2018 17:10. This
 *         domain will be used for new patient registration.
 */

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = { "babyCode", "createdBy" }, name = "uniqueBabyCodeCreatedById") })
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer patientId;

	private String babyCode;

	private String babyCodeHospital;

	private Integer mothersAge;

	private String babyOf;

	private Timestamp deliveryDateAndTime;

	@ManyToOne
	@JoinColumn(nullable = true)
	private TypeDetails deliveryMethod;

	private Double babyWeight;

	private Integer gestationalAgeInWeek;

	@ManyToOne
	@JoinColumn(nullable = true)
	private TypeDetails mothersPrenatalIntent;

	@ManyToOne
	@JoinColumn(nullable = true)
	private TypeDetails parentsKnowledgeOnHmAndLactation;

	private String timeTillFirstExpression;

	@ManyToOne
	@JoinColumn(nullable = true)
	private TypeDetails inpatientOrOutPatient;

	private Date admissionDateForOutdoorPatients;

	@ManyToOne
	@JoinColumn(nullable = true)
	private TypeDetails babyAdmittedTo;

	private String nicuAdmissionReason;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	private String createdBy;

	private String updatedBy;

	private Date dischargeDate;

	private String uuidNumber;

	public Patient() {

	}

	public Patient(Integer patientId) {
		this.patientId = patientId;
	}

	public String getBabyCode() {
		return babyCode;
	}

	public void setBabyCode(String babyCode) {
		this.babyCode = babyCode;
	}

	public String getBabyCodeHospital() {
		return babyCodeHospital;
	}

	public void setBabyCodeHospital(String babyCodeHospital) {
		this.babyCodeHospital = babyCodeHospital;
	}

	public Integer getMothersAge() {
		return mothersAge;
	}

	public void setMothersAge(Integer mothersAge) {
		this.mothersAge = mothersAge;
	}

	public String getBabyOf() {
		return babyOf;
	}

	public void setBabyOf(String babyOf) {
		this.babyOf = babyOf;
	}

	public Timestamp getDeliveryDateAndTime() {
		return deliveryDateAndTime;
	}

	public void setDeliveryDateAndTime(Timestamp deliveryDateAndTime) {
		this.deliveryDateAndTime = deliveryDateAndTime;
	}

	public TypeDetails getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(TypeDetails deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public Double getBabyWeight() {
		return babyWeight;
	}

	public void setBabyWeight(Double babyWeight) {
		this.babyWeight = babyWeight;
	}

	public Integer getGestationalAgeInWeek() {
		return gestationalAgeInWeek;
	}

	public void setGestationalAgeInWeek(Integer gestationalAgeInWeek) {
		this.gestationalAgeInWeek = gestationalAgeInWeek;
	}

	public TypeDetails getMothersPrenatalIntent() {
		return mothersPrenatalIntent;
	}

	public void setMothersPrenatalIntent(TypeDetails mothersPrenatalIntent) {
		this.mothersPrenatalIntent = mothersPrenatalIntent;
	}

	public TypeDetails getParentsKnowledgeOnHmAndLactation() {
		return parentsKnowledgeOnHmAndLactation;
	}

	public void setParentsKnowledgeOnHmAndLactation(TypeDetails parentsKnowledgeOnHmAndLactation) {
		this.parentsKnowledgeOnHmAndLactation = parentsKnowledgeOnHmAndLactation;
	}

	public String getTimeTillFirstExpression() {
		return timeTillFirstExpression;
	}

	public void setTimeTillFirstExpression(String timeTillFirstExpression) {
		this.timeTillFirstExpression = timeTillFirstExpression;
	}

	public TypeDetails getInpatientOrOutPatient() {
		return inpatientOrOutPatient;
	}

	public void setInpatientOrOutPatient(TypeDetails inpatientOrOutPatient) {
		this.inpatientOrOutPatient = inpatientOrOutPatient;
	}

	public Date getAdmissionDateForOutdoorPatients() {
		return admissionDateForOutdoorPatients;
	}

	public void setAdmissionDateForOutdoorPatients(Date admissionDateForOutdoorPatients) {
		this.admissionDateForOutdoorPatients = admissionDateForOutdoorPatients;
	}

	public TypeDetails getBabyAdmittedTo() {
		return babyAdmittedTo;
	}

	public void setBabyAdmittedTo(TypeDetails babyAdmittedTo) {
		this.babyAdmittedTo = babyAdmittedTo;
	}

	public String getNicuAdmissionReason() {
		return nicuAdmissionReason;
	}

	public void setNicuAdmissionReason(String nicuAdmissionReason) {
		this.nicuAdmissionReason = nicuAdmissionReason;
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

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public String getUuidNumber() {
		return uuidNumber;
	}

	public void setUuidNumber(String uuidNumber) {
		this.uuidNumber = uuidNumber;
	}

}
