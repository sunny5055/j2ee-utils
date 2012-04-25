package com.google.code.jee.utils.mail.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class StringParameter.
 */
@Entity
@Table(name = "STR_STRING_PARAMETER")
@SuppressWarnings("serial")
public class StringParameter extends Parameter {
    @Column(name = "STR_VALUE", length = 500, nullable = false, unique = true)
    private String value;

    /**
     * Instantiates a new string parameter.
     */
    public StringParameter() {
        type = "String";
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
