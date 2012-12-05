package com.googlecode.jutils.generator.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.generator.config.GeneratorConfig;
import com.googlecode.jutils.generator.exception.GeneratorServiceException;
import com.googlecode.jutils.generator.formatter.Formatter;
import com.googlecode.jutils.io.IoUtil;
import com.googlecode.jutils.templater.exception.TemplaterServiceException;
import com.googlecode.jutils.templater.service.TemplaterService;

import freemarker.ext.dom.NodeModel;

public abstract class AbstractGenerator implements Generator {
	protected static final Logger LOGGER = Logger.getLogger(AbstractGenerator.class);

	protected GeneratorConfig config;

	@Autowired
	protected TemplaterService templaterService;

	public AbstractGenerator() {
		super();
	}

	public GeneratorConfig getConfig() {
		return config;
	}

	public void setConfig(GeneratorConfig config) {
		this.config = config;
	}

	@Override
	public void generate(String xmlContent) throws GeneratorServiceException {
		if (!StringUtil.isBlank(xmlContent)) {
			try {
				validate(xmlContent);
			} catch (final SAXException e) {
				throw new GeneratorServiceException(e);
			} catch (final IOException e) {
				throw new GeneratorServiceException(e);
			}

			final Document xmlDocument = getXmlDocument(xmlContent);
			if (xmlDocument != null) {
				final NodeModel xmlModel = getModel(xmlContent);
				if (xmlModel != null) {
					generate(xmlDocument, xmlModel);
				}
			}
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

	private String getContent(String templateName, Map<String, Object> data, NodeModel model) throws GeneratorServiceException {
		String content = null;
		if (!StringUtil.isBlank(templateName) && data != null && model != null) {
			data.put("xml", model);
			data.put("templateName", templateName);
			data.putAll(config.getData());
			try {
				content = templaterService.getContent(templateName, data);
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
		if (file != null && config != null) {
			final String extension = FilenameUtils.getExtension(file.getName());
			if (!MapUtil.isEmpty(config.getFormatters())) {
				final Formatter formatter = config.getFormatter(extension);
				if (formatter != null) {
					formatter.format(file);
				}
			}
		}
	}

	private void validate(String xmlContent) throws SAXException, IOException {
		if (!StringUtil.isBlank(xmlContent) && config != null && !CollectionUtil.isEmpty(config.getSchemas())) {
			final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			final List<StreamSource> sources = new ArrayList<StreamSource>();
			for (final Resource schema : config.getSchemas()) {
				sources.add(new StreamSource(schema.getFile()));
			}
			final Schema xmlSchema = factory.newSchema(sources.toArray(new StreamSource[0]));

			final Validator validator = xmlSchema.newValidator();
			final StringReader reader = new StringReader(xmlContent);
			try {
				validator.validate(new StreamSource(reader));
			} finally {
				reader.close();
			}
		}
	}

	private Document getXmlDocument(String xmlContent) throws GeneratorServiceException {
		Document document = null;
		if (!StringUtil.isBlank(xmlContent) && config != null) {
			final Map<String, String> namespaceUris = config.getNamespaceUris();
			if (!MapUtil.isEmpty(namespaceUris)) {
				final DocumentFactory factory = new DocumentFactory();
				factory.setXPathNamespaceURIs(namespaceUris);
			}

			final StringReader reader = new StringReader(xmlContent);
			SAXReader saxReader = null;
			try {
				saxReader = new SAXReader();
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
}