package com.googlecode.jutils.dal.dao;

import java.io.Serializable;
import java.util.Collection;

import com.googlecode.jutils.dal.entity.BaseEntity;

/**
 * The Interface GenericDao.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 */
public interface GenericDao<PK extends Serializable, E extends BaseEntity<PK>> extends GenericReadDao<PK, E> {

	/**
	 * Creates the entity.
	 * 
	 * @param entity
	 *            the entity
	 * @return the primary key
	 */
	PK create(E entity);

	/**
	 * Updates the entity.
	 * 
	 * @param entity
	 *            the entity
	 * @return the number of rows updated
	 */
	Integer update(E entity);

	/**
	 * Deletes the entity by primary key.
	 * 
	 * @param pk
	 *            the pk
	 * @return the number of rows deleted
	 */
	Integer deleteByPrimaryKey(PK pk);

	/**
	 * Deletes the entities by primary keys.
	 * 
	 * @param pks
	 *            the pks
	 * @return the number of rows deleted
	 */
	Integer deleteByPrimaryKeys(Collection<PK> pks);
}
