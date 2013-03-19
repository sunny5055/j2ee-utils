package com.google.code.jee.utils.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

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
     * @param <T> the generic type
     * @param values the values
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
     * @param <T> the generic type
     * @param values the values
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
}
