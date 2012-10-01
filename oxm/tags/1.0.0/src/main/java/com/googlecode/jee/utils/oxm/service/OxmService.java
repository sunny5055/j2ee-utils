package com.googlecode.jee.utils.oxm.service;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import com.googlecode.jee.utils.oxm.exception.OxmServiceException;

/**
 * Interface OxmService.
 */
public interface OxmService {

	/**
	 * Marshall.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	String marshall(Object value) throws OxmServiceException;

	/**
	 * Marshall.
	 * 
	 * @param value
	 *            the value
	 * @param xmlFile
	 *            the xml file
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	void marshall(Object value, File xmlFile) throws OxmServiceException;

	/**
	 * Marshall.
	 * 
	 * @param value
	 *            the value
	 * @param xmlFile
	 *            the xml file
	 * @param encoding
	 *            the encoding
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	void marshall(Object value, File xmlFile, String encoding) throws OxmServiceException;

	/**
	 * Marshall.
	 * 
	 * @param value
	 *            the value
	 * @param outputStream
	 *            the output stream
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	void marshall(Object value, OutputStream outputStream) throws OxmServiceException;

	/**
	 * Marshall.
	 * 
	 * @param value
	 *            the value
	 * @param outputStream
	 *            the output stream
	 * @param encoding
	 *            the encoding
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	void marshall(Object value, OutputStream outputStream, String encoding) throws OxmServiceException;

	/**
	 * Marshall.
	 * 
	 * @param value
	 *            the value
	 * @param writer
	 *            the writer
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	void marshall(Object value, Writer writer) throws OxmServiceException;

	/**
	 * Unmarshall.
	 * 
	 * @param xmlContent
	 *            the xml content
	 * @return the object
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	Object unmarshall(String xmlContent) throws OxmServiceException;

	/**
	 * Unmarshall.
	 * 
	 * @param xmlFile
	 *            the xml file
	 * @return the object
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	Object unmarshall(File xmlFile) throws OxmServiceException;

	/**
	 * Unmarshall.
	 * 
	 * @param xmlFile
	 *            the xml file
	 * @param encoding
	 *            the encoding
	 * @return the object
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	Object unmarshall(File xmlFile, String encoding) throws OxmServiceException;

	/**
	 * Unmarshall.
	 * 
	 * @param inputStream
	 *            the input stream
	 * @return the object
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	Object unmarshall(InputStream inputStream) throws OxmServiceException;

	/**
	 * Unmarshall.
	 * 
	 * @param inputStream
	 *            the input stream
	 * @param encoding
	 *            the encoding
	 * @return the object
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	Object unmarshall(InputStream inputStream, String encoding) throws OxmServiceException;

	/**
	 * Unmarshall.
	 * 
	 * @param reader
	 *            the reader
	 * @return the object
	 * @throws OxmServiceException
	 *             the oxm service exception
	 */
	Object unmarshall(Reader reader) throws OxmServiceException;
}
