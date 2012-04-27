package com.google.code.jee.utils.jsf.taglib.exporter;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import com.google.code.jee.utils.BooleanUtil;

/**
 * The Class PrintExporterTag.
 */
public class PrintExporterTag extends AbstractExporterTag {
    private ValueExpression headTitle;
    private ValueExpression displayCount;
    private ValueExpression entityFoundMsg;

    /**
     * Instantiates a new prints the exporter tag.
     * 
     * @param target the target
     * @param headerRow the header row
     * @param encoding the encoding
     * @param pageOnly the page only
     * @param excludeColumns the exclude columns
     * @param headTitle the head title
     * @param displayCount the display count
     * @param entityFoundMsg the entity found msg
     */
    public PrintExporterTag(ValueExpression target, ValueExpression headerRow, ValueExpression encoding,
            ValueExpression pageOnly, ValueExpression excludeColumns, ValueExpression headTitle,
            ValueExpression displayCount, ValueExpression entityFoundMsg) {
        super(target, headerRow, encoding, pageOnly, excludeColumns);
        this.headTitle = headTitle;
        this.displayCount = displayCount;
        this.entityFoundMsg = entityFoundMsg;
    }

    /**
     * Gets the head title.
     * 
     * @return the head title
     */
    public ValueExpression getHeadTitle() {
        return headTitle;
    }

    /**
     * Sets the head title.
     * 
     * @param headTitle the new head title
     */
    public void setHeadTitle(ValueExpression headTitle) {
        this.headTitle = headTitle;
    }

    /**
     * Gets the display count.
     * 
     * @return the display count
     */
    public ValueExpression getDisplayCount() {
        return displayCount;
    }

    /**
     * Sets the display count.
     * 
     * @param displayCount the new display count
     */
    public void setDisplayCount(ValueExpression displayCount) {
        this.displayCount = displayCount;
    }

    /**
     * Gets the entity found msg.
     * 
     * @return the entity found msg
     */
    public ValueExpression getEntityFoundMsg() {
        return entityFoundMsg;
    }

    /**
     * Sets the entity found msg.
     * 
     * @param entityFoundMsg the new entity found msg
     */
    public void setEntityFoundMsg(ValueExpression entityFoundMsg) {
        this.entityFoundMsg = entityFoundMsg;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    protected AbstractExporter getExporter() {
        final PrintExporter exporter = new PrintExporter();

        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final ELContext elContext = facesContext.getELContext();

        if (encoding != null) {
            exporter.setEncoding((String) encoding.getValue(elContext));
        }
        if (pageOnly != null) {
            boolean isPageOnly = false;
            if (pageOnly.isLiteralText()) {
                isPageOnly = BooleanUtil.toBooleanObject(pageOnly.getValue(facesContext.getELContext()).toString());
            } else {
                isPageOnly = BooleanUtil.toBooleanObject(pageOnly.getValue(facesContext.getELContext()));
            }
            exporter.setPageOnly(isPageOnly);
        }
        if (headerRow != null) {
            boolean hasHeaderRow = false;
            if (headerRow.isLiteralText()) {
                hasHeaderRow = BooleanUtil.toBooleanObject(headerRow.getValue(facesContext.getELContext()).toString());
            } else {
                hasHeaderRow = BooleanUtil.toBooleanObject(headerRow.getValue(facesContext.getELContext()));
            }
            exporter.setHeaderRow(hasHeaderRow);
        }
        if (excludeColumns != null) {
            final int[] excludedColumnIndexes = resolveExcludedColumnIndexes((String) excludeColumns
                    .getValue(elContext));
            exporter.setExcludedColumnIndexes(excludedColumnIndexes);
        }

        if (headTitle != null) {
            exporter.setHeadTitle((String) headTitle.getValue(elContext));
        }

        if (displayCount != null) {
            boolean hasDisplayCount = false;
            if (displayCount.isLiteralText()) {
                hasDisplayCount = BooleanUtil.toBooleanObject(displayCount.getValue(facesContext.getELContext())
                        .toString());
            } else {
                hasDisplayCount = BooleanUtil.toBooleanObject(displayCount.getValue(facesContext.getELContext()));
            }
            exporter.setDisplayCount(hasDisplayCount);
        }

        if (entityFoundMsg != null) {
            exporter.setEntityFoundMsg((String) entityFoundMsg.getValue(elContext));
        }

        return exporter;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void restoreState(FacesContext context, Object state) {
        final Object values[] = (Object[]) state;

        this.target = (ValueExpression) values[0];
        this.headerRow = (ValueExpression) values[1];
        this.encoding = (ValueExpression) values[2];
        this.pageOnly = (ValueExpression) values[3];
        this.excludeColumns = (ValueExpression) values[4];
        this.headTitle = (ValueExpression) values[5];
        this.displayCount = (ValueExpression) values[6];
        this.entityFoundMsg = (ValueExpression) values[7];
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Object saveState(FacesContext context) {
        final Object values[] = new Object[8];

        values[0] = this.target;
        values[1] = this.headerRow;
        values[2] = this.encoding;
        values[3] = this.pageOnly;
        values[4] = this.excludeColumns;
        values[5] = this.headTitle;
        values[6] = this.displayCount;
        values[7] = this.entityFoundMsg;

        return values;
    }
}
