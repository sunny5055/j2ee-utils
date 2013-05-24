package com.googlecode.jutils.dal.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.googlecode.jutils.StringUtil;

/**
 * The Class AbstractDto.
 * 
 * @param <PK>
 *            the generic type
 */
@SuppressWarnings("serial")
public abstract class AbstractDto<PK extends Serializable> implements Dto<PK> {

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

		final Class<?> oClass = o.getClass();

		if (getClass() != oClass) {
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
