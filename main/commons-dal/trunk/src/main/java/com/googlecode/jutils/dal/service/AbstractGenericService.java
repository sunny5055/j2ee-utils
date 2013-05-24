package com.googlecode.jutils.dal.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.dal.dao.GenericDao;
import com.googlecode.jutils.dal.entity.BaseEntity;
import com.googlecode.jutils.dal.util.EntityUtil;

/**
 * The Class AbstractGenericService.
 * 
 * @param <PK>
 *            the generic type
 * @param <DTO>
 *            the element type
 * @param <E>
 *            the element type
 * @param <DAO>
 *            the generic type
 */
public abstract class AbstractGenericService<PK extends Serializable, DTO, E extends BaseEntity<PK>, DAO extends GenericDao<PK, E>> extends
		AbstractGenericReadService<PK, DTO, E, DAO> implements GenericService<PK, DTO> {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public PK create(DTO dto) {
		PK pk = null;
		if (dto != null) {
			final E entity = toEntity(dto);
			pk = this.dao.create(entity);
		}
		return pk;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void update(DTO dto) {
		if (dto != null) {
			final E entity = toEntity(dto);
			this.dao.update(entity);
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer delete(DTO dto) {
		Integer deleted = 0;
		if (dto != null) {
			final E entity = toEntity(dto);
			deleted = deleteByPrimaryKey(entity.getPrimaryKey());
		}
		return deleted;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer delete(Collection<DTO> dtos) {
		Integer deleted = 0;
		if (!CollectionUtil.isEmpty(dtos)) {
			final List<E> entities = toEntities(dtos);
			final List<PK> pks = EntityUtil.getPrimaryKeyList(entities);
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
	public boolean isRemovable(DTO dto) {
		boolean removable = true;
		if (dto != null) {
			final E entity = toEntity(dto);
			removable = isRemovable(entity.getPrimaryKey());
		}
		return removable;
	}
}
