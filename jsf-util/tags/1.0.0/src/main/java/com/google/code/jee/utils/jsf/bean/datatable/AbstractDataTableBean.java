package com.google.code.jee.utils.jsf.bean.datatable;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.springframework.beans.factory.annotation.Value;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.jsf.util.FacesUtils;

/**
 * The Class AbstractDataTableBean.
 * 
 * @param <PK> the generic type
 * @param <E> the element type
 * @param <S> the generic type
 */
@SuppressWarnings("serial")
public abstract class AbstractDataTableBean implements Serializable {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDataTableBean.class);

    @Value("#{config.properties['list_default_rows']}")
    protected String defaultRows;

    @Value("#{config.properties['list_rows_per_page']}")
    protected String rowsPerPage;

    @Value("#{config.properties['list_page_links']}")
    protected String pageLinks;

    /**
     * Instantiates a new abstract data table bean.
     */
    public AbstractDataTableBean() {
        super();
    }

    /**
     * Inits the.
     */
    @PostConstruct
    protected void init() {
    }

    /**
     * Gets the default rows.
     * 
     * @return the default rows
     */
    public String getDefaultRows() {
        return defaultRows;
    }

    /**
     * Sets the default rows.
     * 
     * @param defaultRows the new default rows
     */
    public void setDefaultRows(String defaultRows) {
        this.defaultRows = defaultRows;
    }

    /**
     * Gets the rows per page.
     * 
     * @return the rows per page
     */
    public String getRowsPerPage() {
        return rowsPerPage;
    }

    /**
     * Sets the rows per page.
     * 
     * @param rowsPerPage the new rows per page
     */
    public void setRowsPerPage(String rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * Gets the page links.
     * 
     * @return the page links
     */
    public String getPageLinks() {
        return pageLinks;
    }

    /**
     * Sets the page links.
     * 
     * @param pageLinks the new page links
     */
    public void setPageLinks(String pageLinks) {
        this.pageLinks = pageLinks;
    }

    /**
     * Gets the view page.
     * 
     * @return the view page
     */
    protected abstract String getViewPage();

    /**
     * Save data table state.
     * 
     * @param componentId the component id
     */
    protected void saveDataTableState(String componentId) {
        if (!StringUtil.isBlank(componentId)) {
            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final DataTable dataTable = (DataTable) FacesUtils.findComponentById(facesContext, componentId);
            if (dataTable != null) {
                final String clientId = dataTable.getClientId(facesContext);

                FacesUtils.setSessionAttribute(facesContext, clientId + "_rowCount", dataTable.getRowCount());
                FacesUtils.setSessionAttribute(facesContext, clientId + "_first", dataTable.getFirst());
                FacesUtils.setSessionAttribute(facesContext, clientId + "_rows", dataTable.getRows());

                final String selection = FacesUtils.getRequestParameter(facesContext, clientId + "_selection");
                FacesUtils.setSessionAttribute(facesContext, clientId + "_selectedRowIds", selection);

                FacesUtils.setSessionAttribute(facesContext, clientId + "_filters", dataTable.getFilters());
            }
        }
    }
}
