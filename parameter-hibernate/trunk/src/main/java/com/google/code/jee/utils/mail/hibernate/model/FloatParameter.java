package com.google.code.jee.utils.mail.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class FloatParameter.
 */
@Entity
@Table(name = "FLO_FLOAT_PARAMETER")
@SuppressWarnings("serial")
public class FloatParameter extends Parameter {
    @Column(name = "FLO_VALUE", length = 500, nullable = false, unique = true)
    private String value;

    /**
     * Instantiates a new float parameter.
     */
    public FloatParameter() {
        type = "Float";
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
