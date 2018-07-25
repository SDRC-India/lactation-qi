package org.sdrc.lactation.repository.springdatajpa;

import org.sdrc.lactation.domain.ApiUser;
import org.sdrc.lactation.repository.ApiUserRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in)
 * 
 * This repository will be called to fetch data from the api user table
 *
 */

@RepositoryDefinition(domainClass = ApiUser.class, idClass = Integer.class)
public interface SpringDataApiUserRepository extends ApiUserRepository {

}
