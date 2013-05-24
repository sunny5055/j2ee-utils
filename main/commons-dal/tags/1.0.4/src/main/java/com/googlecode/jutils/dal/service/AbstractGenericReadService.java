package com.googlecode.jutils.dal.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.dao.GenericReadDao;
import com.googlecode.jutils.dal.dto.Dto;
import com.googlecode.jutils.dal.entity.BaseEntity;

/**
 * The Class AbstractGenericReadService.
 * 
 * @param <PK>
 *            the generic type
 * @param <DTO>
 *            the element type
 * @param <E>
 *            the element type
 * @param <DAO>
 *            the generic type
 */
public abstract class AbstractGenericReadService<PK extends Serializable, DTO extends Dto<PK>, E extends BaseEntity<PK>, DAO extends GenericReadDao<PK, E>> implements
		GenericReadService<PK, DTO> {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractGenericReadService.class);
	protected Class<PK> pkClass;
	protected Class<DTO> dtoClass;
	protected Class<E> entityClass;
	protected Mapper mapper;
	protected DAO dao;

	@SuppressWarnings("unchecked")
	public AbstractGenericReadService() {
		final Type type = getClass().getGenericSuperclass();
		final ParameterizedType parameterizedType = (ParameterizedType) type;
		final Type[] typeArguments = parameterizedType.getActualTypeArguments();
		if (!ArrayUtil.isEmpty(typeArguments) && typeArguments.length == 4) {
			pkClass = (Class<PK>) typeArguments[0];
			dtoClass = (Class<DTO>) typeArguments[1];
			entityClass = (Class<E>) typeArguments[2];
		}
	}

	/**
	 * Sets the mapper.
	 * 
	 * @param mapper
	 *            the new mapper
	 */
	public abstract void setMapper(Mapper mapper);

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
	public DTO get(final PK pk) {
		DTO dto = null;
		if (pk != null) {
			final E entity = this.dao.get(pk);
			dto = toDto(entity);
		}
		return dto;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<DTO> getObjects(final Collection<PK> pks) {
		List<DTO> dtos = null;
		if (!CollectionUtil.isEmpty(pks)) {
			final List<E> entities = this.dao.getObjects(pks);

			dtos = toDtos(entities);
		}
		return dtos;
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
	public List<DTO> findAll() {
		final List<E> entities = this.dao.findAll();

		return toDtos(entities);
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
	public List<DTO> findAll(SearchCriteria searchCriteria) {
		List<DTO> dtos = null;
		if (searchCriteria != null) {
			final List<E> entities = this.dao.findAll(searchCriteria);
			dtos = toDtos(entities);
		}

		return dtos;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public boolean existPk(final PK pk) {
		return this.dao.existPk(pk);
	}

	/**
	 * To entity.
	 * 
	 * @param dto
	 *            the dto
	 * @return the e
	 */
	protected E toEntity(DTO dto) {
		E entity = null;
		if (dto != null) {
			entity = mapper.map(dto, entityClass);
		}
		return entity;
	}

	/**
	 * To dto.
	 * 
	 * @param entity
	 *            the entity
	 * @return the dto
	 */
	protected DTO toDto(E entity) {
		DTO dto = null;
		if (entity != null) {
			dto = mapper.map(entity, dtoClass);
		}
		return dto;
	}

	/**
	 * To entities.
	 * 
	 * @param dtos
	 *            the dtos
	 * @return the list
	 */
	protected List<E> toEntities(Collection<DTO> dtos) {
		final List<E> entities = new ArrayList<E>();
		if (!CollectionUtil.isEmpty(dtos)) {
			for (final DTO dto : dtos) {
				entities.add(toEntity(dto));
			}
		}
		return entities;
	}

	/**
	 * To dtos.
	 * 
	 * @param entities
	 *            the entities
	 * @return the list
	 */
	protected List<DTO> toDtos(Collection<E> entities) {
		final List<DTO> dtos = new ArrayList<DTO>();
		if (!CollectionUtil.isEmpty(entities)) {
			for (final E entity : entities) {
				dtos.add(toDto(entity));
			}
		}
		return dtos;
	}
}
