package org.sdrc.lactation.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in)
 * 
 *         This domain class will be used to store the meta info of the api call
 *         that will be made by users.
 * 
 *
 */

@Entity
public class ApiCallMeta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "ip_address", nullable = false, length = 80)
	private String ipAddress;

	@Column(name = "user_agent", nullable = false)
	private String userAgent;

	@Column(name = "api_access_time")
	@CreationTimestamp
	private Timestamp apiAcessTime;

	@Column(name = "username", length = 80)
	private String username;

	public Integer getId() {
		return id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Timestamp getApiAcessTime() {
		return apiAcessTime;
	}

	public void setApiAcessTime(Timestamp apiAcessTime) {
		this.apiAcessTime = apiAcessTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
