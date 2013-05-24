package com.googlecode.jutils.dal.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.dal.entity.BaseEntity;

/**
 * The Class AbstractGenericJpaDao.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 */
public abstract class AbstractGenericJpaDao<PK extends Serializable, E extends BaseEntity<PK>> extends AbstractGenericJpaReadDao<PK, E> implements GenericDao<PK, E> {

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
	public PK create(E entity) {
		PK primaryKey = null;
		if (entity != null) {
			this.entityManager.persist(entity);
			primaryKey = entity.getPrimaryKey();
		}
		return primaryKey;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer update(E entity) {
		Integer updated = 0;
		if (entity != null) {
			this.entityManager.merge(entity);
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
			final List<E> entities = getObjects(pks);
			if (!CollectionUtil.isEmpty(entities)) {
				for (final E entity : entities) {
					if (entity != null) {
						entityManager.remove(entity);
						deleted++;
					}
				}
			}
		}
		return deleted;
	}
}
