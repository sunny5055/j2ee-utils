package com.google.code.jee.utils.parameter.hibernate.dao.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.parameter.hibernate.dao.ParameterDao;
import com.google.code.jee.utils.parameter.hibernate.model.AbstractParameter;

@Repository
public class ParameterDaoImpl implements ParameterDao {
    @Autowired
    private SessionFactory sessionFactory;
    private Class<?> entityClass;

    public ParameterDaoImpl() {
        super();
        this.entityClass = AbstractParameter.class;
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
    @SuppressWarnings("unchecked")
    public List<AbstractParameter<?>> findAll() {
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

                final Query query = getCurrentSession().createQuery(search.getCountQuery());
                if (!ArrayUtil.isEmpty(parameterNames)) {
                    applyNamedParametersToQuery(query, parameterNames, values);
                } else {
                    applyParametersToQuery(query, values);
                }

                final List<?> result = query.list();
                count = getIntegerElementFromList(result);
            }
        }
        return count;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<AbstractParameter<?>> findAll(SearchCriteria searchCriteria) {
        List<AbstractParameter<?>> parameters = null;
        if (searchCriteria != null) {
            final Search search = getSearch(searchCriteria);
            if (search != null) {
                final String[] parameterNames = search.getParameterNames();
                final Object[] values = search.getValues();

                final Query query = getCurrentSession().createQuery(search.getQuery());
                if (!ArrayUtil.isEmpty(parameterNames)) {
                    applyNamedParametersToQuery(query, parameterNames, values);
                } else {
                    applyParametersToQuery(query, values);
                }

                if (searchCriteria.hasPagination()) {
                    query.setFirstResult(searchCriteria.getFirstResult());
                    query.setMaxResults(searchCriteria.getMaxResults());
                }

                parameters = query.list();
            }
        }
        return parameters;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countByName(String name) {
        Integer count = 0;
        if (!StringUtil.isBlank(name)) {
            final Query query = getCurrentSession().getNamedQuery(ParameterDao.COUNT_BY_NAME);
            query.setParameter("name", name);

            final List<?> result = query.list();
            count = getIntegerElementFromList(result);
        }
        return count;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public AbstractParameter<?> findByName(String name) {
        AbstractParameter<?> parameter = null;
        if (!StringUtil.isBlank(name)) {
            final Query query = getCurrentSession().getNamedQuery(ParameterDao.FIND_BY_NAME);
            query.setParameter("name", name);
            final List<AbstractParameter<?>> parameters = query.list();
            parameter = getFirstElementFromList(parameters);
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
            getCurrentSession().saveOrUpdate(parameter);
            primaryKey = parameter.getPrimaryKey();
        }
        return primaryKey;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer delete(AbstractParameter<?> parameter) {
        Integer deleted = 0;
        if (parameter != null) {
            getCurrentSession().delete(parameter);
            deleted = 1;
        }
        return deleted;
    }

    /**
     * Gets the search.
     * 
     * @param searchCriteria the search criteria
     * @return the search
     */
    private Search getSearch(SearchCriteria searchCriteria) {
        return null; // TODO to complete
    }

    /**
     * Gets the first element from list.
     * 
     * @param <T> the generic type
     * @param result the result
     * @return the first element from list
     */
    private <T> T getFirstElementFromList(List<T> result) {
        T object = null;
        if (!CollectionUtils.isEmpty(result)) {
            object = result.get(0);
        }
        return object;
    }

    /**
     * Gets the integer element from list.
     * 
     * @param result the result
     * @return the integer element from list
     */
    private Integer getIntegerElementFromList(List<?> result) {
        Integer value = null;
        final Object object = getFirstElementFromList(result);
        if (object != null) {
            if (object instanceof Long) {
                value = ((Long) object).intValue();
            } else if (object instanceof Integer) {
                value = (Integer) object;
            }
        }
        return value;
    }

    /**
     * Apply parameters to query.
     * 
     * @param query the query
     * @param values the values
     * @throws HibernateException the hibernate exception
     */
    private void applyParametersToQuery(Query query, Object... values) throws HibernateException {
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
    private void applyNamedParametersToQuery(Query query, String[] paramNames, Object... values)
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
    private void applyNamedParameterToQuery(Query query, String paramName, Object value) throws HibernateException {
        if (value instanceof Collection<?>) {
            query.setParameterList(paramName, (Collection<?>) value);
        } else if (value instanceof Object[]) {
            query.setParameterList(paramName, (Object[]) value);
        } else {
            query.setParameter(paramName, value);
        }
    }

    /**
     * Gets the current session.
     * 
     * @return the current session
     */
    private Session getCurrentSession() {
        return this.sessionFactory != null ? sessionFactory.getCurrentSession() : null;
    }
}
