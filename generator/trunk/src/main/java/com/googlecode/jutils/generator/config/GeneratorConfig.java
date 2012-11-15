package com.googlecode.jutils.generator.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.googlecode.jutils.StringUtil;

public class GeneratorConfig {
	private File baseOutputDirectory;
	private String fileNamePattern;
	private Properties properties;

	public GeneratorConfig() {
		super();
		this.properties = new Properties();
	}

	public File getBaseOutputDirectory() {
		return baseOutputDirectory;
	}

	public void setBaseOutputDirectory(File baseOutputDirectory) {
		this.baseOutputDirectory = baseOutputDirectory;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}

	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void addProperty(String key, Object value) {
		if (!StringUtil.isBlank(key) && value != null) {
			this.properties.put(key, value);
		}
	}

	public Map<String, Object> getData() {
		final Map<String, Object> data = new HashMap<String, Object>();
		if (baseOutputDirectory != null) {
			data.put("baseOutputDirectory", baseOutputDirectory);
		}

		if (!StringUtil.isBlank(fileNamePattern)) {
			data.put("fileNamePattern", fileNamePattern);
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
}
