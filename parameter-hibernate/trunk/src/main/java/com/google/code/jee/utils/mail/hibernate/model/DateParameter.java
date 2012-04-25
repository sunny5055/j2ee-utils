package com.google.code.jee.utils.mail.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class DateParameter.
 */
@Entity
@Table(name = "DAT_DATE_PARAMETER")
@SuppressWarnings("serial")
public class DateParameter extends Parameter {
    @Column(name = "DAT_VALUE", length = 500, nullable = false, unique = true)
    private String value;

    /**
     * Instantiates a new date parameter.
     */
    public DateParameter() {
        type = "Date";
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
