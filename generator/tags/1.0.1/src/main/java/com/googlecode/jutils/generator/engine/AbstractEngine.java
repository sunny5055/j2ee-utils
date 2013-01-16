package com.googlecode.jutils.generator.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.generator.config.GeneratorConfig;
import com.googlecode.jutils.generator.exception.GeneratorServiceException;
import com.googlecode.jutils.generator.formatter.Formatter;
import com.googlecode.jutils.generator.formatter.exception.FormatterException;
import com.googlecode.jutils.generator.formatter.impl.XmlFormatter;
import com.googlecode.jutils.generator.util.PropertyUtil;
import com.googlecode.jutils.generator.util.XmlUtil;
import com.googlecode.jutils.io.IoUtil;
import com.googlecode.jutils.spring.ResourceUtil;
import com.googlecode.jutils.templater.exception.TemplaterServiceException;
import com.googlecode.jutils.templater.service.TemplaterService;

import freemarker.ext.dom.NodeModel;

public abstract class AbstractEngine implements Engine {
	protected static final Logger LOGGER = Logger.getLogger(AbstractEngine.class);
	protected static final String FILE_PATH = "file_path";
	protected static final String FILE_NAME_PATTERN = "file_name_pattern";

	protected GeneratorConfig config;
	protected Map<String, String> defaultProperties;
	protected List<Formatter> defaultFormatters;

	@Autowired
	protected TemplaterService templaterService;

	public AbstractEngine() {
		super();
		this.defaultProperties = new HashMap<String, String>();
		this.defaultFormatters = new ArrayList<Formatter>();
	}

	@PostConstruct
	protected void init() {
		this.defaultFormatters.add(new XmlFormatter());
	}

	public GeneratorConfig getConfig() {
		return config;
	}

	public void setConfig(GeneratorConfig config) {
		this.config = config;
	}

	@Override
	public String printHelp() {
		final StringBuilder buffer = new StringBuilder();
		if (!MapUtil.isEmpty(defaultProperties)) {
			for (final Map.Entry<String, String> entry : defaultProperties.entrySet()) {
				final String value = PropertyUtil.resolve(defaultProperties, entry.getKey());
				buffer.append(entry.getKey() + " = ");
				if (StringUtil.equalsIgnoreCase(entry.getValue(), value)) {
					buffer.append(entry.getValue() + "\n");
				} else {
					buffer.append(entry.getValue() + " [" + value + "]\n");
				}
			}
		}
		return buffer.toString();
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

	protected String resolveKey(String key) {
		String value = null;
		if (!StringUtil.isBlank(key)) {
			final Map<String, String> properties = new HashMap<String, String>();
			properties.putAll(defaultProperties);
			properties.putAll(config.getProperties());

			value = PropertyUtil.resolve(properties, key);
		}
		return value;
	}

	protected abstract String getEngineKey();

	protected abstract void generate(Document xmlDocument, NodeModel model) throws GeneratorServiceException;

	protected abstract String getPathToElement(String key, Node node);

	protected abstract String getOutputFileName(String key, Node node) throws GeneratorServiceException;

	protected String getOutputFileName(String key, Object... values) throws GeneratorServiceException {
		String outputFileName = null;
		if (!StringUtil.isBlank(key)) {
			final String fileNamePattern = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + key);
			if (!StringUtil.isBlank(fileNamePattern) && !ArrayUtil.isEmpty(values)) {
				outputFileName = String.format(fileNamePattern, values);
			} else {
				outputFileName = fileNamePattern;
			}
		}
		return outputFileName;
	}

	protected File getOutputDirectory(String key, Node node) {
		File outputDirectory = null;
		if (!StringUtil.isBlank(key)) {
			final String pathKey = getEngineKey() + "." + FILE_PATH + "." + key;
			final String value = resolveKey(pathKey);
			if (!StringUtil.isBlank(value)) {
				outputDirectory = new File(config.getBaseOutputDirectory(), value);
			} else {
				outputDirectory = config.getBaseOutputDirectory();
			}

			final String pathToElement = getPathToElement(key, node);
			if (!StringUtil.isBlank(pathToElement)) {
				outputDirectory = new File(outputDirectory, pathToElement);
			}
		}
		return outputDirectory;
	}

