package com.googlecode.jutils.xslt.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.io.DocumentSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.xslt.configuration.XslTemplate;
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
			File xslFile = null;
			try {
				xslFile = configuration.getXslFile(xslFileName);
			} catch (final IOException e) {
				throw new XsltServiceException(e);
			}
			result = transform(xmlFileName, xslFile, parameters);
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
			File xmlFile = null;
			try {
				xmlFile = configuration.getXmlFile(xmlFileName);
			} catch (final IOException e) {
				throw new XsltServiceException(e);
			}
			result = transform(xmlFile, xslFile, parameters);
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
			File xslFile = null;
			try {
				xslFile = configuration.getXslFile(xslFileName);
			} catch (final IOException e) {
				throw new XsltServiceException(e);
			}
			result = transform(xmlReader, xslFile, parameters);
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
			result = doTransform(new StreamSource(xmlReader), xslFile, parameters);
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
			File xslFile = null;
			try {
				xslFile = configuration.getXslFile(xslFileName);
			} catch (final IOException e) {
				throw new XsltServiceException(e);
			}
			result = transform(xmlDocument, xslFile, parameters);
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
			result = doTransform(new DocumentSource(xmlDocument), xslFile, parameters);
		}
		return result;
	}

	/**
	 * Do transform.
	 * 
	 * @param xmlSource
	 *            the xml source
	 * @param xslFile
	 *            the xsl file
	 * @param parameters
	 *            the parameters
	 * @return the string
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	protected String doTransform(Source xmlSource, File xslFile, Map<String, Object> parameters) throws XsltServiceException {
		String result = null;
		if (xmlSource != null && xslFile != null) {
			final Transformer transformer = getTransformer(xslFile);
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
	 * Getter : return the transformer.
	 * 
	 * @param xslFile
	 *            the xsl file
	 * @return the transformer
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	protected Transformer getTransformer(File xslFile) throws XsltServiceException {
		Transformer transformer = null;
		if (xslFile != null) {
			Templates templates = null;
			final XslTemplate template = configuration.getTemplate(xslFile.getAbsolutePath());
			if (template != null && template.getLastModified() == xslFile.lastModified()) {
				templates = template.getTemplates();
			} else {
				final TransformerFactory factory = configuration.getTransformerFactory();
				try {
					templates = factory.newTemplates(new StreamSource(xslFile));
					configuration.addTemplate(xslFile.getAbsolutePath(), new XslTemplate(templates, xslFile.lastModified()));
				} catch (final TransformerConfigurationException e) {
					throw new XsltServiceException(e);
				}

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
