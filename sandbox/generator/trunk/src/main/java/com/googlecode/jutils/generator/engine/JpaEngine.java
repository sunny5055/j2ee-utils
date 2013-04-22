package com.googlecode.jutils.generator.engine;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Node;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.generator.exception.GeneratorServiceException;

import freemarker.ext.dom.NodeModel;

public class JpaEngine extends AbstractJavaEngine {
	public static final String ENTITY_KEY = "entity";
	public static final String EMBEDDED_ID_KEY = "embedded_id";
	public static final String DAO_KEY = "dao";
	public static final String DAO_IMPL_KEY = "dao_impl";
	public static final String SERVICE_KEY = "service";
	public static final String SERVICE_IMPL_KEY = "service_impl";
	public static final String TEST_SERVICE_KEY = "test_service";
	public static final String TEST_XML_DATASET_KEY = "test_xml_dataset";
	public static final String SPRING_BUSINESS_KEY = "spring_business";
	public static final String SPRING_JDBC_KEY = "spring_jdbc";
	public static final String SPRING_JPA_KEY = "spring_jpa";
	public static final String SPRING_TX_KEY = "spring_tx";
	public static final String SPRING_DATABASE_KEY = "spring_database";
	public static final String JPA_PERSISTENCE_KEY = "jpa_persistence";
	public static final String SPRING_TEST_BUSINESS_KEY = "spring_test_business";
	public static final String SPRING_TEST_DATABASE_KEY = "spring_test_database";
	public static final String INSERT_SQL_KEY = "insert_sql";
	public static final String EXPORTER_KEY = "ddl_exporter";

	@Override
	protected void init() {
		super.init();

		loadConfigFile("classpath:/config/jpa-engine.properties");
	}

	@Override
	protected String getEngineKey() {
		return "jpa_engine";
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void generate(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final List<Node> entities = xmlDocument.selectNodes("//j:entity");
			if (!CollectionUtil.isEmpty(entities)) {
				for (final Node node : entities) {
					generateEntity(node, model);

					final Node embeddedId = node.selectSingleNode("j:embedded-id");
					if (embeddedId != null) {
						generateEmbeddedId(node, model);
					}

					generateDao(node, model);

					generateDaoImpl(node, model);

					generateService(node, model);

					generateServiceImpl(node, model);

					generateTestService(node, model);

					generateTestDataset(node, model);
				}

				generateJpaFiles(xmlDocument, model);

				generateSpring(xmlDocument, model);

				generateExporter(xmlDocument, model);

				generateSql(xmlDocument, model);
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
			} else if (StringUtil.equals(key, TEST_SERVICE_KEY) && node != null) {
				final String nodePackageName = node.valueOf("ancestor::p:package/@name");
				final String packageName = getPackageName(nodePackageName, SERVICE_KEY);
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
					|| StringUtil.equals(key, SERVICE_KEY) || StringUtil.equals(key, SERVICE_IMPL_KEY) || StringUtil.equals(key, TEST_SERVICE_KEY) || StringUtil.equals(key,
					TEST_XML_DATASET_KEY)) && node != null) {
				if (StringUtil.equals(key, EMBEDDED_ID_KEY)) {
					value = node.valueOf("j:embedded-id/@targetEntity");
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
			generate(outputFile, "jpa/model/entity.ftl", data, model);
		}
	}

	private void generateEmbeddedId(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(EMBEDDED_ID_KEY, node);
			generate(outputFile, "jpa/model/embeddedId.ftl", data, model);
		}
	}

