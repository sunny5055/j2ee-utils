package com.google.code.jee.utils.dal.dao;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.dto.Dto;

/**
 * The Class AbstractGenericReadDaoHibernate.
 * 
 * @param <PK> the generic type
 * @param <E> the element type
 */
public abstract class AbstractGenericReadDaoHibernate<PK extends Serializable, E extends Dto<PK>> extends
        AbstractGenericReadDao<PK, E> {
    @Autowired
    protected SessionFactory sessionFactory;

    /**
     * Instantiates a new abstract generic read dao hibernate.
     * 
     * @param type the type
     */
    public AbstractGenericReadDaoHibernate(Class<E> type) {
        super(type);
    }

    /**
     * Getter : return the sessionFactory.
     * 
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(PK pk) {
        E dto = null;
        if (pk != null) {
            dto = (E) getCurrentSession().get(entityClass, pk);
        }
        return dto;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<E> getObjects(PK... pks) {
        List<E> dtos = null;
        if (!ArrayUtil.isEmpty(pks)) {
            dtos = getObjects(Arrays.asList(pks));
        }
        return dtos;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<E> getObjects(final Collection<PK> pks) {
        List<E> dtos = null;
        if (!CollectionUtil.isEmpty(pks)) {
            final Criteria criteria = getCurrentSession().createCriteria(entityClass);
            criteria.add(Restrictions.in(getIdName(), pks));
            dtos = criteria.list();
        }
        return dtos;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        return getCurrentSession().createQuery("from " + entityClass.getName()).list();
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
                    count = getNumberByNamedParam(search.getCountQuery(), parameterNames, values);
                } else {
                    count = getNumber(search.getCountQuery());
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
                        dtos = findByNamedParam(search.getQuery(), searchCriteria.getFirstResult(),
                                searchCriteria.getMaxResults(), parameterNames, values);
                    } else {
                        dtos = findByNamedParam(search.getQuery(), parameterNames, values);
                    }
                } else {
                    if (searchCriteria.hasPagination()) {
                        dtos = find(search.getQuery(), searchCriteria.getFirstResult(), searchCriteria.getMaxResults());
                    } else {
                        dtos = find(search.getQuery());
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
        final Criteria criteria = getCurrentSession().createCriteria(entityClass);
        criteria.setProjection(Projections.rowCount());
        return ((Long) criteria.list().get(0)).intValue();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existPk(final PK pk) {
        boolean exist = false;
        if (pk != null) {
            final Criteria criteria = getCurrentSession().createCriteria(entityClass);
            criteria.add(Restrictions.eq(getIdName(), pk));
            criteria.setProjection(Projections.rowCount());
            final Integer count = ((Long) criteria.list().get(0)).intValue();
            exist = count != 0;
        }
        return exist;
    }

    /**
     * Gets the.
     * 
     * @param queryString the query string
     * @param values the values
     * @return the e
     */
    @SuppressWarnings("unchecked")
    protected <T> T get(String queryString, Object... values) {
        T dto = null;
        if (!StringUtil.isBlank(queryString)) {
            final List<E> dtos = find(queryString, values);
            dto = (T) getFirstElementFromList(dtos);
        }
        return dto;
    }

    /**
     * Gets the by named param.
     * 
     * @param queryString the query string
     * @param paramNames the param names
     * @param values the values
     * @return the by named param
     */
    @SuppressWarnings("unchecked")
    protected <T> T getByNamedParam(String queryString, String[] paramNames, Object... values) {
        T dto = null;
        if (!StringUtil.isBlank(queryString) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            final List<E> dtos = findByNamedParam(queryString, paramNames, values);
            dto = (T) getFirstElementFromList(dtos);
        }
        return dto;
    }

    /**
     * Gets the by named query.
     * 
     * @param queryName the query name
     * @param values the values
     * @return the by named query
     */
    @SuppressWarnings("unchecked")
    protected <T> T getByNamedQuery(String queryName, Object... values) {
        T dto = null;
        if (!StringUtil.isBlank(queryName)) {
            final List<E> dtos = findByNamedQuery(queryName, values);
            dto = (T) getFirstElementFromList(dtos);
        }
        return dto;
    }

    /**
     * Gets the by named query and named param.
     * 
     * @param queryName the query name
     * @param paramNames the param names
     * @param values the values
     * @return the by named query and named param
     */
    @SuppressWarnings("unchecked")
    protected <T> T getByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object... values) {
        T dto = null;
        if (!StringUtil.isBlank(queryName) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            final List<E> dtos = findByNamedQueryAndNamedParam(queryName, paramNames, values);
            dto = (T) getFirstElementFromList(dtos);
        }
        return dto;
    }

    /**
     * Finds the.
     * 
     * @param queryString the query string
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> find(final String queryString, final Object... values) {
        List<T> dtos = null;
        if (!StringUtil.isBlank(queryString)) {
            final Query query = getCurrentSession().createQuery(queryString);
            applyParametersToQuery(query, values);

            dtos = query.list();
        }
        return dtos;
    }

    /**
     * Finds the.
     * 
     * @param queryString the query string
     * @param firstResult the firstResult
     * @param maxResults the max results
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> find(final String queryString, final Integer firstResult, final Integer maxResults,
            final Object... values) {
        List<T> dtos = null;
        if (!StringUtil.isBlank(queryString) && firstResult != null && maxResults != null) {
            final Query query = getCurrentSession().createQuery(queryString);
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
     * @param queryString the query string
     * @param paramNames the param names
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> findByNamedParam(String queryString, String[] paramNames, Object... values) {
        List<T> dtos = null;
        if (!StringUtil.isBlank(queryString) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            final Query query = getCurrentSession().createQuery(queryString);
            applyNamedParametersToQuery(query, paramNames, values);

            dtos = query.list();
        }
        return dtos;
    }

    /**
     * Finds the by named param.
     * 
     * @param queryString the query string
     * @param firstResult the first result
     * @param maxResults the max result
     * @param paramNames the param names
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> findByNamedParam(final String queryString, final Integer firstResult,
            final Integer maxResults, final String[] paramNames, final Object... values) {
        List<T> dtos = null;
        if (!StringUtil.isBlank(queryString) && firstResult != null && maxResults != null
                && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            final Query query = getCurrentSession().createQuery(queryString);
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
     * @param queryName the query name
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> findByNamedQuery(String queryName, Object... values) {
        List<T> dtos = null;
        if (!StringUtil.isBlank(queryName)) {
            final Query query = getCurrentSession().getNamedQuery(queryName);
            applyParametersToQuery(query, values);

            dtos = query.list();
        }
        return dtos;
    }

    /**
     * Finds the by named query.
     * 
     * @param queryName the query name
     * @param firstResult the first result
     * @param maxResults the max result
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> findByNamedQuery(final String queryName, final Integer firstResult, final Integer maxResults,
            final Object... values) {
        List<T> dtos = null;
        if (!StringUtil.isBlank(queryName) && firstResult != null && maxResults != null) {
            final Query query = getCurrentSession().getNamedQuery(queryName);
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
     * @param queryName the query name
     * @param paramNames the param names
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object... values) {
        List<T> dtos = null;
        if (!StringUtil.isBlank(queryName) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            final Query query = getCurrentSession().getNamedQuery(queryName);
            applyNamedParametersToQuery(query, paramNames, values);

            dtos = query.list();
        }
        return dtos;
    }

    /**
     * Finds the by named query and named param.
     * 
     * @param queryName the query name
     * @param firstResult the first result
     * @param maxResults the max result
     * @param paramNames the param names
     * @param values the values
     * @return the list
     */
    @SuppressWarnings("unchecked")
    protected <T> List<T> findByNamedQueryAndNamedParam(final String queryName, final Integer firstResult,
            final Integer maxResults, final String[] paramNames, final Object... values) {
        List<T> dtos = null;
        if (!StringUtil.isBlank(queryName) && firstResult != null && maxResults != null
                && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            final Query query = getCurrentSession().getNamedQuery(queryName);
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
     * @param queryString the query string
     * @param values the values
     * @return the number
     */
    protected Integer getNumber(String queryString, Object... values) {
        return getIntegerElementFromList(find(queryString, values));
    }

    /**
     * Gets the number by named param.
     * 
     * @param queryString the query string
     * @param paramNames the param names
     * @param values the values
     * @return the number by named param
     */
    protected Integer getNumberByNamedParam(String queryString, String[] paramNames, Object... values) {
        return getIntegerElementFromList(findByNamedParam(queryString, paramNames, values));
    }

    /**
     * Gets the number by named query.
     * 
     * @param queryName the query name
     * @param values the values
     * @return the number by named query
     */
    protected Integer getNumberByNamedQuery(String queryName, Object... values) {
        return getIntegerElementFromList(findByNamedQuery(queryName, values));
    }

    /**
     * Gets the number by named query and named param.
     * 
     * @param queryName the query name
     * @param paramNames the param names
     * @param values the values
     * @return the number by named query and named param
     */
    protected Integer getNumberByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object... values) {
        return getIntegerElementFromList(findByNamedQueryAndNamedParam(queryName, paramNames, values));
    }

    /**
     * Gets the boolean.
     * 
     * @param queryString the query string
     * @param values the values
     * @return the boolean
     */
    protected Boolean getBoolean(String queryString, Object... values) {
        return getBooleanElementFromList(find(queryString, values));
    }

    /**
     * Gets the boolean by named param.
     * 
     * @param queryString the query string
     * @param paramNames the param names
     * @param values the values
     * @return the boolean by named param
     */
    protected Boolean getBooleanByNamedParam(String queryString, String[] paramNames, Object... values) {
        return getBooleanElementFromList(findByNamedParam(queryString, paramNames, values));
    }

    /**
     * Gets the boolean by named query.
     * 
     * @param queryName the query name
     * @param values the values
     * @return the boolean by named query
     */
    protected Boolean getBooleanByNamedQuery(String queryName, Object... values) {
        return getBooleanElementFromList(findByNamedQuery(queryName, values));
    }

    /**
     * Gets the boolean by named query and named param.
     * 
     * @param queryName the query name
     * @param paramNames the param names
     * @param values the values
     * @return the boolean by named query and named param
     */
    protected Boolean getBooleanByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object... values) {
        return getBooleanElementFromList(findByNamedQueryAndNamedParam(queryName, paramNames, values));
    }

    /**
     * Apply parameters to query.
     * 
     * @param query the query
     * @param values the values
     * @throws HibernateException the hibernate exception
     */
    protected void applyParametersToQuery(Query query, Object... values) throws HibernateException {
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
     * @param query the query
     * @param paramNames the param names
     * @param values the values
     * @throws HibernateException the hibernate exception
     */
    protected void applyNamedParametersToQuery(Query query, String[] paramNames, Object... values)
            throws HibernateException {
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
     * @param query the query
     * @param paramName the param name
     * @param value the value
     * @throws HibernateException the hibernate exception
     */
    protected void applyNamedParameterToQuery(Query query, String paramName, Object value) throws HibernateException {
        if (value instanceof Collection<?>) {
            query.setParameterList(paramName, (Collection<?>) value);
        } else if (value instanceof Object[]) {
            query.setParameterList(paramName, (Object[]) value);
        } else {
            query.setParameter(paramName, value);
        }
    }

    /**
     * Getter : return the idName.
     * 
     * @return the idName
     */
    protected String getIdName() {
        String idName = null;
        if (sessionFactory != null) {
            final ClassMetadata classMetadata = sessionFactory.getClassMetadata(entityClass);
            idName = classMetadata.getIdentifierPropertyName();
        }
        return idName;
    }

    /**
     * Gets the current session.
     * 
     * @return the current session
     */
    protected Session getCurrentSession() {
        return this.sessionFactory != null ? sessionFactory.getCurrentSession() : null;
    }
}
