package com.google.code.jee.utils.jsf.taglib.exporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.component.datatable.DataTable;

import com.google.code.jee.utils.collection.ArrayUtil;

/**
 * The Class AbstractExporter.
 */
public abstract class AbstractExporter {
    public static final String HEADER_FACET = "header";
    public static final String UTF_8 = "UTF-8";
    public static final String ISO_LATIN1 = "ISO-8859-1";

    protected boolean headerRow;
    protected String encoding;
    protected boolean pageOnly;
    protected int[] excludedColumnIndexes;

    /**
     * Instantiates a new abstract exporter.
     */
    public AbstractExporter() {
        super();
        this.encoding = ISO_LATIN1;
    }

    /**
     * Checks for header row.
     * 
     * @return true, if successful
     */
    public boolean hasHeaderRow() {
        return headerRow;
    }

    /**
     * Sets the header row.
     * 
     * @param headerRow the new header row
     */
    public void setHeaderRow(boolean headerRow) {
        this.headerRow = headerRow;
    }

    /**
     * Gets the encoding.
     * 
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the encoding.
     * 
     * @param encoding the new encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * Checks if is page only.
     * 
     * @return true, if is page only
     */
    public boolean isPageOnly() {
        return pageOnly;
    }

    /**
     * Sets the page only.
     * 
     * @param pageOnly the new page only
     */
    public void setPageOnly(boolean pageOnly) {
        this.pageOnly = pageOnly;
    }

    /**
     * Gets the excluded column indexes.
     * 
     * @return the excluded column indexes
     */
    public int[] getExcludedColumnIndexes() {
        return excludedColumnIndexes;
    }

    /**
     * Sets the excluded column indexes.
     * 
     * @param excludedColumnIndexes the new excluded column indexes
     */
    public void setExcludedColumnIndexes(int[] excludedColumnIndexes) {
        this.excludedColumnIndexes = excludedColumnIndexes;
    }

    /**
     * Export.
     * 
     * @param facesContext the faces context
     * @param table the table
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract void export(FacesContext facesContext, DataTable table) throws IOException;

    /**
     * Gets the columns to export.
     * 
     * @param table the table
     * @return the columns to export
     */
    protected List<UIColumn> getColumnsToExport(UIData table) {
        final List<UIColumn> columns = new ArrayList<UIColumn>();
        if (table != null) {
            int columnIndex = -1;

            for (final UIComponent child : table.getChildren()) {
                if (child instanceof UIColumn) {
                    final UIColumn column = (UIColumn) child;
                    columnIndex++;

                    if (ArrayUtil.isEmpty(excludedColumnIndexes) || column.isRendered()
                            && Arrays.binarySearch(excludedColumnIndexes, columnIndex) < 0) {
                        columns.add(column);
                    }
                }
            }
        }

        return columns;
    }

    /**
     * Checks for column footer.
     * 
     * @param columns the columns
     * @return true, if successful
     */
    protected boolean hasColumnFooter(List<UIColumn> columns) {
        for (final UIColumn column : columns) {
            if (column.getFooter() != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Export value.
     * 
     * @param context the context
     * @param component the component
     * @return the string
     */
    protected String exportValue(FacesContext context, UIComponent component) {

        if (component instanceof HtmlCommandLink) {
            final HtmlCommandLink link = (HtmlCommandLink) component;
            final Object value = link.getValue();

            if (value != null) {
                return String.valueOf(value);
            } else {
                for (final UIComponent child : link.getChildren()) {
                    if (child instanceof ValueHolder) {
                        return exportValue(context, child);
                    }
                }

                return null;
            }
        } else if (component instanceof ValueHolder) {
            if (component instanceof EditableValueHolder) {
                final Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
                if (submittedValue != null) {
                    return submittedValue.toString();
                }
            }

            final ValueHolder valueHolder = (ValueHolder) component;
            final Object value = valueHolder.getValue();
            if (value == null) {
                return "";
            }

            if (valueHolder.getConverter() != null) {
                return valueHolder.getConverter().getAsString(context, component, value);
            } else {
                final ValueExpression expr = component.getValueExpression("value");
                if (expr != null) {
                    final Class<?> valueType = expr.getType(context.getELContext());
                    if (valueType != null) {
                        final Converter converterForType = context.getApplication().createConverter(valueType);

                        if (converterForType != null) {
                            return converterForType.getAsString(context, component, value);
                        }
                    }
                }
            }
            return value.toString();
        } else {
            final String value = component.toString();

            if (value != null) {
                return value.trim();
            } else {
                return "";
            }
        }
    }
}
