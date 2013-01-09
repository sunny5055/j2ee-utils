package com.googlecode.jutils.generator.engine;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.dom4j.Document;
import org.dom4j.Node;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.generator.exception.GeneratorServiceException;

import freemarker.ext.dom.NodeModel;

public class HibernateEngine extends AbstractJavaEngine {
	private static final String ENTITY_KEY = "entity";
	private static final String EMBEDDED_ID_KEY = "embedded_id";
	private static final String DAO_KEY = "dao";
	private static final String DAO_IMPL_KEY = "dao_impl";
	private static final String SERVICE_KEY = "service";
	private static final String SERVICE_IMPL_KEY = "service_impl";
	private static final String SPRING_BUSINESS_KEY = "spring_business";
	private static final String SPRING_JDBC_KEY = "spring_jdbc";
	private static final String SPRING_HIBERNATE_KEY = "spring_hibernate";
	private static final String SPRING_TX_KEY = "spring_tx";
	private static final String SPRING_CONFIG_KEY = "spring_config";

	@Override
	protected void init() {
		super.init();

		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + ENTITY_KEY, "{" + JAVA_PATH_KEY + "}");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + EMBEDDED_ID_KEY, "{" + JAVA_PATH_KEY + "}");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + DAO_KEY, "{" + JAVA_PATH_KEY + "}");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + DAO_IMPL_KEY, "{" + JAVA_PATH_KEY + "}");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + SERVICE_KEY, "{" + JAVA_PATH_KEY + "}");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + SERVICE_IMPL_KEY, "{" + JAVA_PATH_KEY + "}");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + SPRING_BUSINESS_KEY, "{" + RESOURCES_PATH_KEY + "}/spring");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + SPRING_JDBC_KEY, "{" + RESOURCES_PATH_KEY + "}/spring");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + SPRING_HIBERNATE_KEY, "{" + RESOURCES_PATH_KEY + "}/spring");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + SPRING_TX_KEY, "{" + RESOURCES_PATH_KEY + "}/spring");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + SPRING_CONFIG_KEY, "{" + RESOURCES_PATH_KEY + "}/spring");

		this.defaultProperties.put(getEngineKey() + "." + ENTITY_KEY + ".package", ".model");
		this.defaultProperties.put(getEngineKey() + "." + EMBEDDED_ID_KEY + ".package", ".model");
		this.defaultProperties.put(getEngineKey() + "." + DAO_KEY + ".package", ".dao");
		this.defaultProperties.put(getEngineKey() + "." + DAO_IMPL_KEY + ".package", ".dao.impl");
		this.defaultProperties.put(getEngineKey() + "." + SERVICE_KEY + ".package", ".service");
		this.defaultProperties.put(getEngineKey() + "." + SERVICE_IMPL_KEY + ".package", ".service.impl");

		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + ENTITY_KEY, "%1s.java");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + EMBEDDED_ID_KEY, "%1s.java");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + DAO_KEY, "%1sDao.java");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + DAO_IMPL_KEY, "%1sDaoImpl.java");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SERVICE_KEY, "%1sService.java");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SERVICE_IMPL_KEY, "%1sServiceImpl.java");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_BUSINESS_KEY, "business-context.xml");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_JDBC_KEY, "jdbc-context.xml");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_HIBERNATE_KEY, "hibernate-context.xml");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_TX_KEY, "tx-context.xml");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_CONFIG_KEY, "database.properties");

		this.defaultProperties.put(getEngineKey() + ".database", "postgresql");
	}

	@Override
	protected String getEngineKey() {
		return "hibernate_engine";
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
						generateEmbeddedId(node, model);
					}

					generateDao(node, model);

					generateDaoImpl(node, model);

					generateService(node, model);

					generateServiceImpl(node, model);
				}

				generateSpring(xmlDocument, model);
			}
		}
	}

	@Override
	protected String getPathToElement(String key, Node node) {
		String pathToElement = null;
		if (!StringUtil.isBlank(key)) {
			if (StringUtil.equals(key, ENTITY_KEY) && node != null) {
				final String nodePackageName = node.valueOf("ancestor::p:package/@name");
				if (!StringUtil.isBlank(nodePackageName)) {
					pathToElement = nodePackageName.replace(".", File.separator);
				}
			} else if ((StringUtil.equals(key, EMBEDDED_ID_KEY) || StringUtil.equals(key, DAO_KEY) || StringUtil.equals(key, DAO_IMPL_KEY) || StringUtil.equals(key, SERVICE_KEY) || StringUtil
					.equals(key, SERVICE_IMPL_KEY)) && node != null) {
				final String nodePackageName = node.valueOf("ancestor::p:package/@name");
				final String packageName = getPackageName(nodePackageName, key);
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
		if (!StringUtil.isBlank(key)) {
			String value = null;
			if ((StringUtil.equals(key, ENTITY_KEY) || StringUtil.equals(key, EMBEDDED_ID_KEY) || StringUtil.equals(key, DAO_KEY) || StringUtil.equals(key, DAO_IMPL_KEY)
					|| StringUtil.equals(key, SERVICE_KEY) || StringUtil.equals(key, SERVICE_IMPL_KEY))
					&& node != null) {
				if (StringUtil.equals(key, EMBEDDED_ID_KEY)) {
					value = node.valueOf("h:embedded-id/@targetEntity");
					if (!StringUtil.isBlank(value) && StringUtil.contains(value, ".")) {
						value = StringUtil.substringAfterLast(value, ".");
					}
				} else {
					value = node.valueOf("@name");
				}
			}

			outputFileName = getOutputFileName(key, value);
		}
		return outputFileName;
	}

	private void generateEntity(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(ENTITY_KEY, node);
			generate(outputFile, "hibernate/model/entity.ftl", data, model);
		}
	}

	private void generateEmbeddedId(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(EMBEDDED_ID_KEY, node);
			generate(outputFile, "hibernate/model/embeddedId.ftl", data, model);
		}
	}

	private void generateDao(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(DAO_KEY, node);
			generate(outputFile, "hibernate/dao/interface.ftl", data, model);
		}
	}

	private void generateDaoImpl(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(DAO_IMPL_KEY, node);
			generate(outputFile, "hibernate/dao/implementation.ftl", data, model);
		}
	}

	private void generateService(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(SERVICE_KEY, node);
			generate(outputFile, "hibernate/service/interface.ftl", data, model);
		}
	}

	private void generateServiceImpl(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(SERVICE_IMPL_KEY, node);
			generate(outputFile, "hibernate/service/implementation.ftl", data, model);
		}
	}

	@SuppressWarnings("unchecked")
	private void generateSpring(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final Map<String, Object> data = new HashMap<String, Object>();
			final Set<String> basePackages = new HashSet<String>();
			final List<Node> entities = xmlDocument.selectNodes("//h:entity");
			if (!CollectionUtil.isEmpty(entities)) {
				for (final Node node : entities) {
					final String nodePackageName = node.valueOf("ancestor::p:package/@name");
					if (!StringUtil.isBlank(nodePackageName)) {
						final String basePackageName = getBasePackageName(nodePackageName);
						basePackages.add(basePackageName);
					}
				}
			}
			data.put("basePackages", basePackages);

			final String database = resolveKey(getEngineKey() + ".database");
			if (!StringUtil.isBlank(database)) {
				data.put("database", database.toLowerCase());
			}

			final String daoFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + DAO_KEY);
			if (!StringUtil.isBlank(daoFileName)) {
				String daoName = StringUtil.substringBeforeLast(daoFileName, ".");
				daoName = String.format(daoName, "*");
				data.put("daoName", daoName);
			}

			final String serviceFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SERVICE_KEY);
			if (!StringUtil.isBlank(serviceFileName)) {
				String serviceName = StringUtil.substringBeforeLast(serviceFileName, ".");
				serviceName = String.format(serviceName, "*");
				data.put("serviceName", serviceName);
			}

			final String springBusinessFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_BUSINESS_KEY);
			final String springBusinessFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_BUSINESS_KEY);
			final String springBusinessFile = getClassPathResource(springBusinessFilePath, springBusinessFileName);
			data.put("springBusinessFile", springBusinessFile);

			final String springJdbcFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_JDBC_KEY);
			final String springJdbcFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_JDBC_KEY);
			final String springJdbcFile = getClassPathResource(springJdbcFilePath, springJdbcFileName);
			data.put("springJdbcFile", springJdbcFile);

			final String springHibernateFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_HIBERNATE_KEY);
			final String springHibernateFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_HIBERNATE_KEY);
			final String springHibernateFile = getClassPathResource(springHibernateFilePath, springHibernateFileName);
			data.put("springHibernateFile", springHibernateFile);

			final String springTxFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_TX_KEY);
			final String springTxFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_TX_KEY);
			final String springTxFile = getClassPathResource(springTxFilePath, springTxFileName);
			data.put("springTxFile", springTxFile);

			final String springConfigFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_CONFIG_KEY);
			final String springConfigFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_CONFIG_KEY);
			final String springConfigFile = getClassPathResource(springConfigFilePath, springConfigFileName);
			data.put("springConfigFile", springConfigFile);

			File outputFile = null;
			outputFile = getOutputFile(SPRING_BUSINESS_KEY, null);
			generate(outputFile, "hibernate/spring/business-context.ftl", data, model);

			outputFile = getOutputFile(SPRING_JDBC_KEY, null);
			generate(outputFile, "hibernate/spring/jdbc-context.ftl", data, model);

			outputFile = getOutputFile(SPRING_HIBERNATE_KEY, null);
			generate(outputFile, "hibernate/spring/hibernate-context.ftl", data, model);

			outputFile = getOutputFile(SPRING_TX_KEY, null);
			generate(outputFile, "hibernate/spring/tx-context.ftl", data, model);

			outputFile = getOutputFile(SPRING_CONFIG_KEY, null);
			generate(outputFile, "hibernate/spring/database.ftl", data, model);
		}
	}

	private Map<String, Object> getData(Node node) throws GeneratorServiceException {
		final Map<String, Object> data = new HashMap<String, Object>();
		if (node != null) {
			final String packageName = node.valueOf("ancestor::p:package/@name");
			final String className = node.valueOf("@name");

			final String daoPackageName = getPackageName(packageName, DAO_KEY);
			final String daoName = getClassName(DAO_KEY, node);
			final String daoImplPackageName = getPackageName(packageName, DAO_IMPL_KEY);
			final String daoImplName = getClassName(DAO_IMPL_KEY, node);
			final String servicePackageName = getPackageName(packageName, SERVICE_KEY);
			final String serviceName = getClassName(SERVICE_KEY, node);
			final String serviceImplPackageName = getPackageName(packageName, SERVICE_IMPL_KEY);
			final String serviceImplName = getClassName(SERVICE_IMPL_KEY, node);

			data.put("packageName", packageName);
			data.put("className", className);

			final Node embeddedId = node.selectSingleNode("h:embedded-id");
			if (embeddedId != null) {
				final String embeddedIdPackageName = getPackageName(packageName, EMBEDDED_ID_KEY);
				final String embeddedIdName = getClassName(EMBEDDED_ID_KEY, node);

				data.put("embeddedIdPackageName", embeddedIdPackageName);
				data.put("embeddedIdName", embeddedIdName);
			}

			data.put("daoPackageName", daoPackageName);
			data.put("daoName", daoName);
			data.put("daoImplPackageName", daoImplPackageName);
			data.put("daoImplName", daoImplName);
			data.put("servicePackageName", servicePackageName);
			data.put("serviceName", serviceName);
			data.put("serviceImplPackageName", serviceImplPackageName);
			data.put("serviceImplName", serviceImplName);
		}
		return data;
	}

	private String getPackageName(String nodePackageName, String key) {
		String packageName = null;
		if (!StringUtil.isBlank(nodePackageName) && !StringUtil.isBlank(key)) {
			packageName = getBasePackageName(nodePackageName);
			final String packageProp = resolveKey(getEngineKey() + "." + key + ".package");

			if (!StringUtil.isBlank(packageName) && !StringUtil.isBlank(packageProp)) {
				packageName += packageProp;
			}
		}
		return packageName;
	}

	private String getBasePackageName(String packageName) {
		String basePackageName = null;
		if (!StringUtil.isBlank(packageName)) {
			basePackageName = packageName;
			final String entityPackageProp = resolveKey(getEngineKey() + "." + ENTITY_KEY + ".package");
			if (!StringUtil.isBlank(entityPackageProp)) {
				basePackageName = StringUtil.removeEnd(basePackageName, entityPackageProp);
			}
		}
		return basePackageName;
	}

	private String getClassName(String key, Node node) throws GeneratorServiceException {
		String outputFileName = null;
		if (!StringUtil.isBlank(key) && node != null) {
			outputFileName = getOutputFileName(key, node);
			outputFileName = FilenameUtils.getBaseName(outputFileName);
		}
		return outputFileName;
	}
}
