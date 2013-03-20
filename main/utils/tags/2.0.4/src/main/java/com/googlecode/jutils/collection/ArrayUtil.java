package com.googlecode.jutils.collection;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

/**
 * The Class ArrayUtil.
 */
public final class ArrayUtil extends ArrayUtils {

    /**
     * Instantiates a new array util.
     */
    private ArrayUtil() {
        super();
    }

    /**
     * Checks if an array contains the searched value.
     * 
     * @param <T> the generic type
     * @param searchValue the search value
     * @param values the values
     * @return true, if successful
     */
    public static <T> boolean contains(T searchValue, T... values) {
        boolean contains = false;
        if (!ArrayUtils.isEmpty(values)) {
            contains = ArrayUtils.contains(values, searchValue);
        }
        return contains;
    }

    /**
     * As set.
     * 
     * @param <T> the generic type
     * @param values the values
     * @return the sets the
     */
    public static <T> Set<T> asSet(T... values) {
        Set<T> valueSet = null;
        if (!ArrayUtil.isEmpty(values)) {
            final List<T> tmpList = Arrays.asList(values);
            valueSet = new LinkedHashSet<T>(tmpList);
        }
        return valueSet;
    }
}
