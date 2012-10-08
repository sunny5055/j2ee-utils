package com.googlecode.jutils.generator.exporter;

import java.io.File;
import java.util.Map;
import java.util.Properties;

public interface Exporter {
	String getTemplateName();

	Map<String, Object> getData();

	File getOutputDirectory();

	void setOutputDirectory(File file);

	String getOutputFileName();

	Properties getProperties();

	void setProperties(Properties properties);

}
