package com.googlecode.jutils.parameter.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class IntegerParameter.
 */
@Entity
@Table(name = "INP_INTEGER_PARAMETER")
@SuppressWarnings("serial")
public class IntegerParameter extends AbstractParameter<Integer> {
    public static final String TYPE = "INTEGER";

    @Column(name = "INP_VALUE", nullable = false)
    private Integer value;

    /**
     * Instantiates a new integer parameter.
     */
    public IntegerParameter() {
        super();
        this.type = TYPE;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setValue(Integer value) {
        this.value = value;
    }
}
