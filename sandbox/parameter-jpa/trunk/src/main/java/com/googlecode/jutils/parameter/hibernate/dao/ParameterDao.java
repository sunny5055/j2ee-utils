package com.googlecode.jutils.parameter.hibernate.dao;

import java.util.List;

import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.parameter.hibernate.model.AbstractParameter;

/**
 * The Interface ParameterDao.
 */
public interface ParameterDao {
	String COUNT_BY_NAME = "abstractParameter.countByName";
	String FIND_BY_NAME = "abstractParameter.findByName";

	/**
	 * Count.
	 * 
	 * @return the integer
	 */
	Integer count();

	/**
	 * Finds all entities.
	 * 
	 * @return the list
	 */
	List<AbstractParameter<?>> findAll();

	/**
	 * Count.
	 * 
	 * @param searchCriteria
	 *            the search criteria
	 * @return the integer
	 */
	Integer count(SearchCriteria searchCriteria);

	/**
	 * Finds all entities.
	 * 
	 * @param searchCriteria
	 *            the search criteria
	 * @return the list
	 */
	List<AbstractParameter<?>> findAll(SearchCriteria searchCriteria);

	/**
	 * Count by name.
	 * 
	 * @param name
	 *            the name
	 * @return the integer
	 */
	Integer countByName(String name);

	/**
	 * Finds the by name.
	 * 
	 * @param name
	 *            the name
	 * @return the abstract parameter
	 */
	AbstractParameter<?> findByName(String name);

	/**
	 * Saves the paramter.
	 * 
	 * @param parameter
	 *            the parameter
	 * @return the integer
	 */
	Integer save(AbstractParameter<?> parameter);

	/**
	 * Deletes the paramter.
	 * 
	 * @param name
	 *            the name
	 * @return the integer
	 */
	Integer deleteByName(String name);
}
