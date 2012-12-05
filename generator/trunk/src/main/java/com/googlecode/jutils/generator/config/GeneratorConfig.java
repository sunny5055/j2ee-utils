package com.googlecode.jutils.generator.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.Resource;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.generator.formatter.Formatter;

public class GeneratorConfig {
	private File baseOutputDirectory;
	private Map<String, String> outputDirectories;
	private Map<String, String> fileNamePatterns;
	private Properties properties;
	private Map<String, Formatter> formatters;
	private List<Resource> schemas;
	private Map<String, String> namespaceUris;

	public GeneratorConfig() {
		super();
		this.outputDirectories = new HashMap<String, String>();
		this.fileNamePatterns = new HashMap<String, String>();
		this.properties = new Properties();
		this.formatters = new HashMap<String, Formatter>();
		this.schemas = new ArrayList<Resource>();
		this.namespaceUris = new HashMap<String, String>();
	}

	public File getBaseOutputDirectory() {
		return baseOutputDirectory;
	}

	public void setBaseOutputDirectory(File baseOutputDirectory) {
		this.baseOutputDirectory = baseOutputDirectory;
	}

	public Map<String, String> getOutputDirectories() {
		return outputDirectories;
	}

	public void setOutputDirectories(Map<String, String> outputDirectories) {
		this.outputDirectories = outputDirectories;
	}

	public String getOutputDirectory(String key) {
		String outputDirectory = null;
		if (!StringUtil.isBlank(key)) {
			outputDirectory = outputDirectories.get(key);
		}
		return outputDirectory;
	}

	public boolean hasOutputDirectory(String key) {
		boolean outputDirectory = false;
		if (!StringUtil.isBlank(key)) {
			outputDirectory = outputDirectories.containsKey(key);
		}
		return outputDirectory;
	}

	public void addOutputDirectory(String key, String outputDirectory) {
		if (!StringUtil.isBlank(key) && outputDirectory != null) {
			outputDirectories.put(key, outputDirectory);
		}
	}

	public Map<String, String> getFileNamePatterns() {
		return fileNamePatterns;
	}

	public void setFileNamePatterns(Map<String, String> fileNamePatterns) {
		this.fileNamePatterns = fileNamePatterns;
	}

	public String getFileNamePattern(String key) {
		String fileNamePattern = null;
		if (!StringUtil.isBlank(key)) {
			fileNamePattern = fileNamePatterns.get(key);
		}
		return fileNamePattern;
	}

	public boolean hasFileNamePattern(String key) {
		boolean fileNamePattern = false;
		if (!StringUtil.isBlank(key)) {
			fileNamePattern = fileNamePatterns.containsKey(key);
		}
		return fileNamePattern;
	}

	public void addFileNamePattern(String key, String fileNamePattern) {
		if (!StringUtil.isBlank(key) && !StringUtil.isBlank(fileNamePattern)) {
			fileNamePatterns.put(key, fileNamePattern);
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void addProperty(String key, Object value) {
		if (!StringUtil.isBlank(key) && value != null) {
			properties.put(key, value);
		}
	}

	public Map<String, Object> getData() {
		final Map<String, Object> data = new HashMap<String, Object>();
		if (baseOutputDirectory != null) {
			data.put("baseOutputDirectory", baseOutputDirectory);
		}
		if (!MapUtil.isEmpty(outputDirectories)) {
			data.put("outputDirectories", outputDirectories);
		}
		if (!MapUtil.isEmpty(fileNamePatterns)) {
			data.put("fileNamePatterns", fileNamePatterns);
		}

		if (properties != null) {
			for (final Map.Entry<Object, Object> property : properties.entrySet()) {
				final String key = property.getKey().toString();
				final Object value = property.getValue();

				data.put(key, value);
			}
		}

		return data;
	}

	public Map<String, Formatter> getFormatters() {
		return formatters;
	}

	public void setFormatters(Map<String, Formatter> formatters) {
		this.formatters = formatters;
	}

	public Formatter getFormatter(String key) {
		Formatter formatter = null;
		if (!StringUtil.isBlank(key)) {
			formatter = formatters.get(key);
		}
		return formatter;
	}

	public List<Resource> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<Resource> schemas) {
		this.schemas = schemas;
	}

	public Map<String, String> getNamespaceUris() {
		return namespaceUris;
	}

	public void setNamespaceUris(Map<String, String> namespaceUris) {
		this.namespaceUris = namespaceUris;
	}
}
