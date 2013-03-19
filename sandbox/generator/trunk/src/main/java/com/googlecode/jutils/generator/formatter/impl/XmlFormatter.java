package com.googlecode.jutils.generator.formatter.impl;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import com.googlecode.jutils.generator.formatter.exception.FormatterException;
import com.googlecode.jutils.xml.XmlUtil;

public class XmlFormatter extends AbstractFormatter {

	public XmlFormatter() {
		super();
	}

	@Override
	public void format(File file) throws FormatterException {
		if (accept(file)) {
			try {
				final String document = XmlUtil.formatDocument(file);
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
}
