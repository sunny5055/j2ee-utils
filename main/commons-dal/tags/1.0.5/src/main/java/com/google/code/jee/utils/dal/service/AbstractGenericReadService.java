package com.google.code.jee.utils.dal.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.dao.GenericReadDao;
import com.google.code.jee.utils.dal.dto.Dto;

/**
 * The Class AbstractGenericReadService.
 * 
 * @param <PK> the generic type
 * @param <E> the element type
 * @param <DAO> the generic type
 */
public abstract class AbstractGenericReadService<PK extends Serializable, E extends Dto<PK>, DAO extends GenericReadDao<PK, E>>
        implements GenericReadService<PK, E> {
    /**
     * Logger
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractGenericReadService.class);
    protected DAO dao;

    /**
     * Sets the dao.
     * 
     * @param dao the new dao
     */
    public abstract void setDao(DAO dao);

    /**
     * {@inheritedDoc}
     */
    @Override
    public E get(final PK pk) {
        return this.dao.get(pk);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer count() {
        return this.dao.count();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<E> getObjects(final PK... pks) {
        List<E> entities = null;
        if (!ArrayUtils.isEmpty(pks)) {
            entities = this.getObjects(Arrays.asList(pks));
        }

        if (entities == null) {
            entities = new ArrayList<E>();
        }
        return entities;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<E> getObjects(final Collection<PK> pks) {
        List<E> entities = this.dao.getObjects(pks);

        if (entities == null) {
            entities = new ArrayList<E>();
        }
        return entities;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<E> findAll() {
        List<E> entities = this.dao.findAll();

        if (entities == null) {
            entities = new ArrayList<E>();
        }
        return entities;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<E> findAll(SearchCriteria searchCriteria) {
        List<E> entities = this.dao.findAll(searchCriteria);

        if (entities == null) {
            entities = new ArrayList<E>();
        }
        return entities;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer count(SearchCriteria searchCriteria) {
        return this.dao.count(searchCriteria);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existPk(final PK pk) {
        return this.dao.existPk(pk);
    }
}
