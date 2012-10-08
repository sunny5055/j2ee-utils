package com.googlecode.jutils.jsf.bean.filters;

import java.io.Serializable;
import java.util.Map;

import javax.faces.event.ActionEvent;

/**
 * The Class AbstractFiltersBean.
 */
@SuppressWarnings("serial")
public abstract class AbstractFiltersBean implements Serializable {

    /**
     * Checks for filters.
     * 
     * @return true, if successful
     */
    public abstract boolean hasFilters();

    /**
     * Gets the filters.
     * 
     * @return the filters
     */
    public abstract Map<String, String> getFilters();

    /**
     * Clear filters.
     * 
     * @param e the e
     */
    public abstract void clearFilters(ActionEvent e);

    /**
     * Re init.
     */
    protected abstract void reInit();
}
