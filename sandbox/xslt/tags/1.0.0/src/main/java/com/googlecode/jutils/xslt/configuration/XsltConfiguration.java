package com.googlecode.jutils.xslt.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.core.ClassUtil;
import com.googlecode.jutils.xslt.exception.XsltServiceException;

/**
 * The Class XsltConfiguration.
 */
public class XsltConfiguration {
	private Map<String, XslTemplate> templates;
	private String xmlLoaderPath;
	private String xslLoaderPath;
	private Map<String, Object> attributes;
	private Map<String, Boolean> features;
	private Class<URIResolver> uriResolverClass;

	/**
	 * Instantiates a new xslt configuration.
	 */
	public XsltConfiguration() {
		super();
		this.templates = new HashMap<String, XslTemplate>();
		this.attributes = new HashMap<String, Object>();
		this.features = new HashMap<String, Boolean>();
	}

	/**
	 * Getter : return the templates.
	 * 
	 * @return the templates
	 */
	public Map<String, XslTemplate> getTemplates() {
		return templates;
	}

	/**
	 * Sets the templates.
	 * 
	 * @param templates
	 *            the templates
	 */
	public void setTemplates(Map<String, XslTemplate> templates) {
		this.templates = templates;
	}

	/**
	 * Getter : return the template.
	 * 
	 * @param uri
	 *            the uri
	 * @return the template
	 */
	public XslTemplate getTemplate(String uri) {
		return this.templates.get(uri);
	}

	/**
	 * Adds the template.
	 * 
	 * @param uri
	 *            the uri
	 * @param template
	 *            the template
	 */
	public void addTemplate(String uri, XslTemplate template) {
		this.templates.put(uri, template);
	}

	/**
	 * Getter : return the xml loader path.
	 * 
	 * @return the xml loader path
	 */
	public String getXmlLoaderPath() {
		return xmlLoaderPath;
	}

	/**
	 * Setter : affect the xml loader path.
	 * 
	 * @param xmlLoaderPath
	 *            the xml loader path
	 */
	public void setXmlLoaderPath(String xmlLoaderPath) {
		this.xmlLoaderPath = xmlLoaderPath;
	}

	/**
	 * Getter : return the xsl loader path.
	 * 
	 * @return the xsl loader path
	 */
	public String getXslLoaderPath() {
		return xslLoaderPath;
	}

	/**
	 * Setter : affect the xsl loader path.
	 * 
	 * @param xslLoaderPath
	 *            the xsl loader path
	 */
	public void setXslLoaderPath(String xslLoaderPath) {
		this.xslLoaderPath = xslLoaderPath;
	}

	/**
	 * Getter : return the attributes.
	 * 
	 * @return the attributes
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * Sets the attributes.
	 * 
	 * @param attributes
	 *            the attributes
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	/**
	 * Getter : return the features.
	 * 
	 * @return the features
	 */
	public Map<String, Boolean> getFeatures() {
		return features;
	}

	/**
	 * Sets the features.
	 * 
	 * @param features
	 *            the features
	 */
	public void setFeatures(Map<String, Boolean> features) {
		this.features = features;
	}

	/**
	 * Getter : return the uri resolver class.
	 * 
	 * @return the uri resolver class
	 */
	public Class<URIResolver> getUriResolverClass() {
		return uriResolverClass;
	}

	/**
	 * Setter : affect the uri resolver class.
	 * 
	 * @param uriResolverClass
	 *            the uri resolver class
	 */
	public void setUriResolverClass(Class<URIResolver> uriResolverClass) {
		this.uriResolverClass = uriResolverClass;
	}

	/**
	 * Getter : return the xml file.
	 * 
	 * @param xmlFileName
	 *            the xml file name
	 * @return the xml file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public File getXmlFile(String xmlFileName) throws IOException {
		File xmlFile = null;
		if (!StringUtil.isBlank(xmlFileName)) {
			xmlFile = resolveFile(xmlLoaderPath, xmlFileName);
		}
		return xmlFile;
	}

	/**
	 * Getter : return the xsl file.
	 * 
	 * @param xslFileName
	 *            the xsl file name
	 * @return the xsl file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public File getXslFile(String xslFileName) throws IOException {
		File xslFile = null;
		if (!StringUtil.isBlank(xslFileName)) {
			xslFile = resolveFile(xslLoaderPath, xslFileName);
		}
		return xslFile;
	}

	/**
	 * Resolve file.
	 * 
	 * @param rootPath
	 *            the root path
	 * @param fileName
	 *            the file name
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	private File resolveFile(String rootPath, String fileName) throws IOException, FileNotFoundException {
		File file = null;
		if (!StringUtil.isBlank(fileName)) {
			String fullFileName = null;
			if (!StringUtil.isBlank(rootPath)) {
				fullFileName = FilenameUtils.concat(rootPath, fileName);
			} else {
				fullFileName = fileName;
			}

			final ClassPathResource classPathResource = new ClassPathResource(fullFileName);
			if (classPathResource.exists()) {
				file = classPathResource.getFile();
			} else {
				throw new FileNotFoundException(fullFileName + " not found");
			}
		}
		return file;
	}

	/**
	 * Getter : return the transformer factory.
	 * 
	 * @return the transformer factory
	 * @throws XsltServiceException
	 *             the xslt service exception
	 */
	public TransformerFactory getTransformerFactory() throws XsltServiceException {
		final TransformerFactory factory = TransformerFactory.newInstance();
		if (!MapUtil.isEmpty(attributes)) {
			for (final Map.Entry<String, Object> entry : attributes.entrySet()) {
				factory.setAttribute(entry.getKey(), entry.getValue());
			}
		}

		if (!MapUtil.isEmpty(features)) {
			for (final Map.Entry<String, Boolean> entry : features.entrySet()) {
				try {
					factory.setFeature(entry.getKey(), entry.getValue());
				} catch (final TransformerConfigurationException e) {
					throw new XsltServiceException(e);
				}
			}
		}

		if (uriResolverClass != null) {
			final URIResolver uriResolver = ClassUtil.instantiateClass(uriResolverClass);
			if (uriResolver != null) {
				factory.setURIResolver(uriResolver);
			}
		}
		return factory;
	}
}
