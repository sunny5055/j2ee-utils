package com.googlecode.jutils.dal.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Class DtoUtil.
 */
public final class DtoUtil {

	/**
	 * Instantiates a new dto util.
	 */
	private DtoUtil() {
		super();
	}

	/**
	 * Gets the primary key list.
	 * 
	 * @param <PK>
	 *            the generic type
	 * @param <E>
	 *            the element type
	 * @param entities
	 *            the entities
	 * @return the primary key list
	 */
	public static <PK extends Serializable, E extends Dto<PK>> List<PK> getPrimaryKeyList(E... entities) {
		List<PK> pks = new ArrayList<PK>();
		if (!ArrayUtil.isEmpty(entities)) {
			pks = getPrimaryKeyList(Arrays.asList(entities));
		}
		return pks;
	}

	/**
	 * Gets the primary key list.
	 * 
	 * @param <PK>
	 *            the generic type
	 * @param <E>
	 *            the element type
	 * @param entities
	 *            the entities
	 * @return the primary key list
	 */
	public static <PK extends Serializable, E extends Dto<PK>> List<PK> getPrimaryKeyList(Collection<E> entities) {
		final List<PK> pks = new ArrayList<PK>();
		if (!CollectionUtils.isEmpty(entities)) {
			for (final E entity : entities) {
				if (entity != null) {
					pks.add(entity.getPrimaryKey());
				}
			}
		}
		return pks;
	}

	/**
	 * Gets the primary key array.
	 * 
	 * @param <PK>
	 *            the generic type
	 * @param <E>
	 *            the element type
	 * @param entities
	 *            the entities
	 * @return the primary key array
	 */
	@SuppressWarnings("unchecked")
	public static <PK extends Serializable, E extends Dto<PK>> PK[] getPrimaryKeyArray(E... entities) {
		final List<PK> pks = getPrimaryKeyList(entities);
		return (PK[]) pks.toArray();
	}

	/**
	 * Gets the primary key array.
	 * 
	 * @param <PK>
	 *            the generic type
	 * @param <E>
	 *            the element type
	 * @param entities
	 *            the entities
	 * @return the primary key array
	 */
	@SuppressWarnings("unchecked")
	public static <PK extends Serializable, E extends Dto<PK>> PK[] getPrimaryKeyArray(List<E> entities) {
		final List<PK> pks = getPrimaryKeyList(entities);
		return (PK[]) pks.toArray();
	}
}
