package com.googlecode.jutils.generator.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.generator.formatter.Formatter;

public class GeneratorConfig {
	private File baseOutputDirectory;
	private Map<String, String> properties;
	private List<Formatter> formatters;
	private List<Resource> schemas;

	public GeneratorConfig() {
		super();
		this.properties = new HashMap<String, String>();
		this.formatters = new ArrayList<Formatter>();
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

	public List<Formatter> getFormatters() {
		return formatters;
	}

	public void setFormatters(List<Formatter> formatters) {
		this.formatters = formatters;
	}

	public Formatter getFormatter(String key) {
		Formatter formatter = null;
		if (!StringUtil.isBlank(key)) {
			for (final Formatter f : formatters) {
				if (f.accept(key)) {
					formatter = f;
					break;
				}
			}
		}
		return formatter;
	}

	public boolean hasFormatter(String key) {
		boolean formatter = false;
		if (!CollectionUtil.isEmpty(formatters) && !StringUtil.isBlank(key)) {
			formatter = getFormatter(key) != null;
		}
		return formatter;
	}

	public void addFormatter(Formatter formatter) {
		if (formatter != null) {
			formatters.add(formatter);
		}
	}

	public List<Resource> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<Resource> schemas) {
		this.schemas = schemas;
	}
}
