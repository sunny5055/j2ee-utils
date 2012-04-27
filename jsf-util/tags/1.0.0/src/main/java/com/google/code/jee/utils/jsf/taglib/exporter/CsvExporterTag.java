package com.google.code.jee.utils.jsf.taglib.exporter;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import com.google.code.jee.utils.BooleanUtil;

/**
 * The Class CsvExporterTag.
 */
public class CsvExporterTag extends AbstractExporterTag {
    private ValueExpression fileName;
    private ValueExpression separator;
    private ValueExpression lineBreakReplacement;

    /**
     * Instantiates a new csv exporter tag.
     * 
     * @param target the target
     * @param headerRow the header row
     * @param encoding the encoding
     * @param pageOnly the page only
     * @param excludeColumns the exclude columns
     * @param fileName the file name
     * @param separator the separator
     * @param lineBreakReplacement the line break replacement
     */
    public CsvExporterTag(ValueExpression target, ValueExpression headerRow, ValueExpression encoding,
            ValueExpression pageOnly, ValueExpression excludeColumns, ValueExpression fileName,
            ValueExpression separator, ValueExpression lineBreakReplacement) {
        super(target, headerRow, encoding, pageOnly, excludeColumns);
        this.fileName = fileName;
        this.separator = separator;
        this.lineBreakReplacement = lineBreakReplacement;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    protected AbstractExporter getExporter() {
        CsvExporter exporter = null;
        if (fileName != null) {
            final FacesContext facesContext = FacesContext.getCurrentInstance();
            final ELContext elContext = facesContext.getELContext();

            exporter = new CsvExporter((String) fileName.getValue(elContext));

            if (lineBreakReplacement != null) {
                exporter.setLineBreakReplacement((String) lineBreakReplacement.getValue(elContext));
            }
            if (separator != null) {
                exporter.setSeparator((String) separator.getValue(elContext));
            }
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
                    hasHeaderRow = BooleanUtil.toBooleanObject(headerRow.getValue(facesContext.getELContext())
                            .toString());
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
        this.fileName = (ValueExpression) values[5];
        this.separator = (ValueExpression) values[6];
        this.lineBreakReplacement = (ValueExpression) values[7];
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
        values[5] = this.fileName;
        values[6] = this.separator;
        values[7] = this.lineBreakReplacement;

        return values;
    }
}
