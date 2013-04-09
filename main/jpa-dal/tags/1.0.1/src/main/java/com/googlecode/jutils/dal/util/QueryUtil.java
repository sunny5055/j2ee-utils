package com.googlecode.jutils.dal.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.collection.CollectionUtil;

/**
 * The Class QueryUtil.
 */
public class QueryUtil {

	/**
	 * Instantiates a new QueryUtil.
	 */
	private QueryUtil() {
		super();
	}

	/**
	 * Gets.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(EntityManager entityManager, String queryString, Object... values) {
		T dto = null;
		if (entityManager != null && !StringUtil.isBlank(queryString)) {
			final List<?> dtos = find(entityManager, queryString, values);
			dto = (T) CollectionUtil.getFirstElementFromList(dtos);
		}
		return dto;
	}

	/**
	 * Gets the by named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryString
	 *            the query string
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the by named param
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getByNamedParam(EntityManager entityManager, String queryString, String[] paramNames, Object... values) {
		T dto = null;
		if (entityManager != null && !StringUtil.isBlank(queryString) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final List<?> dtos = findByNamedParam(entityManager, queryString, paramNames, values);
			dto = (T) CollectionUtil.getFirstElementFromList(dtos);
		}
		return dto;
	}

	/**
	 * Gets the by named query.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryName
	 *            the query name
	 * @param values
	 *            the values
	 * @return the by named query
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getByNamedQuery(EntityManager entityManager, String queryName, Object... values) {
		T dto = null;
		if (entityManager != null && !StringUtil.isBlank(queryName)) {
			final List<?> dtos = findByNamedQuery(entityManager, queryName, values);
			dto = (T) CollectionUtil.getFirstElementFromList(dtos);
		}
		return dto;
	}

	/**
	 * Gets the by named query and named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryName
	 *            the query name
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the by named query and named param
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getByNamedQueryAndNamedParam(EntityManager entityManager, String queryName, String[] paramNames, Object... values) {
		T dto = null;
		if (entityManager != null && !StringUtil.isBlank(queryName) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final List<?> dtos = findByNamedQueryAndNamedParam(entityManager, queryName, paramNames, values);
			dto = (T) CollectionUtil.getFirstElementFromList(dtos);
		}
		return dto;
	}

	/**
	 * Finds.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> find(EntityManager entityManager, final String queryString, final Object... values) {
		List<T> dtos = null;
		if (entityManager != null && !StringUtil.isBlank(queryString)) {
			final Query query = entityManager.createQuery(queryString);
			applyParametersToQuery(query, values);

			dtos = query.getResultList();
		}
		return dtos;
	}

	/**
	 * Finds.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryString
	 *            the query string
	 * @param firstResult
	 *            the first result
	 * @param maxResults
	 *            the max results
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> find(EntityManager entityManager, final String queryString, final Integer firstResult, final Integer maxResults, final Object... values) {
		List<T> dtos = null;
		if (entityManager != null && !StringUtil.isBlank(queryString) && firstResult != null && maxResults != null) {
			final Query query = entityManager.createQuery(queryString);
			applyParametersToQuery(query, values);

			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
			dtos = query.getResultList();
		}
		return dtos;
	}

	/**
	 * Finds the by named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryString
	 *            the query string
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findByNamedParam(EntityManager entityManager, String queryString, String[] paramNames, Object... values) {
		List<T> dtos = null;
		if (entityManager != null && !StringUtil.isBlank(queryString) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final Query query = entityManager.createQuery(queryString);
			applyNamedParametersToQuery(query, paramNames, values);

			dtos = query.getResultList();
		}
		return dtos;
	}

	/**
	 * Finds the by named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryString
	 *            the query string
	 * @param firstResult
	 *            the first result
	 * @param maxResults
	 *            the max results
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findByNamedParam(EntityManager entityManager, final String queryString, final Integer firstResult, final Integer maxResults,
			final String[] paramNames, final Object... values) {
		List<T> dtos = null;
		if (entityManager != null && !StringUtil.isBlank(queryString) && firstResult != null && maxResults != null && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final Query query = entityManager.createQuery(queryString);
			applyNamedParametersToQuery(query, paramNames, values);

			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
			dtos = query.getResultList();
		}
		return dtos;
	}

	/**
	 * Finds the by named query.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryName
	 *            the query name
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findByNamedQuery(EntityManager entityManager, String queryName, Object... values) {
		List<T> dtos = null;
		if (entityManager != null && !StringUtil.isBlank(queryName)) {
			final Query query = entityManager.createNamedQuery(queryName);
			applyParametersToQuery(query, values);

			dtos = query.getResultList();
		}
		return dtos;
	}

	/**
	 * Finds the by named query.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryName
	 *            the query name
	 * @param firstResult
	 *            the first result
	 * @param maxResults
	 *            the max results
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findByNamedQuery(EntityManager entityManager, final String queryName, final Integer firstResult, final Integer maxResults, final Object... values) {
		List<T> dtos = null;
		if (entityManager != null && !StringUtil.isBlank(queryName) && firstResult != null && maxResults != null) {
			final Query query = entityManager.createNamedQuery(queryName);
			applyParametersToQuery(query, values);

			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
			dtos = query.getResultList();
		}
		return dtos;
	}

	/**
	 * Finds the by named query and named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryName
	 *            the query name
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findByNamedQueryAndNamedParam(EntityManager entityManager, String queryName, String[] paramNames, Object... values) {
		List<T> dtos = null;
		if (entityManager != null && !StringUtil.isBlank(queryName) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final Query query = entityManager.createNamedQuery(queryName);
			applyNamedParametersToQuery(query, paramNames, values);

			dtos = query.getResultList();
		}
		return dtos;
	}

	/**
	 * Finds the by named query and named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param entityManager
	 *            the entityManager
	 * @param queryName
	 *            the query name
	 * @param firstResult
	 *            the first result
	 * @param maxResults
	 *            the max results
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findByNamedQueryAndNamedParam(EntityManager entityManager, final String queryName, final Integer firstResult, final Integer maxResults,
			final String[] paramNames, final Object... values) {
		List<T> dtos = null;
		if (entityManager != null && !StringUtil.isBlank(queryName) && firstResult != null && maxResults != null && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final Query query = entityManager.createNamedQuery(queryName);
			applyNamedParametersToQuery(query, paramNames, values);

			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
			dtos = query.getResultList();
		}
		return dtos;
	}

	/**
	 * Gets the number.
	 * 
	 * @param entityManager
	 *            the entityManager
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the number
	 */
	public static Integer getNumber(EntityManager entityManager, String queryString, Object... values) {
		final List<?> list = find(entityManager, queryString, values);
		return CollectionUtil.getIntegerElementFromList(list);
	}

