package com.googlecode.jutils.dal.dao;

import java.io.Serializable;
import java.util.Collection;

import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Class AbstractGenericJpaDao.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 */
public abstract class AbstractGenericJpaDao<PK extends Serializable, E extends Dto<PK>> extends AbstractGenericJpaReadDao<PK, E> implements GenericDao<PK, E> {

	/**
	 * Instantiates a new abstract generic jpa dao.
	 * 
	 */
	public AbstractGenericJpaDao() {
		super();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public PK create(E dto) {
		PK primaryKey = null;
		if (dto != null) {
			this.entityManager.persist(dto);
			primaryKey = dto.getPrimaryKey();
		}
		return primaryKey;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer update(E dto) {
		Integer updated = 0;
		if (dto != null) {
			this.entityManager.merge(dto);
			updated = 1;
		}
		return updated;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer deleteByPrimaryKey(PK pk) {
		Integer deleted = 0;
		if (pk != null) {
			final E entity = get(pk);
			if (entity != null) {
				entityManager.remove(entity);
				deleted = 1;
			}
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
			for (final PK pk : pks) {
				deleted += deleteByPrimaryKey(pk);
			}
		}
		return deleted;
	}
}
