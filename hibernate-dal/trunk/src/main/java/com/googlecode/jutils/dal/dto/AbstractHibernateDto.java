package com.googlecode.jutils.dal.dto;

import java.io.Serializable;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.HibernateProxyHelper;

import com.googlecode.jutils.dal.util.HibernateUtil;

/**
 * The Class AbstractHibernateDto.
 * 
 * @param <PK> the generic type
 */
@SuppressWarnings ("serial")
public abstract class AbstractHibernateDto<PK extends Serializable> extends AbstractDto<PK> {

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        Class<?> oClass = o.getClass();
        if (o instanceof HibernateProxy) {
            oClass = HibernateProxyHelper.getClassWithoutInitializingProxy(o);
        }

        if (HibernateUtil.getRealClass(this) != oClass) {
            return false;
        }

        final Dto<?> that = (Dto<?>) o;
        return this.getPrimaryKey() != null && this.getPrimaryKey().equals(that.getPrimaryKey());
    }
}
