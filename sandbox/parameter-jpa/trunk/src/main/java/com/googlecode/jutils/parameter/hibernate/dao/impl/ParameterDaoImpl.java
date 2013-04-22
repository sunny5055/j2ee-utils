package com.googlecode.jutils.parameter.hibernate.dao.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.dal.Search;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.SortOrder;
import com.googlecode.jutils.dal.util.QueryUtil;
import com.googlecode.jutils.parameter.hibernate.dao.ParameterDao;
import com.googlecode.jutils.parameter.hibernate.model.AbstractParameter;

@Repository
public class ParameterDaoImpl implements ParameterDao {
	@PersistenceContext
	protected EntityManager entityManager;
	protected Class<Integer> pkClass;
	protected Class<?> entityClass;

	public ParameterDaoImpl() {
		super();
		this.pkClass = Integer.class;
		this.entityClass = AbstractParameter.class;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Integer count() {
		Integer count = 0;

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> query = builder.createQuery(Long.class);
		final Root<AbstractParameter<?>> from = (Root<AbstractParameter<?>>) query.from(entityClass);
		query.select(builder.count(from));
		count = entityManager.createQuery(query).getSingleResult().intValue();

		return count;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AbstractParameter<?>> findAll() {
		List<AbstractParameter<?>> dtos = null;

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<AbstractParameter<?>> query = (CriteriaQuery<AbstractParameter<?>>) builder.createQuery(entityClass);
		final Root<AbstractParameter<?>> from = (Root<AbstractParameter<?>>) query.from(entityClass);
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
	public List<AbstractParameter<?>> findAll(SearchCriteria searchCriteria) {
		List<AbstractParameter<?>> dtos = null;
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
	public Integer countByName(String name) {
		Integer count = 0;
		if (!StringUtil.isBlank(name)) {
			count = QueryUtil.getNumberByNamedQueryAndNamedParam(entityManager, ParameterDao.COUNT_BY_NAME, new String[] { "name" }, name);
		}
		return count;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public AbstractParameter<?> findByName(String name) {
		AbstractParameter<?> parameter = null;
		if (!StringUtil.isBlank(name)) {
			parameter = QueryUtil.getByNamedQueryAndNamedParam(entityManager, ParameterDao.FIND_BY_NAME, new String[] { "name" }, name);
		}
		return parameter;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer save(AbstractParameter<?> parameter) {
		Integer primaryKey = null;
		if (parameter != null) {
			entityManager.persist(parameter);
			primaryKey = parameter.getPrimaryKey();
		}
		return primaryKey;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer deleteByName(String name) {
		Integer deleted = 0;
		if (!StringUtil.isBlank(name)) {
			final AbstractParameter<?> entity = findByName(name);
			if (entity != null) {
				entityManager.remove(entity);
				deleted = 1;
			}
		}
		return deleted;
	}

	/**
	 * Gets the search.
	 * 
	 * @param searchCriteria
	 *            the search criteria
	 * @return the search
	 */
	private Search getSearch(SearchCriteria searchCriteria) {
		Search search = null;
		if (searchCriteria != null) {
			search = new Search();

			final StringBuilder buffer = new StringBuilder();
			buffer.append("from AbstractParameter p ");
			if (searchCriteria.hasFilters()) {
				buffer.append("where ");
				int index = 0;
				for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
					if (entry.getValue() != null) {
						if (index != 0) {
							buffer.append("AND ");
						}
						if (entry.getKey().equals("name")) {
							buffer.append("upper(p.name) like upper(:name) ");
							search.addStringParameter("name", entry.getValue());
						} else if (entry.getKey().equals("type")) {
							buffer.append("upper(p.type) like upper(:type) ");
							search.addStringParameter("type", entry.getValue());
						}
						index++;
					}
				}
			}

			search.setCountQuery("select count(*) " + buffer.toString());

			if (searchCriteria.hasSorts()) {
				buffer.append("order by ");
				int index = 0;
				for (final Map.Entry<String, SortOrder> entry : searchCriteria.getSorts().entrySet()) {
					if (index != 0) {
						buffer.append(", ");
					}
					if (entry.getKey().equals("name")) {
						buffer.append("p.name ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					} else if (entry.getKey().equals("type")) {
						buffer.append("p.type ");
						if (entry.getValue() == SortOrder.DESCENDING) {
							buffer.append("desc ");
						}
					}
					index++;
				}
			}

			search.setQuery(buffer.toString());
		}
		return search;
	}
}
