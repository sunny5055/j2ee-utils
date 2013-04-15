package com.googlecode.jutils.dal.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.dal.Search;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.util.QueryUtil;

/**
 * The Class AbstractGenericJpaReadDao.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 */
public abstract class AbstractGenericJpaReadDao<PK extends Serializable, E extends Dto<PK>> extends AbstractGenericReadDao<PK, E> {
	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * Instantiates a new abstract generic jpa read dao.
	 * 
	 * @param type
	 *            the type
	 */
	public AbstractGenericJpaReadDao() {
		super();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public E get(PK pk) {
		E dto = null;
		if (pk != null) {
			dto = this.entityManager.find(entityClass, pk);
		}
		return dto;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<E> getObjects(final Collection<PK> pks) {
		List<E> dtos = null;
		if (!CollectionUtil.isEmpty(pks)) {
			final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<E> query = builder.createQuery(entityClass);
			final Root<E> from = query.from(entityClass);
			query.select(from).where(from.get(getIdName()).in(pks));
			dtos = entityManager.createQuery(query).getResultList();
		}
		return dtos;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<E> findAll() {
		List<E> dtos = null;

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<E> query = builder.createQuery(entityClass);
		final Root<E> from = query.from(entityClass);
		query.select(from);
		dtos = entityManager.createQuery(query).getResultList();

		return dtos;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer count(SearchCriteria searchCriteria) {
		Integer count = 0;
		if (searchCriteria != null) {
			final Search search = getSearch(searchCriteria);
			if (search != null) {
				final String[] parameterNames = search.getParameterNames();
				final Object[] values = search.getValues();
				if (!ArrayUtil.isEmpty(parameterNames)) {
					count = QueryUtil.getNumberByNamedParam(this.entityManager, search.getCountQuery(), parameterNames, values);
				} else {
					count = QueryUtil.getNumber(this.entityManager, search.getCountQuery());
				}
			}
		}
		return count;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<E> findAll(SearchCriteria searchCriteria) {
		List<E> dtos = null;
		if (searchCriteria != null) {
			final Search search = getSearch(searchCriteria);
			if (search != null) {
				final String[] parameterNames = search.getParameterNames();
				final Object[] values = search.getValues();

				if (!ArrayUtil.isEmpty(parameterNames)) {
					if (searchCriteria.hasPagination()) {
						dtos = QueryUtil.findByNamedParam(this.entityManager, search.getQuery(), searchCriteria.getFirstResult(), searchCriteria.getMaxResults(), parameterNames,
								values);
					} else {
						dtos = QueryUtil.findByNamedParam(this.entityManager, search.getQuery(), parameterNames, values);
					}
				} else {
					if (searchCriteria.hasPagination()) {
						dtos = QueryUtil.find(this.entityManager, search.getQuery(), searchCriteria.getFirstResult(), searchCriteria.getMaxResults());
					} else {
						dtos = QueryUtil.find(this.entityManager, search.getQuery());
					}
				}
			}
		}
		return dtos;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer count() {
		Integer count = 0;

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		final Root<E> from = query.from(entityClass);
		query.select(builder.count(from));
		count = entityManager.createQuery(query).getSingleResult().intValue();

		return count;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public boolean existPk(final PK pk) {
		boolean exist = false;
		if (pk != null) {
			Integer count = 0;

			final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Long> query = builder.createQuery(Long.class);
			final Root<E> from = query.from(entityClass);
			query.select(builder.count(from));
			query.where(builder.equal(from.get(getIdName()), pk));
			count = entityManager.createQuery(query).getSingleResult().intValue();

			exist = count != 0;
		}
		return exist;
	}

	/**
	 * Getter : return the idName.
	 * 
	 * @return the idName
	 */
	protected String getIdName() {
		String idName = null;
		if (entityManager != null) {
			final Metamodel metamodel = entityManager.getMetamodel();
			final EntityType<E> entityType = metamodel.entity(entityClass);
			if (entityType != null) {
				final SingularAttribute<E, PK> id = entityType.getDeclaredId(pkClass);
				if (id != null) {
					idName = id.getName();
				}
			}
		}
		return idName;
	}
}
