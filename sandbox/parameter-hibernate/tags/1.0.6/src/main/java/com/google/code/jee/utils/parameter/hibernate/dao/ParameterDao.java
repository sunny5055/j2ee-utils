package com.google.code.jee.utils.parameter.hibernate.dao;

import java.util.List;

import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.parameter.hibernate.model.AbstractParameter;

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
     * @param searchCriteria the search criteria
     * @return the integer
     */
    Integer count(SearchCriteria searchCriteria);

    /**
     * Finds all entities.
     * 
     * @param searchCriteria the search criteria
     * @return the list
     */
    List<AbstractParameter<?>> findAll(SearchCriteria searchCriteria);

    /**
     * Count by name.
     * 
     * @param name the name
     * @return the integer
     */
    Integer countByName(String name);

    /**
     * Finds the by name.
     * 
     * @param name the name
     * @return the abstract parameter
     */
    AbstractParameter<?> findByName(String name);

    /**
     * Save.
     * 
     * @param dto the dto
     * @return the integer
     */
    Integer save(AbstractParameter<?> dto);

    /**
     * Deletes the entity.
     * 
     * @param dto the dto
     * @return the number of rows deleted
     */
    Integer delete(AbstractParameter<?> dto);

}
