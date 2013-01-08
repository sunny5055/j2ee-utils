package com.googlecode.jutils.generator.engine;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.generator.exception.GeneratorServiceException;

import freemarker.ext.dom.NodeModel;

public class JavaBeansEngine extends AbstractJavaEngine {
	private static final String CLASS_KEY = "class";
	private static final String INTERFACE_KEY = "interface";

	@Override
	protected void init() {
		super.init();

		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + CLASS_KEY, "{path.java}");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + INTERFACE_KEY, "{path.java}");

		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + CLASS_KEY, "%1s.java");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + INTERFACE_KEY, "%1s.java");
	}

	@Override
	protected String getEngineKey() {
		return "java_engine";
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void generate(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final List<Node> classes = xmlDocument.selectNodes("//b:class");
			if (!CollectionUtil.isEmpty(classes)) {
				for (final Node node : classes) {
					generateClass(node, model);
				}
			}

			final List<Node> interfaces = xmlDocument.selectNodes("//b:interface");
			if (!CollectionUtil.isEmpty(interfaces)) {
				for (final Node node : interfaces) {
					generateInterface(node, model);
				}
			}
		}
	}

	@Override
	protected String getPathToElement(String key, Node node) {
		String pathToElement = null;
		if (!StringUtil.isBlank(key) && node != null) {
			if (StringUtil.equals(key, CLASS_KEY) || StringUtil.equals(key, INTERFACE_KEY)) {
				final String packageName = node.valueOf("ancestor::p:package/@name");
				if (!StringUtil.isBlank(packageName)) {
					pathToElement = packageName.replace(".", File.separator);
				}
			}
		}
		return pathToElement;
	}

	@Override
	protected String getOutputFileName(String key, Node node) throws GeneratorServiceException {
		String outputFileName = null;
		if (!StringUtil.isBlank(key) && node != null) {
			String className = null;
			if (StringUtil.equals(key, CLASS_KEY) || StringUtil.equals(key, INTERFACE_KEY)) {
				className = node.valueOf("@name");
			}

			outputFileName = getOutputFileName(key, className);
		}
		return outputFileName;
	}

	private void generateClass(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final String className = node.valueOf("@name");
			final String packageName = node.valueOf("ancestor::p:package/@name");

			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("packageName", packageName);
			data.put("className", className);

			final File outputFile = getOutputFile(CLASS_KEY, node);
			generate(outputFile, "java-beans/class.ftl", data, model);
		}
	}

	private void generateInterface(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final String interfaceName = node.valueOf("@name");
			final String packageName = node.valueOf("ancestor::p:package/@name");

			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("packageName", packageName);
			data.put("interfaceName", interfaceName);

			final File outputFile = getOutputFile(INTERFACE_KEY, node);
			generate(outputFile, "java-beans/interface.ftl", data, model);
		}
	}

}
