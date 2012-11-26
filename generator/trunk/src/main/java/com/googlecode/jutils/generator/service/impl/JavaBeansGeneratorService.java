package com.googlecode.jutils.generator.service.impl;

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

public class JavaBeansGeneratorService extends AbstractGeneratorService {
	private static final String DEFAULT_FILE_NAME_PATTERN = "%1s.java";

	private static final String CLASS_TYPE = "class";
	private static final String CLASS_TEMPLATE_FILE = "java-beans/class.ftl";

	private static final String INTERFACE_TYPE = "interface";
	private static final String INTERFACE_TEMPLATE_FILE = "java-beans/interface.ftl";

	@Override
	protected Map<String, String> getNamespaceUris() {
		final Map<String, String> namespaceUris = new HashMap<String, String>();
		namespaceUris.put("p", "http://code.google.com/p/j2ee-utils/schema/java-beans");

		return namespaceUris;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void generate(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final List<Node> classes = xmlDocument.selectNodes("//p:class");
			if (!CollectionUtil.isEmpty(classes)) {
				for (final Node node : classes) {
					generateClass(node, model);
				}
			}

			final List<Node> interfaces = xmlDocument.selectNodes("//p:interfaces/p:interface");
			if (!CollectionUtil.isEmpty(interfaces)) {
				for (final Node node : interfaces) {
					generateInterface(node, model);
				}
			}
		}
	}

	@Override
	protected String getPathToElement(String fileType, Node node) {
		String pathToElement = null;
		if (!StringUtil.isBlank(fileType) && node != null) {
			final String packageName = node.valueOf("ancestor::p:package/@name");
			if (!StringUtil.isBlank(packageName)) {
				pathToElement = packageName.replace(".", File.separator);
			}
		}
		return pathToElement;
	}

	@Override
	protected String getOutputFileName(String fileType, Node node) {
		String outputFileName = null;
		if (!StringUtil.isBlank(fileType) && node != null) {
			final String className = node.valueOf("@name");

			String fileNamePattern = DEFAULT_FILE_NAME_PATTERN;
			if (config.hasFileNamePattern(fileType)) {
				fileNamePattern = config.getFileNamePattern(fileType);
			}
			outputFileName = String.format(fileNamePattern, className);
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

			generate(CLASS_TYPE, node, CLASS_TEMPLATE_FILE, data, model);
		}
	}

	private void generateInterface(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final String interfaceName = node.valueOf("@name");
			final String packageName = node.valueOf("ancestor::p:package/@name");

			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("packageName", packageName);
			data.put("interfaceName", interfaceName);

			generate(INTERFACE_TYPE, node, INTERFACE_TEMPLATE_FILE, data, model);
		}
	}

}
