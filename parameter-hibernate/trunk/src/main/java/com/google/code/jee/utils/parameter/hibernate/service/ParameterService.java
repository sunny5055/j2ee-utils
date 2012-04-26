package com.google.code.jee.utils.parameter.hibernate.service;

import java.util.List;

import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.parameter.hibernate.model.AbstractParameter;

/**
 * The Interface ParameterService.
 */
public interface ParameterService {

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
     * Exist with name.
     * 
     * @param name the name
     * @return true, if successful
     */
    boolean existWithName(String name);

    /**
     * Gets the value.
     * 
     * @param <V> the value type
     * @param name the name
     * @return the value
     */
    <V> V getValue(String name);

    /**
     * Sets the value.
     * 
     * @param <V> the value type
     * @param name the name
     * @param value the value
     */
    <V> void setValue(String name, V value);

    /**
     * Removes the value.
     * 
     * @param name the name
     * @return the integer
     */
    Integer removeValue(String name);
}
