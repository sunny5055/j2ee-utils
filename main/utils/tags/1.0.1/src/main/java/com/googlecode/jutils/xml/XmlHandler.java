package com.googlecode.jutils.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import com.googlecode.jutils.StringUtil;

public class XmlHandler extends DefaultHandler2 {
	private StringBuilder buffer;
	private boolean suppressDeclaration;
	private boolean newLineAfterDeclaration;
	private String indentString;
	private boolean expandEmptyElements;
	private String lineSeparator;
	private char attributeQuoteChar;
	private String previousElement;
	private String currentElement;
	private Integer indentLevel;
	private boolean emptyElement;
	private Integer lineWidth;
	private boolean keepComments;

	public XmlHandler() {
		super();
		buffer = new StringBuilder();

		this.indentString = "\t";
		this.lineSeparator = "\n";
		this.attributeQuoteChar = '\"';
		this.indentLevel = 0;
		this.lineWidth = 120;
		this.keepComments = true;
	}

	public boolean isSuppressDeclaration() {
		return suppressDeclaration;
	}

	public void setSuppressDeclaration(boolean suppressDeclaration) {
		this.suppressDeclaration = suppressDeclaration;
	}

	public boolean isNewLineAfterDeclaration() {
		return newLineAfterDeclaration;
	}

	public void setNewLineAfterDeclaration(boolean newLineAfterDeclaration) {
		this.newLineAfterDeclaration = newLineAfterDeclaration;
	}

	public String getIndentString() {
		return indentString;
	}

	public void setIndentString(String indentString) {
		this.indentString = indentString;
	}

	public boolean isExpandEmptyElements() {
		return expandEmptyElements;
	}

	public void setExpandEmptyElements(boolean expandEmptyElements) {
		this.expandEmptyElements = expandEmptyElements;
	}

	public String getLineSeparator() {
		return lineSeparator;
	}

	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}

	public char getAttributeQuoteChar() {
		return attributeQuoteChar;
	}

	public void setAttributeQuoteChar(char attributeQuoteChar) {
		this.attributeQuoteChar = attributeQuoteChar;
	}

	public Integer getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(Integer lineWidth) {
		this.lineWidth = lineWidth;
	}

	public boolean isKeepComments() {
		return keepComments;
	}

	public void setKeepComments(boolean keepComments) {
		this.keepComments = keepComments;
	}

	@Override
	public void startDocument() throws SAXException {
		if (!suppressDeclaration) {
			writeDeclaration();
			if (newLineAfterDeclaration) {
				buffer.append("\n");
			}
		}
	}

	@Override
	public void startDTD(String name, String publicId, String systemId) throws SAXException {
		buffer.append("<!DOCTYPE " + name);
		if (!StringUtil.isBlank(publicId) && !StringUtil.isBlank(systemId)) {
			buffer.append(" PUBLIC \"" + publicId + "\" \"" + systemId + "\"");
		}
		buffer.append(">\n");
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (!StringUtil.isBlank(currentElement) && emptyElement) {
			buffer.append(">");
		}
		if (buffer.length() > 0 && !StringUtil.endsWith(buffer, "\n")) {
			buffer.append("\n");
		}

		previousElement = currentElement;
		currentElement = qName;
		emptyElement = true;

		indent();
		buffer.append("<" + qName);

		attributes(qName, attributes);

		indentLevel++;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		indentLevel--;

		if (StringUtil.equals(qName, currentElement)) {
			if (emptyElement) {
				if (!expandEmptyElements) {
					buffer.append(" />");
				} else {
					buffer.append("></" + qName + ">");
				}
			} else {
				buffer.append("</" + qName + ">");
			}
		} else if (!StringUtil.isBlank(previousElement)) {
			if (!StringUtil.endsWith(buffer, "\n")) {
				buffer.append("\n");
			}
			indent();
			buffer.append("</" + qName + ">");
		}

		previousElement = qName;
		currentElement = null;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String value = new String(ch, start, length);
		value = StringUtil.remove(value, "\t");

		if (!StringUtil.isBlank(currentElement)) {
			buffer.append(">");
			emptyElement = false;
		}

		if (value.matches("[\n]+") || !StringUtil.isBlank(value)) {
			buffer.append(value);
		}
	}

	@Override
	public void comment(char[] ch, int start, int length) throws SAXException {
		if (keepComments) {
			final String value = new String(ch, start, length);
			indent();
			buffer.append("<!-- " + value + " -->");
		}
	}

	@Override
	public String toString() {
		return buffer.toString();
	}

	protected void indent() {
		if (indentString != null) {
			for (int i = 0; i < indentLevel; i++) {
				buffer.append(indentString);
			}
		}
	}

	protected void attributes(String elementName, Attributes attributes) {
		if (!StringUtil.isBlank(elementName) && attributes != null) {
			int currentLineWidth = elementName.length() + 1;
			String line = "";
			for (int i = 0; i < attributes.getLength(); i++) {
				final String attributeName = attributes.getQName(i);
				final String attributeValue = StringUtil.trimToEmpty(attributes.getValue(i));

				final String attribute = attributeName + "=" + attributeQuoteChar + attributeValue + attributeQuoteChar;
				if (currentLineWidth + attribute.length() > lineWidth) {
					buffer.append(line + "\n");
					line = indentString + attribute;
					currentLineWidth = 0;
				} else {
					line += " ";
					line += attribute;
				}

				if (i + 1 >= attributes.getLength()) {
					buffer.append(line);
				}

				currentLineWidth += getLineWidth(line);
			}
		}
	}

	protected Integer getLineWidth(String line) {
		Integer lineWidth = 0;
		if (!StringUtil.isBlank(line)) {
			final String tmp = StringUtil.replace(line, "\t", "    ");
			lineWidth = tmp.length();
		}
		return lineWidth;
	}

	protected void writeDeclaration() {
		buffer.append("<?xml version=" + attributeQuoteChar + "1.0" + attributeQuoteChar + " encoding=" + attributeQuoteChar + "UTF-8" + attributeQuoteChar + "?>\n");
	}
}
