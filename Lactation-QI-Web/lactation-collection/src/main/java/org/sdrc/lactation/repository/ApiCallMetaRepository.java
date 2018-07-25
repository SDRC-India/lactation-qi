package org.sdrc.lactation.repository;

import org.sdrc.lactation.domain.ApiCallMeta;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in)
 * 
 * This repository will be called to store meta data of the api calls that will be made
 *
 */
public interface ApiCallMetaRepository {

	void save(ApiCallMeta apiCallMeta);

}
