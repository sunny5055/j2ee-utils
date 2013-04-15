package com.googlecode.jutils.dal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * The Class AbstractSearchCriteria.
 */
public class SearchCriteria {
    protected Integer firstResult;
    protected Integer maxResults;
    protected Map<String, Object> filters;
    protected Map<String, SortOrder> sorts;

    /**
     * Instantiates a new abstract search criteria.
     */
    public SearchCriteria() {
        super();
        this.filters = new HashMap<String, Object>();
        this.sorts = new HashMap<String, SortOrder>();
    }

    /**
     * Getter : return the firstResult
     * 
     * @return the firstResult
     */
    public Integer getFirstResult() {
        return firstResult;
    }

    /**
     * Setter : affect the firstResult
     * 
     * @param firstResult the firstResult
     */
    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
    }

    /**
     * Getter : return the maxResults
     * 
     * @return the maxResults
     */
    public Integer getMaxResults() {
        return maxResults;
    }

    /**
     * Setter : affect the maxResults
     * 
     * @param maxResults the maxResults
     */
    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    /**
     * Getter : return the filters
     * 
     * @return the filters
     */
    public Map<String, Object> getFilters() {
        return filters;
    }

    /**
     * Setter : affect the filters
     * 
     * @param filters the filters
     */
    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    /**
     * Adds the filter.
     * 
     * @param property the property
     * @param value the value
     */
    public void addFilter(String property, Object value) {
        this.filters.put(property, value);
    }

    /**
     * Checks for filter.
     * 
     * @param property the property
     * @return true, if successful
     */
    public boolean hasFilter(String property) {
        return hasFilters() && this.filters.containsKey(property);
    }

    /**
     * Gets the filter.
     * 
     * @param property the property
     * @return the filter
     */
    public Object getFilter(String property) {
        Object value = null;
        if (hasFilter(property)) {
            value = this.filters.get(property);
        }
        return value;
    }

    /**
     * Getter : return the sorts
     * 
     * @return the sorts
     */
    public Map<String, SortOrder> getSorts() {
        return sorts;
    }

    /**
     * Setter : affect the sorts
     * 
     * @param sorts the sorts
     */
    public void setSorts(Map<String, SortOrder> sorts) {
        this.sorts = sorts;
    }

    /**
     * Adds the sort.
     * 
     * @param property the property
     * @param sortOrder the sort order
     */
    public void addSort(String property, SortOrder sortOrder) {
        this.sorts.put(property, sortOrder);
    }

    /**
     * Checks for sort.
     * 
     * @param property the property
     * @return true, if successful
     */
    public boolean hasSort(String property) {
        return hasSorts() && this.sorts.containsKey(property);
    }

    /**
     * Checks if is ascending sort.
     * 
     * @param property the property
     * @return the boolean
     */
    public boolean isAscendingSort(String property) {
        boolean ascending = false;
        if (hasSort(property)) {
            ascending = this.sorts.get(property) == SortOrder.ASCENDING;
        }
        return ascending;
    }

    public boolean hasFilters() {
        return !MapUtils.isEmpty(filters);
    }

    /**
     * Checks for sorts.
     * 
     * @return true, if successful
     */
    public boolean hasSorts() {
        return !MapUtils.isEmpty(sorts);
    }

    /**
     * Checks for pagination.
     * 
     * @return true, if successful
     */
    public boolean hasPagination() {
        return firstResult != null && maxResults != null;
    }
}
