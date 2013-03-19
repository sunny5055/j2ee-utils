package com.googlecode.jutils.collection;

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
     * @param map1 the map1
     * @param map2 the map2
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
}