	private void generateDao(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(DAO_KEY, node);
			generate(outputFile, "jpa/dao/interface.ftl", data, model);
		}
	}

	private void generateDaoImpl(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(DAO_IMPL_KEY, node);
			generate(outputFile, "jpa/dao/implementation.ftl", data, model);
		}
	}

	private void generateService(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(SERVICE_KEY, node);
			generate(outputFile, "jpa/service/interface.ftl", data, model);
		}
	}

	private void generateServiceImpl(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(SERVICE_IMPL_KEY, node);
			generate(outputFile, "jpa/service/implementation.ftl", data, model);
		}
	}

	private void generateTestService(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);
			addSpringFiles(data);

			final String xmlDatasetFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + TEST_XML_DATASET_KEY);
			final String xmlDatasetFileName = getOutputFileName(TEST_XML_DATASET_KEY, node);
			final String xmlDatasetFile = getClassPathResource(xmlDatasetFilePath, xmlDatasetFileName);
			data.put("xmlDatasetFile", xmlDatasetFile);

			final File outputFile = getOutputFile(TEST_SERVICE_KEY, node);
			generate(outputFile, "jpa/test/class/class.ftl", data, model);
		}
	}

	private void generateTestDataset(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(TEST_XML_DATASET_KEY, node);
			generate(outputFile, "jpa/test/xml/xml-dataset.ftl", data, model);
		}
	}

	private void generateJpaFiles(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final Map<String, Object> data = new HashMap<String, Object>();

			File outputFile = null;
			outputFile = getOutputFile(JPA_PERSISTENCE_KEY, xmlDocument);
			generate(outputFile, "jpa/persistence.ftl", data, model);
		}
	}

	@SuppressWarnings("unchecked")
	private void generateSpring(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final Map<String, Object> data = new HashMap<String, Object>();
			final Set<String> basePackages = new HashSet<String>();
			final List<Node> entities = xmlDocument.selectNodes("//j:entity");
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

			addSpringFiles(data);

			File outputFile = null;
			outputFile = getOutputFile(SPRING_BUSINESS_KEY, xmlDocument);
			generate(outputFile, "jpa/spring/business-context.ftl", data, model);

			outputFile = getOutputFile(SPRING_JDBC_KEY, xmlDocument);
			generate(outputFile, "jpa/spring/jdbc-context.ftl", data, model);

			outputFile = getOutputFile(SPRING_JPA_KEY, xmlDocument);
			generate(outputFile, "jpa/spring/jpa-context.ftl", data, model);

			outputFile = getOutputFile(SPRING_TX_KEY, xmlDocument);
			generate(outputFile, "jpa/spring/tx-context.ftl", data, model);

			outputFile = getOutputFile(SPRING_DATABASE_KEY, xmlDocument);
			generate(outputFile, "jpa/spring/database.ftl", data, model);

			outputFile = getOutputFile(SPRING_TEST_BUSINESS_KEY, xmlDocument);
			generate(outputFile, "jpa/spring/test-context.ftl", data, model);

			outputFile = getOutputFile(SPRING_TEST_DATABASE_KEY, xmlDocument);
			generate(outputFile, "jpa/spring/test-database.ftl", data, model);
		}
	}

	private void generateExporter(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final Map<String, Object> data = new HashMap<String, Object>();
			addSpringFiles(data);

			final File outputFile = getOutputFile(EXPORTER_KEY, xmlDocument);
			generate(outputFile, "jpa/exporter.ftl", data, model);
		}
	}

	private void generateSql(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final Map<String, Object> data = new HashMap<String, Object>();

			final String database = resolveKey(getEngineKey() + ".database");
			if (!StringUtil.isBlank(database)) {
				data.put("database", database.toLowerCase());
			}

			final File outputFile = getOutputFile(INSERT_SQL_KEY, xmlDocument);
			generate(outputFile, "jpa/sql/insert.ftl", data, model);
		}
	}

	private void addSpringFiles(final Map<String, Object> data) {
		final String springBusinessFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_BUSINESS_KEY);
		final String springBusinessFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_BUSINESS_KEY);
		final String springBusinessFile = getClassPathResource(springBusinessFilePath, springBusinessFileName);
		data.put("springBusinessFile", springBusinessFile);

		final String springJdbcFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_JDBC_KEY);
		final String springJdbcFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_JDBC_KEY);
		final String springJdbcFile = getClassPathResource(springJdbcFilePath, springJdbcFileName);
		data.put("springJdbcFile", springJdbcFile);

		final String springJpaFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_JPA_KEY);
		final String springJpaFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_JPA_KEY);
		final String springJpaFile = getClassPathResource(springJpaFilePath, springJpaFileName);
		data.put("springJpaFile", springJpaFile);

		final String springTxFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_TX_KEY);
		final String springTxFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_TX_KEY);
		final String springTxFile = getClassPathResource(springTxFilePath, springTxFileName);
		data.put("springTxFile", springTxFile);

		final String springDatabaseFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_DATABASE_KEY);
		final String springDatabaseFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_DATABASE_KEY);
		final String springDatabaseFile = getClassPathResource(springDatabaseFilePath, springDatabaseFileName);
		data.put("springDatabaseFile", springDatabaseFile);

		final String springTestBusinessFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_TEST_BUSINESS_KEY);
		final String springTestBusinessFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_TEST_BUSINESS_KEY);
		final String springTestBusinessFile = getClassPathResource(springTestBusinessFilePath, springTestBusinessFileName);
		data.put("springTestBusinessFile", springTestBusinessFile);

		final String springTestDatabaseFilePath = resolveKey(getEngineKey() + "." + FILE_PATH + "." + SPRING_TEST_DATABASE_KEY);
		final String springTestDatabaseFileName = resolveKey(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_TEST_DATABASE_KEY);
		final String springTestDatabaseFile = getClassPathResource(springTestDatabaseFilePath, springTestDatabaseFileName);
		data.put("springTestDatabaseFile", springTestDatabaseFile);
	}

	private Map<String, Object> getData(Node node) throws GeneratorServiceException {
		final Map<String, Object> data = new HashMap<String, Object>();
		if (node != null) {
			final String packageName = node.valueOf("ancestor::p:package/@name");
			final String className = node.valueOf("@name");

			data.put("packageName", packageName);
			data.put("className", className);
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
}
