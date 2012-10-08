package com.googlecode.jutils.parameter.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class BooleanParameter.
 */
@Entity
@Table(name = "BOP_BOOLEAN_PARAMETER")
@SuppressWarnings("serial")
public class BooleanParameter extends AbstractParameter<Boolean> {
    public static final String TYPE = "BOOLEAN";

    @Column(name = "BOP_VALUE", nullable = false)
    private Boolean value;

    /**
     * Instantiates a new boolean parameter.
     */
    public BooleanParameter() {
        super();
        this.type = TYPE;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Boolean getValue() {
        return value;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
}
