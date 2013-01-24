package com.googlecode.jutils.generator.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.generator.exception.GeneratorServiceException;
import com.googlecode.jutils.generator.formatter.exception.FormatterException;
import com.googlecode.jutils.xml.XPathUtil;
import com.googlecode.jutils.xml.XmlUtil;
import com.googlecode.jutils.xslt.exception.XsltServiceException;

import freemarker.ext.dom.NodeModel;

public abstract class AbstractGuiEngine extends AbstractJavaEngine {

	@Override
	protected void init() {
		super.init();

		this.defaultProperties.put(getEngineKey() + ".generate.xml", "true");
	}

	@Override
	protected void generate(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final Integer countForm = XPathUtil.getIntegerValue(xmlDocument, "count(//g:form)");
			final Integer countDataTable = XPathUtil.getIntegerValue(xmlDocument, "count(//g:datatable)");
			final String doGenerate = resolveKey(getEngineKey() + ".generate.xml");

			if (countForm == 0 && countDataTable == 0) {
				final String xmlContent = getXmlContent(xmlDocument);
				if (!StringUtil.isBlank(xmlContent)) {
					if (StringUtil.equalsIgnoreCase(doGenerate, "true")) {
						final File file = new File(config.getBaseOutputDirectory(), "gui.xml");
						try {
							writeToFile(file, xmlContent);
						} catch (final FileNotFoundException e) {
							throw new GeneratorServiceException(e);
						} catch (final IOException e) {
							throw new GeneratorServiceException(e);
						} catch (final FormatterException e) {
							throw new GeneratorServiceException(e);
						}
					} else {
						Document document = null;
						try {
							document = XmlUtil.getXmlDocument(xmlContent);
						} catch (final DocumentException e) {
							throw new GeneratorServiceException(e);
						}
						final NodeModel xmlModel = getModel(xmlContent);

						doGenerate(document, xmlModel);
					}
				}
			} else {
				doGenerate(xmlDocument, model);
			}
		}
	}

	protected abstract void doGenerate(Document xmlDocument, NodeModel model);

	private String getXmlContent(Document xmlDocument) throws GeneratorServiceException {
		String xmlContent = null;
		if (xmlDocument != null) {
			if (XPathUtil.getIntegerValue(xmlDocument, "count(//h:entity)") > 0) {
				try {
					xmlContent = xsltService.transform(xmlDocument, "gui.xsl");
				} catch (final XsltServiceException e) {
					throw new GeneratorServiceException(e);
				}
			}
		}
		return xmlContent;
	}
}
