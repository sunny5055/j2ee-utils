package com.googlecode.jutils.generator.exporter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.googlecode.jutils.StringUtil;

public abstract class AbstractExporter implements Exporter {
	protected static final Logger LOGGER = Logger.getLogger(AbstractExporter.class);
	protected String templateName;
	protected File outputDirectory;
	protected String fileNamePattern;
	protected Properties properties;

	public AbstractExporter() {
		super();
		this.properties = new Properties();
	}

	protected abstract void init();

	@Override
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Override
	public File getOutputDirectory() {
		return outputDirectory;
	}

	@Override
	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}

	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Map<String, Object> getData() {
		final Map<String, Object> data = new HashMap<String, Object>();
		if (!StringUtil.isBlank(templateName)) {
			data.put("templateName", templateName);
		}
		if (outputDirectory != null) {
			data.put("outputdir", outputDirectory);
		}

		if (!StringUtil.isBlank(fileNamePattern)) {
			data.put("fileNamePattern", fileNamePattern);
		}

		if (!StringUtil.isBlank(getOutputFileName())) {
			data.put("outputFileName", getOutputFileName());
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