	/**
	 * Gets the number by named param.
	 * 
	 * @param entityManager
	 *            the entityManager
	 * @param queryString
	 *            the query string
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the number by named param
	 */
	public static Integer getNumberByNamedParam(EntityManager entityManager, String queryString, String[] paramNames, Object... values) {
		final List<?> list = findByNamedParam(entityManager, queryString, paramNames, values);
		return CollectionUtil.getIntegerElementFromList(list);
	}

	/**
	 * Gets the number by named query.
	 * 
	 * @param entityManager
	 *            the entityManager
	 * @param queryName
	 *            the query name
	 * @param values
	 *            the values
	 * @return the number by named query
	 */
	public static Integer getNumberByNamedQuery(EntityManager entityManager, String queryName, Object... values) {
		final List<?> list = findByNamedQuery(entityManager, queryName, values);
		return CollectionUtil.getIntegerElementFromList(list);
	}

	/**
	 * Gets the number by named query and named param.
	 * 
	 * @param entityManager
	 *            the entityManager
	 * @param queryName
	 *            the query name
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the number by named query and named param
	 */
	public static Integer getNumberByNamedQueryAndNamedParam(EntityManager entityManager, String queryName, String[] paramNames, Object... values) {
		final List<?> list = findByNamedQueryAndNamedParam(entityManager, queryName, paramNames, values);
		return CollectionUtil.getIntegerElementFromList(list);
	}

	/**
	 * Gets the boolean.
	 * 
	 * @param entityManager
	 *            the entityManager
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the boolean
	 */
	public static Boolean getBoolean(EntityManager entityManager, String queryString, Object... values) {
		final List<?> list = find(entityManager, queryString, values);
		return CollectionUtil.getBooleanElementFromList(list);
	}

	/**
	 * Gets the boolean by named param.
	 * 
	 * @param entityManager
	 *            the entityManager
	 * @param queryString
	 *            the query string
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the boolean by named param
	 */
	public static Boolean getBooleanByNamedParam(EntityManager entityManager, String queryString, String[] paramNames, Object... values) {
		final List<?> list = findByNamedParam(entityManager, queryString, paramNames, values);
		return CollectionUtil.getBooleanElementFromList(list);
	}

	/**
	 * Gets the boolean by named query.
	 * 
	 * @param entityManager
	 *            the entityManager
	 * @param queryName
	 *            the query name
	 * @param values
	 *            the values
	 * @return the boolean by named query
	 */
	public static Boolean getBooleanByNamedQuery(EntityManager entityManager, String queryName, Object... values) {
		final List<?> list = findByNamedQuery(entityManager, queryName, values);
		return CollectionUtil.getBooleanElementFromList(list);
	}

	/**
	 * Gets the boolean by named query and named param.
	 * 
	 * @param entityManager
	 *            the entityManager
	 * @param queryName
	 *            the query name
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the boolean by named query and named param
	 */
	public static Boolean getBooleanByNamedQueryAndNamedParam(EntityManager entityManager, String queryName, String[] paramNames, Object... values) {
		final List<?> list = findByNamedQueryAndNamedParam(entityManager, queryName, paramNames, values);
		return CollectionUtil.getBooleanElementFromList(list);
	}

	/**
	 * Apply parameters to query.
	 * 
	 * @param query
	 *            the query
	 * @param values
	 *            the values
	 */
	public static void applyParametersToQuery(Query query, Object... values) {
		if (query != null) {
			if (!ArrayUtil.isEmpty(values)) {
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
			}
		}
	}

	/**
	 * Apply named parameters to query.
	 * 
	 * @param query
	 *            the query
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 */
	public static void applyNamedParametersToQuery(Query query, String[] paramNames, Object... values) {
		if (query != null) {
			if (!ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
				for (int i = 0; i < values.length; i++) {
					query.setParameter(paramNames[i], values[i]);
				}
			}
		}
	}
}
