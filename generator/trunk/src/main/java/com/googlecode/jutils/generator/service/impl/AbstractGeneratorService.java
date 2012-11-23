package com.googlecode.jutils.generator.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.generator.config.GeneratorConfig;
import com.googlecode.jutils.generator.exception.GeneratorServiceException;
import com.googlecode.jutils.generator.formatter.Formatter;
import com.googlecode.jutils.generator.service.GeneratorService;
import com.googlecode.jutils.io.IoUtil;
import com.googlecode.jutils.templater.exception.TemplaterServiceException;
import com.googlecode.jutils.templater.service.TemplaterService;

import freemarker.ext.dom.NodeModel;

public abstract class AbstractGeneratorService implements GeneratorService {
	protected static final Logger LOGGER = Logger.getLogger(AbstractGeneratorService.class);
	protected GeneratorConfig config;
	protected Map<String, Formatter> formatters;

	@Autowired
	protected TemplaterService templaterService;

	public AbstractGeneratorService() {
		super();
		this.formatters = new HashMap<String, Formatter>();
	}

	public GeneratorConfig getConfig() {
		return config;
	}

	public void setConfig(GeneratorConfig config) {
		this.config = config;
	}

	public Map<String, Formatter> getFormatters() {
		return formatters;
	}

	public void setFormatters(Map<String, Formatter> formatters) {
		this.formatters = formatters;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void generate(String xmlContent) throws GeneratorServiceException {
		if (!StringUtil.isBlank(xmlContent)) {
			final Document xmlDocument = getXmlDocument(xmlContent);
			if (xmlDocument != null) {
				final NodeModel xmlModel = getModel(xmlContent);
				if (xmlModel != null) {
					generate(xmlDocument, xmlModel);
				}
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void generate(File xmlFile) throws GeneratorServiceException {
		if (xmlFile != null) {
			Reader reader = null;
			try {
				reader = new BufferedReader(new FileReader(xmlFile));
				generate(reader);
			} catch (final FileNotFoundException e) {
				throw new GeneratorServiceException(e);
			} catch (final Exception e) {
				throw new GeneratorServiceException(e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException e) {
						throw new GeneratorServiceException(e);
					}
				}
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void generate(InputStream inputStream) throws GeneratorServiceException {
		if (inputStream != null) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(inputStream);
				generate(reader);
			} catch (final Exception e) {
				throw new GeneratorServiceException(e);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void generate(Reader reader) throws GeneratorServiceException {
		if (reader != null) {
			String xmlContent = null;
			try {
				xmlContent = IoUtil.toString(reader);
			} catch (final IOException e) {
				throw new GeneratorServiceException(e);
			}

			generate(xmlContent);
		}
	}

	protected abstract void generate(Document xmlDocument, NodeModel model) throws GeneratorServiceException;

	protected abstract String getPathToElement(String fileType, Node node);

	protected abstract String getOutputFileName(String fileType, Node node);

	protected File getOutputDirectory(String fileType, Node node) {
		File outputDirectory = null;
		if (!StringUtil.isBlank(fileType)) {
			if (config.hasOutputDirectory(fileType)) {
				outputDirectory = new File(config.getBaseOutputDirectory(), config.getOutputDirectory(fileType));
			} else {
				outputDirectory = config.getBaseOutputDirectory();
			}

			final String pathToElement = getPathToElement(fileType, node);
			if (!StringUtil.isBlank(pathToElement)) {
				outputDirectory = new File(outputDirectory, pathToElement);
			}
		}
		return outputDirectory;
	}

	protected File getOutputFile(String fileType, Node node) {
		File outputFile = null;
		if (!StringUtil.isBlank(fileType) && node != null) {
			final File outputDirectory = getOutputDirectory(fileType, node);
			final String outputFileName = getOutputFileName(fileType, node);
			if (outputDirectory != null && !StringUtil.isBlank(outputFileName)) {
				outputFile = new File(outputDirectory, outputFileName);
			}
		}
		return outputFile;
	}

	protected void generate(String fileType, Node node, String templateName, Map<String, Object> data, NodeModel model) throws GeneratorServiceException {
		if (!StringUtil.isBlank(fileType) && node != null && !StringUtil.isBlank(templateName) && !MapUtil.isEmpty(data) && model != null) {
			File file = null;
			final File outputDirectory = getOutputDirectory(fileType, node);
			final String outputFileName = getOutputFileName(fileType, node);

			if (outputDirectory != null && !StringUtil.isBlank(outputFileName)) {
				data.put("outputDirectory", outputDirectory);
				data.put("outputFileName", outputFileName);
				file = new File(outputDirectory, outputFileName);

				if (file != null) {
					final String content = getContent(templateName, data, model);
					if (!StringUtil.isBlank(content)) {
						try {
							writeToFile(file, content);
						} catch (final FileNotFoundException e) {
							throw new GeneratorServiceException(e);
						} catch (final IOException e) {
							throw new GeneratorServiceException(e);
						}
					}
				}
			}
		}
	}

	private Document getXmlDocument(String xmlContent) throws GeneratorServiceException {
		Document document = null;
		if (!StringUtil.isBlank(xmlContent)) {
			final StringReader reader = new StringReader(xmlContent);
			final SAXReader saxReader = new SAXReader();
			try {
				document = saxReader.read(reader);
			} catch (final DocumentException e) {
				throw new GeneratorServiceException(e);
			} finally {
				reader.close();
			}
		}
		return document;
	}

	private NodeModel getModel(String xmlContent) throws GeneratorServiceException {
		NodeModel xmlModel = null;
		if (!StringUtil.isBlank(xmlContent)) {
			final StringReader reader = new StringReader(xmlContent);
			try {
				NodeModel.useJaxenXPathSupport();
				xmlModel = NodeModel.parse(new InputSource(reader));
			} catch (final SAXException e) {
				throw new GeneratorServiceException(e);
			} catch (final IOException e) {
				throw new GeneratorServiceException(e);
			} catch (final ParserConfigurationException e) {
				throw new GeneratorServiceException(e);
			} catch (final Exception e) {
				throw new GeneratorServiceException(e);
			} finally {
				reader.close();
			}
		}
		return xmlModel;
	}

	private String getContent(String templateName, Map<String, Object> data, NodeModel model) throws GeneratorServiceException {
		String content = null;
		if (!StringUtil.isBlank(templateName) && data != null && model != null) {
			data.put("xml", model);
			data.put("templateName", templateName);
			data.putAll(config.getData());
			try {
				content = templaterService.getContent(templateName, data);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(content);
				}

			} catch (final TemplaterServiceException e) {
				throw new GeneratorServiceException(e);
			}
		}
		return content;
	}

	private void writeToFile(File file, String content) throws IOException, FileNotFoundException {
		if (file != null && !StringUtil.isBlank(content)) {
			final File parent = file.getParentFile();
			if (!parent.exists()) {
				FileUtils.forceMkdir(parent);
			}

			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(file);
				IoUtil.write(content, outputStream);
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}

			formatFile(file);
		}
	}

	private void formatFile(File file) throws IOException {
		if (file != null) {
			final String extension = FilenameUtils.getExtension(file.getName());
			if (!MapUtil.isEmpty(formatters)) {
				final Formatter formatter = formatters.get(extension);
				if (formatter != null) {
					formatter.format(file);
				}
			}
		}
	}

}
