package com.googlecode.jutils.templater.service;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import com.googlecode.jutils.templater.exception.TemplateServiceException;

/**
 * Interface TemplaterService.
 */
public interface TemplaterService {

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param templateName
	 *            the template name
	 * @param data
	 *            the data
	 * @return String
	 * @throws TemplateServiceException
	 *             the TemplateServiceException
	 */
	String getContent(String templateName, Map<String, Object> data) throws TemplateServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param templateName
	 *            the template name
	 * @param data
	 *            the data
	 * @param encoding
	 *            the encoding
	 * @return String
	 * @throws TemplateServiceException
	 *             the TemplateServiceException
	 */
	String getContent(String templateName, Map<String, Object> data, String encoding) throws TemplateServiceException;

	/**
	 * Get the content from the string using data from the map parameter.
	 * 
	 * @param templateContent
	 *            the templateContent
	 * @param data
	 *            the data
	 * @return String
	 * @throws TemplateServiceException
	 *             the TemplateServiceException
	 */
	String getContentFromString(String templateContent, Map<String, Object> data) throws TemplateServiceException;

	/**
	 * Get the content from the string using data from the map parameter.
	 * 
	 * @param templateContent
	 *            the templateContent
	 * @param data
	 *            the data
	 * @param encoding
	 *            the encoding
	 * @return String
	 * @throws TemplateServiceException
	 *             the TemplateServiceException
	 */
	String getContentFromString(String templateContent, Map<String, Object> data, String encoding) throws TemplateServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param templateAbsoluteFile
	 *            the templateAbsoluteFile
	 * @param data
	 *            the data
	 * @return String
	 * @throws TemplateServiceException
	 *             the TemplateServiceException
	 */
	String getContentFromFile(File templateAbsoluteFile, Map<String, Object> data) throws TemplateServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param templateAbsoluteFile
	 *            the templateAbsoluteFile
	 * @param data
	 *            the data
	 * @param encoding
	 *            the encoding
	 * @return String
	 * @throws TemplateServiceException
	 *             the TemplateServiceException
	 */
	String getContentFromFile(File templateAbsoluteFile, Map<String, Object> data, String encoding) throws TemplateServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param inputTemplate
	 *            the inputTemplate
	 * @param data
	 *            the data
	 * @return String
	 * @throws TemplateServiceException
	 *             the TemplateServiceException
	 */
	String getContentFromInputStream(InputStream inputTemplate, Map<String, Object> data) throws TemplateServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param inputTemplate
	 *            the inputTemplate
	 * @param data
	 *            the data
	 * @param encoding
	 *            the encoding
	 * @return String
	 * @throws TemplateServiceException
	 *             the TemplateServiceException
	 */
	String getContentFromInputStream(InputStream inputTemplate, Map<String, Object> data, String encoding) throws TemplateServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param readerTemplate
	 *            the readerTemplate
	 * @param data
	 *            the data
	 * @return String
	 * @throws TemplateServiceException
	 *             the TemplateServiceException
	 */
	String getContentFromReader(Reader readerTemplate, Map<String, Object> data) throws TemplateServiceException;

	/**
	 * Get the content from the template using data from the map parameter.
	 * 
	 * @param readerTemplate
	 *            the readerTemplate
	 * @param data
	 *            the data
	 * @param encoding
	 *            the encoding
	 * @return String
	 * @throws TemplateServiceException
	 *             the TemplateServiceException
	 */
	String getContentFromReader(Reader readerTemplate, Map<String, Object> data, String encoding) throws TemplateServiceException;
}
