#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.bean.datatable;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.googlecode.jutils.dal.dto.Dto;

/**
 * The Class AbstractDataTableBean.
 *
 * @param <PK> the generic type
 * @param <E> the element type
 */
@SuppressWarnings("serial")
public abstract class AbstractDataTableBean<PK extends Serializable, E extends Dto<PK>> implements Serializable {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataTableBean.class);
    protected E selectedObject;

    @Value("#{config.getProperty('list_default_rows')}")
    protected String defaultRows;

    @Value("#{config.getProperty('list_rows_per_page')}")
    protected String rowsPerPage;

    @Value("#{config.getProperty('liste_page_links')}")
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

    public E getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(E newSelectedObject) {
        this.selectedObject = newSelectedObject;
    }

    public String getDefaultRows() {
        return defaultRows;
    }

    public void setDefaultRows(String defaultRows) {
        this.defaultRows = defaultRows;
    }

    public String getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(String rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public String getPageLinks() {
        return pageLinks;
    }

    public void setPageLinks(String pageLinks) {
        this.pageLinks = pageLinks;
    }

    protected String getViewPage() {
        return "";
    }
}
