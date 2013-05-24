package com.googlecode.jutils.dal.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.googlecode.jutils.dal.SearchCriteria;

/**
 * The Interface GenericReadService.
 * 
 * @param <PK>
 *            the generic type
 * @param <DTO>
 *            the element type
 */
public interface GenericReadService<PK extends Serializable, DTO> {

	/**
	 * Gets the dto.
	 * 
	 * @param pk
	 *            the primary key
	 * @return the dto
	 */
	DTO get(PK pk);

	/**
	 * Gets the dtos.
	 * 
	 * @param pks
	 *            the primary keys
	 * @return the objects
	 */
	List<DTO> getObjects(Collection<PK> pks);

	/**
	 * Count.
	 * 
	 * @return the integer
	 */
	Integer count();

	/**
	 * Finds all dtos.
	 * 
	 * @return the list
	 */
	List<DTO> findAll();

	/**
	 * Count.
	 * 
	 * @param searchCriteria
	 *            the search criteria
	 * @return the integer
	 */
	Integer count(SearchCriteria searchCriteria);

	/**
	 * Finds all dtos.
	 * 
	 * @param searchCriteria
	 *            the search criteria
	 * @return the list
	 */
	List<DTO> findAll(SearchCriteria searchCriteria);

	/**
	 * Exist primary key.
	 * 
	 * @param pk
	 *            the primary key
	 * @return true, if successful
	 */
	boolean existPk(PK pk);
}
