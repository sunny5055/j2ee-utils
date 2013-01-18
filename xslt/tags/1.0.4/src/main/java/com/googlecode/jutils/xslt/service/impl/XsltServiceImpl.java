package com.googlecode.jutils.xslt.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.io.DocumentSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.xslt.configuration.XsltConfiguration;
import com.googlecode.jutils.xslt.exception.XsltServiceException;
import com.googlecode.jutils.xslt.service.XsltService;

/**
 * The Class XsltServiceImpl.
 */
@Service
public class XsltServiceImpl implements XsltService {
	@Autowired(required = false)
	private XsltConfiguration configuration;

	/**
	 * Instantiates a new xslt service impl.
	 */
	public XsltServiceImpl() {
		super();
		this.configuration = new XsltConfiguration();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(String xmlFileName, String xslFileName) throws XsltServiceException {
		return transform(xmlFileName, xslFileName, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(String xmlFileName, String xslFileName, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (!StringUtil.isBlank(xmlFileName) && !StringUtil.isBlank(xslFileName)) {
			String xmlContent = null;
			try {
				xmlContent = configuration.getXmlContent(xmlFileName);
			} catch (final IOException e) {
				throw new XsltServiceException(e);
			}
			result = transformFromString(xmlContent, xslFileName, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(String xmlFileName, File xslFile) throws XsltServiceException {
		return transform(xmlFileName, xslFile, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(String xmlFileName, File xslFile, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (!StringUtil.isBlank(xmlFileName) && xslFile != null) {
			String xmlContent = null;
			try {
				xmlContent = configuration.getXmlContent(xmlFileName);
			} catch (final IOException e) {
				throw new XsltServiceException(e);
			}
			result = transformFromString(xmlContent, xslFile, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transformFromString(String xmlContent, String xslFileName) throws XsltServiceException {
		return transformFromString(xmlContent, xslFileName, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transformFromString(String xmlContent, String xslFileName, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (!StringUtil.isBlank(xmlContent)) {
			InputStream inputStream = null;
			try {
				inputStream = IOUtils.toInputStream(xmlContent);
			} catch (final Exception e) {
				throw new XsltServiceException(e);
			}
			result = transform(inputStream, xslFileName, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transformFromString(String xmlContent, File xslFile) throws XsltServiceException {
		return transformFromString(xmlContent, xslFile, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transformFromString(String xmlContent, File xslFile, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (!StringUtil.isBlank(xmlContent)) {
			InputStream inputStream = null;
			try {
				inputStream = IOUtils.toInputStream(xmlContent);
			} catch (final Exception e) {
				throw new XsltServiceException(e);
			}
			result = transform(inputStream, xslFile, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(File xmlFile, String xslFileName) throws XsltServiceException {
		return transform(xmlFile, xslFileName, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(File xmlFile, String xslFileName, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (xmlFile != null) {
			Reader reader = null;
			try {
				reader = new BufferedReader(new FileReader(xmlFile));
			} catch (final FileNotFoundException e) {
				throw new XsltServiceException(e);
			} catch (final Exception e) {
				throw new XsltServiceException(e);
			}
			result = transform(reader, xslFileName, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(File xmlFile, File xslFile) throws XsltServiceException {
		return transform(xmlFile, xslFile, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(File xmlFile, File xslFile, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (xmlFile != null) {
			Reader reader = null;
			try {
				reader = new BufferedReader(new FileReader(xmlFile));
			} catch (final FileNotFoundException e) {
				throw new XsltServiceException(e);
			} catch (final Exception e) {
				throw new XsltServiceException(e);
			}
			result = transform(reader, xslFile, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(InputStream xmlInputStream, String xslFileName) throws XsltServiceException {
		return transform(xmlInputStream, xslFileName, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(InputStream xmlInputStream, String xslFileName, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (xmlInputStream != null) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(xmlInputStream);
			} catch (final Exception e) {
				throw new XsltServiceException(e);
			}
			result = transform(reader, xslFileName, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(InputStream xmlInputStream, File xslFile) throws XsltServiceException {
		return transform(xmlInputStream, xslFile, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(InputStream xmlInputStream, File xslFile, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (xmlInputStream != null) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(xmlInputStream);
			} catch (final Exception e) {
				throw new XsltServiceException(e);
			}
			result = transform(reader, xslFile, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(Reader xmlReader, String xslFileName) throws XsltServiceException {
		return transform(xmlReader, xslFileName, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(Reader xmlReader, String xslFileName, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (xmlReader != null && !StringUtil.isBlank(xslFileName)) {
			String xslContent = null;
			try {
				xslContent = configuration.getXslContent(xslFileName);
			} catch (final IOException e) {
				throw new XsltServiceException(e);
			}
			result = doTransform(new StreamSource(xmlReader), xslContent, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(Reader xmlReader, File xslFile) throws XsltServiceException {
		return transform(xmlReader, xslFile, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(Reader xmlReader, File xslFile, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (xmlReader != null && xslFile != null) {
			String xslContent = null;
			try {
				xslContent = FileUtils.readFileToString(xslFile);
			} catch (final IOException e) {
				throw new XsltServiceException(e);
			}
			result = doTransform(new StreamSource(xmlReader), xslContent, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(Document xmlDocument, String xslFileName) throws XsltServiceException {
		return transform(xmlDocument, xslFileName, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(Document xmlDocument, String xslFileName, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (xmlDocument != null && !StringUtil.isBlank(xslFileName)) {
			String xslContent = null;
			try {
				xslContent = configuration.getXslContent(xslFileName);
			} catch (final IOException e) {
				throw new XsltServiceException(e);
			}

			result = doTransform(new DocumentSource(xmlDocument), xslContent, parameters);
		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(Document xmlDocument, File xslFile) throws XsltServiceException {
		return transform(xmlDocument, xslFile, null);
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String transform(Document xmlDocument, File xslFile, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (xmlDocument != null && xslFile != null) {
			String xslContent = null;
			try {
				xslContent = FileUtils.readFileToString(xslFile);
			} catch (final IOException e) {
				throw new XsltServiceException(e);
			}

			result = doTransform(new DocumentSource(xmlDocument), xslContent, parameters);
		}
		return result;
	}

	/**
	 * Do transform.
	 * 
	 * @param xmlSource
	 *            the xml source
	 * @param xslContent
	 *            the xsl content
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	protected String doTransform(Source xmlSource, String xslContent, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (xmlSource != null && !StringUtil.isBlank(xslContent)) {
			final Transformer transformer = getTransformer(xslContent);
			if (transformer != null) {
				if (!MapUtil.isEmpty(parameters)) {
					for (final Map.Entry<String, Object> entry : parameters.entrySet()) {
						transformer.setParameter(entry.getKey(), entry.getValue());
					}
				}

				final StringWriter writer = new StringWriter();
				try {
					transformer.transform(xmlSource, new StreamResult(writer));
				} catch (final TransformerException e) {
					throw new XsltServiceException(e);
				}
				result = writer.toString();
			}
		}
		return result;
	}

	/**
	 * Gets the transformer.
	 * 
	 * @param xslContent
	 *            the xsl content
	 * @return the transformer
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	protected Transformer getTransformer(String xslContent) throws XsltServiceException {
		Transformer transformer = null;
		if (!StringUtil.isBlank(xslContent)) {
			Templates templates = null;

			final TransformerFactory factory = configuration.getTransformerFactory();
			try {
				templates = factory.newTemplates(new StreamSource(new StringReader(xslContent)));
			} catch (final TransformerConfigurationException e) {
				throw new XsltServiceException(e);
			}

			if (templates != null) {
				try {
					transformer = templates.newTransformer();
				} catch (final TransformerConfigurationException e) {
					throw new XsltServiceException(e);
				}
			}
		}
		return transformer;
	}
}
