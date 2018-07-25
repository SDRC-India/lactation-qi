package org.sdrc.lactation.repository;

import java.util.List;

import org.sdrc.lactation.domain.LactationUser;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store LactationUser Data / Registration Data.
 */

public interface LactationUserRepository {

	void save(Iterable<LactationUser> users);
	
	LactationUser findByEmail(String email);

	List<LactationUser> findByInstitutionId(int i);

	List<LactationUser> findAll();

}
