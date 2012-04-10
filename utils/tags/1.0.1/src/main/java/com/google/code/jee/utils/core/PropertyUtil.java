package com.google.code.jee.utils.core;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import com.google.code.jee.utils.StringUtil;

/**
 * The Class PropertyUtil.
 */
public final class PropertyUtil extends PropertyUtils {

    /**
     * Instantiates a new property util.
     */
    private PropertyUtil() {
    }

    /**
     * Gets the string value.
     * 
     * @param entity the entity
     * @param fieldName the field name
     * @return the string value
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     * @throws NoSuchMethodException the no such method exception
     */
    public static String getStringValue(Object entity, String fieldName) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        String fieldValue = "";
        if (entity != null && !StringUtil.isBlank(fieldName)) {
            Object value = null;
            value = PropertyUtils.getProperty(entity, fieldName);
            if (value != null) {
                fieldValue = value.toString();
            }
        }
        return fieldValue;
    }

}
