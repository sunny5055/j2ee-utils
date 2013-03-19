package com.googlecode.jutils.generator.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.spring.ResourceUtil;

public class XmlUtil {

	public static void validate(String xmlContent, List<Resource> resources) throws SAXException, IOException {
		if (!StringUtil.isBlank(xmlContent) && !CollectionUtil.isEmpty(resources)) {
			final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			factory.setResourceResolver(new MyResolver(resources));

			final Source[] schemas = toSource(resources);
			final Schema xmlSchema = factory.newSchema(schemas);

			final Validator validator = xmlSchema.newValidator();
			final StringReader reader = new StringReader(xmlContent);
			try {
				validator.validate(new StreamSource(reader));
			} finally {
				reader.close();
			}
		}
	}

	public static Map<String, String> getDeclaredNamespaces(File xmlFile) throws DocumentException {
		Map<String, String> namespaceUris = null;

		if (xmlFile != null) {
			final Document document = XmlUtil.getXmlDocument(xmlFile);
			namespaceUris = getDeclaredNamespaces(document);
		}
		return namespaceUris;
	}

	public static Map<String, String> getDeclaredNamespaces(String xmlContent) throws DocumentException {
		Map<String, String> namespaceUris = null;

		if (!StringUtil.isBlank(xmlContent)) {
			final Document document = XmlUtil.getXmlDocument(xmlContent);
			namespaceUris = getDeclaredNamespaces(document);
		}
		return namespaceUris;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getDeclaredNamespaces(Document document) {
		Map<String, String> namespaceUris = null;

		if (document != null) {
			namespaceUris = new HashMap<String, String>();

			final List<Namespace> declaredNamespaces = document.getRootElement().declaredNamespaces();
			if (!CollectionUtil.isEmpty(declaredNamespaces)) {
				for (final Namespace namespace : declaredNamespaces) {
					namespaceUris.put(namespace.getPrefix(), namespace.getURI());
				}
			}
		}
		return namespaceUris;
	}

	public static Document getXmlDocument(String xmlContent) throws DocumentException {
		Document document = null;
		if (!StringUtil.isBlank(xmlContent)) {
			final StringReader reader = new StringReader(xmlContent);
			SAXReader saxReader = null;
			try {
				saxReader = new SAXReader();
				document = saxReader.read(reader);
			} finally {
				reader.close();
			}
		}
		return document;
	}

	public static Document getXmlDocument(File xmlFile) throws DocumentException {
		Document document = null;
		if (xmlFile != null) {
			final SAXReader saxReader = new SAXReader();
			document = saxReader.read(xmlFile);
		}
		return document;
	}

	// public static Source getSource(String content) {
	// Source source = null;
	// if (!StringUtil.isBlank(content)) {
	// source = new StreamSource(new StringReader(content));
	// }
	// return source;
	// }

	private static Source[] toSource(List<Resource> resources) throws IOException {
		Source[] result = null;
		if (!CollectionUtil.isEmpty(resources)) {
			final List<Source> sources = new ArrayList<Source>();
			for (final Resource schema : resources) {
				final String schemaContent = ResourceUtil.getContent(schema);
				if (!StringUtil.isBlank(schemaContent)) {
					sources.add(new StreamSource(new StringReader(schemaContent)));
				}
			}

			result = sources.toArray(new Source[0]);
		}
		return result;
	}
}
