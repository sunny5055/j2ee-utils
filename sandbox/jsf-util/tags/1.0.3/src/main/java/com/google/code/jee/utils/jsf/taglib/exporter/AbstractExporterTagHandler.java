package com.google.code.jee.utils.jsf.taglib.exporter;

import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

/**
 * The Class AbstractExporterTagHandler.
 */
public abstract class AbstractExporterTagHandler extends TagHandler {
    protected TagAttribute target;
    protected TagAttribute headerRow;
    protected TagAttribute encoding;
    protected TagAttribute pageOnly;
    protected TagAttribute excludeColumns;

    /**
     * Instantiates a new abstract exporter tag handler.
     * 
     * @param tagConfig the tag config
     */
    public AbstractExporterTagHandler(TagConfig tagConfig) {
        super(tagConfig);
        this.target = getRequiredAttribute("target");
        this.headerRow = getAttribute("headerRow");
        this.encoding = getAttribute("encoding");
        this.pageOnly = getAttribute("pageOnly");
        this.excludeColumns = getAttribute("excludeColumns");
    }

}
