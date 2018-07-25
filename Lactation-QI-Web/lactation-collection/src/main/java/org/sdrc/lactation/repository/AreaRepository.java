package org.sdrc.lactation.repository;

import java.util.List;

import org.sdrc.lactation.domain.Area;

/**
 * 
 * @author Naseem Akhtar (naseem@sdrc.co.in) on 9th February 2018 17:10. This
 *         repository will be used to store country,state,district,insitution
 *         data.
 */

public interface AreaRepository {
	
	List<Area> findAll();
	
}
