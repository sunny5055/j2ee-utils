package com.googlecode.jutils.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Namespace;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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

	public static Map<String, String> getDeclaredNamespaces(File xmlFile) throws DocumentException, IOException {
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

	public static boolean hasXmlDeclaration(String xmlContent) {
		boolean declaration = false;
		if (!StringUtil.isBlank(xmlContent)) {
			declaration = xmlContent.startsWith("<?xml");
		}
		return declaration;
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

	public static Document getXmlDocument(File xmlFile) throws DocumentException, IOException {
		Document document = null;
		if (xmlFile != null) {
			final String xmlContent = FileUtils.readFileToString(xmlFile);
			document = getXmlDocument(xmlContent);
		}
		return document;
	}

	public static String formatDocument(String xmlContent) throws ParserConfigurationException, SAXException, IOException {
		String formattedXml = null;
		if (!StringUtil.isBlank(xmlContent)) {
			formattedXml = formatDocument(xmlContent, !XmlUtil.hasXmlDeclaration(xmlContent));
		}
		return formattedXml;
	}

	public static String formatDocument(String xmlContent, boolean suppressDeclaration) throws ParserConfigurationException, SAXException, IOException {
		String formattedXml = null;
		if (!StringUtil.isBlank(xmlContent)) {
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

			final SAXParser parser = factory.newSAXParser();
			final XmlHandler handler = new XmlHandler();
			if (suppressDeclaration) {
				handler.setSuppressDeclaration(true);
			}

			parser.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
			parser.parse(new InputSource(new StringReader(xmlContent)), handler);

			formattedXml = handler.toString();
		}
		return formattedXml;
	}

	public static String formatDocument(File xmlFile) throws IOException, ParserConfigurationException, SAXException {
		String formattedXml = null;
		if (xmlFile != null) {
			final String xmlContent = FileUtils.readFileToString(xmlFile);
			formattedXml = formatDocument(xmlContent);
		}
		return formattedXml;
	}

	public static String formatDocument(File xmlFile, boolean suppressDeclaration) throws IOException, ParserConfigurationException, SAXException {
		String formattedXml = null;
		if (xmlFile != null) {
			final String xmlContent = FileUtils.readFileToString(xmlFile);
			formattedXml = formatDocument(xmlContent, suppressDeclaration);
		}
		return formattedXml;
	}
}
