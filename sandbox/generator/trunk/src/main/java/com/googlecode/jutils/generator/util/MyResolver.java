package com.googlecode.jutils.generator.util;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.spring.ResourceUtil;

public class MyResolver implements LSResourceResolver {
	protected static final Logger LOGGER = LoggerFactory.getLogger(MyResolver.class);
	private List<Resource> resources;

	public MyResolver(List<Resource> resources) {
		this.resources = resources;
	}

	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		LSInput result = null;
		final DOMImplementationRegistry registry = getRegistry();
		if (registry != null) {
			final DOMImplementationLS domImplementationLS = (DOMImplementationLS) registry.getDOMImplementation("LS 3.0");

			result = domImplementationLS.createLSInput();

			final String source = getSource(systemId);
			if (!StringUtil.isBlank(source)) {
				result.setStringData(source);
				result.setSystemId(systemId);
			}
		}

		return result;
	}

	private String getSource(String systemId) {
		String schemaContent = null;
		if (!StringUtil.isBlank(systemId)) {
			Resource resource = null;
			if (!CollectionUtil.isEmpty(resources)) {
				for (final Resource r : resources) {
					if (StringUtil.equals(r.getFilename(), systemId)) {
						resource = r;
						break;
					}
				}
			}

			if (resource == null) {
				try {
					resource = ResourceUtil.getResource("classpath:" + systemId);
				} catch (final IOException e) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(e.getMessage(), e);
					}
				}
			}

			if (resource != null) {
				try {
					schemaContent = ResourceUtil.getContent(resource);
				} catch (final IOException e) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(e.getMessage(), e);
					}
				}
			}
		}
		return schemaContent;
	}

	private DOMImplementationRegistry getRegistry() {
		DOMImplementationRegistry registry = null;
		try {
			registry = DOMImplementationRegistry.newInstance();
		} catch (final ClassCastException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(e.getMessage(), e);
			}
		} catch (final ClassNotFoundException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(e.getMessage(), e);
			}
		} catch (final InstantiationException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(e.getMessage(), e);
			}
		} catch (final IllegalAccessException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(e.getMessage(), e);
			}
		}
		return registry;
	}

}