	protected File getOutputFile(String key, Node node) throws GeneratorServiceException {
		File outputFile = null;
		if (!StringUtil.isBlank(key)) {
			final File outputDirectory = getOutputDirectory(key, node);
			final String outputFileName = getOutputFileName(key, node);
			if (outputDirectory != null && !StringUtil.isBlank(outputFileName)) {
				outputFile = new File(outputDirectory, outputFileName);
			}
		}
		return outputFile;
	}

	protected void generate(File outputFile, String templateName, Map<String, Object> data, NodeModel model) throws GeneratorServiceException {
		if (outputFile != null && !StringUtil.isBlank(templateName) && data != null && model != null) {
			final String content = getContent(templateName, data, model);
			if (!StringUtil.isBlank(content)) {
				try {
					writeToFile(outputFile, content);
				} catch (final FileNotFoundException e) {
					throw new GeneratorServiceException(e);
				} catch (final IOException e) {
					throw new GeneratorServiceException(e);
				} catch (final FormatterException e) {
					throw new GeneratorServiceException(e);
				}
			}
		}
	}

	protected void addFreemarkerExt(Map<String, Object> data) {
		if (data != null) {
		}
	}

	private String getContent(String templateName, Map<String, Object> data, NodeModel model) throws GeneratorServiceException {
		String content = null;
		if (!StringUtil.isBlank(templateName) && data != null && model != null) {
			data.put("xml", model);
			data.put("templateName", templateName);
			data.putAll(config.getData());

			addFreemarkerExt(data);

			try {
				content = templaterService.getContent(templateName, data);
			} catch (final TemplaterServiceException e) {
				throw new GeneratorServiceException(e);
			}
		}
		return content;
	}

	private void writeToFile(File file, String content) throws IOException, FileNotFoundException, FormatterException {
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

	private void formatFile(File file) throws IOException, FormatterException {
		if (file != null) {
			final String extension = FilenameUtils.getExtension(file.getName());
			final Formatter formatter = getFormatter(extension);
			if (formatter != null) {
				formatter.format(file);
			}
		}
	}

	private Formatter getFormatter(String key) {
		Formatter formatter = null;
		if (!StringUtil.isBlank(key)) {
			formatter = config.getFormatter(key);
			if (formatter == null) {
				if (!CollectionUtil.isEmpty(defaultFormatters)) {
					for (final Formatter f : defaultFormatters) {
						if (f.accept(key)) {
							formatter = f;
							break;
						}
					}
				}
			}
		}
		return formatter;
	}

	private void validate(String xmlContent) throws SAXException, IOException {
		if (!StringUtil.isBlank(xmlContent) && config != null && !CollectionUtil.isEmpty(config.getSchemas())) {
			final List<Source> sources = new ArrayList<Source>();
			for (final Resource schema : config.getSchemas()) {
				final String schemaContent = ResourceUtil.getContent(schema);
				if (!StringUtil.isBlank(schemaContent)) {
					sources.add(XmlUtil.getSource(schemaContent));
				}
			}

			XmlUtil.validate(xmlContent, sources);
		}
	}

	private Map<String, String> getNamespaceUris() throws GeneratorServiceException {
		Map<String, String> namespaceUris = null;
		if (config != null && !CollectionUtil.isEmpty(config.getSchemas())) {
			namespaceUris = new HashMap<String, String>();

			for (final Resource schema : config.getSchemas()) {
				Map<String, String> declaredNamespaces = null;
				try {
					final String schemaContent = ResourceUtil.getContent(schema);
					if (!StringUtil.isBlank(schemaContent)) {
						declaredNamespaces = XmlUtil.getDeclaredNamespaces(schemaContent);
					}
				} catch (final DocumentException e) {
					throw new GeneratorServiceException(e);
				} catch (final IOException e) {
					throw new GeneratorServiceException(e);
				}

				if (!MapUtil.isEmpty(declaredNamespaces)) {
					namespaceUris.putAll(declaredNamespaces);
				}
			}
		}
		return namespaceUris;
	}

	private Document getXmlDocument(String xmlContent) throws GeneratorServiceException {
		Document document = null;
		if (!StringUtil.isBlank(xmlContent) && config != null) {
			final Map<String, String> namespaceUris = getNamespaceUris();
			if (!MapUtil.isEmpty(namespaceUris)) {
				final DocumentFactory factory = new DocumentFactory();
				factory.setXPathNamespaceURIs(namespaceUris);
			}

			try {
				document = XmlUtil.getXmlDocument(xmlContent);
			} catch (final DocumentException e) {
				throw new GeneratorServiceException(e);
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
