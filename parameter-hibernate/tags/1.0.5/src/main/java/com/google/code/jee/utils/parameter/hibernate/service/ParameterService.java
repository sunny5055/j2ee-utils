package com.google.code.jee.utils.parameter.hibernate.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.google.code.jee.utils.dal.Result;
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
     * Search an element by its name.
     * 
     * @param name the name
     * @return the parameter
     */
    AbstractParameter<?> findByName(String name);
    
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
     * Sets the value.
     * 
     * @param <V> the value type
     * @param name the name
     * @param description the description
     * @param value the value
     */
    <V> void setValue(String name, String description, V value);

    /**
     * Removes the value.
     * 
     * @param name the name
     * @return the integer
     */
    Integer removeValue(String name);

    /**
     * Removes all the values.
     * 
     * @return the integer
     */
    Integer removeAllValues();

    /**
     * Export all the parameters into a .properties file .
     * 
     * @param outputStream the output stream
     * @param dateFormat the date format. If no date format is specified, the
     *            default one will be used instead.
     * @throws IOException
     */
    void exportProperties(OutputStream outputStream) throws IOException;

    /**
     * Import properties.
     *
     * @param inputStream the input stream
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void importProperties(InputStream inputStream) throws IOException;
    
    /**
     * Updates the parameter
     * 
     * @param parameter to update
     * @return the results list
     */
    Result<AbstractParameter<?>> update(AbstractParameter<?> parameter);
}