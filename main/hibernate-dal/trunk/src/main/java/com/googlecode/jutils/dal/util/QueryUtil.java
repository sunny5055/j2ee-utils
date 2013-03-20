package com.googlecode.jutils.dal.util;

import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.collection.CollectionUtil;

/**
 * The Class SessionUtil.
 */
public class QueryUtil {

	/**
	 * Instantiates a new session util.
	 */
	private QueryUtil() {
		super();
	}

	/**
	 * Gets.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Session session, String queryString, Object... values) {
		T dto = null;
		if (session != null && !StringUtil.isBlank(queryString)) {
			final List<?> dtos = find(session, queryString, values);
			dto = (T) CollectionUtil.getFirstElementFromList(dtos);
		}
		return dto;
	}

	/**
	 * Gets the by named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
	 * @param queryString
	 *            the query string
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the by named param
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getByNamedParam(Session session, String queryString, String[] paramNames, Object... values) {
		T dto = null;
		if (session != null && !StringUtil.isBlank(queryString) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final List<?> dtos = findByNamedParam(session, queryString, paramNames, values);
			dto = (T) CollectionUtil.getFirstElementFromList(dtos);
		}
		return dto;
	}

	/**
	 * Gets the by named query.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
	 * @param queryName
	 *            the query name
	 * @param values
	 *            the values
	 * @return the by named query
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getByNamedQuery(Session session, String queryName, Object... values) {
		T dto = null;
		if (session != null && !StringUtil.isBlank(queryName)) {
			final List<?> dtos = findByNamedQuery(session, queryName, values);
			dto = (T) CollectionUtil.getFirstElementFromList(dtos);
		}
		return dto;
	}

	/**
	 * Gets the by named query and named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
	 * @param queryName
	 *            the query name
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the by named query and named param
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getByNamedQueryAndNamedParam(Session session, String queryName, String[] paramNames, Object... values) {
		T dto = null;
		if (session != null && !StringUtil.isBlank(queryName) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final List<?> dtos = findByNamedQueryAndNamedParam(session, queryName, paramNames, values);
			dto = (T) CollectionUtil.getFirstElementFromList(dtos);
		}
		return dto;
	}

	/**
	 * Finds.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> find(Session session, final String queryString, final Object... values) {
		List<T> dtos = null;
		if (session != null && !StringUtil.isBlank(queryString)) {
			final Query query = session.createQuery(queryString);
			applyParametersToQuery(query, values);

			dtos = query.list();
		}
		return dtos;
	}

	/**
	 * Finds.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
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
	public static <T> List<T> find(Session session, final String queryString, final Integer firstResult, final Integer maxResults, final Object... values) {
		List<T> dtos = null;
		if (session != null && !StringUtil.isBlank(queryString) && firstResult != null && maxResults != null) {
			final Query query = session.createQuery(queryString);
			applyParametersToQuery(query, values);

			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
			dtos = query.list();
		}
		return dtos;
	}

	/**
	 * Finds the by named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
	 * @param queryString
	 *            the query string
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findByNamedParam(Session session, String queryString, String[] paramNames, Object... values) {
		List<T> dtos = null;
		if (session != null && !StringUtil.isBlank(queryString) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final Query query = session.createQuery(queryString);
			applyNamedParametersToQuery(query, paramNames, values);

			dtos = query.list();
		}
		return dtos;
	}

	/**
	 * Finds the by named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
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
	public static <T> List<T> findByNamedParam(Session session, final String queryString, final Integer firstResult, final Integer maxResults, final String[] paramNames,
			final Object... values) {
		List<T> dtos = null;
		if (session != null && !StringUtil.isBlank(queryString) && firstResult != null && maxResults != null && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final Query query = session.createQuery(queryString);
			applyNamedParametersToQuery(query, paramNames, values);

			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
			dtos = query.list();
		}
		return dtos;
	}

	/**
	 * Finds the by named query.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
	 * @param queryName
	 *            the query name
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findByNamedQuery(Session session, String queryName, Object... values) {
		List<T> dtos = null;
		if (session != null && !StringUtil.isBlank(queryName)) {
			final Query query = session.getNamedQuery(queryName);
			applyParametersToQuery(query, values);

			dtos = query.list();
		}
		return dtos;
	}

	/**
	 * Finds the by named query.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
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
	public static <T> List<T> findByNamedQuery(Session session, final String queryName, final Integer firstResult, final Integer maxResults, final Object... values) {
		List<T> dtos = null;
		if (session != null && !StringUtil.isBlank(queryName) && firstResult != null && maxResults != null) {
			final Query query = session.getNamedQuery(queryName);
			applyParametersToQuery(query, values);

			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
			dtos = query.list();
		}
		return dtos;
	}

	/**
	 * Finds the by named query and named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
	 * @param queryName
	 *            the query name
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findByNamedQueryAndNamedParam(Session session, String queryName, String[] paramNames, Object... values) {
		List<T> dtos = null;
		if (session != null && !StringUtil.isBlank(queryName) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final Query query = session.getNamedQuery(queryName);
			applyNamedParametersToQuery(query, paramNames, values);

			dtos = query.list();
		}
		return dtos;
	}

	/**
	 * Finds the by named query and named param.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param session
	 *            the session
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
	public static <T> List<T> findByNamedQueryAndNamedParam(Session session, final String queryName, final Integer firstResult, final Integer maxResults,
			final String[] paramNames, final Object... values) {
		List<T> dtos = null;
		if (session != null && !StringUtil.isBlank(queryName) && firstResult != null && maxResults != null && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
			final Query query = session.getNamedQuery(queryName);
			applyNamedParametersToQuery(query, paramNames, values);

			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
			dtos = query.list();
		}
		return dtos;
	}

	/**
	 * Gets the number.
	 * 
	 * @param session
	 *            the session
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the number
	 */
	public static Integer getNumber(Session session, String queryString, Object... values) {
		final List<?> list = find(session, queryString, values);
		return CollectionUtil.getIntegerElementFromList(list);
	}

