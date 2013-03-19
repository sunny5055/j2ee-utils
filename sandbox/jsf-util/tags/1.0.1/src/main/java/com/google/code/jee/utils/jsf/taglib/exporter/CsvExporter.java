package com.google.code.jee.utils.jsf.taglib.exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.component.datatable.DataTable;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.CollectionUtil;

/**
 * The Class CsvExporter.
 */
public class CsvExporter extends AbstractExporter {
    public static final String SEMI_COLON_SEPARATOR = ";";
    public static final String COMA_SEPARATOR = ",";

    private String fileName;
    private String separator;
    private String lineBreakReplacement;

    /**
     * Instantiates a new csv exporter.
     * 
     * @param fileName the file name
     */
    public CsvExporter(String fileName) {
        super();
        this.fileName = fileName;
        this.separator = SEMI_COLON_SEPARATOR;
    }

    /**
     * Gets the file name.
     * 
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name.
     * 
     * @param fileName the new file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the separator.
     * 
     * @return the separator
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Sets the separator.
     * 
     * @param separator the new separator
     */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    /**
     * Gets the line break replacement.
     * 
     * @return the line break replacement
     */
    public String getLineBreakReplacement() {
        return lineBreakReplacement;
    }

    /**
     * Sets the line break replacement.
     * 
     * @param lineBreakReplacement the new line break replacement
     */
    public void setLineBreakReplacement(String lineBreakReplacement) {
        this.lineBreakReplacement = lineBreakReplacement;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void export(FacesContext facesContext, DataTable table) throws IOException {
        if (facesContext != null && table != null) {
            final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
            final OutputStream outputStream = response.getOutputStream();
            final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, encoding);
            final PrintWriter writer = new PrintWriter(outputStreamWriter);
            final List<UIColumn> columns = getColumnsToExport(table);
            if (!CollectionUtil.isEmpty(columns)) {
                final String rowIndexVar = table.getRowIndexVar();

                if (hasHeaderRow()) {
                    addHeaderColumns(writer, columns);
                }

                final Integer previousFirst = table.getFirst();
                Integer first = null;
                Integer rows = null;
                Integer size = null;

                if (pageOnly) {
                    first = table.getFirst();
                    rows = table.getRows();
                    size = first + rows;
                } else {
                    first = 0;
                    rows = table.getRows();
                    size = table.getRowCount();
                }

                int index = 0;
                for (int i = first; i < size; i++) {
                    if (!pageOnly && i % rows == 0) {
                        table.setFirst(i);
                        table.loadLazyData();
                    }

                    table.setRowIndex(i);
                    if (!table.isRowAvailable()) {
                        break;
                    }
                    if (rowIndexVar != null) {
                        facesContext.getExternalContext().getRequestMap().put(rowIndexVar, i);
                    }

                    addColumnValues(writer, columns);
                    writer.write("\n");
                    index++;
                }

                if (!pageOnly) {
                    table.setFirst(previousFirst);
                    table.loadLazyData();
                }

                table.setRowIndex(-1);
                if (rowIndexVar != null) {
                    facesContext.getExternalContext().getRequestMap().remove(rowIndexVar);
                }

                response.setContentType("text/csv");
                response.setHeader("Expires", "0");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".csv");

                writer.flush();
                writer.close();

                response.getOutputStream().flush();
            }
        }
    }

    /**
     * Adds the column values.
     * 
     * @param writer the writer
     * @param columns the columns
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void addColumnValues(PrintWriter writer, List<UIColumn> columns) throws IOException {
        if (writer != null && !CollectionUtil.isEmpty(columns)) {
            for (final Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
                addColumnValue(writer, iterator.next().getChildren());

                if (iterator.hasNext()) {
                    writer.write(separator);
                }
            }
        }
    }

    /**
     * Adds the header columns.
     * 
     * @param writer the writer
     * @param columns the columns
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void addHeaderColumns(PrintWriter writer, List<UIColumn> columns) throws IOException {
        if (writer != null && !CollectionUtil.isEmpty(columns)) {
            for (final Iterator<UIColumn> iterator = columns.iterator(); iterator.hasNext();) {
                addColumnValue(writer, iterator.next().getFacet(AbstractExporter.HEADER_FACET));

                if (iterator.hasNext()) {
                    writer.write(separator);
                }
            }

            writer.write("\n");
        }
    }

    /**
     * Adds the column value.
     * 
     * @param writer the writer
     * @param component the component
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void addColumnValue(PrintWriter writer, UIComponent component) throws IOException {
        if (writer != null) {
            String value = "";
            if (component != null) {
                value = exportValue(FacesContext.getCurrentInstance(), component);
                value = cleanValue(value);
            }

            writer.write("\"" + value + "\"");
        }
    }

    /**
     * Clean value.
     * 
     * @param value the value
     * @return the string
     */
    private String cleanValue(String value) {
        if (!StringUtil.isBlank(value)) {
            if (!StringUtil.isEmpty(lineBreakReplacement)) {
                if (StringUtil.equals(lineBreakReplacement, "\\s")) {
                    value = StringUtil.replace(value, "<br>", " ");
                    value = StringUtil.replace(value, "<br/>", " ");
                    value = StringUtil.replace(value, "<br />", " ");
                } else {
                    value = StringUtil.replace(value, "<br>", lineBreakReplacement);
                    value = StringUtil.replace(value, "<br/>", lineBreakReplacement);
                    value = StringUtil.replace(value, "<br />", lineBreakReplacement);
                }
            }

            value = value.replaceAll("\\s+", " ");
            value = StringUtil.trim(value);
        }
        return value;
    }

    /**
     * Adds the column value.
     * 
     * @param writer the writer
     * @param components the components
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void addColumnValue(PrintWriter writer, List<UIComponent> components) throws IOException {
        if (writer != null && !CollectionUtil.isEmpty(components)) {
            final StringBuilder builder = new StringBuilder();

            String value = "";
            for (final UIComponent component : components) {
                if (component.isRendered()) {
                    value = exportValue(FacesContext.getCurrentInstance(), component);
                    value = cleanValue(value);
                    builder.append(value);
                }
            }

            writer.write("\"" + builder.toString() + "\"");
        }
    }
}
