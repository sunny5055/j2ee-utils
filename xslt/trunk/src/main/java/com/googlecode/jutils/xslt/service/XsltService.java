package com.googlecode.jutils.xslt.service;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import org.dom4j.Document;

import com.googlecode.jutils.xslt.exception.XsltServiceException;

/**
 * Interface XsltService.
 */
public interface XsltService {

	/**
	 * Transform.
	 * 
	 * @param xmlFileName
	 *            the xml file name
	 * @param xslFileName
	 *            the xsl file name
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(String xmlFileName, String xslFileName) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlFileName
	 *            the xml file name
	 * @param xslFileName
	 *            the xsl file name
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(String xmlFileName, String xslFileName, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlFileName
	 *            the xml file name
	 * @param xslFile
	 *            the xsl file
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(String xmlFileName, File xslFile) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlFileName
	 *            the xml file name
	 * @param xslFile
	 *            the xsl file
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(String xmlFileName, File xslFile, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform from string.
	 * 
	 * @param xmlContent
	 *            the xml content
	 * @param xslFileName
	 *            the xsl file name
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transformFromString(String xmlContent, String xslFileName) throws XsltServiceException;

	/**
	 * Transform from string.
	 * 
	 * @param xmlContent
	 *            the xml content
	 * @param xslFileName
	 *            the xsl file name
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transformFromString(String xmlContent, String xslFileName, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform from string.
	 * 
	 * @param xmlContent
	 *            the xml content
	 * @param xslFile
	 *            the xsl file
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transformFromString(String xmlContent, File xslFile) throws XsltServiceException;

	/**
	 * Transform from string.
	 * 
	 * @param xmlContent
	 *            the xml content
	 * @param xslFile
	 *            the xsl file
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transformFromString(String xmlContent, File xslFile, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlFile
	 *            the xml file
	 * @param xslFileName
	 *            the xsl file name
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(File xmlFile, String xslFileName) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlFile
	 *            the xml file
	 * @param xslFileName
	 *            the xsl file name
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(File xmlFile, String xslFileName, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlFile
	 *            the xml file
	 * @param xslFile
	 *            the xsl file
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(File xmlFile, File xslFile) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlFile
	 *            the xml file
	 * @param xslFile
	 *            the xsl file
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(File xmlFile, File xslFile, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlInputStream
	 *            the xml input stream
	 * @param xslFileName
	 *            the xsl file name
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(InputStream xmlInputStream, String xslFileName) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlInputStream
	 *            the xml input stream
	 * @param xslFileName
	 *            the xsl file name
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(InputStream xmlInputStream, String xslFileName, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlInputStream
	 *            the xml input stream
	 * @param xslFile
	 *            the xsl file
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(InputStream xmlInputStream, File xslFile) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlInputStream
	 *            the xml input stream
	 * @param xslFile
	 *            the xsl file
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(InputStream xmlInputStream, File xslFile, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlReader
	 *            the xml reader
	 * @param xslFileName
	 *            the xsl file name
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(Reader xmlReader, String xslFileName) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlReader
	 *            the xml reader
	 * @param xslFileName
	 *            the xsl file name
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(Reader xmlReader, String xslFileName, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlReader
	 *            the xml reader
	 * @param xslFile
	 *            the xsl file
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(Reader xmlReader, File xslFile) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlReader
	 *            the xml reader
	 * @param xslFile
	 *            the xsl file
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(Reader xmlReader, File xslFile, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlDocument
	 *            the xml document
	 * @param xslFileName
	 *            the xsl file name
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(Document xmlDocument, String xslFileName) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlDocument
	 *            the xml document
	 * @param xslFileName
	 *            the xsl file name
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(Document xmlDocument, String xslFileName, Map<String, Object> parameters) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlDocument
	 *            the xml document
	 * @param xslFile
	 *            the xsl file
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(Document xmlDocument, File xslFile) throws XsltServiceException;

	/**
	 * Transform.
	 * 
	 * @param xmlDocument
	 *            the xml document
	 * @param xslFile
	 *            the xsl file
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	String transform(Document xmlDocument, File xslFile, Map<String, Object> parameters) throws XsltServiceException;
}