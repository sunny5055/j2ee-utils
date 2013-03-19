package com.google.code.jee.utils.dal.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.HibernateProxyHelper;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.util.HibernateUtil;

/**
 * The Class AbstractHibernateDto.
 * 
 * @param <PK> the generic type
 */
@SuppressWarnings ("serial")
public abstract class AbstractHibernateDto<PK extends Serializable> implements Dto<PK> {

    /**
     * {@inheritedDoc}
     */
    @Override
    public String getStringPrimaryKey() {
        return StringUtil.toString(getPrimaryKey());
    }

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

    /**
     * {@inheritedDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getPrimaryKey()).toHashCode();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer(this.getClass().getName());
        buffer.append(" [id=");
        buffer.append(this.getStringPrimaryKey());
        buffer.append("]");
        return buffer.toString();
    }
}
