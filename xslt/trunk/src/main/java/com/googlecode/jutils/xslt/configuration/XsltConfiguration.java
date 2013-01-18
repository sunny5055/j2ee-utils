package com.googlecode.jutils.xslt.configuration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.core.ClassUtil;
import com.googlecode.jutils.spring.ResourceUtil;
import com.googlecode.jutils.xslt.exception.XsltServiceException;

/**
 * The Class XsltConfiguration.
 */
public class XsltConfiguration {
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
		this.attributes = new HashMap<String, Object>();
		this.features = new HashMap<String, Boolean>();
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
	 * Gets the xml content.
	 * 
	 * @param xmlFileName
	 *            the xml file name
	 * @return the xml content
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String getXmlContent(String xmlFileName) throws IOException {
		String xmlContent = null;
		if (!StringUtil.isBlank(xmlFileName)) {
			String fullXmlName = null;
			if (!StringUtil.startsWith(xmlFileName, "classpath")) {
				fullXmlName = xmlLoaderPath + "/" + xmlFileName;
			} else {
				fullXmlName = xmlFileName;
			}
			xmlContent = ResourceUtil.getContent(fullXmlName);
		}
		return xmlContent;
	}

	/**
	 * Gets the xsl content.
	 * 
	 * @param xmlFileName
	 *            the xml file name
	 * @return the xsl content
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String getXslContent(String xmlFileName) throws IOException {
		String xmlContent = null;
		if (!StringUtil.isBlank(xmlFileName)) {
			String fullXmlName = null;
			if (!StringUtil.startsWith(xmlFileName, "classpath")) {
				fullXmlName = xslLoaderPath + "/" + xmlFileName;
			} else {
				fullXmlName = xmlFileName;
			}
			xmlContent = ResourceUtil.getContent(fullXmlName);
		}
		return xmlContent;
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
			URIResolver uriResolver = null;
			try {
				uriResolver = ClassUtil.instantiateClass(uriResolverClass);
			} catch (final IllegalArgumentException e) {
				throw new XsltServiceException(e);
			} catch (final InstantiationException e) {
				throw new XsltServiceException(e);
			} catch (final IllegalAccessException e) {
				throw new XsltServiceException(e);
			} catch (final InvocationTargetException e) {
				throw new XsltServiceException(e);
			}
			if (uriResolver != null) {
				factory.setURIResolver(uriResolver);
			}
		}
		return factory;
	}
}
