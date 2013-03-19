package com.google.code.jee.utils.dal.dao;

import java.io.Serializable;

import com.google.code.jee.utils.dal.dto.Dto;

/**
 * The Class AbstractGenericDaoHibernate.
 * 
 * @param <PK> the generic type
 * @param <E> the element type
 */
public abstract class AbstractGenericDaoHibernate<PK extends Serializable, E extends Dto<PK>> extends
        AbstractGenericReadDaoHibernate<PK, E> implements GenericDao<PK, E> {

    /**
     * Instantiates a new abstract generic dao hibernate.
     * 
     * @param type the type
     */
    public AbstractGenericDaoHibernate(Class<E> type) {
        super(type);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public PK create(E dto) {
        PK primaryKey = null;
        if (dto != null) {
            getCurrentSession().saveOrUpdate(dto);
            primaryKey = dto.getPrimaryKey();
        }
        return primaryKey;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer update(E dto) {
        Integer updated = 0;
        if (dto != null) {
            getCurrentSession().saveOrUpdate(dto);
            updated = 1;
        }
        return updated;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer delete(E dto) {
        Integer deleted = 0;
        if (dto != null) {
            deleted = deleteByPrimaryKey(dto.getPrimaryKey());
        }
        return deleted;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer deleteByPrimaryKey(final PK pk) {
        Integer deleted = 0;
        if (pk != null) {
            final E dto = get(pk);
            if (dto != null) {
                getCurrentSession().delete(dto);
                deleted = 1;
            }
        }
        return deleted;
    }
}
