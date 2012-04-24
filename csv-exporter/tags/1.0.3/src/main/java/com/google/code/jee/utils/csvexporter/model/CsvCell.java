package com.google.code.jee.utils.csvexporter.model;

/**
 * The Class CsvCellSimple.
 */
public class CsvCell {
    public static final String LIST_SEPARATOR = ", ";
    private String propertyName;
    private String header;
    private String format;
    private String conditionalDisplay;
    private String listSeparator;

    /**
     * The Constructor.
     */
    public CsvCell() {
        super();
        this.listSeparator = LIST_SEPARATOR;
    }

    /**
     * The Constructor.
     * 
     * @param header the header
     */
    public CsvCell(String header) {
        this();
        this.header = header;
    }

    /**
     * The Constructor.
     * 
     * @param header the header
     * @param propertyName the property name
     */
    public CsvCell(String header, String propertyName) {
        this(header);
        this.propertyName = propertyName;
    }

    /**
     * The Constructor.
     * 
     * @param header the header
     * @param propertyName the property name
     * @param format the format
     */
    public CsvCell(String header, String propertyName, String format) {
        this(header, propertyName);
        this.format = format;
    }

    /**
     * The Constructor.
     * 
     * @param header the header
     * @param propertyName the property name
     * @param format the format
     * @param conditionalDisplay the conditional display
     */
    public CsvCell(String header, String propertyName, String format, String conditionalDisplay) {
        this(header, propertyName, format);
        this.conditionalDisplay = conditionalDisplay;
    }

    /**
     * Getter : return the propertyName.
     * 
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Setter : affect the propertyName.
     * 
     * @param propertyName the propertyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Getter : return the header.
     * 
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * Setter : affect the header.
     * 
     * @param header the header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Getter : return the format.
     * 
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Setter : affect the format.
     * 
     * @param format the format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Gets the conditional display.
     * 
     * @return the conditional display
     */
    public String getConditionalDisplay() {
        return conditionalDisplay;
    }

    /**
     * Sets the conditional display.
     * 
     * @param conditionalDisplay the conditional display
     */
    public void setConditionalDisplay(String conditionalDisplay) {
        this.conditionalDisplay = conditionalDisplay;
    }

    /**
     * Getter : return the listSeparator.
     * 
     * @return the listSeparator
     */
    public String getListSeparator() {
        return listSeparator;
    }

    /**
     * Setter : affect the listSeparator.
     * 
     * @param listSeparator the listSeparator
     */
    public void setListSeparator(String listSeparator) {
        this.listSeparator = listSeparator;
    }

}
