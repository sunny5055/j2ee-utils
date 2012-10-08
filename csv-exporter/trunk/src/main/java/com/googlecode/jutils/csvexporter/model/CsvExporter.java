package com.googlecode.jutils.csvexporter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;

/**
 * The Class CsvExporter.
 */
public class CsvExporter {
    public static final String UTF8_ENCODING = "UTF-8";
    public static final String SEMI_COLON_SEPARATOR = ";";
    public static final String COMA_SEPARATOR = ",";

    private String encoding;
    private String separator;
    private boolean encapsulateValue;
    private boolean trimValue;
    private Map<String, String> characterEscapes;
    private List<CsvCell> cells;
    private Map<String, Map<String, String>> replaceValues;

    private String defaultStringFormat;
    private String defaultIntegerFormat;
    private String defaultDoubleFormat;
    private String defaultDateFormat;
    private String defaultTrue;
    private String defaultFalse;

    /**
     * Instantiates a new csv exporter.
     */
    public CsvExporter() {
        super();
        this.encoding = UTF8_ENCODING;
        this.separator = SEMI_COLON_SEPARATOR;
        this.encapsulateValue = false;
        this.trimValue = true;
        this.characterEscapes = new HashMap<String, String>();

        this.cells = new ArrayList<CsvCell>();
        this.replaceValues = new HashMap<String, Map<String, String>>();
        this.defaultStringFormat = "%1$s";
        this.defaultIntegerFormat = "%1$d";
        this.defaultDoubleFormat = "%1$f";
        this.defaultDateFormat = "%1$td/%1$tm/%1$tY";
    }

    /**
     * Getter : retourne le encoding.
     * 
     * @return le encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Setter : affecte le encoding.
     * 
     * @param encoding le encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Getter : return the separator.
     * 
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Setter : affect the separator.
     * 
     * @param separator the separator
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * Getter : return the encapsulateValue.
     * 
     * @return the encapsulateValue
     */
    public boolean isEncapsulateValue() {
        return encapsulateValue;
    }

    /**
     * Setter : affect the encapsulateValue.
     * 
     * @param encapsulateValue the encapsulateValue
     */
    public void setEncapsulateValue(boolean encapsulateValue) {
        this.encapsulateValue = encapsulateValue;
    }

    /**
     * Getter : return the trimValue.
     * 
     * @return the trimValue
     */
    public boolean isTrimValue() {
        return trimValue;
    }

    /**
     * Setter : affect the trimValue.
     * 
     * @param trimValue the trimValue
     */
    public void setTrimValue(boolean trimValue) {
        this.trimValue = trimValue;
    }

    /**
     * Getter : return the characterEscapes.
     * 
     * @return the characterEscapes
     */
    public Map<String, String> getCharacterEscapes() {
        return characterEscapes;
    }

    /**
     * Setter : affect the characterEscapes.
     * 
     * @param characterEscapes the characterEscapes
     */
    public void setCharacterEscapes(Map<String, String> characterEscapes) {
        this.characterEscapes = characterEscapes;
    }

    /**
     * Checks for character escapes.
     * 
     * @return true, if successful
     */
    public boolean hasCharacterEscapes() {
        return !MapUtil.isEmpty(characterEscapes);
    }

    /**
     * Adds the character escape.
     * 
     * @param key the key
     * @param replaceValue the replace value
     */
    public void addCharacterEscape(String key, String replaceValue) {
        if (!StringUtil.isBlank(key)) {
            this.characterEscapes.put(key, replaceValue);
        }
    }

    /**
     * Getter : return the cells.
     * 
     * @return the cells
     */
    public List<CsvCell> getCells() {
        return cells;
    }

    /**
     * Checks for cells.
     * 
     * @return true, if successful
     */
    public boolean hasCells() {
        return !CollectionUtil.isEmpty(cells);
    }

    /**
     * Adds the cell.
     * 
     * @param cell the cell
     */
    public void addCell(CsvCell cell) {
        this.cells.add(cell);
    }

    /**
     * Adds the cell.
     * 
     * @param header the header
     * @param propertyName the property name
     */
    public void addCell(String header, String propertyName) {
        this.cells.add(new CsvCell(header, propertyName));
    }

    /**
     * Adds the cell.
     * 
     * @param header the header
     * @param propertyName the property name
     * @param format the format
     */
    public void addCell(String header, String propertyName, String format) {
        this.cells.add(new CsvCell(header, propertyName, format));
    }

    public void addCell(String header, String propertyName, String format, String conditionalDisplay) {
        this.cells.add(new CsvCell(header, propertyName, format, conditionalDisplay));
    }

    /**
     * Gets the headers.
     * 
     * @return the headers
     */
    public List<String> getHeaders() {
        List<String> headers = null;
        if (hasCells()) {
            headers = new ArrayList<String>();
            for (final CsvCell cell : this.cells) {
                String header = cell.getHeader();
                if (StringUtil.isBlank(header)) {
                    header = "";
                }
                headers.add(header);
            }
        }
        return headers;
    }

