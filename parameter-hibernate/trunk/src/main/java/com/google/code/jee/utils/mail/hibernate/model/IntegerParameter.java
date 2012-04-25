package com.google.code.jee.utils.mail.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class IntegerParameter.
 */
@Entity
@Table(name = "INT_INTEGER_PARAMETER")
@SuppressWarnings("serial")
public class IntegerParameter extends Parameter {
    @Column(name = "INT_VALUE", length = 500, nullable = false, unique = true)
    private String value;

    /**
     * Instantiates a new integer parameter.
     */
    public IntegerParameter() {
        type = "Integer";
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
