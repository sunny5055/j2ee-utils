package com.google.code.jee.utils.dal.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.HibernateProxyHelper;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.ArrayUtil;

/**
 * The Class HibernateUtil.
 */
public class HibernateUtil {
    /**
     * Instantiates a new hibernate util.
     */
    private HibernateUtil() {
        super();
    }

    /**
     * Gets the real class.
     * 
     * @return the real class
     */
    public static Class<?> getRealClass(Object obj) {
        Class<?> clazz = null;
        if (obj != null) {
            clazz = obj.getClass();
            if (obj instanceof HibernateProxy) {
                clazz = HibernateProxyHelper.getClassWithoutInitializingProxy(obj);
            }
        }
        return clazz;
    }

    /**
     * Copy entity.
     * 
     * @param <E> the element type
     * @param formEntity the form entity
     * @param hibernateEntity the hibernate entity
     * @param properties the properties
     * @return the e
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <E> E copyEntity(E formEntity, E hibernateEntity, String... properties)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (formEntity != null && hibernateEntity != null && !ArrayUtil.isEmpty(properties)) {
            for (final String property : properties) {
                final Object value = PropertyUtils.getProperty(formEntity, property);
                if (value instanceof String) {
                    if (StringUtil.isBlank((String) value)) {
                        PropertyUtils.setProperty(hibernateEntity, property, null);
                    } else {
                        PropertyUtils.setProperty(hibernateEntity, property, value);
                    }
                } else {
                    PropertyUtils.setProperty(hibernateEntity, property, value);
                }
            }
        }
        return hibernateEntity;
    }
}
