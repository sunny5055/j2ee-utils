package com.googlecode.jutils.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.googlecode.jutils.BooleanUtil;

/**
 * The Class CollectionUtil.
 */
public final class CollectionUtil extends CollectionUtils {

	/**
	 * Instantiates a new collection util.
	 */
	private CollectionUtil() {
		super();
	}

	/**
	 * To list.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param values
	 *            the values
	 * @return the list
	 */
	public static <T> List<T> toList(Collection<T> values) {
		List<T> list = null;
		if (values != null) {
			list = new ArrayList<T>(values);
		}
		return list;
	}

	/**
	 * To set.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param values
	 *            the values
	 * @return the sets the
	 */
	public static <T> Set<T> toSet(Collection<T> values) {
		Set<T> valueSet = null;
		if (values != null) {
			if (!CollectionUtil.isEmpty(values)) {
				valueSet = new LinkedHashSet<T>(values);
			} else {
				valueSet = new LinkedHashSet<T>();
			}
		}
		return valueSet;
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
	public static <T> T getFirstElementFromList(List<T> result) {
		T object = null;
		if (!CollectionUtil.isEmpty(result)) {
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
	public static Integer getIntegerElementFromList(List<?> result) {
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
	public static Boolean getBooleanElementFromList(List<?> result) {
		Boolean value = null;
		final Object object = getFirstElementFromList(result);
		if (object != null) {
			value = BooleanUtil.toBooleanObject(object);
		}
		return value;
	}

	public static <T> T get(List<T> list, int index) {
		T value = null;
		if (!CollectionUtil.isEmpty(list) && index >= 0) {
			if (list.size() > index) {
				value = list.get(index);
			}
		}
		return value;
	}
}
