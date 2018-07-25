package org.sdrc.lactation.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.sdrc.lactation.controller.DataDumpController;

/**
 * 
 * @author Naseem Akhtar on 18th April 2018 13:45
 * 
 *         This domain class will be used to validate the users who will be
 *         accessing the API's {@link DataDumpController} which we have exposed
 *
 */

@Entity
public class ApiUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 80, unique = true, nullable = false)
	private String username;

	@Column(length = 40, nullable = false)
	private String password;

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