    /**
     * Checks for headers.
     * 
     * @return true, if successful
     */
    public boolean hasHeaders() {
        boolean headers = false;
        if (hasCells()) {
            for (final CsvCell cell : this.cells) {
                final String header = cell.getHeader();
                if (!StringUtil.isBlank(header)) {
                    headers = true;
                    break;
                }
            }
        }
        return headers;
    }

    /**
     * Getter : return the replaceValues.
     * 
     * @return the replaceValues
     */
    public Map<String, Map<String, String>> getReplaceValues() {
        return replaceValues;
    }

    /**
     * Setter : affect the replaceValues.
     * 
     * @param replaceValues the replaceValues
     */
    public void setReplaceValues(Map<String, Map<String, String>> replaceValues) {
        this.replaceValues = replaceValues;
    }

    /**
     * Adds the replace values.
     * 
     * @param key the key
     * @param values the values
     */
    public void addReplaceValues(String key, Map<String, String> values) {
        if (!StringUtil.isBlank(key) && !MapUtil.isEmpty(values)) {
            if (this.replaceValues.containsKey(key)) {
                final Map<String, String> actualValues = this.replaceValues.get(key);
                if (!MapUtil.isEmpty(actualValues)) {
                    values.putAll(actualValues);
                }
            }

            this.replaceValues.put(key, values);
        }
    }

    /**
     * Adds the replace value.
     * 
     * @param key the key
     * @param originalValue the original value
     * @param replaceValue the replace value
     */
    public void addReplaceValue(String key, String originalValue, String replaceValue) {
        if (!StringUtil.isBlank(key) && !StringUtil.isBlank(originalValue)) {
            final Map<String, String> values = new HashMap<String, String>();
            values.put(originalValue, replaceValue);
            addReplaceValues(key, values);
        }
    }

    /**
     * Checks for replace values.
     * 
     * @return true, if successful
     */
    public boolean hasReplaceValues() {
        return !MapUtil.isEmpty(replaceValues);
    }

    /**
     * Getter : return the defaultStringFormat.
     * 
     * @return the defaultStringFormat
     */
    public String getDefaultStringFormat() {
        return defaultStringFormat;
    }

    /**
     * Setter : affect the defaultStringFormat.
     * 
     * @param defaultStringFormat the defaultStringFormat
     */
    public void setDefaultStringFormat(String defaultStringFormat) {
        this.defaultStringFormat = defaultStringFormat;
    }

    /**
     * Getter : return the defaultIntegerFormat.
     * 
     * @return the defaultIntegerFormat
     */
    public String getDefaultIntegerFormat() {
        return defaultIntegerFormat;
    }

    /**
     * Setter : affect the defaultIntegerFormat.
     * 
     * @param defaultIntegerFormat the defaultIntegerFormat
     */
    public void setDefaultIntegerFormat(String defaultIntegerFormat) {
        this.defaultIntegerFormat = defaultIntegerFormat;
    }

    /**
     * Getter : return the defaultDoubleFormat.
     * 
     * @return the defaultDoubleFormat
     */
    public String getDefaultDoubleFormat() {
        return defaultDoubleFormat;
    }

    /**
     * Setter : affect the defaultDoubleFormat.
     * 
     * @param defaultDoubleFormat the defaultDoubleFormat
     */
    public void setDefaultDoubleFormat(String defaultDoubleFormat) {
        this.defaultDoubleFormat = defaultDoubleFormat;
    }

    /**
     * Getter : return the defaultDateFormat.
     * 
     * @return the defaultDateFormat
     */
    public String getDefaultDateFormat() {
        return defaultDateFormat;
    }

    /**
     * Setter : affect the defaultDateFormat.
     * 
     * @param defaultDateFormat the defaultDateFormat
     */
    public void setDefaultDateFormat(String defaultDateFormat) {
        this.defaultDateFormat = defaultDateFormat;
    }

    /**
     * Getter : return the defaultTrue.
     * 
     * @return the defaultTrue
     */
    public String getDefaultTrue() {
        return defaultTrue;
    }

    /**
     * Setter : affect the defaultTrue.
     * 
     * @param defaultTrue the defaultTrue
     */
    public void setDefaultTrue(String defaultTrue) {
        this.defaultTrue = defaultTrue;
    }

    /**
     * Getter : return the defaultFalse.
     * 
     * @return the defaultFalse
     */
    public String getDefaultFalse() {
        return defaultFalse;
    }

    /**
     * Setter : affect the defaultFalse.
     * 
     * @param defaultFalse the defaultFalse
     */
    public void setDefaultFalse(String defaultFalse) {
        this.defaultFalse = defaultFalse;
    }
}
