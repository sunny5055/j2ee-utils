#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.bean.datatable;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


/**
 * The Class AbstractDataTableBean.
 */
@SuppressWarnings("serial")
public abstract class AbstractDataTableBean implements Serializable {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataTableBean.class);

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
	 * @param defaultRows
	 *            the new default rows
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
	 * @param rowsPerPage
	 *            the new rows per page
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
	 * @param pageLinks
	 *            the new page links
	 */
    public void setPageLinks(String pageLinks) {
        this.pageLinks = pageLinks;
    }
}
