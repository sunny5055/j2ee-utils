package com.google.code.jee.utils;

import org.apache.commons.lang3.BooleanUtils;

/**
 * Class BooleanUtil.
 */
public final class BooleanUtil extends BooleanUtils {

    /**
     * Instantiates a new boolean util.
     */
    private BooleanUtil() {
        super();
    }

    /**
     * To boolean object.
     * 
     * @param value the value
     * @return the boolean
     */
    public static Boolean toBooleanObject(Object value) {
        Boolean booleanValue = null;
        if (value != null) {
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else if (value instanceof Integer) {
                booleanValue = toBooleanObject((Integer) value);
            } else if (value instanceof String) {
                booleanValue = toBooleanObject((String) value);
            }
        }
        return booleanValue;
    }
}
