package org.sdrc.lactation.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 6th February 2018 13:10. This
 *         domain will be used for registering new user in the system.
 * @author Ratikanta
 */

@Entity
@JsonAutoDetect
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LactationUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;

	private String firstName;

	private String lastName;

	@Column(nullable = false, unique = true)
	private String email;

	@ManyToOne
	@JoinColumn
	private Area country;

	@ManyToOne
	@JoinColumn
	private Area state;

	@ManyToOne
	@JoinColumn
	private Area district;

	@ManyToOne
	@JoinColumn
	private Area institution;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Timestamp createdDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private Timestamp updatedDate;

	private String uuidNumber;

	public Integer getId() {
		return id;
	}

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

	public Area getCountry() {
		return country;
	}

	public void setCountry(Area country) {
		this.country = country;
	}

	public Area getState() {
		return state;
	}

	public void setState(Area state) {
		this.state = state;
	}

	public Area getDistrict() {
		return district;
	}

	public void setDistrict(Area district) {
		this.district = district;
	}

	public Area getInstitution() {
		return institution;
	}

	public void setInstitution(Area institution) {
		this.institution = institution;
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
