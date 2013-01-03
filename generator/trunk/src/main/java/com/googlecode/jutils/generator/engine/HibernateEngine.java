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

public class HibernateEngine extends AbstractEngine {
	public static final String ENTITY_PACKAGE_KEY = "entityPackage";
	public static final String EMBEDDED_ID_PACKAGE_KEY = "embeddedIdPackage";
	public static final String DAO_PACKAGE_KEY = "daoPackage";
	public static final String DAO_IMPL_PACKAGE_KEY = "daoImplPackage";
	public static final String SERVICE_PACKAGE_KEY = "servicePackage";
	public static final String SERVICE_IMPL_PACKAGE_KEY = "serviceImplPackage";
	public static final String SPRING_DIRECTORY_KEY = "springDirectory";

	public static final String ENTITY_KEY = "entity";
	public static final String EMBEDDED_ID_KEY = "embeddedId";
	public static final String DAO_KEY = "dao";
	public static final String DAO_IMPL_KEY = "daoImpl";
	public static final String SERVICE_KEY = "service";
	public static final String SERVICE_IMPL_KEY = "serviceImpl";
	public static final String SPRING_BUSINESS_FILE_KEY = "springBusinessFile";
	public static final String SPRING_HIBERNATE_FILE_KEY = "springHibernateFile";
	public static final String SPRING_JDBC_FILE_KEY = "springJdbcFile";
	public static final String SPRING_CONFIG_FILE_KEY = "springConfigFile";

	@Override
	protected void init() {
		addDefaultFileNamePattern(ENTITY_KEY, "%1s.java");
		addDefaultFileNamePattern(DAO_KEY, "%1sDao.java");
		addDefaultFileNamePattern(DAO_IMPL_KEY, "%1sDaoImpl.java");
		addDefaultFileNamePattern(SERVICE_KEY, "%1sService.java");
		addDefaultFileNamePattern(SERVICE_IMPL_KEY, "%1sServiceImpl.java");

		this.defaults.put(ENTITY_PACKAGE_KEY, ".model");
		this.defaults.put(EMBEDDED_ID_PACKAGE_KEY, ".model");
		this.defaults.put(DAO_PACKAGE_KEY, ".dao");
		this.defaults.put(DAO_IMPL_PACKAGE_KEY, ".dao.impl");
		this.defaults.put(SERVICE_PACKAGE_KEY, ".service");
		this.defaults.put(SERVICE_IMPL_PACKAGE_KEY, ".service.impl");
		this.defaults.put(SPRING_DIRECTORY_KEY, "spring");
		this.defaults.put(SPRING_BUSINESS_FILE_KEY, "business-context");
		this.defaults.put(SPRING_HIBERNATE_FILE_KEY, "hibernate-context");
		this.defaults.put(SPRING_JDBC_FILE_KEY, "jdbc-context");
		this.defaults.put(SPRING_CONFIG_FILE_KEY, "database");
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
	protected String getPathToElement(String fileKey, Node node) {
		String pathToElement = null;
		if (!StringUtil.isBlank(fileKey)) {

			if (StringUtil.equals(fileKey, SPRING_BUSINESS_FILE_KEY) || StringUtil.equals(fileKey, SPRING_HIBERNATE_FILE_KEY) || StringUtil.equals(fileKey, SPRING_JDBC_FILE_KEY)
					|| StringUtil.equals(fileKey, SPRING_CONFIG_FILE_KEY)) {
				pathToElement = resolveKey(SPRING_DIRECTORY_KEY);
			} else if (node != null) {
				final String packageName = node.valueOf("ancestor::p:package/@name");
				if (StringUtil.equals(fileKey, ENTITY_KEY)) {
					if (!StringUtil.isBlank(packageName)) {
						pathToElement = packageName.replace(".", File.separator);
					}
				} else if (StringUtil.equals(fileKey, EMBEDDED_ID_KEY)) {
					final String embeddedIdPackageName = getPackageName(packageName, EMBEDDED_ID_PACKAGE_KEY);
					if (!StringUtil.isBlank(embeddedIdPackageName)) {
						pathToElement = embeddedIdPackageName.replace(".", File.separator);
					}
				} else if (StringUtil.equals(fileKey, DAO_KEY)) {
					final String daoPackageName = getPackageName(packageName, DAO_PACKAGE_KEY);
					if (!StringUtil.isBlank(daoPackageName)) {
						pathToElement = daoPackageName.replace(".", File.separator);
					}
				} else if (StringUtil.equals(fileKey, DAO_IMPL_KEY)) {
					final String daoImplPackageName = getPackageName(packageName, DAO_IMPL_PACKAGE_KEY);
					if (!StringUtil.isBlank(daoImplPackageName)) {
						pathToElement = daoImplPackageName.replace(".", File.separator);
					}
				} else if (StringUtil.equals(fileKey, SERVICE_KEY)) {
					final String servicePackageName = getPackageName(packageName, SERVICE_PACKAGE_KEY);
					if (!StringUtil.isBlank(servicePackageName)) {
						pathToElement = servicePackageName.replace(".", File.separator);
					}
				} else if (StringUtil.equals(fileKey, SERVICE_IMPL_KEY)) {
					final String serviceImplPackageName = getPackageName(packageName, SERVICE_IMPL_PACKAGE_KEY);
					if (!StringUtil.isBlank(serviceImplPackageName)) {
						pathToElement = serviceImplPackageName.replace(".", File.separator);
					}
				}
			}
		}
		return pathToElement;
	}

	@Override
	protected String getOutputFileName(String fileExtension, String fileKey, Node node) throws GeneratorServiceException {
		String outputFileName = null;
		if (!StringUtil.isBlank(fileExtension) && !StringUtil.isBlank(fileKey)) {
			String value = null;
			if ((StringUtil.equals(fileKey, ENTITY_KEY) || StringUtil.equals(fileKey, DAO_KEY) || StringUtil.equals(fileKey, DAO_IMPL_KEY)
					|| StringUtil.equals(fileKey, SERVICE_KEY) || StringUtil.equals(fileKey, SERVICE_IMPL_KEY))
					&& node != null) {
				value = node.valueOf("@name");
			} else if (StringUtil.equals(fileKey, EMBEDDED_ID_KEY) && node != null) {
				value = node.valueOf("h:embedded-id/@targetEntity");
				if (!StringUtil.isBlank(value) && StringUtil.contains(value, ".")) {
					value = StringUtil.substringAfterLast(value, ".");
				}
			} else {
				value = resolveKey(fileKey);
			}
			final String fileNamePattern = getFileNamePattern(fileExtension, fileKey);
			if (!StringUtil.isBlank(fileNamePattern)) {
				outputFileName = String.format(fileNamePattern, value);
			}
		}
		return outputFileName;
	}

	private void generateEntity(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile("java", ENTITY_KEY, node);
			generate(outputFile, "hibernate/model/entity.ftl", data, model);
		}
	}

