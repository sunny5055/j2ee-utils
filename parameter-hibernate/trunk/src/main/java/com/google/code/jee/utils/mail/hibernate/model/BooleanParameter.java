package com.google.code.jee.utils.mail.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class BooleanParameter.
 */
@Entity
@Table(name = "BOO_BOOLEAN_PARAMETER")
@SuppressWarnings("serial")
public class BooleanParameter extends Parameter {
    @Column(name = "BOO_VALUE", length = 500, nullable = false, unique = true)
    private String value;

    /**
     * Instantiates a new boolean parameter.
     */
    public BooleanParameter() {
        type = "Boolean";
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     * 
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
