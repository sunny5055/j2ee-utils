package com.googlecode.jutils.templater.engine;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
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
	 * Adds the template loader path.
	 * 
	 * @param file
	 *            the file
	 */
	void addTemplateLoaderPath(File file);

	/**
	 * Process.
	 * 
	 * @param templateName
	 *            the template name
	 * @param data
	 *            the data
	 * @param writer
	 *            the writer
	 * @throws TemplaterServiceException
	 *             the templater service exception
	 */
	void process(String templateName, Map<String, Object> data, Writer writer) throws TemplaterServiceException;

	/**
	 * Process.
	 * 
	 * @param reader
	 *            the reader
	 * @param data
	 *            the data
	 * @param writer
	 *            the writer
	 * @throws TemplaterServiceException
	 *             the templater service exception
	 */
	void processFromReader(Reader reader, Map<String, Object> data, Writer writer) throws TemplaterServiceException;

	/**
	 * Gets the content into a String.
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
	 * Gets the content into a String.
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
