package com.googlecode.jutils.dal.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.dal.dao.GenericDao;
import com.googlecode.jutils.dal.dto.Dto;

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
		E result = null;
		if (dto != null) {
			final PK pk = this.dao.create(dto);
			if (pk != null) {
				result = this.get(pk);
			}
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public E update(E dto) {
		E result = null;
		if (dto != null) {
			final Integer updated = this.dao.update(dto);
			if (updated != 0) {
				result = this.get(dto.getPrimaryKey());
			}
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer delete(E dto) {
		Integer result = 0;
		if (dto != null) {
			if (isRemovable(dto)) {
				result = this.dao.delete(dto);
			}
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer delete(Collection<E> dtos) {
		final Integer result = 0;
		if (!CollectionUtil.isEmpty(dtos)) {
			Integer deleted = 0;
			for (final E dto : dtos) {
				if (isRemovable(dto)) {
					deleted += this.dao.delete(dto);
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer deleteByPrimaryKey(PK pk) {
		Integer result = 0;
		if (pk != null) {
			final E dto = this.dao.get(pk);
			if (dto != null) {
				result = delete(dto);
			}
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer deleteByPrimaryKeys(Collection<PK> pks) {
		Integer result = 0;
		if (!CollectionUtil.isEmpty(pks)) {
			final List<E> dtos = this.dao.getObjects(pks);
			result = delete(dtos);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer deleteAll() {
		final List<E> dtos = this.dao.findAll();
		return delete(dtos);
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
