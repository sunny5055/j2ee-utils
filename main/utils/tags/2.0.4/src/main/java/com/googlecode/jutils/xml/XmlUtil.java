package com.googlecode.jutils.xml;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;

public class XmlUtil {

	public static Map<String, String> getDeclaredNamespaces(String xmlContent) throws DocumentException {
		Map<String, String> namespaceUris = null;

		if (!StringUtil.isBlank(xmlContent)) {
			final Document document = XmlUtil.getXmlDocument(xmlContent);
			namespaceUris = getDeclaredNamespaces(document);
		}
		return namespaceUris;
	}

	public static Map<String, String> getDeclaredNamespaces(File xmlFile) throws DocumentException {
		Map<String, String> namespaceUris = null;

		if (xmlFile != null) {
			final Document document = XmlUtil.getXmlDocument(xmlFile);
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
}
