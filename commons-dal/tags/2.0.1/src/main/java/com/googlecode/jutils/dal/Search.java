package com.googlecode.jutils.dal;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.googlecode.jutils.BooleanUtil;
import com.googlecode.jutils.DateUtil;
import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.convertor.ConvertorUtil;

/**
 * The Class Search.
 */
public class Search {
    private String countQuery;
    private String query;
    private Map<String, Object> parameters;

    /**
     * Instantiates a new search.
     */
    public Search() {
        super();
        this.parameters = new HashMap<String, Object>();
    }

    /**
     * Getter : return the countQuery.
     * 
     * @return the countQuery
     */
    public String getCountQuery() {
        return countQuery;
    }

    /**
     * Setter : affect the countQuery.
     * 
     * @param countQuery the countQuery
     */
    public void setCountQuery(String countQuery) {
        this.countQuery = countQuery;
    }

    /**
     * Getter : return the query.
     * 
     * @return the query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Setter : affect the query.
     * 
     * @param query the query
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Getter : return the parameters.
     * 
     * @return the parameters
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * Setter : affect the parameters.
     * 
     * @param parameters the parameters
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * Adds the parameter.
     * 
     * @param name the name
     * @param value the value
     */
    public void addParameter(String name, Object value) {
        this.parameters.put(name, value);
    }

    /**
     * Adds the boolean parameter.
     * 
     * @param name the name
     * @param value the value
     */
    public void addBooleanParameter(String name, Object value) {
        final Boolean booleanValue = BooleanUtil.toBooleanObject(value);

        this.parameters.put(name, booleanValue);
    }

    /**
     * Adds the integer parameter.
     * 
     * @param name the name
     * @param value the value
     */
    public void addIntegerParameter(String name, Object value) {
        final Integer integerValue = ConvertorUtil.toInteger(value.toString());

        this.parameters.put(name, integerValue);
    }

    /**
     * Adds the date parameter.
     * 
     * @param name the name
     * @param value the value
     */
    public void addDateParameter(String name, Object value, String pattern) {
        Date dateValue = null;
        try {
            dateValue = DateUtil.parseDate(value.toString(), new String[] { pattern });
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        this.parameters.put(name, dateValue);
    }

    /**
     * Adds the double parameter.
     * 
     * @param name the name
     * @param value the value
     */
    public void addDoubleParameter(String name, Object value) {
        final Double doubleValue = ConvertorUtil.toDouble(value.toString());

        this.parameters.put(name, doubleValue);
    }

    /**
     * Adds the long parameter.
     * 
     * @param name the name
     * @param value the value
     */
    public void addLongParameter(String name, Object value) {
        final Long longValue = ConvertorUtil.toLong(value.toString());

        this.parameters.put(name, longValue);
    }

    /**
     * Adds the string parameter.
     * 
     * @param name the name
     * @param value the value
     */
    public void addStringParameter(String name, Object value) {
        String stringValue = value.toString();
        stringValue = StringUtil.replace(stringValue, "*", "%");
        this.parameters.put(name, stringValue);
    }

    /**
     * Gets the parameter names.
     * 
     * @return the parameter names
     */
    public String[] getParameterNames() {
        String[] parameterNames = null;
        if (!MapUtils.isEmpty(parameters)) {
            parameterNames = parameters.keySet().toArray(new String[0]);
        }
        return parameterNames;
    }

    /**
     * Gets the values.
     * 
     * @return the values
     */
    public Object[] getValues() {
        Object[] values = null;
        if (!MapUtils.isEmpty(parameters)) {
            values = parameters.values().toArray(new Object[0]);
        }
        return values;
    }
}
