package com.googlecode.jutils.dal.service;

import java.io.Serializable;
import java.util.Collection;

import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Interface GenericService.
 * 
 * @param <PK>
 *            the generic type
 * @param <DTO>
 *            the element type
 */
public interface GenericService<PK extends Serializable, DTO extends Dto<PK>> extends GenericReadService<PK, DTO> {

	/**
	 * Creates the dto.
	 * 
	 * @param dto
	 *            the dto
	 * @return the pk
	 */
	PK create(DTO dto);

	/**
	 * Updates the dto.
	 * 
	 * @param dto
	 *            the dto
	 */
	void update(DTO dto);

	/**
	 * Deletes the dto.
	 * 
	 * @param dto
	 *            the dto
	 * @return the number of rows deleted
	 */
	Integer delete(DTO dto);

	/**
	 * Deletes the dtos.
	 * 
	 * @param dtos
	 *            the dtos
	 * @return the number of rows deleted
	 */
	Integer delete(Collection<DTO> dtos);

	/**
	 * Deletes the dto by primary key.
	 * 
	 * @param pk
	 *            the pk
	 * @return the number of rows deleted
	 */
	Integer deleteByPrimaryKey(PK pk);

	/**
	 * Deletes the dtos by primary keys.
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
	boolean isRemovable(DTO dto);
}
