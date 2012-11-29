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

public class HibernateGeneratorService extends AbstractGeneratorService {
	private static final String DEFAULT_FILE_NAME_PATTERN = "%1s.java";

	private static final String ENTITY_TYPE = "class";
	private static final String ENTITY_TEMPLATE_FILE = "hibernate/model/entity.ftl";
	private static final String EMBEDDED_ID_TEMPLATE_FILE = "hibernate/model/embeddedId.ftl";

	@Override
	protected Map<String, String> getNamespaceUris() {
		final Map<String, String> namespaceUris = new HashMap<String, String>();
		namespaceUris.put("p", "http://code.google.com/p/j2ee-utils/schema/hibernate");

		return namespaceUris;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void generate(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final List<Node> entities = xmlDocument.selectNodes("//p:entity");
			if (!CollectionUtil.isEmpty(entities)) {
				for (final Node node : entities) {
					generateEntity(node, model);

					final Node embeddedId = node.selectSingleNode("p:embedded-id");
					if (embeddedId != null) {
						generateEmbeddedId(embeddedId, model);
					}
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
			String className = null;
			if (node.getName().equalsIgnoreCase("entity")) {
				className = node.valueOf("@name");
			} else if (node.getName().equalsIgnoreCase("embedded-id")) {
				className = node.valueOf("@targetEntity");
				if (!StringUtil.isBlank(className) && StringUtil.contains(className, ".")) {
					className = StringUtil.substringAfterLast(className, ".");
				}
			}

			String fileNamePattern = DEFAULT_FILE_NAME_PATTERN;
			if (config.hasFileNamePattern(fileType)) {
				fileNamePattern = config.getFileNamePattern(fileType);
			}
			outputFileName = String.format(fileNamePattern, className);
		}
		return outputFileName;
	}

	private void generateEntity(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final String className = node.valueOf("@name");
			final String packageName = node.valueOf("ancestor::p:package/@name");

			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("packageName", packageName);
			data.put("className", className);

			generate(ENTITY_TYPE, node, ENTITY_TEMPLATE_FILE, data, model);
		}
	}

	private void generateEmbeddedId(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final String className = node.valueOf("ancestor::p:entity/@name");
			final String packageName = node.valueOf("ancestor::p:package/@name");

			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("packageName", packageName);
			data.put("className", className);

			generate(ENTITY_TYPE, node, EMBEDDED_ID_TEMPLATE_FILE, data, model);
		}
	}
}
