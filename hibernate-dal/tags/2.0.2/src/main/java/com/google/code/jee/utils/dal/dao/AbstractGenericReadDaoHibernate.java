package com.google.code.jee.utils.dal.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.dto.Dto;
import com.google.code.jee.utils.dal.util.QueryUtil;

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
                    count = QueryUtil.getNumberByNamedParam(getCurrentSession(), search.getCountQuery(),
                            parameterNames, values);
                } else {
                    count = QueryUtil.getNumber(getCurrentSession(), search.getCountQuery());
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
                        dtos = QueryUtil
                                .findByNamedParam(getCurrentSession(), search.getQuery(),
                                        searchCriteria.getFirstResult(), searchCriteria.getMaxResults(),
                                        parameterNames, values);
                    } else {
                        dtos = QueryUtil.findByNamedParam(getCurrentSession(), search.getQuery(), parameterNames,
                                values);
                    }
                } else {
                    if (searchCriteria.hasPagination()) {
                        dtos = QueryUtil.find(getCurrentSession(), search.getQuery(), searchCriteria.getFirstResult(),
                                searchCriteria.getMaxResults());
                    } else {
                        dtos = QueryUtil.find(getCurrentSession(), search.getQuery());
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
