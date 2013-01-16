package com.googlecode.jutils.generator.formatter.impl;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.generator.formatter.exception.FormatterException;
import com.googlecode.jutils.generator.util.XmlHandler;

public class XmlFormatter extends AbstractFormatter {

	public XmlFormatter() {
		super();
	}

	@Override
	public void format(File file) throws FormatterException {
		if (accept(file)) {
			try {
				final String document = getXmlDocument(file);
				FileUtils.writeStringToFile(file, document);
			} catch (final SAXException e) {
				throw new FormatterException(e.getMessage());
			} catch (final ParserConfigurationException e) {
				throw new FormatterException(e.getMessage());
			} catch (final IOException e) {
				throw new FormatterException(e.getMessage());
			}
		}
	}

	@Override
	protected String[] getExtensions() {
		return new String[] { "xml", "htm", "html", "xhtml", "xsl" };
	}

	private boolean hasDeclaration(File file) {
		boolean declaration = false;
		if (file != null) {
			try {
				final String content = FileUtils.readFileToString(file);
				if (!StringUtil.isBlank(content)) {
					declaration = StringUtil.contains(content, "<?");
				}
			} catch (final IOException e) {
				declaration = false;
			}
		}
		return declaration;
	}

	private String getXmlDocument(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		String document = null;
		if (xmlFile != null) {
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

			final SAXParser parser = factory.newSAXParser();
			final DefaultHandler2 handler = getHandler(xmlFile);
			parser.setProperty("http://xml.org/sax/properties/lexical-handler", handler);

			parser.parse(xmlFile, handler);

			document = handler.toString();
		}
		return document;
	}

	private DefaultHandler2 getHandler(File file) {
		final XmlHandler handler = new XmlHandler();
		if (handler != null) {
			final boolean declaration = hasDeclaration(file);

			if (!declaration) {
				handler.setSuppressDeclaration(true);
			}
		}
		return handler;
	}
}
