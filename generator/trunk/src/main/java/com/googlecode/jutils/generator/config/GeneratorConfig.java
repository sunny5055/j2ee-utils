package com.googlecode.jutils.generator.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.generator.formatter.Formatter;

public class GeneratorConfig {
	private File baseOutputDirectory;
	private Map<String, String> properties;
	private Map<String, Formatter> formatters;
	private List<Resource> schemas;

	public GeneratorConfig() {
		super();
		this.properties = new HashMap<String, String>();
		this.formatters = new HashMap<String, Formatter>();
		this.schemas = new ArrayList<Resource>();
	}

	public File getBaseOutputDirectory() {
		return baseOutputDirectory;
	}

	public void setBaseOutputDirectory(File baseOutputDirectory) {
		this.baseOutputDirectory = baseOutputDirectory;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getProperty(String key) {
		String value = null;
		if (!StringUtil.isBlank(key)) {
			value = properties.get(key);
		}
		return value;
	}

	public boolean hasProperty(String key) {
		boolean property = false;
		if (!MapUtil.isEmpty(properties) && !StringUtil.isBlank(key)) {
			property = properties.containsKey(key);
		}
		return property;
	}

	public void addProperty(String key, String value) {
		if (!StringUtil.isBlank(key) && value != null) {
			properties.put(key, value);
		}
	}

	public Map<String, Object> getData() {
		final Map<String, Object> data = new HashMap<String, Object>();
		if (baseOutputDirectory != null) {
			data.put("baseOutputDirectory", baseOutputDirectory);
		}

		if (!MapUtil.isEmpty(properties)) {
			data.put("properties", properties);
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

	public boolean hasFormatter(String key) {
		boolean formatter = false;
		if (!MapUtil.isEmpty(formatters) && !StringUtil.isBlank(key)) {
			formatter = formatters.containsKey(key);
		}
		return formatter;
	}

	public void addFormatter(String key, Formatter formatter) {
		if (!StringUtil.isBlank(key) && formatter != null) {
			formatters.put(key, formatter);
		}
	}

	public List<Resource> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<Resource> schemas) {
		this.schemas = schemas;
	}
}
