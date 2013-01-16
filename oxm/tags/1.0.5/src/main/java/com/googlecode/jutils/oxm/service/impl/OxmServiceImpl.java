package com.googlecode.jutils.oxm.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.oxm.exception.OxmServiceException;
import com.googlecode.jutils.oxm.service.OxmService;

/**
 * The Class OxmServiceImpl.
 */
@Service
public class OxmServiceImpl implements OxmService {
	private String defaultEncoding;

	@Autowired
	private Marshaller marshaller;

	@Autowired
	private Unmarshaller unmarshaller;

	/**
	 * Instantiates a new oxm service impl.
	 */
	public OxmServiceImpl() {
		super();
		this.defaultEncoding = "UTF-8";
	}

	/**
	 * Getter : return the default encoding.
	 * 
	 * @return the default encoding
	 */
	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	/**
	 * Setter : affect the default encoding.
	 * 
	 * @param defaultEncoding
	 *            the default encoding
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String marshall(Object value) throws OxmServiceException {
		String result = null;
		if (value != null) {
			Writer writer = null;
			try {
				writer = new StringWriter();
				marshall(value, writer);
				result = writer.toString();
			} finally {
				if (writer != null) {
					try {
						writer.flush();
						writer.close();
					} catch (final IOException e) {
						throw new OxmServiceException(e);
					}
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void marshall(Object value, File xmlFile) throws OxmServiceException {
		marshall(value, xmlFile, defaultEncoding);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void marshall(Object value, File xmlFile, String encoding) throws OxmServiceException {
		if (xmlFile != null && !StringUtil.isBlank(encoding)) {
			Writer writer = null;
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlFile), encoding));
				marshall(value, writer);
			} catch (final FileNotFoundException e) {
				throw new OxmServiceException(e);
			} catch (final Exception e) {
				throw new OxmServiceException(e);
			} finally {
				if (writer != null) {
					try {
						writer.flush();
						writer.close();
					} catch (final IOException e) {
						throw new OxmServiceException(e);
					}
				}
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void marshall(Object value, OutputStream outputStream) throws OxmServiceException {
		marshall(value, outputStream, defaultEncoding);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void marshall(Object value, OutputStream outputStream, String encoding) throws OxmServiceException {
		if (outputStream != null && !StringUtil.isBlank(encoding)) {
			Writer writer;
			try {
				writer = new OutputStreamWriter(outputStream, encoding);
			} catch (final UnsupportedEncodingException e) {
				throw new OxmServiceException(e);
			}

			marshall(value, writer);
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void marshall(Object value, Writer writer) throws OxmServiceException {
		if (value != null && writer != null) {
			try {
				marshaller.marshal(value, new StreamResult(writer));
			} catch (final XmlMappingException e) {
				throw new OxmServiceException(e);
			} catch (final IOException e) {
				throw new OxmServiceException(e);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Object unmarshall(String xmlContent) throws OxmServiceException {
		Object result = null;
		if (!StringUtil.isBlank(xmlContent)) {
			Reader reader = null;
			try {
				reader = new StringReader(xmlContent);
				result = unmarshall(reader);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException e) {
						throw new OxmServiceException(e);
					}
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Object unmarshall(File xmlFile) throws OxmServiceException {
		return unmarshall(xmlFile, defaultEncoding);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Object unmarshall(File xmlFile, String encoding) throws OxmServiceException {
		Object result = null;
		if (xmlFile != null && !StringUtil.isBlank(encoding)) {
			Reader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(xmlFile), encoding));
				result = unmarshall(reader);
			} catch (final FileNotFoundException e) {
				throw new OxmServiceException(e);
			} catch (final Exception e) {
				throw new OxmServiceException(e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException e) {
						throw new OxmServiceException(e);
					}
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Object unmarshall(InputStream inputStream) throws OxmServiceException {
		return unmarshall(inputStream, defaultEncoding);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Object unmarshall(InputStream inputStream, String encoding) throws OxmServiceException {
		Object result = null;
		if (inputStream != null && !StringUtil.isBlank(encoding)) {
			Reader reader;
			try {
				reader = new InputStreamReader(inputStream, encoding);
			} catch (final UnsupportedEncodingException e) {
				throw new OxmServiceException(e);
			}

			result = unmarshall(reader);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Object unmarshall(Reader reader) throws OxmServiceException {
		Object result = null;
		if (reader != null) {
			try {
				result = unmarshaller.unmarshal(new StreamSource(reader));
			} catch (final IOException e) {
				throw new OxmServiceException(e);
			}
		}
		return result;
	}
}