	private void generateEmbeddedId(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile("java", EMBEDDED_ID_KEY, node);
			generate(outputFile, "hibernate/model/embeddedId.ftl", data, model);
		}
	}

	private void generateDao(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile("java", DAO_KEY, node);
			generate(outputFile, "hibernate/dao/interface.ftl", data, model);
		}
	}

	private void generateDaoImpl(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile("java", DAO_IMPL_KEY, node);
			generate(outputFile, "hibernate/dao/implementation.ftl", data, model);
		}
	}

	private void generateService(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile("java", SERVICE_KEY, node);
			generate(outputFile, "hibernate/service/interface.ftl", data, model);
		}
	}

	private void generateServiceImpl(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile("java", SERVICE_IMPL_KEY, node);
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
					String packageName = node.valueOf("ancestor::p:package/@name");
					if (!StringUtil.isBlank(packageName)) {
						final String entityPackageProp = resolveKey(ENTITY_PACKAGE_KEY);
						if (!StringUtil.isBlank(entityPackageProp)) {
							packageName = StringUtil.removeEnd(packageName, entityPackageProp);
						}

						basePackages.add(packageName);
					}
				}
			}

			data.put("basePackages", basePackages);

			File outputFile = null;
			outputFile = getOutputFile("xml", SPRING_BUSINESS_FILE_KEY, null);
			generate(outputFile, "hibernate/spring/business-context.ftl", data, model);

			outputFile = getOutputFile("xml", SPRING_HIBERNATE_FILE_KEY, null);
			generate(outputFile, "hibernate/spring/hibernate-context.ftl", data, model);

			outputFile = getOutputFile("xml", SPRING_JDBC_FILE_KEY, null);
			generate(outputFile, "hibernate/spring/jdbc-context.ftl", data, model);

			outputFile = getOutputFile("properties", SPRING_CONFIG_FILE_KEY, null);
			generate(outputFile, "hibernate/spring/database.ftl", data, model);
		}
	}

	private Map<String, Object> getData(Node node) throws GeneratorServiceException {
		final Map<String, Object> data = new HashMap<String, Object>();
		if (node != null) {
			final String packageName = node.valueOf("ancestor::p:package/@name");
			final String className = node.valueOf("@name");

			final String daoPackageName = getPackageName(packageName, DAO_PACKAGE_KEY);
			final String daoName = getClassName(DAO_KEY, node);
			final String daoImplPackageName = getPackageName(packageName, DAO_IMPL_PACKAGE_KEY);
			final String daoImplName = getClassName(DAO_IMPL_KEY, node);
			final String servicePackageName = getPackageName(packageName, SERVICE_PACKAGE_KEY);
			final String serviceName = getClassName(SERVICE_KEY, node);
			final String serviceImplPackageName = getPackageName(packageName, SERVICE_IMPL_PACKAGE_KEY);
			final String serviceImplName = getClassName(SERVICE_IMPL_KEY, node);

			data.put("packageName", packageName);
			data.put("className", className);

			final Node embeddedId = node.selectSingleNode("h:embedded-id");
			if (embeddedId != null) {
				final String embeddedIdPackageName = getPackageName(packageName, EMBEDDED_ID_PACKAGE_KEY);
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

	private String getPackageName(String entityPackageName, String key) {
		String packageName = null;
		if (!StringUtil.isBlank(entityPackageName) && !StringUtil.isBlank(key)) {
			final String entityPackageProp = resolveKey(ENTITY_PACKAGE_KEY);
			final String packageProp = resolveKey(key);

			if (!StringUtil.isBlank(entityPackageProp) && !StringUtil.isBlank(packageProp)) {
				packageName = StringUtil.removeEnd(entityPackageName, entityPackageProp);
				packageName += packageProp;
			}
		}
		return packageName;
	}

	private String getClassName(String fileKey, Node node) throws GeneratorServiceException {
		String outputFileName = null;
		if (!StringUtil.isBlank(fileKey) && node != null) {
			outputFileName = getOutputFileName("java", fileKey, node);
			outputFileName = FilenameUtils.getBaseName(outputFileName);
		}
		return outputFileName;
	}
}
