package com.google.code.jee.utils.dal.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.dto.Dto;

/**
 * The Interface GenericReadDao.
 * 
 * @param <PK> the generic type
 * @param <E> the element type
 */
public interface GenericReadDao<PK extends Serializable, E extends Dto<PK>> {

    /**
     * Gets the entity.
     * 
     * @param pk the primary key
     * @return the entity
     */
    E get(PK pk);

    /**
     * Gets the entities.
     * 
     * @param pks the primary keys
     * @return the objects
     */
    List<E> getObjects(PK... pks);

    /**
     * Gets the entities.
     * 
     * @param pks the primary keys
     * @return the objects
     */
    List<E> getObjects(Collection<PK> pks);

    /**
     * Finds all entities.
     * 
     * @return the list
     */
    List<E> findAll();

    /**
     * Finds all entities.
     * 
     * @param searchCriteria the search criteria
     * @return the list
     */
    List<E> findAll(SearchCriteria searchCriteria);

    /**
     * Count.
     * 
     * @return the integer
     */
    Integer count();

    /**
     * Count.
     * 
     * @param searchCriteria the search criteria
     * @return the integer
     */
    Integer count(SearchCriteria searchCriteria);

    /**
     * Exist primary key.
     * 
     * @param pk the primary key
     * @return true, if successful
     */
    boolean existPk(PK pk);
}
