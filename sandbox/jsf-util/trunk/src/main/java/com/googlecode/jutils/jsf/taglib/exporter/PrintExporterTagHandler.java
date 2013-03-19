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
 * The Class PrintExporterTagHandler.
 */
public class PrintExporterTagHandler extends AbstractExporterTagHandler {
    private TagAttribute headTitle;
    private TagAttribute displayCount;
    private TagAttribute entityFoundMsg;

    /**
     * Instantiates a new prints the exporter tag handler.
     * 
     * @param tagConfig the tag config
     */
    public PrintExporterTagHandler(TagConfig tagConfig) {
        super(tagConfig);
        this.headTitle = getAttribute("headTitle");
        this.displayCount = getAttribute("displayCount");
        this.entityFoundMsg = getAttribute("entityFoundMsg");
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
            ValueExpression headTitleValue = null;
            ValueExpression displayCountValue = null;
            ValueExpression entityFoundMsgValue = null;

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
            if (headTitle != null) {
                headTitleValue = headTitle.getValueExpression(faceletContext, Object.class);
            }
            if (displayCount != null) {
                displayCountValue = displayCount.getValueExpression(faceletContext, Object.class);
            }
            if (entityFoundMsg != null) {
                entityFoundMsgValue = entityFoundMsg.getValueExpression(faceletContext, Object.class);
            }
            final ActionSource actionSource = (ActionSource) parent;
            actionSource.addActionListener(new PrintExporterTag(targetValue, headerRowValue, encodingValue,
                    pageOnlyValue, excludeColumnsValue, headTitleValue, displayCountValue, entityFoundMsgValue));
        }
    }

}
