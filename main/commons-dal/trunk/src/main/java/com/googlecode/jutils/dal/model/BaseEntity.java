package com.googlecode.jutils.dal.model;

import java.io.Serializable;

/**
 * The Interface BaseEntity.
 * 
 * @param <PK>
 *            the generic type
 */
public interface BaseEntity<PK extends Serializable> extends Serializable {

	/**
	 * Getter : return the primaryKey
	 * 
	 * @return the primaryKey
	 */
	PK getPrimaryKey();

	/**
	 * Setter : affect the primaryKey
	 * 
	 * @param primaryKey
	 *            the primaryKey
	 */
	void setPrimaryKey(PK primaryKey);

	/**
	 * Gets the string primary key.
	 * 
	 * @return the string primary key
	 */
	String getStringPrimaryKey();
}
