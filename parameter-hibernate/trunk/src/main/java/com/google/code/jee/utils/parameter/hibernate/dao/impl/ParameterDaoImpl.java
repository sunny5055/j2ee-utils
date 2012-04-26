package com.google.code.jee.utils.parameter.hibernate.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.SortOrder;
import com.google.code.jee.utils.dal.util.QueryUtil;
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
                    QueryUtil.applyNamedParametersToQuery(query, parameterNames, values);
                } else {
                    QueryUtil.applyParametersToQuery(query, values);
                }

                final List<?> result = query.list();
                count = CollectionUtil.getIntegerElementFromList(result);
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
                    QueryUtil.applyNamedParametersToQuery(query, parameterNames, values);
                } else {
                    QueryUtil.applyParametersToQuery(query, values);
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
            count = QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), ParameterDao.COUNT_BY_NAME,
                    new String[] { "name" }, name);
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
            parameter = QueryUtil.getByNamedQueryAndNamedParam(getCurrentSession(), ParameterDao.FIND_BY_NAME,
                    new String[] { "name" }, name);
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

    /**
     * Gets the current session.
     * 
     * @return the current session
     */
    private Session getCurrentSession() {
        return this.sessionFactory != null ? sessionFactory.getCurrentSession() : null;
    }
}
