package com.google.code.jee.utils.jsf.taglib.exporter;

import java.io.IOException;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.jsf.util.FacesUtils;

/**
 * The Class AbstractExporterTag.
 */
public abstract class AbstractExporterTag implements ActionListener, StateHolder {
    protected static final Logger LOGGER = Logger.getLogger(AbstractExporterTag.class);
    protected ValueExpression target;
    protected ValueExpression headerRow;
    protected ValueExpression encoding;
    protected ValueExpression pageOnly;
    protected ValueExpression excludeColumns;

    /**
     * Instantiates a new abstract exporter tag.
     * 
     * @param target the target
     * @param headerRow the header row
     * @param encoding the encoding
     * @param pageOnly the page only
     * @param excludeColumns the exclude columns
     */
    public AbstractExporterTag(ValueExpression target, ValueExpression headerRow, ValueExpression encoding,
            ValueExpression pageOnly, ValueExpression excludeColumns) {
        super();
        this.target = target;
        this.headerRow = headerRow;
        this.encoding = encoding;
        this.pageOnly = pageOnly;
        this.excludeColumns = excludeColumns;
    }

    /**
     * Gets the target.
     * 
     * @return the target
     */
    public ValueExpression getTarget() {
        return target;
    }

    /**
     * Sets the target.
     * 
     * @param target the new target
     */
    public void setTarget(ValueExpression target) {
        this.target = target;
    }

    /**
     * Gets the header row.
     * 
     * @return the header row
     */
    public ValueExpression getHeaderRow() {
        return headerRow;
    }

    /**
     * Sets the header row.
     * 
     * @param headerRow the new header row
     */
    public void setHeaderRow(ValueExpression headerRow) {
        this.headerRow = headerRow;
    }

    /**
     * Gets the encoding.
     * 
     * @return the encoding
     */
    public ValueExpression getEncoding() {
        return encoding;
    }

    /**
     * Sets the encoding.
     * 
     * @param encoding the new encoding
     */
    public void setEncoding(ValueExpression encoding) {
        this.encoding = encoding;
    }

    /**
     * Gets the page only.
     * 
     * @return the page only
     */
    public ValueExpression getPageOnly() {
        return pageOnly;
    }

    /**
     * Sets the page only.
     * 
     * @param pageOnly the new page only
     */
    public void setPageOnly(ValueExpression pageOnly) {
        this.pageOnly = pageOnly;
    }

    /**
     * Gets the exclude columns.
     * 
     * @return the exclude columns
     */
    public ValueExpression getExcludeColumns() {
        return excludeColumns;
    }

    /**
     * Sets the exclude columns.
     * 
     * @param excludeColumns the new exclude columns
     */
    public void setExcludeColumns(ValueExpression excludeColumns) {
        this.excludeColumns = excludeColumns;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void processAction(ActionEvent event) {
        if (event != null && target != null) {
            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final ELContext elContext = facesContext.getELContext();
            final String tableId = (String) target.getValue(elContext);
            if (!StringUtil.isBlank(tableId)) {
                final UIComponent target = FacesUtils.findComponentById(facesContext, tableId);
                if (target == null) {
                    throw new FacesException("Cannot find component \"" + tableId + "\" in view.");
                }
                if (!(target instanceof DataTable)) {
                    throw new FacesException("Unsupported datasource target:\"" + target.getClass().getName()
                            + "\", exporter must target a PrimeFaces DataTable.");
                }

                final AbstractExporter exporter = getExporter();
                if (exporter != null) {
                    try {
                        exporter.export(facesContext, (DataTable) target);
                    } catch (final IOException e) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(e.getMessage(), e);
                        }
                    }

                    facesContext.responseComplete();
                }
            }
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean isTransient() {
        return false;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setTransient(boolean value) {
    }

    /**
     * Gets the exporter.
     * 
     * @return the exporter
     */
    protected abstract AbstractExporter getExporter();

    /**
     * Resolve excluded column indexes.
     * 
     * @param columnsToExclude the columns to exclude
     * @return the int[]
     */
    protected int[] resolveExcludedColumnIndexes(String columnsToExclude) {
        int[] excludedColumnIndexes = null;
        if (!StringUtil.isBlank(columnsToExclude)) {
            final String[] columnIndexesAsString = StringUtil.split(columnsToExclude, ",");
            if (!ArrayUtil.isEmpty(columnIndexesAsString)) {
                excludedColumnIndexes = new int[columnIndexesAsString.length];

                for (int i = 0; i < excludedColumnIndexes.length; i++) {
                    excludedColumnIndexes[i] = Integer.parseInt(columnIndexesAsString[i].trim());
                }
            }
        }
        return excludedColumnIndexes;
    }

}
