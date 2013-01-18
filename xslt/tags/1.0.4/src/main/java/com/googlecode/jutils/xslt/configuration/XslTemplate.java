package com.googlecode.jutils.xslt.configuration;

import javax.xml.transform.Templates;

// TODO: Auto-generated Javadoc
/**
 * The Class XslTemplate.
 */
public class XslTemplate {
	private Templates templates;
	private long lastModified;

	/**
	 * Instantiates a new xsl template.
	 */
	public XslTemplate() {
		super();
	}

	/**
	 * Instantiates a new xsl template.
	 * 
	 * @param templates
	 *            the templates
	 * @param lastModified
	 *            the last modified
	 */
	public XslTemplate(Templates templates, long lastModified) {
		super();
		this.templates = templates;
		this.lastModified = lastModified;
	}

	/**
	 * Getter : return the templates.
	 * 
	 * @return the templates
	 */
	public Templates getTemplates() {
		return templates;
	}

	/**
	 * Setter : affect the template.
	 * 
	 * @param templates
	 *            the template
	 */
	public void setTemplate(Templates templates) {
		this.templates = templates;
	}

	/**
	 * Getter : return the last modified.
	 * 
	 * @return the last modified
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * Setter : affect the last modified.
	 * 
	 * @param lastModified
	 *            the last modified
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
}