	/**
	 * Gets the number by named param.
	 * 
	 * @param session
	 *            the session
	 * @param queryString
	 *            the query string
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the number by named param
	 */
	public static Integer getNumberByNamedParam(Session session, String queryString, String[] paramNames, Object... values) {
		final List<?> list = findByNamedParam(session, queryString, paramNames, values);
		return CollectionUtil.getIntegerElementFromList(list);
	}

	/**
	 * Gets the number by named query.
	 * 
	 * @param session
	 *            the session
	 * @param queryName
	 *            the query name
	 * @param values
	 *            the values
	 * @return the number by named query
	 */
	public static Integer getNumberByNamedQuery(Session session, String queryName, Object... values) {
		final List<?> list = findByNamedQuery(session, queryName, values);
		return CollectionUtil.getIntegerElementFromList(list);
	}

	/**
	 * Gets the number by named query and named param.
	 * 
	 * @param session
	 *            the session
	 * @param queryName
	 *            the query name
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the number by named query and named param
	 */
	public static Integer getNumberByNamedQueryAndNamedParam(Session session, String queryName, String[] paramNames, Object... values) {
		final List<?> list = findByNamedQueryAndNamedParam(session, queryName, paramNames, values);
		return CollectionUtil.getIntegerElementFromList(list);
	}

	/**
	 * Gets the boolean.
	 * 
	 * @param session
	 *            the session
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the boolean
	 */
	public static Boolean getBoolean(Session session, String queryString, Object... values) {
		final List<?> list = find(session, queryString, values);
		return CollectionUtil.getBooleanElementFromList(list);
	}

	/**
	 * Gets the boolean by named param.
	 * 
	 * @param session
	 *            the session
	 * @param queryString
	 *            the query string
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the boolean by named param
	 */
	public static Boolean getBooleanByNamedParam(Session session, String queryString, String[] paramNames, Object... values) {
		final List<?> list = findByNamedParam(session, queryString, paramNames, values);
		return CollectionUtil.getBooleanElementFromList(list);
	}

	/**
	 * Gets the boolean by named query.
	 * 
	 * @param session
	 *            the session
	 * @param queryName
	 *            the query name
	 * @param values
	 *            the values
	 * @return the boolean by named query
	 */
	public static Boolean getBooleanByNamedQuery(Session session, String queryName, Object... values) {
		final List<?> list = findByNamedQuery(session, queryName, values);
		return CollectionUtil.getBooleanElementFromList(list);
	}

	/**
	 * Gets the boolean by named query and named param.
	 * 
	 * @param session
	 *            the session
	 * @param queryName
	 *            the query name
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the boolean by named query and named param
	 */
	public static Boolean getBooleanByNamedQueryAndNamedParam(Session session, String queryName, String[] paramNames, Object... values) {
		final List<?> list = findByNamedQueryAndNamedParam(session, queryName, paramNames, values);
		return CollectionUtil.getBooleanElementFromList(list);
	}

	/**
	 * Apply parameters to query.
	 * 
	 * @param query
	 *            the query
	 * @param values
	 *            the values
	 * @throws HibernateException
	 *             the hibernate exception
	 */
	public static void applyParametersToQuery(Query query, Object... values) throws HibernateException {
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
	 * @throws HibernateException
	 *             the hibernate exception
	 */
	public static void applyNamedParametersToQuery(Query query, String[] paramNames, Object... values) throws HibernateException {
		if (query != null) {
			if (!ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
				for (int i = 0; i < values.length; i++) {
					applyNamedParameterToQuery(query, paramNames[i], values[i]);
				}
			}
		}
	}

	/**
	 * Apply named parameter to query.
	 * 
	 * @param query
	 *            the query
	 * @param paramName
	 *            the param name
	 * @param value
	 *            the value
	 * @throws HibernateException
	 *             the hibernate exception
	 */
	public static void applyNamedParameterToQuery(Query query, String paramName, Object value) throws HibernateException {
		if (value instanceof Collection<?>) {
			query.setParameterList(paramName, (Collection<?>) value);
		} else if (value instanceof Object[]) {
			query.setParameterList(paramName, (Object[]) value);
		} else {
			query.setParameter(paramName, value);
		}
	}
}
