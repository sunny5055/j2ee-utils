package com.googlecode.jutils.collection;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * The Class MapUtil.
 */
public final class MapUtil extends MapUtils {

	/**
	 * Instantiates a new map util.
	 */
	private MapUtil() {
		super();
	}

	/**
	 * Checks if is same size.
	 * 
	 * @param map1
	 *            the map1
	 * @param map2
	 *            the map2
	 * @return true, if is same size
	 */
	public static boolean isSameSize(Map<?, ?> map1, Map<?, ?> map2) {
		boolean sameSize = false;
		if (map1 == null && !MapUtil.isEmpty(map2)) {
			sameSize = false;
		} else if (map2 == null && !MapUtil.isEmpty(map1)) {
			sameSize = false;
		} else if (map1 != null && map2 != null && map1.size() != map2.size()) {
			sameSize = false;
		} else {
			sameSize = true;
		}
		return sameSize;
	}

	/**
	 * Keep only.
	 * 
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 * @param map
	 *            the map
	 * @param keys
	 *            the keys
	 * @return the map
	 */
	public static <K, V> Map<K, V> keepOnly(Map<K, V> map, K... keys) {
		Map<K, V> result = null;
		if (!MapUtil.isEmpty(map) && !ArrayUtil.isEmpty(keys)) {
			result = new HashMap<K, V>();

			for (final K key : keys) {
				final V value = map.get(key);
				result.put(key, value);
			}
		}

		return result;
	}

	/**
	 * Removes the entry in the map.
	 * 
	 * @param <K>
	 *            the key type
	 * @param <V>
	 *            the value type
	 * @param map
	 *            the map
	 * @param keys
	 *            the keys
	 * @return the map
	 */
	public static <K, V> Map<K, V> remove(Map<K, V> map, K... keys) {
		if (!MapUtil.isEmpty(map) && !ArrayUtil.isEmpty(keys)) {
			for (final K key : keys) {
				map.remove(key);
			}
		}

		return map;
	}
}
