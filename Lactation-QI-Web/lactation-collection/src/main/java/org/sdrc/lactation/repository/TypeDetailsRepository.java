package org.sdrc.lactation.repository;

import java.util.List;

import org.sdrc.lactation.domain.TypeDetails;

public interface TypeDetailsRepository {
	List<TypeDetails> findAll();
}
