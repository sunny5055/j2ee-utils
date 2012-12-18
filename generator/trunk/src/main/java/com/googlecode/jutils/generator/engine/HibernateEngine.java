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

public class HibernateEngine extends AbstractEngine {
	private static final String JAVA_FILE_TYPE = "java";
	private static final String ENTITY_KEY = "entity";
	private static final String EMBEDDED_ID_KEY = "embeddedId";
	private static final String DAO_KEY = "dao";
	private static final String SERVICE_KEY = "service";

	private static final String ENTITY_TEMPLATE_FILE = "hibernate/model/entity.ftl";
	private static final String EMBEDDED_ID_TEMPLATE_FILE = "hibernate/model/embeddedId.ftl";
	private static final String DAO_TEMPLATE_FILE = "hibernate/dao/interface.ftl";
	private static final String SERVICE_TEMPLATE_FILE = "hibernate/service/interface.ftl";

	private static final String MODEL_PACKAGE_PROP = "modelPackage";
	private static final String DAO_PACKAGE_PROP = "daoPackage";
	private static final String SERVICE_PACKAGE_PROP = "servicePackage";

	@Override
	protected void init() {
		this.defaults.put(MODEL_PACKAGE_PROP, ".model");
		this.defaults.put(DAO_PACKAGE_PROP, ".dao");
		this.defaults.put(SERVICE_PACKAGE_PROP, ".service");
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void generate(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final List<Node> entities = xmlDocument.selectNodes("//h:entity");
			if (!CollectionUtil.isEmpty(entities)) {
				for (final Node node : entities) {
					generateEntity(node, model);

					final Node embeddedId = node.selectSingleNode("h:embedded-id");
					if (embeddedId != null) {
						generateEmbeddedId(embeddedId, model);
					}

					generateDao(node, model);

					// generateService(node, model);
				}
			}
		}
	}

	@Override
	protected String getPathToElement(String fileKey, Node node) {
		String pathToElement = null;
		if (!StringUtil.isBlank(fileKey) && node != null) {
			final String packageName = node.valueOf("ancestor::p:package/@name");
			if (StringUtil.equals(fileKey, ENTITY_KEY) || StringUtil.equals(fileKey, EMBEDDED_ID_KEY)) {
				if (!StringUtil.isBlank(packageName)) {
					pathToElement = packageName.replace(".", File.separator);
				}
			} else if (StringUtil.equals(fileKey, DAO_KEY)) {
				final String daoPackageName = getPackageName(packageName, DAO_PACKAGE_PROP);
				if (!StringUtil.isBlank(daoPackageName)) {
					pathToElement = daoPackageName.replace(".", File.separator);
				}
			} else if (StringUtil.equals(fileKey, SERVICE_KEY)) {
				final String servicePackageName = getPackageName(packageName, SERVICE_PACKAGE_PROP);
				if (!StringUtil.isBlank(servicePackageName)) {
					pathToElement = servicePackageName.replace(".", File.separator);
				}
			}
		}
		return pathToElement;
	}

	@Override
	protected String getOutputFileName(String fileKey, String fileType, Node node) throws GeneratorServiceException {
		String outputFileName = null;
		if (!StringUtil.isBlank(fileKey) && !StringUtil.isBlank(fileType) && node != null) {
			String className = null;
			if (StringUtil.equals(fileKey, ENTITY_KEY) || StringUtil.equals(fileKey, DAO_KEY) || StringUtil.equals(fileKey, SERVICE_KEY)) {
				className = node.valueOf("@name");
			} else if (StringUtil.equals(fileKey, EMBEDDED_ID_KEY)) {
				className = node.valueOf("@targetEntity");
				if (!StringUtil.isBlank(className) && StringUtil.contains(className, ".")) {
					className = StringUtil.substringAfterLast(className, ".");
				}
			}
			final String fileNamePattern = getFileNamePattern(fileKey, fileType);
			if (!StringUtil.isBlank(fileNamePattern)) {
				outputFileName = String.format(fileNamePattern, className);
			}
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

			generate(ENTITY_KEY, JAVA_FILE_TYPE, node, ENTITY_TEMPLATE_FILE, data, model);
		}
	}

	private void generateEmbeddedId(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final String className = node.valueOf("ancestor::h:entity/@name");
			final String packageName = node.valueOf("ancestor::p:package/@name");

			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("packageName", packageName);
			data.put("className", className);

			generate(EMBEDDED_ID_KEY, JAVA_FILE_TYPE, node, EMBEDDED_ID_TEMPLATE_FILE, data, model);
		}
	}

	private void generateDao(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final String className = node.valueOf("@name");
			final String packageName = node.valueOf("ancestor::p:package/@name");
			final String daoPackageName = getPackageName(packageName, DAO_PACKAGE_PROP);

			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("packageName", packageName);
			data.put("daoPackageName", daoPackageName);
			data.put("className", className);

			generate(DAO_KEY, JAVA_FILE_TYPE, node, DAO_TEMPLATE_FILE, data, model);
		}
	}

	private void generateService(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final String className = node.valueOf("@name");
			final String packageName = node.valueOf("ancestor::p:package/@name");
			final String daoPackageName = getPackageName(packageName, DAO_PACKAGE_PROP);
			final String servicePackageName = getPackageName(packageName, SERVICE_PACKAGE_PROP);

			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("packageName", packageName);
			data.put("daoPackageName", daoPackageName);
			data.put("servicePackageName", servicePackageName);
			data.put("className", className);

			generate(SERVICE_KEY, JAVA_FILE_TYPE, node, SERVICE_TEMPLATE_FILE, data, model);
		}
	}

	private String getPackageName(String modelPackageName, String key) {
		String packageName = null;
		if (!StringUtil.isBlank(modelPackageName) && !StringUtil.isBlank(key)) {
			final String modelPackageProp = resolveKey(MODEL_PACKAGE_PROP);
			final String packageProp = resolveKey(key);

			if (!StringUtil.isBlank(modelPackageProp) && !StringUtil.isBlank(packageProp)) {
				packageName = StringUtil.removeEnd(modelPackageName, modelPackageProp);
				packageName += packageProp;
			}
		}
		return packageName;
	}
}
