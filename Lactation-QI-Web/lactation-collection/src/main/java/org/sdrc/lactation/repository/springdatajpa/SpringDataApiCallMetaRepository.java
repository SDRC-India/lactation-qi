package org.sdrc.lactation.repository.springdatajpa;

import org.sdrc.lactation.domain.ApiCallMeta;
import org.sdrc.lactation.repository.ApiCallMetaRepository;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in)
 * 
 * This repository will be called to store meta data of the api calls that will be made
 *
 */

@RepositoryDefinition(domainClass = ApiCallMeta.class, idClass = Integer.class)
public interface SpringDataApiCallMetaRepository extends ApiCallMetaRepository {

}
