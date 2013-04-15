package com.googlecode.jutils.dal.service;

import java.io.Serializable;
import java.util.Collection;

import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Interface GenericService.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 */
public interface GenericService<PK extends Serializable, E extends Dto<PK>> extends GenericReadService<PK, E> {

	/**
	 * Creates the entity.
	 * 
	 * @param dto
	 *            the dto
	 * @return the entity
	 */
	E create(E dto);

	/**
	 * Updates the entity.
	 * 
	 * @param dto
	 *            the dto
	 * @return the entity
	 */
	E update(E dto);

	/**
	 * Deletes the entity.
	 * 
	 * @param dto
	 *            the dto
	 * @return the number of rows deleted
	 */
	Integer delete(E dto);

	/**
	 * Deletes the entities.
	 * 
	 * @param dtos
	 *            the dtos
	 * @return the number of rows deleted
	 */
	Integer delete(Collection<E> dtos);

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

	/**
	 * Check if a dto is removable.
	 * 
	 * @param pk
	 *            the pk
	 * @return boolean
	 */
	boolean isRemovable(PK pk);

	/**
	 * Check if a dto is removable.
	 * 
	 * @param dto
	 *            the dto
	 * @return boolean
	 */
	boolean isRemovable(E dto);
}
