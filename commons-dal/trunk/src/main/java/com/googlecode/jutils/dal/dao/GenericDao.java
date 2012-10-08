package com.googlecode.jutils.dal.dao;

import java.io.Serializable;

import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Interface GenericDao.
 * 
 * @param <PK> the generic type
 * @param <E> the element type
 */
public interface GenericDao<PK extends Serializable, E extends Dto<PK>> extends GenericReadDao<PK, E> {

    /**
     * Creates the entity.
     * 
     * @param dto the dto
     * @return the primary key
     */
    PK create(E dto);

    /**
     * Updates the entity.
     * 
     * @param dto the dto
     * @return the number of rows updated
     */
    Integer update(E dto);

    /**
     * Deletes the entity.
     * 
     * @param dto the dto
     * @return the number of rows deleted
     */
    Integer delete(E dto);

    /**
     * Deletes the entity by primary key.
     * 
     * @param pk the pk
     * @return the number of rows deleted
     */
    Integer deleteByPrimaryKey(PK pk);
}
