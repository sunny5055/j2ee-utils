package com.googlecode.jutils.dal.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.dal.dao.GenericDao;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.util.DtoUtil;

/**
 * The Class AbstractGenericService.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 * @param <DAO>
 *            the generic type
 */
public abstract class AbstractGenericService<PK extends Serializable, E extends Dto<PK>, DAO extends GenericDao<PK, E>> extends AbstractGenericReadService<PK, E, DAO> implements
		GenericService<PK, E> {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public E create(E dto) {
		if (dto != null) {
			this.dao.create(dto);
		}
		return dto;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public E update(E dto) {
		if (dto != null) {
			this.dao.update(dto);
		}
		return dto;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer delete(E dto) {
		Integer deleted = 0;
		if (dto != null) {
			deleted = deleteByPrimaryKey(dto.getPrimaryKey());
		}
		return deleted;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer delete(Collection<E> dtos) {
		Integer deleted = 0;
		if (!CollectionUtil.isEmpty(dtos)) {
			final List<PK> pks = DtoUtil.getPrimaryKeyList(dtos);
			deleted = deleteByPrimaryKeys(pks);
		}
		return deleted;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer deleteByPrimaryKey(PK pk) {
		Integer deleted = 0;
		if (pk != null) {
			deleted = this.dao.deleteByPrimaryKey(pk);
		}
		return deleted;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer deleteByPrimaryKeys(Collection<PK> pks) {
		Integer deleted = 0;
		if (!CollectionUtil.isEmpty(pks)) {
			deleted = this.dao.deleteByPrimaryKeys(pks);
		}
		return deleted;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public boolean isRemovable(PK pk) {
		return true;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public boolean isRemovable(E dto) {
		boolean removable = true;
		if (dto != null) {
			removable = isRemovable(dto.getPrimaryKey());
		}
		return removable;
	}
}
