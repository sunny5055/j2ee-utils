package com.googlecode.jutils.dal.dao;

import java.io.Serializable;

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
	 * Creates the.
	 * 
	 * @param dto
	 *            the dto
	 * @return the pk {@inheritedDoc}
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
	 * Update.
	 * 
	 * @param dto
	 *            the dto
	 * @return the integer {@inheritedDoc}
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
	 * Delete.
	 * 
	 * @param dto
	 *            the dto
	 * @return the integer {@inheritedDoc}
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
	 * Delete by primary key.
	 * 
	 * @param pk
	 *            the pk
	 * @return the integer {@inheritedDoc}
	 */
	@Override
	public Integer deleteByPrimaryKey(final PK pk) {
		Integer deleted = 0;
		if (pk != null) {
			this.entityManager.remove(this.entityManager.getReference(entityClass, pk));
			deleted = 1;
		}
		return deleted;
	}
}
