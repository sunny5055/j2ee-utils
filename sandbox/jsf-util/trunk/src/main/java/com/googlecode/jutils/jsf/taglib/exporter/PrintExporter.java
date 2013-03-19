package com.googlecode.jutils.jsf.taglib.exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.component.datatable.DataTable;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.jsf.util.FacesUtils;

/**
 * The Class PrintExporter.
 */
public class PrintExporter extends AbstractExporter {
    private String headTitle;
    private boolean displayCount;
    private String entityFoundMsg;

    /**
     * Instantiates a new prints the exporter.
     */
    public PrintExporter() {
        super();
    }

    /**
     * Gets the head title.
     * 
     * @return the head title
     */
    public String getHeadTitle() {
        return headTitle;
    }

    /**
     * Sets the head title.
     * 
     * @param headTitle the new head title
     */
    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    /**
     * Checks if is display count.
     * 
     * @return true, if is display count
     */
    public boolean isDisplayCount() {
        return displayCount;
    }

    /**
     * Sets the display count.
     * 
     * @param displayCount the new display count
     */
    public void setDisplayCount(boolean displayCount) {
        this.displayCount = displayCount;
    }

    /**
     * Gets the entity found msg.
     * 
     * @return the entity found msg
     */
    public String getEntityFoundMsg() {
        return entityFoundMsg;
    }

    /**
     * Sets the entity found msg.
     * 
     * @param entityFoundMsg the new entity found msg
     */
    public void setEntityFoundMsg(String entityFoundMsg) {
        this.entityFoundMsg = entityFoundMsg;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void export(FacesContext facesContext, DataTable table) throws IOException {
        if (facesContext != null && table != null) {
            final HttpServletResponse response = FacesUtils.getResponse(facesContext);
            final OutputStream outputStream = response.getOutputStream();
            final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, encoding);
            final PrintWriter writer = new PrintWriter(outputStreamWriter);
            final List<UIColumn> columns = getColumnsToExport(table);
            if (!CollectionUtil.isEmpty(columns)) {
                final String rowIndexVar = table.getRowIndexVar();

                writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
                writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
                writer.println("<head>");
                writer.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=8\" />");

                if (!StringUtil.isBlank(headTitle)) {
                    writer.println("<title>" + headTitle + "</title>");
                }

                final HttpServletRequest request = FacesUtils.getRequest(facesContext);
                final String contextPath = request.getContextPath();
                final String cssPath = contextPath + "/resources/css";

                writer.println("<link type=\"text/css\" rel=\"stylesheet\" media=\"print\" href=\"" + cssPath
                        + "/popup.css\"/>");
                writer.println("<link type=\"text/css\" rel=\"stylesheet\" media=\"screen\" href=\"" + cssPath
                        + "/popup.css\"/>");
                writer.println("</head>");
                writer.println("<body>");

                if (displayCount) {
                    writer.println("<div>");
                    writer.println("<span>");
                    final String searchResult = FacesUtils.getLabel(facesContext, "list_results");
                    String countSummary = searchResult + " : " + table.getRowCount();
                    if (!StringUtil.isBlank(entityFoundMsg)) {
                        countSummary += " " + entityFoundMsg;
                    }

                    writer.println(countSummary);
                    writer.println("</span> ");
                    writer.println("<span class=\"clear\">&nbsp;</span>");
                    writer.println("</div>");
                }

                writer.println("<br /><table class='print_export_class'>");

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

                writer.println("<tbody>");
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

                    if (index % 2 == 0) {
                        writer.println("<tr>");
                    } else {
                        writer.println("<tr>");
                    }

                    addColumnValues(writer, columns);

                    writer.println("</tr>");
                    index++;
                }

                if (!pageOnly) {
                    table.setFirst(previousFirst);
                    table.loadLazyData();
                }
                writer.println("</tbody>");
                writer.println("</table>");
                writer.println("</body>");
                writer.println("</html>");

                table.setRowIndex(-1);
                if (rowIndexVar != null) {
                    facesContext.getExternalContext().getRequestMap().remove(rowIndexVar);
                }

                response.setContentType("text/html");
                response.setCharacterEncoding(encoding);

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
            for (final UIColumn uiColumn : columns) {
                addColumnValue(writer, uiColumn.getChildren());
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
            writer.println("<thead><tr>");
            for (final UIColumn uiColumn : columns) {
                final UIComponent component = uiColumn.getFacet(AbstractExporter.HEADER_FACET);
                if (component != null) {
                    writer.write("<th>" + exportValue(FacesContext.getCurrentInstance(), component) + "</th>");
                }
            }

            writer.println("</tr></thead>");
        }
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

            for (final UIComponent component : components) {
                if (component.isRendered()) {
                    final String value = exportValue(FacesContext.getCurrentInstance(), component);

                    builder.append(value);
                }
            }

            writer.write("<td>" + builder.toString() + "</td>");
        }
    }
}
