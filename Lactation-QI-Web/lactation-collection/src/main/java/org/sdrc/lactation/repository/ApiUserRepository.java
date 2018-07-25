package org.sdrc.lactation.repository;

import org.sdrc.lactation.domain.ApiUser;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in)
 * 
 * This repository will be called to insert/fetch/update data from the api user table
 *
 */

public interface ApiUserRepository {

	/**
	 * Fetching apiUser by user name
	 * @param username
	 * @return - {@link ApiUser}
	 */
	ApiUser findByUsername(String username);

}
