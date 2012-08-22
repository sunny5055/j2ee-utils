package com.google.code.jee.utils.dal.dto;

import java.io.Serializable;

/**
 * Interface Dto
 */
public interface Dto<PK extends Serializable> extends Serializable {

    /**
     * Getter : return the primaryKey
     * 
     * @return the primaryKey
     */
    PK getPrimaryKey();

    /**
     * Setter : affect the primaryKey
     * 
     * @param primaryKey the primaryKey
     */
    void setPrimaryKey(PK primaryKey);

    /**
     * Gets the string primary key.
     * 
     * @return the string primary key
     */
    String getStringPrimaryKey();
}
