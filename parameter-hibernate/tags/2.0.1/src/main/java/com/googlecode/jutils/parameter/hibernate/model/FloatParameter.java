package com.googlecode.jutils.parameter.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class FloatParameter.
 */
@Entity
@Table(name = "FLP_FLOAT_PARAMETER")
@SuppressWarnings("serial")
public class FloatParameter extends AbstractParameter<Float> {
    public static final String TYPE = "FLOAT";
    @Column(name = "FLP_VALUE", nullable = false)
    private Float value;

    /**
     * Instantiates a new float parameter.
     */
    public FloatParameter() {
        super();
        type = TYPE;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Float getValue() {
        return value;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setValue(Float value) {
        this.value = value;
    }
}
