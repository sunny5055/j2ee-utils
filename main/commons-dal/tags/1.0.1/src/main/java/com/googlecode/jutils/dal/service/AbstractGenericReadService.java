package com.googlecode.jutils.dal.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.dao.GenericReadDao;
import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Class AbstractGenericReadService.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 * @param <DAO>
 *            the generic type
 */
public abstract class AbstractGenericReadService<PK extends Serializable, E extends Dto<PK>, DAO extends GenericReadDao<PK, E>> implements GenericReadService<PK, E> {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractGenericReadService.class);
	protected DAO dao;

	/**
	 * Sets the dao.
	 * 
	 * @param dao
	 *            the new dao
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
	public List<E> getObjects(final Collection<PK> pks) {
		List<E> entities = null;
		if (!CollectionUtil.isEmpty(pks)) {
			entities = this.dao.getObjects(pks);
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
	public Integer count() {
		return this.dao.count();
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
	public Integer count(SearchCriteria searchCriteria) {
		return this.dao.count(searchCriteria);
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
	public boolean existPk(final PK pk) {
		return this.dao.existPk(pk);
	}
}
