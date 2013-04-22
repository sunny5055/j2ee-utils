package com.googlecode.jutils.parameter.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class StringParameter.
 */
@Entity
@Table(name = "STP_STRING_PARAMETER")
@SuppressWarnings("serial")
public class StringParameter extends AbstractParameter<String> {
    public static final String TYPE = "STRING";

    @Column(name = "STP_VALUE", length = 255, nullable = false)
    private String value;

    /**
     * Instantiates a new string parameter.
     */
    public StringParameter() {
        super();
        this.type = TYPE;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }

}
