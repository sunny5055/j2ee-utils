package com.googlecode.jutils.templater.engine;

import java.io.Reader;
import java.util.Map;

import com.googlecode.jutils.templater.exception.TemplaterServiceException;

/**
 * The Interface TemplaterEngine.
 */
public interface TemplaterEngine {

	/**
	 * Accept.
	 * 
	 * @param extension
	 *            the extension
	 * @return true, if successful
	 */
	boolean accept(String extension);

	/**
	 * Getter : return the content.
	 * 
	 * @param templateName
	 *            the template name
	 * @param data
	 *            the data
	 * @return the content
	 * @throws TemplaterServiceException
	 *             the templater service exception
	 */
	String getContent(String templateName, Map<String, Object> data) throws TemplaterServiceException;

	/**
	 * Getter : return the content from reader.
	 * 
	 * @param reader
	 *            the reader
	 * @param data
	 *            the data
	 * @return the content from reader
	 * @throws TemplaterServiceException
	 *             the templater service exception
	 */
	String getContentFromReader(Reader reader, Map<String, Object> data) throws TemplaterServiceException;
}
