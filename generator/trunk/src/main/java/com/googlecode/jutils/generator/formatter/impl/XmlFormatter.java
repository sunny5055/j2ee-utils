package com.googlecode.jutils.generator.formatter.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.generator.formatter.exception.FormatterException;

public class XmlFormatter extends AbstractFormatter {

	public XmlFormatter() {
		super();
	}

	@Override
	public void format(File file) throws FormatterException {
		if (accept(file)) {
			final OutputFormat format = getOutputFormat(file);

			Writer writer = null;

			try {
				final Document document = getXmlDocument(file);
				writer = new FileWriter(file);

				final XMLWriter xmlWriter = new XMLWriter(writer, format);
				xmlWriter.write(document);
			} catch (final SAXException e) {
				throw new FormatterException(e.getMessage());
			} catch (final DocumentException e) {
				throw new FormatterException(e.getMessage());
			} catch (final IOException e) {
				throw new FormatterException(e.getMessage());
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (final IOException e) {
						throw new FormatterException(e.getMessage());
					}
				}
			}
		}
	}

	@Override
	protected String[] getExtensions() {
		return new String[] { "xml", "htm", "html", "xhtml", "xsl" };
	}

	private OutputFormat getOutputFormat(File file) {
		final OutputFormat format = OutputFormat.createPrettyPrint();
		format.setIndent("\t");
		if (file != null) {
			final boolean declaration = hasDeclaration(file);

			if (!declaration) {
				format.setSuppressDeclaration(true);
			}
		}
		return format;
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

	private Document getXmlDocument(File xmlFile) throws DocumentException, SAXException {
		Document document = null;
		if (xmlFile != null) {
			final SAXReader saxReader = new SAXReader();
			saxReader.setValidation(false);
			saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			document = saxReader.read(xmlFile);
		}
		return document;
	}
}
