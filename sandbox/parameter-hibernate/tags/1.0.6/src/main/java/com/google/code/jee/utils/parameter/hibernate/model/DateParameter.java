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
    
    @Column(name = "DAP_DATE_FORMAT", nullable = false)
    private String dateFormat;

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

	/**
	 * Getter : return the date format
	 * 
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * Setter : affect the date format
	 * 
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
