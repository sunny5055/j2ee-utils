package com.googlecode.jutils.dal.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.googlecode.jutils.BooleanUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.dal.Search;
import com.googlecode.jutils.dal.SearchCriteria;
import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Class AbstractGenericReadDao.
 * 
 * @param <PK>
 *            the generic type
 * @param <E>
 *            the element type
 */
public abstract class AbstractGenericReadDao<PK extends Serializable, E extends Dto<PK>> implements GenericReadDao<PK, E> {
	protected static final Logger LOGGER = Logger.getLogger(AbstractGenericReadDao.class);
	protected Class<PK> pkClass;
	protected Class<E> entityClass;

	/**
	 * Instantiates a new abstract generic read dao.
	 * 
	 * @param type
	 *            the type
	 */
	@SuppressWarnings("unchecked")
	public AbstractGenericReadDao() {
		final Type type = getClass().getGenericSuperclass();
		final ParameterizedType parameterizedType = (ParameterizedType) type;
		final Type[] typeArguments = parameterizedType.getActualTypeArguments();
		if (!ArrayUtil.isEmpty(typeArguments) && typeArguments.length == 2) {
			pkClass = (Class<PK>) typeArguments[0];
			entityClass = (Class<E>) typeArguments[1];
		}
	}

	/**
	 * Gets the first element from list.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param result
	 *            the result
	 * @return the first element from list
	 */
	protected <T> T getFirstElementFromList(List<T> result) {
		T object = null;
		if (!CollectionUtils.isEmpty(result)) {
			object = result.get(0);
		}
		return object;
	}

	/**
	 * Gets the integer element from list.
	 * 
	 * @param result
	 *            the result
	 * @return the integer element from list
	 */
	protected Integer getIntegerElementFromList(List<?> result) {
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
	 * Gets the boolean element from list.
	 * 
	 * @param result
	 *            the result
	 * @return the boolean element from list
	 */
	protected Boolean getBooleanElementFromList(List<?> result) {
		Boolean value = null;
		final Object object = getFirstElementFromList(result);
		if (object != null) {
			value = BooleanUtil.toBooleanObject(object);
		}
		return value;
	}

	/**
	 * Gets the search.
	 * 
	 * @param searchCriteria
	 *            the search criteria
	 * @return the search
	 */
	protected abstract Search getSearch(SearchCriteria searchCriteria);
}
