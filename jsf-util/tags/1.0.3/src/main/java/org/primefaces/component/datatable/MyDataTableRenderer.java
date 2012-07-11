package org.primefaces.component.datatable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.component.column.Column;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.primefaces.util.HTML;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.jsf.util.FacesUtils;

/**
 * The Class MyDataTableRenderer.
 */
public class MyDataTableRenderer extends DataTableRenderer {
    private static final String RESET = "RESET";
    private static final String CACHE = "CACHE";

    /**
     * Instantiates a new my data table renderer.
     */
    public MyDataTableRenderer() {
        super();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void decode(FacesContext context, UIComponent component) {
        final DataTable table = (DataTable) component;
        final boolean isSortRequest = table.isSortRequest(context);

        if (table.isDraggableColumns()) {
            table.syncColumnOrder();
        }

        if (table.isResizableColumns() && table.isColResizeRequest(context)) {
            table.syncColumnWidths();
        }

        if (table.isFilteringEnabled()) {
            dataHelper.decodeFilters(context, table);

            if (!isSortRequest && table.getValueExpression("sortBy") != null && !table.isLazyLoading()) {
                sort(context, table);
            }
        }

        // JSC -- Bug fix for "read-only" dataTable
        if (!StringUtil.isBlank(table.getSelectionMode()) || !StringUtil.isBlank(table.getColumnSelectionMode())) {
            dataHelper.decodeSelection(context, table);
        }
        // JSC -- Bug fix for "read-only" dataTable

        if (isSortRequest) {
            dataHelper.decodeSortRequest(context, table);
        }

        decodeBehaviors(context, component);

        // JSC -- Add support for dataTable saving
        if (isReloadFiltersRequest(context, component)) {
            updateDataTableState(context, component);
        } else {
            saveDataTableState(context, component);
        }
        // JSC -- Add support for dataTable saving
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        final DataTable table = (DataTable) component;
        if (!isUnloadRequest(context, component)) {
            if (isReloadContentRequest(context, component)) {
                if (table.isLazyLoading()) {
                    table.loadLazyData();
                }

                super.encodeTbody(context, table, true);
            } else if (isReloadFiltersRequest(context, component)) {
                encodeReloadFiltersRequest(context, table);
            } else {
                if (!isDataManipulationRequest(context, table) && !table.isRowExpansionRequest(context)
                        && !table.isRowEditRequest(context) && !table.isScrollingRequest(context)) {
                    updateDataTableState(context, component);
                }
                super.encodeEnd(context, component);
            }
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    protected void encodeColumnHeader(FacesContext context, DataTable table, Column column) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final String clientId = column.getClientId(context);
        final ValueExpression tableSortByVe = table.getValueExpression("sortBy");
        final ValueExpression columnSortByVe = column.getValueExpression("sortBy");
        final boolean isSortable = columnSortByVe != null;
        final boolean hasFilter = column.getValueExpression("filterBy") != null;
        final String selectionMode = column.getSelectionMode();
        String sortIcon = isSortable ? DataTable.SORTABLE_COLUMN_ICON_CLASS : null;
        final boolean resizable = table.isResizableColumns() && column.isResizable();

        String columnClass = isSortable ? DataTable.COLUMN_HEADER_CLASS + " " + DataTable.SORTABLE_COLUMN_CLASS
                : DataTable.COLUMN_HEADER_CLASS;
        columnClass = hasFilter ? columnClass + " " + DataTable.FILTER_COLUMN_CLASS : columnClass;
        columnClass = selectionMode != null ? columnClass + " " + DataTable.SELECTION_COLUMN_CLASS : columnClass;
        columnClass = resizable ? columnClass + " " + DataTable.RESIZABLE_COLUMN_CLASS : columnClass;
        columnClass = column.getStyleClass() != null ? columnClass + " " + column.getStyleClass() : columnClass;

        if (isSortable) {
            final String columnSortByExpression = columnSortByVe.getExpressionString();

            if (tableSortByVe != null) {
                final String tableSortByExpression = tableSortByVe.getExpressionString();

                if (tableSortByExpression != null && tableSortByExpression.equals(columnSortByExpression)) {
                    final String sortOrder = table.getSortOrder().toUpperCase();

                    if (sortOrder.equals("ASCENDING")) {
                        sortIcon = DataTable.SORTABLE_COLUMN_ASCENDING_ICON_CLASS;
                    } else if (sortOrder.equals("DESCENDING")) {
                        sortIcon = DataTable.SORTABLE_COLUMN_DESCENDING_ICON_CLASS;
                    }

                    columnClass = columnClass + " ui-state-active";
                }
            }
        }

        writer.startElement("th", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", columnClass, null);
        writer.writeAttribute("role", "columnheader", null);

        if (column.getStyle() != null) {
            writer.writeAttribute("style", column.getStyle(), null);
        }
        if (column.getRowspan() != 1) {
            writer.writeAttribute("rowspan", column.getRowspan(), null);
        }
        if (column.getColspan() != 1) {
            writer.writeAttribute("colspan", column.getColspan(), null);
        }

        // column content wrapper
        writer.startElement("div", null);
        writer.writeAttribute("class", DataTable.COLUMN_CONTENT_WRAPPER, null);
        if (column.getWidth() != -1) {
            writer.writeAttribute("style", "width:" + column.getWidth() + "px", null);
        }

        if (selectionMode != null && selectionMode.equalsIgnoreCase("multiple")) {
            encodeCheckbox(context, table, false, column.isDisabledSelection(), HTML.CHECKBOX_ALL_CLASS);
        } else {
            if (hasFilter) {
                table.enableFiltering();
                final String filterPosition = column.getFilterPosition();

                if (filterPosition.equals("bottom")) {
                    encodeColumnHeaderContent(context, column, sortIcon);
                    encodeFilter(context, table, column);
                } else if (filterPosition.equals("top")) {
                    encodeFilter(context, table, column);
                    encodeColumnHeaderContent(context, column, sortIcon);
                } else {
                    throw new FacesException(filterPosition
                            + " is an invalid option for filterPosition, valid values are 'bottom' or 'top'.");
                }
            } else {
                encodeColumnHeaderContent(context, column, sortIcon);
            }

            // JSC -- Add sort icon at the end
            if (sortIcon != null) {
                writer.startElement("span", null);
                writer.writeAttribute("class", sortIcon, null);
                writer.endElement("span");
            }
            // JSC -- Add sort icon at the end
        }

        writer.endElement("div");

        writer.endElement("th");
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    protected void encodeColumnHeaderContent(FacesContext context, Column column, String sortIcon) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final UIComponent header = column.getFacet("header");
        final String headerText = column.getHeaderText();

        writer.startElement("span", null);

        if (header != null) {
            header.encodeAll(context);
        } else if (headerText != null) {
            writer.write(headerText);
        }

        writer.endElement("span");
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    protected void encodeFilter(FacesContext context, DataTable table, Column column) throws IOException {
        final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        final ResponseWriter writer = context.getResponseWriter();

        final String filterId = column.getClientId(context) + "_filter";
        final String filterValue = params.containsKey(filterId) ? params.get(filterId) : "";
        String filterStyleClass = column.getFilterStyleClass();

        // JSC -- Override to move sort icon next to filter
        writer.startElement("div", null);
        writer.writeAttribute("class", "ui-datatable-header-filter", null);
        // JSC -- Override to move sort icon next to filter

        if (column.getValueExpression("filterOptions") == null) {
            filterStyleClass = filterStyleClass == null ? DataTable.COLUMN_INPUT_FILTER_CLASS
                    : DataTable.COLUMN_INPUT_FILTER_CLASS + " " + filterStyleClass;

            writer.startElement("input", null);
            writer.writeAttribute("id", filterId, null);
            writer.writeAttribute("name", filterId, null);
            writer.writeAttribute("class", filterStyleClass, null);
            writer.writeAttribute("value", filterValue, null);
            writer.writeAttribute("autocomplete", "off", null);

            if (column.getFilterStyle() != null) {
                writer.writeAttribute("style", column.getFilterStyle(), null);
            }

            if (column.getFilterMaxLength() != Integer.MAX_VALUE) {
                writer.writeAttribute("maxlength", column.getFilterMaxLength(), null);
            }

            writer.endElement("input");
        } else {
            filterStyleClass = filterStyleClass == null ? DataTable.COLUMN_FILTER_CLASS : DataTable.COLUMN_FILTER_CLASS
                    + " " + filterStyleClass;

            writer.startElement("select", null);
            writer.writeAttribute("id", filterId, null);
            writer.writeAttribute("name", filterId, null);
            writer.writeAttribute("class", filterStyleClass, null);

            final SelectItem[] itemsArray = getFilterOptions(column);

            for (final SelectItem item : itemsArray) {
                final Object itemValue = item.getValue();

                writer.startElement("option", null);
                writer.writeAttribute("value", item.getValue(), null);
                if (itemValue != null && String.valueOf(itemValue).equals(filterValue)) {
                    writer.writeAttribute("selected", "selected", null);
                }
                writer.writeText(item.getLabel(), null);
                writer.endElement("option");
            }

            writer.endElement("select");
        }

        // JSC -- Override to move sort icon next to filter
        writer.endElement("div");
        // JSC -- Override to move sort icon next to filter
    }

    /**
     * Checks if is data manipulation request.
     * 
     * @param context the context
     * @param table the table
     * @return true, if is data manipulation request
     */
    protected boolean isDataManipulationRequest(FacesContext context, DataTable table) {
        return table.isPaginationRequest(context) || table.isSortRequest(context) || table.isFilterRequest(context);
    }

    /**
     * Encode reload filters request.
     * 
     * @param context the context
     * @param table the table
     */
    protected void encodeReloadFiltersRequest(FacesContext context, DataTable table) {
        if (table.hasFilter() && !MapUtils.isEmpty(table.getFilters())) {
            final Map<String, String> filterValues = new HashMap<String, String>();
            final Map<String, Column> filterMap = table.getFilterMap();
            final Map<String, String> filters = table.getFilters();

            for (final String filterClientId : filterMap.keySet()) {
                final Column column = filterMap.get(filterClientId);
                final String filterField = resolveField(column.getValueExpression("filterBy"));

                if (filters.containsKey(filterField)) {
                    final String filterValue = filters.get(filterField);
                    filterValues.put(column.getId(), filterValue);
                }
            }

            if (filterValues.size() != filters.size() && table.getAttributes().containsKey("EXTRA_FILTERS")) {
                final String extraFiltersString = (String) table.getAttributes().get("EXTRA_FILTERS");
                if (!StringUtil.isBlank(extraFiltersString)) {
                    final String[] extraFilters = extraFiltersString.split(",");
                    if (!ArrayUtil.isEmpty(extraFilters)) {
                        for (final String extraFilter : extraFilters) {
                            final String filterName = StringUtil.substringAfterLast(extraFilter, ":");
                            if (!StringUtil.isBlank(filterName)) {
                                final String filterValue = filters.get(filterName);
                                if (filterValue != null) {
                                    filterValues.put(extraFilter, filterValue);
                                }
                            }
                        }
                    }
                }
            }

            RequestContext.getCurrentInstance().addCallbackParam("filterValues",
                    new JSONObject(filterValues).toString());
        }

        final String clientId = table.getClientId(context);
        final String selectedRowIds = (String) FacesUtils.getSessionAttribute(context, clientId + "_selectedRowIds");
        if (!StringUtil.isBlank(selectedRowIds)) {
            final String[] selectedRowIdsArray = selectedRowIds.split(",");
            if (!ArrayUtil.isEmpty(selectedRowIdsArray)) {
                try {
                    RequestContext.getCurrentInstance().addCallbackParam("selectedRowIds",
                            new JSONArray(selectedRowIdsArray).toString());
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Resolve field.
     * 
     * @param expression the expression
     * @return the string
     */
    protected String resolveField(ValueExpression expression) {
        String expressionString = expression.getExpressionString();
        expressionString = expressionString.substring(2, expressionString.length() - 1);

        return expressionString.substring(expressionString.indexOf(".") + 1);
    }

    /**
     * Checks if is reload filters request.
     * 
     * @param context the context
     * @param component the component
     * @return true, if is reload filters request
     */
    protected boolean isReloadFiltersRequest(FacesContext context, UIComponent component) {
        boolean reloadFiltersRequest = false;
        if (context != null && component != null) {
            final DataTable table = (DataTable) component;
            final String clientId = table.getClientId(context);

            reloadFiltersRequest = FacesUtils.containsRequestParameter(context, clientId + "_reloadFilters");
        }
        return reloadFiltersRequest;
    }

    /**
     * Checks if is reload request.
     * 
     * @param context the context
     * @param component the component
     * @return true, if is reload request
     */
    protected boolean isReloadContentRequest(FacesContext context, UIComponent component) {
        boolean reloadContentRequest = false;
        if (context != null && component != null) {
            final DataTable table = (DataTable) component;
            final String clientId = table.getClientId(context);

            reloadContentRequest = FacesUtils.containsRequestParameter(context, clientId + "_reloadContent");
        }
        return reloadContentRequest;
    }

    /**
     * Checks if is unload request.
     * 
     * @param context the context
     * @param component the component
     * @return true, if is unload request
     */
    protected boolean isUnloadRequest(FacesContext context, UIComponent component) {
        boolean unloadRequest = false;
        if (context != null && component != null) {
            final DataTable table = (DataTable) component;
            final String clientId = table.getClientId(context);

            unloadRequest = FacesUtils.containsRequestParameter(context, clientId + "_unload");
        }
        return unloadRequest;
    }

    /**
     * Updates the data table state.
     * 
     * @param context the context
     * @param component the component
     */
    @SuppressWarnings("unchecked")
    protected void updateDataTableState(FacesContext context, UIComponent component) {
        final DataTable dataTable = (DataTable) component;
        final String clientId = dataTable.getClientId(context);
        final String cacheValue = (String) dataTable.getAttributes().get(CACHE);
        final String resetValue = (String) dataTable.getAttributes().get(RESET);

        if (StringUtil.isBlank(cacheValue) || BooleanUtils.toBoolean(cacheValue)) {
            final Integer first = (Integer) FacesUtils.getSessionAttribute(context, clientId + "_first");
            final Integer rows = (Integer) FacesUtils.getSessionAttribute(context, clientId + "_rows");

            final Map<String, String> filters = (Map<String, String>) FacesUtils.getSessionAttribute(context, clientId
                    + "_filters");
            if (first != null) {
                dataTable.setFirst(first);
            }
            if (rows != null) {
                dataTable.setRows(rows);
            }
            if (!MapUtils.isEmpty(filters)) {
                dataTable.setFilters(filters);
            }
        }
        if (!StringUtil.isBlank(resetValue) && BooleanUtils.toBoolean(resetValue)) {
            dataTable.setFirst(0);
            dataTable.setFilters(null);
        }
    }

    /**
     * Save data table state.
     * 
     * @param context the context
     * @param component the component
     */
    protected void saveDataTableState(FacesContext context, UIComponent component) {
        final DataTable dataTable = (DataTable) component;
        final String clientId = dataTable.getClientId(context);
        final String cacheValue = (String) dataTable.getAttributes().get(CACHE);

        if (StringUtil.isBlank(cacheValue) || BooleanUtils.toBoolean(cacheValue)) {
            if (dataTable.isFilterRequest(context)) {
                FacesUtils.setSessionAttribute(context, clientId + "_filters", dataTable.getFilters());
            }

            if (dataTable.isSelectionEnabled()) {
                final String selection = FacesUtils.getRequestParameter(context, clientId + "_selection");
                FacesUtils.setSessionAttribute(context, clientId + "_selectedRowIds", selection);
            }

            if (dataTable.isPaginationRequest(context)) {
                FacesUtils.setSessionAttribute(context, clientId + "_rowCount", dataTable.getRowCount());
                FacesUtils.setSessionAttribute(context, clientId + "_first", dataTable.getFirst());
                FacesUtils.setSessionAttribute(context, clientId + "_rows", dataTable.getRows());
            }
        }
    }
}