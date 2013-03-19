package com.google.code.jee.utils.dal.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

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
    protected HibernateTemplate hibernateTemplate;

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
     * Inits the.
     */
    @PostConstruct
    protected void init() {
        if (hibernateTemplate == null || sessionFactory != hibernateTemplate.getSessionFactory()) {
            hibernateTemplate = new HibernateTemplate(sessionFactory);
        }
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
     * Getter : return the hibernateTemplate.
     * 
     * @return the hibernateTemplate
     */
    public HibernateTemplate getHibernateTemplate() {
        return this.hibernateTemplate;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public E get(PK pk) {
        E dto = null;
        if (pk != null) {
            dto = hibernateTemplate.get(entityClass, pk);
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
    public List<E> getObjects(final Collection<PK> pks) {
        List<E> dtos = null;
        if (!CollectionUtil.isEmpty(pks)) {
            dtos = hibernateTemplate.execute(new HibernateCallback<List<E>>() {
                @Override
                @SuppressWarnings ("unchecked")
                public List<E> doInHibernate(Session session) throws HibernateException, SQLException {
                    final Criteria criteria = session.createCriteria(entityClass);
                    criteria.add(Restrictions.in(getIdName(), pks));
                    return criteria.list();
                }
            });
        }
        return dtos;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<E> findAll() {
        return hibernateTemplate.loadAll(entityClass);
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
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                final Criteria criteria = session.createCriteria(entityClass);
                criteria.setProjection(Projections.rowCount());
                return ((Long) criteria.list().get(0)).intValue();
            }
        });
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existPk(final PK pk) {
        boolean exist = false;
        if (pk != null) {
            final Integer count = hibernateTemplate.execute(new HibernateCallback<Integer>() {
                @Override
                public Integer doInHibernate(Session session) throws HibernateException, SQLException {
                    final Criteria criteria = session.createCriteria(entityClass);
                    criteria.add(Restrictions.eq(getIdName(), pk));
                    criteria.setProjection(Projections.rowCount());
                    return ((Long) criteria.list().get(0)).intValue();
                }
            });
            exist = count != 0;
        }
        return exist;
    }

    /**
     * Finds the.
     * 
     * @param queryString the query string
     * @param values the values
     * @return the list
     */
    protected List<E> find(final String queryString, final Object... values) {
        List<E> dtos = null;
        if (!StringUtil.isBlank(queryString)) {
            dtos = hibernateTemplate.execute(new HibernateCallback<List<E>>() {
                @Override
                @SuppressWarnings ("unchecked")
                public List<E> doInHibernate(Session session) throws HibernateException, SQLException {
                    final Query query = session.createQuery(queryString);
                    prepareQuery(query);
                    if (!ArrayUtil.isEmpty(values)) {
                        for (int i = 0; i < values.length; i++) {
                            query.setParameter(i, values[i]);
                        }
                    }
                    return query.list();
                }
            });
        }
        return dtos;
    }

    /**
     * Finds the.
     * 
     * @param queryString the query string
     * @param begin the begin
     * @param maxResults the max results
     * @param values the values
     * @return the list
     */
    protected List<E> find(final String queryString, final Integer begin, final Integer maxResults,
            final Object... values) {
        List<E> dtos = null;
        if (!StringUtil.isBlank(queryString) && begin != null && maxResults != null) {
            dtos = hibernateTemplate.execute(new HibernateCallback<List<E>>() {
                @Override
                @SuppressWarnings ("unchecked")
                public List<E> doInHibernate(Session session) throws HibernateException, SQLException {
                    final Query query = session.createQuery(queryString);
                    prepareQuery(query);
                    if (!ArrayUtil.isEmpty(values)) {
                        for (int i = 0; i < values.length; i++) {
                            query.setParameter(i, values[i]);
                        }
                    }
                    query.setMaxResults(maxResults);
                    query.setFirstResult(begin);

                    return query.list();
                }
            });
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
    @SuppressWarnings ("unchecked")
    protected List<E> findByNamedParam(String queryString, String[] paramNames, Object... values) {
        List<E> dtos = null;
        if (!StringUtil.isBlank(queryString) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            dtos = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
        }
        return dtos;
    }

    /**
     * Finds the by named param.
     * 
     * @param queryString the query string
     * @param firstResult the first result
     * @param maxResult the max result
     * @param paramNames the param names
     * @param values the values
     * @return the list
     */
    protected List<E> findByNamedParam(final String queryString, final Integer firstResult, final Integer maxResult,
            final String[] paramNames, final Object... values) {
        List<E> dtos = null;
        if (!StringUtil.isBlank(queryString) && firstResult != null && maxResult != null
                && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            dtos = hibernateTemplate.execute(new HibernateCallback<List<E>>() {
                @Override
                @SuppressWarnings ("unchecked")
                public List<E> doInHibernate(Session session) throws HibernateException {
                    final Query query = session.createQuery(queryString);
                    prepareQuery(query);
                    for (int i = 0; i < values.length; i++) {
                        applyNamedParameterToQuery(query, paramNames[i], values[i]);
                    }
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResult);
                    return query.list();
                }
            });
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
    @SuppressWarnings ("unchecked")
    protected List<E> findByNamedQuery(String queryName, Object... values) {
        List<E> dtos = null;
        if (!StringUtil.isBlank(queryName)) {
            if (!ArrayUtil.isEmpty(values)) {
                dtos = hibernateTemplate.findByNamedQuery(queryName, values);
            } else {
                dtos = hibernateTemplate.findByNamedQuery(queryName);
            }
        }
        return dtos;
    }

    /**
     * Finds the by named query.
     * 
     * @param queryName the query name
     * @param firstResult the first result
     * @param maxResult the max result
     * @param values the values
     * @return the list
     */
    protected List<E> findByNamedQuery(final String queryName, final Integer firstResult, final Integer maxResult,
            final Object... values) {
        List<E> dtos = null;
        if (!StringUtil.isBlank(queryName) && firstResult != null && maxResult != null) {
            dtos = hibernateTemplate.execute(new HibernateCallback<List<E>>() {
                @Override
                @SuppressWarnings ("unchecked")
                public List<E> doInHibernate(Session session) throws HibernateException {
                    final Query query = session.getNamedQuery(queryName);
                    prepareQuery(query);
                    if (!ArrayUtil.isEmpty(values)) {
                        for (int i = 0; i < values.length; i++) {
                            query.setParameter(i, values[i]);
                        }
                    }
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResult);
                    return query.list();
                }
            });
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
    @SuppressWarnings ("unchecked")
    protected List<E> findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object... values) {
        List<E> dtos = null;
        if (!StringUtil.isBlank(queryName) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            dtos = hibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramNames, values);
        }
        return dtos;
    }

    /**
     * Finds the by named query and named param.
     * 
     * @param queryName the query name
     * @param firstResult the first result
     * @param maxResult the max result
     * @param paramNames the param names
     * @param values the values
     * @return the list
     */
    protected List<E> findByNamedQueryAndNamedParam(final String queryName, final Integer firstResult,
            final Integer maxResult, final String[] paramNames, final Object... values) {
        List<E> dtos = null;
        if (!StringUtil.isBlank(queryName) && firstResult != null && maxResult != null
                && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            dtos = hibernateTemplate.execute(new HibernateCallback<List<E>>() {
                @Override
                @SuppressWarnings ("unchecked")
                public List<E> doInHibernate(Session session) throws HibernateException {
                    final Query query = session.getNamedQuery(queryName);
                    prepareQuery(query);
                    for (int i = 0; i < values.length; i++) {
                        applyNamedParameterToQuery(query, paramNames[i], values[i]);
                    }
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResult);
                    return query.list();
                }
            });
        }
        return dtos;
    }

    /**
     * Gets the.
     * 
     * @param queryString the query string
     * @param values the values
     * @return the e
     */
    @SuppressWarnings ("unchecked")
    protected E get(String queryString, Object... values) {
        E dto = null;
        if (!StringUtil.isBlank(queryString)) {
            dto = (E) getFirstElementFromList(hibernateTemplate.find(queryString, values));
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
    @SuppressWarnings ("unchecked")
    protected E getByNamedParam(String queryString, String[] paramNames, Object... values) {
        E dto = null;
        if (!StringUtil.isBlank(queryString) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            dto = (E) getFirstElementFromList(hibernateTemplate.findByNamedParam(queryString, paramNames, values));
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
    @SuppressWarnings ("unchecked")
    protected E getByNamedQuery(String queryName, Object... values) {
        E dto = null;
        if (!StringUtil.isBlank(queryName)) {
            dto = (E) getFirstElementFromList(hibernateTemplate.findByNamedQuery(queryName, values));
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
    @SuppressWarnings ("unchecked")
    protected E getByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object... values) {
        E dto = null;
        if (!StringUtil.isBlank(queryName) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            dto = (E) getFirstElementFromList(hibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramNames,
                    values));
        }
        return dto;
    }

    /**
     * Exec.
     * 
     * @param queryString the query string
     * @param values the values
     * @return the list
     */
    protected List<?> exec(String queryString, Object... values) {
        List<?> dtos = null;
        if (!StringUtil.isBlank(queryString)) {
            dtos = hibernateTemplate.find(queryString, values);
        }
        return dtos;
    }

    /**
     * Exec.
     * 
     * @param queryString the query string
     * @param firstResult the first result
     * @param maxResult the max result
     * @param values the values
     * @return the list
     */
    protected List<?> exec(final String queryString, final Integer firstResult, final Integer maxResult,
            final Object... values) {
        List<?> dtos = null;
        if (!StringUtil.isBlank(queryString) && firstResult != null && maxResult != null) {
            dtos = hibernateTemplate.execute(new HibernateCallback<List<?>>() {
                @Override
                public List<?> doInHibernate(Session session) throws HibernateException {
                    final Query query = session.createQuery(queryString);
                    prepareQuery(query);
                    if (!ArrayUtil.isEmpty(values)) {
                        for (int i = 0; i < values.length; i++) {
                            query.setParameter(i, values[i]);
                        }
                    }
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResult);
                    return query.list();
                }
            });
        }
        return dtos;
    }

    /**
     * Exec by named param.
     * 
     * @param queryString the query string
     * @param paramNames the param names
     * @param values the values
     * @return the list
     */
    protected List<?> execByNamedParam(String queryString, String[] paramNames, Object... values) {
        List<?> dtos = null;
        if (!StringUtil.isBlank(queryString) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            dtos = hibernateTemplate.findByNamedParam(queryString, paramNames, values);
        }
        return dtos;
    }

    /**
     * Exec by named param.
     * 
     * @param queryString the query string
     * @param firstResult the first result
     * @param maxResult the max result
     * @param paramNames the param names
     * @param values the values
     * @return the list
     */
    protected List<?> execByNamedParam(final String queryString, final Integer firstResult, final Integer maxResult,
            final String[] paramNames, final Object... values) {
        List<?> dtos = null;
        if (!StringUtil.isBlank(queryString) && firstResult != null && maxResult != null
                && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            dtos = hibernateTemplate.execute(new HibernateCallback<List<?>>() {
                @Override
                public List<?> doInHibernate(Session session) throws HibernateException {
                    final Query query = session.createQuery(queryString);
                    prepareQuery(query);
                    for (int i = 0; i < values.length; i++) {
                        applyNamedParameterToQuery(query, paramNames[i], values[i]);
                    }
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResult);
                    return query.list();
                }
            });
        }
        return dtos;
    }

    /**
     * Exec by named query.
     * 
     * @param queryName the query name
     * @param values the values
     * @return the list
     */
    protected List<?> execByNamedQuery(String queryName, Object... values) {
        List<?> dtos = null;
        if (!StringUtil.isBlank(queryName)) {
            dtos = hibernateTemplate.findByNamedQuery(queryName, values);
        }
        return dtos;
    }

    /**
     * Exec by named query.
     * 
     * @param queryName the query name
     * @param firstResult the first result
     * @param maxResult the max result
     * @param values the values
     * @return the list
     */
    protected List<?> execByNamedQuery(final String queryName, final Integer firstResult, final Integer maxResult,
            final Object... values) {
        List<?> dtos = null;
        if (!StringUtil.isBlank(queryName) && firstResult != null && maxResult != null) {
            dtos = hibernateTemplate.execute(new HibernateCallback<List<?>>() {
                @Override
                public List<?> doInHibernate(Session session) throws HibernateException {
                    final Query query = session.getNamedQuery(queryName);
                    prepareQuery(query);
                    if (!ArrayUtil.isEmpty(values)) {
                        for (int i = 0; i < values.length; i++) {
                            query.setParameter(i, values[i]);
                        }
                    }
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResult);
                    return query.list();
                }
            });
        }
        return dtos;
    }

    /**
     * Exec by named query and named param.
     * 
     * @param queryName the query name
     * @param paramNames the param names
     * @param values the values
     * @return the list
     */
    protected List<?> execByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object... values) {
        List<?> dtos = null;
        if (!StringUtil.isBlank(queryName) && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            dtos = hibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramNames, values);
        }
        return dtos;
    }

    /**
     * Exec by named query and named param.
     * 
     * @param queryName the query name
     * @param firstResult the first result
     * @param maxResult the max result
     * @param paramNames the param names
     * @param values the values
     * @return the list
     */
    protected List<?> execByNamedQueryAndNamedParam(final String queryName, final Integer firstResult,
            final Integer maxResult, final String[] paramNames, final Object... values) {
        List<?> dtos = null;
        if (!StringUtil.isBlank(queryName) && firstResult != null && maxResult != null
                && !ArrayUtil.isEmpty(paramNames) && !ArrayUtil.isEmpty(values)) {
            dtos = hibernateTemplate.execute(new HibernateCallback<List<?>>() {
                @Override
                public List<?> doInHibernate(Session session) throws HibernateException {
                    final Query query = session.getNamedQuery(queryName);
                    prepareQuery(query);
                    for (int i = 0; i < values.length; i++) {
                        applyNamedParameterToQuery(query, paramNames[i], values[i]);
                    }
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResult);
                    return query.list();
                }
            });
        }
        return dtos;
    }

    /**
     * Exec first.
     * 
     * @param queryString the query string
     * @param values the values
     * @return the object
     */
    protected Object execFirst(String queryString, Object... values) {
        return getFirstElementFromList(exec(queryString, values));
    }

    /**
     * Exec first by named param.
     * 
     * @param queryString the query string
     * @param paramNames the param names
     * @param values the values
     * @return the object
     */
    protected Object execFirstByNamedParam(String queryString, String[] paramNames, Object... values) {
        return getFirstElementFromList(execByNamedParam(queryString, paramNames, values));
    }

    /**
     * Exec first by named query.
     * 
     * @param queryName the query name
     * @param values the values
     * @return the object
     */
    protected Object execFirstByNamedQuery(String queryName, Object... values) {
        return getFirstElementFromList(execByNamedQuery(queryName, values));
    }

    /**
     * Exec first by named query and named param.
     * 
     * @param queryName the query name
     * @param paramNames the param names
     * @param values the values
     * @return the object
     */
    protected Object execFirstByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object... values) {
        return getFirstElementFromList(execByNamedQueryAndNamedParam(queryName, paramNames, values));
    }

    /**
     * Gets the number.
     * 
     * @param queryString the query string
     * @param values the values
     * @return the number
     */
    protected Integer getNumber(String queryString, Object... values) {
        return getIntegerElementFromList(exec(queryString, values));
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
        return getIntegerElementFromList(execByNamedParam(queryString, paramNames, values));
    }

    /**
     * Gets the number by named query.
     * 
     * @param queryName the query name
     * @param values the values
     * @return the number by named query
     */
    protected Integer getNumberByNamedQuery(String queryName, Object... values) {
        return getIntegerElementFromList(execByNamedQuery(queryName, values));
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
        return getIntegerElementFromList(execByNamedQueryAndNamedParam(queryName, paramNames, values));
    }

    /**
     * Gets the boolean.
     * 
     * @param queryString the query string
     * @param values the values
     * @return the boolean
     */
    protected Boolean getBoolean(String queryString, Object... values) {
        return getBooleanElementFromList(exec(queryString, values));
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
        return getBooleanElementFromList(execByNamedParam(queryString, paramNames, values));
    }

    /**
     * Gets the boolean by named query.
     * 
     * @param queryName the query name
     * @param values the values
     * @return the boolean by named query
     */
    protected Boolean getBooleanByNamedQuery(String queryName, Object... values) {
        return getBooleanElementFromList(execByNamedQuery(queryName, values));
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
        return getBooleanElementFromList(execByNamedQueryAndNamedParam(queryName, paramNames, values));
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
     * Prepare query.
     * 
     * @param session the session
     * @param queryName the query name
     * @param queryArgs the query args
     * @return the query
     */
    protected Query prepareQuery(Session session, String queryName, Object[] queryArgs) {
        final Query queryObject = session.getNamedQuery(queryName);
        prepareQuery(queryObject);
        if (queryArgs != null) {
            for (int i = 0; i < queryArgs.length; i++) {
                queryObject.setParameter(i, queryArgs[i]);
            }
        }
        return queryObject;
    }

    /**
     * Prepare query.
     * 
     * @param queryObject the query object
     */
    protected void prepareQuery(Query queryObject) {
        if (hibernateTemplate.isCacheQueries()) {
            queryObject.setCacheable(true);
            if (hibernateTemplate.getQueryCacheRegion() != null) {
                queryObject.setCacheRegion(hibernateTemplate.getQueryCacheRegion());
            }
        }
        if (hibernateTemplate.getFetchSize() > 0) {
            queryObject.setFetchSize(hibernateTemplate.getFetchSize());
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
     * Gets the id list.
     * 
     * @param dtos the dtos
     * @return the id list
     */
    protected List<PK> getIdList(E... dtos) {
        List<PK> pks = null;
        if (!ArrayUtil.isEmpty(dtos)) {
            pks = getIdList(Arrays.asList(dtos));
        }
        return pks;
    }

    /**
     * Gets the id list.
     * 
     * @param dtos the dtos
     * @return the id list
     */
    protected List<PK> getIdList(Collection<E> dtos) {
        List<PK> pks = null;
        if (!CollectionUtil.isEmpty(dtos)) {
            pks = new ArrayList<PK>();
            for (final E dto : dtos) {
                pks.add(dto.getPrimaryKey());
            }
        }
        return pks;
    }

    /**
     * Gets the session.
     * 
     * @return the session
     */
    protected Session getSession() {
        return this.sessionFactory != null ? sessionFactory.getCurrentSession() : null;
    }
}
