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
import com.googlecode.jutils.dal.entity.BaseEntity;
import com.googlecode.jutils.dal.util.QueryUtil;

/**
 * The Class AbstractGenericJpaReadDao.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 */
public abstract class AbstractGenericJpaReadDao<PK extends Serializable, E extends BaseEntity<PK>> extends AbstractGenericReadDao<PK, E> {
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
		E entity = null;
		if (pk != null) {
			entity = this.entityManager.find(entityClass, pk);
		}
		return entity;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<E> getObjects(final Collection<PK> pks) {
		List<E> entities = null;
		if (!CollectionUtil.isEmpty(pks)) {
			final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			final CriteriaQuery<E> query = builder.createQuery(entityClass);
			final Root<E> from = query.from(entityClass);
			query.select(from).where(from.get(getIdName()).in(pks));
			entities = entityManager.createQuery(query).getResultList();
		}
		return entities;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<E> findAll() {
		List<E> entities = null;

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<E> query = builder.createQuery(entityClass);
		final Root<E> from = query.from(entityClass);
		query.select(from);
		entities = entityManager.createQuery(query).getResultList();

		return entities;
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
		List<E> entities = null;
		if (searchCriteria != null) {
			final Search search = getSearch(searchCriteria);
			if (search != null) {
				final String[] parameterNames = search.getParameterNames();
				final Object[] values = search.getValues();

				if (!ArrayUtil.isEmpty(parameterNames)) {
					if (searchCriteria.hasPagination()) {
						entities = QueryUtil.findByNamedParam(this.entityManager, search.getQuery(), searchCriteria.getFirstResult(), searchCriteria.getMaxResults(),
								parameterNames, values);
					} else {
						entities = QueryUtil.findByNamedParam(this.entityManager, search.getQuery(), parameterNames, values);
					}
				} else {
					if (searchCriteria.hasPagination()) {
						entities = QueryUtil.find(this.entityManager, search.getQuery(), searchCriteria.getFirstResult(), searchCriteria.getMaxResults());
					} else {
						entities = QueryUtil.find(this.entityManager, search.getQuery());
					}
				}
			}
		}
		return entities;
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
