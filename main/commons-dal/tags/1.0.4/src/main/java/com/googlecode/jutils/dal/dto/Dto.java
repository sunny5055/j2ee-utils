package com.googlecode.jutils.dal.dto;

import java.io.Serializable;

/**
 * The Interface Dto.
 * 
 * @param <PK>
 *            the generic type
 */
public interface Dto<PK extends Serializable> extends Serializable {

	/**
	 * Gets the primary key.
	 * 
	 * @return the primary key
	 */
	PK getPrimaryKey();

	/**
	 * Sets the primary key.
	 * 
	 * @param primaryKey
	 *            the new primary key
	 */
	void setPrimaryKey(PK primaryKey);

	/**
	 * Gets the string primary key.
	 * 
	 * @return the string primary key
	 */
	String getStringPrimaryKey();
}
