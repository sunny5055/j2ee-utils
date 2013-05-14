package com.googlecode.jutils.templater.service;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import com.googlecode.jutils.templater.exception.TemplaterServiceException;

/**
 * Interface TemplaterService.
 */
public interface TemplaterService {
	/**
	 * Adds the template loader path.
	 * 
	 * @param file
	 *            the file
	 */
	void addTemplateLoaderPath(File file);

	/**
	 * Process the template using data from the map parameter.
	 * 
	 * @param templateName
	 *            the template name
	 * @param data
	 *            the data
	 * @param writer
	 *            the writer
	 * @throws TemplaterServiceException
	 *             the TemplaterServiceException
	 */
	void process(String templateName, Map<String, Object> data, Writer writer) throws TemplaterServiceException;

	/**
	 * Process the string using data from the map parameter.
	 * 
	 * @param templateContent
	 *            the templateContent
	 * @param templateType
	 *            the template type
	 * @param data
	 *            the data
	 * @param writer
	 *            the writer
	 * @throws TemplaterServiceException
	 *             the TemplaterServiceException
	 */
	void processFromString(String templateContent, String templateType, Map<String, Object> data, Writer writer) throws TemplaterServiceException;

	/**
	 * Process the template using data from the map parameter.
	 * 
	 * @param templateFile
	 *            the templateFile
	 * @param data
	 *            the data
	 * @param writer
	 *            the writer
	 * @throws TemplaterServiceException
	 *             the TemplaterServiceException
	 */
	void processFromFile(File templateFile, Map<String, Object> data, Writer writer) throws TemplaterServiceException;

	/**
	 * Process the template using data from the map parameter.
	 * 
	 * @param inputStream
	 *            the inputStream
	 * @param templateType
	 *            the template type
	 * @param data
	 *            the data
	 * @param writer
	 *            the writer
	 * @throws TemplaterServiceException
	 *             the TemplaterServiceException
	 */
	void processFromInputStream(InputStream inputStream, String templateType, Map<String, Object> data, Writer writer) throws TemplaterServiceException;

	/**
	 * Process the template using data from the map parameter.
	 * 
	 * @param reader
	 *            the reader
	 * @param templateType
	 *            the template type
	 * @param data
	 *            the data
	 * @param writer
	 *            the writer
	 * @throws TemplaterServiceException
	 *             the TemplaterServiceException
	 */
	void processFromReader(Reader reader, String templateType, Map<String, Object> data, Writer writer) throws TemplaterServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param templateName
	 *            the template name
	 * @param data
	 *            the data
	 * @return String
	 * @throws TemplaterServiceException
	 *             the TemplaterServiceException
	 */
	String getContent(String templateName, Map<String, Object> data) throws TemplaterServiceException;

	/**
	 * Get the content from the string using data from the map parameter.
	 * 
	 * @param templateContent
	 *            the templateContent
	 * @param templateType
	 *            the template type
	 * @param data
	 *            the data
	 * @return String
	 * @throws TemplaterServiceException
	 *             the TemplaterServiceException
	 */
	String getContentFromString(String templateContent, String templateType, Map<String, Object> data) throws TemplaterServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param templateFile
	 *            the templateFile
	 * @param data
	 *            the data
	 * @return String
	 * @throws TemplaterServiceException
	 *             the TemplaterServiceException
	 */
	String getContentFromFile(File templateFile, Map<String, Object> data) throws TemplaterServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param inputStream
	 *            the inputStream
	 * @param templateType
	 *            the template type
	 * @param data
	 *            the data
	 * @return String
	 * @throws TemplaterServiceException
	 *             the TemplaterServiceException
	 */
	String getContentFromInputStream(InputStream inputStream, String templateType, Map<String, Object> data) throws TemplaterServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param reader
	 *            the reader
	 * @param templateType
	 *            the template type
	 * @param data
	 *            the data
	 * @return String
	 * @throws TemplaterServiceException
	 *             the TemplaterServiceException
	 */
	String getContentFromReader(Reader reader, String templateType, Map<String, Object> data) throws TemplaterServiceException;
}
