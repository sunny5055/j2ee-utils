package com.google.code.jee.utils.parameter.hibernate.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The Class DateParameter.
 */
@Entity
@Table(name = "DAP_DATE_PARAMETER")
@SuppressWarnings("serial")
public class DateParameter extends AbstractParameter<Date> {
    public static final String DATE = "DATE";

    @Column(name = "DAP_VALUE", nullable = false)
    private Date value;

    /**
     * Instantiates a new date parameter.
     */
    public DateParameter() {
        super();
        this.type = DATE;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Date getValue() {
        return value;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setValue(Date value) {
        this.value = value;
    }
}
