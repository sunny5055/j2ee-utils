package com.googlecode.jutils.jsf.taglib.exporter;

import java.io.IOException;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;

/**
 * The Class CsvExporterTagHandler.
 */
public class CsvExporterTagHandler extends AbstractExporterTagHandler {
    private TagAttribute fileName;
    private TagAttribute separator;
    private TagAttribute lineBreakReplacement;

    /**
     * Instantiates a new csv exporter tag handler.
     * 
     * @param tagConfig the tag config
     */
    public CsvExporterTagHandler(TagConfig tagConfig) {
        super(tagConfig);
        this.fileName = getRequiredAttribute("fileName");
        this.separator = getAttribute("separator");
        this.lineBreakReplacement = getAttribute("lineBreakReplacement");
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void apply(FaceletContext faceletContext, UIComponent parent) throws IOException, FacesException,
            FaceletException, ELException {
        if (ComponentHandler.isNew(parent)) {
            final ValueExpression targetValue = target.getValueExpression(faceletContext, Object.class);
            ValueExpression headerRowValue = null;
            ValueExpression encodingValue = null;
            ValueExpression pageOnlyValue = null;
            ValueExpression excludeColumnsValue = null;
            final ValueExpression fileNameValue = fileName.getValueExpression(faceletContext, Object.class);
            ValueExpression separatorValue = null;
            ValueExpression lineBreakReplacementValue = null;

            if (headerRow != null) {
                headerRowValue = headerRow.getValueExpression(faceletContext, Object.class);
            }
            if (encoding != null) {
                encodingValue = encoding.getValueExpression(faceletContext, Object.class);
            }
            if (pageOnly != null) {
                pageOnlyValue = pageOnly.getValueExpression(faceletContext, Object.class);
            }
            if (excludeColumns != null) {
                excludeColumnsValue = excludeColumns.getValueExpression(faceletContext, Object.class);
            }
            if (separator != null) {
                separatorValue = separator.getValueExpression(faceletContext, Object.class);
            }
            if (lineBreakReplacement != null) {
                lineBreakReplacementValue = lineBreakReplacement.getValueExpression(faceletContext, Object.class);
            }
            final ActionSource actionSource = (ActionSource) parent;
            actionSource.addActionListener(new CsvExporterTag(targetValue, headerRowValue, encodingValue,
                    pageOnlyValue, excludeColumnsValue, fileNameValue, separatorValue, lineBreakReplacementValue));
        }
    }

}
