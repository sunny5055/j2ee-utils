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

public class ExtJsEngine extends AbstractJavaEngine {
	// extends AbstractGuiEngine {
	private static final String EXTJS_APP_KEY = "extjs_app";
	private static final String EXTJS_MODEL_KEY = "extjs_model";
	private static final String EXTJS_STORE_KEY = "extjs_store";
	private static final String EXTJS_CONTROLLER_KEY = "extjs_controller";
	private static final String EXTJS_EDIT_VIEW_KEY = "extjs_edit_view";
	private static final String EXTJS_LIST_VIEW_KEY = "extjs_list_view";
	private static final String SPRING_CONTROLLER_KEY = "spring_controller";

	// copied from HibernateEngine
	private static final String ENTITY_KEY = "entity";
	private static final String EMBEDDED_ID_KEY = "embedded_id";
	private static final String DAO_KEY = "dao";
	private static final String DAO_IMPL_KEY = "dao_impl";
	private static final String SERVICE_KEY = "service";
	private static final String SERVICE_IMPL_KEY = "service_impl";

	@Override
	protected void init() {
		super.init();

		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + EXTJS_APP_KEY, "{" + WEBAPP_PATH_KEY + "}");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + EXTJS_MODEL_KEY, "{" + WEBAPP_PATH_KEY + "}/app/model");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + EXTJS_STORE_KEY, "{" + WEBAPP_PATH_KEY + "}/app/store");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + EXTJS_CONTROLLER_KEY, "{" + WEBAPP_PATH_KEY + "}/app/controller");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + EXTJS_EDIT_VIEW_KEY, "{" + WEBAPP_PATH_KEY + "}/app/view");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + EXTJS_LIST_VIEW_KEY, "{" + WEBAPP_PATH_KEY + "}/app/view");
		this.defaultProperties.put(getEngineKey() + "." + FILE_PATH + "." + SPRING_CONTROLLER_KEY, "{" + JAVA_PATH_KEY + "}");

		this.defaultProperties.put(getEngineKey() + "." + SPRING_CONTROLLER_KEY + ".package", ".web.controller");

		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + EXTJS_APP_KEY, "app.js");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + EXTJS_MODEL_KEY, "%1s.js");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + EXTJS_STORE_KEY, "%1sStore.js");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + EXTJS_CONTROLLER_KEY, "%1sController.js");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + EXTJS_EDIT_VIEW_KEY, "%1sEdit.js");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + EXTJS_LIST_VIEW_KEY, "%1sList.js");
		this.defaultProperties.put(getEngineKey() + "." + FILE_NAME_PATTERN + "." + SPRING_CONTROLLER_KEY, "%1sController.java");

		// copied from HibernateEngine
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
	}

	@Override
	protected String getEngineKey() {
		return "extjs_engine";
	}

	@Override
	protected String getPathToElement(String key, Node node) {
		String pathToElement = null;
		if (!StringUtil.isBlank(key)) {
			if (StringUtil.equals(key, SPRING_CONTROLLER_KEY) && node != null) {
				final String nodePackageName = node.valueOf("ancestor::p:package/@name");
				final String packageName = getPackageName(nodePackageName, key);
				if (!StringUtil.isBlank(packageName)) {
					pathToElement = packageName.replace(".", File.separator);
				}
			} else if (StringUtil.equals(key, EXTJS_EDIT_VIEW_KEY) || StringUtil.equals(key, EXTJS_LIST_VIEW_KEY)) {
				pathToElement = node.valueOf("@name").toLowerCase();
			}
		}
		return pathToElement;
	}

	@Override
	protected String getOutputFileName(String key, Node node) throws GeneratorServiceException {
		String outputFileName = null;
		if (!StringUtil.isBlank(key)) {
			String value = null;
			if ((StringUtil.equals(key, EXTJS_MODEL_KEY) || StringUtil.equals(key, EXTJS_STORE_KEY) || StringUtil.equals(key, EXTJS_CONTROLLER_KEY)
					|| StringUtil.equals(key, EXTJS_EDIT_VIEW_KEY) || StringUtil.equals(key, EXTJS_LIST_VIEW_KEY) || StringUtil.equals(key, SPRING_CONTROLLER_KEY))
					&& node != null) {
				value = node.valueOf("@name");
			}

			outputFileName = getOutputFileName(key, value);
		}
		return outputFileName;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void generate(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final List<Node> entities = xmlDocument.selectNodes("//h:entity");
			if (!CollectionUtil.isEmpty(entities)) {
				for (final Node node : entities) {
					generateModel(node, model);

					generateStore(node, model);

					generateExtJsController(node, model);

					generateController(node, model);

					generateEditView(node, model);

					generateListView(node, model);
				}

				generateApp(xmlDocument, model);
			}
		}
	}

	// @Override
	// @SuppressWarnings("unchecked")
	// protected void doGenerate(Document xmlDocument, NodeModel model) {
	// if (xmlDocument != null && model != null) {
	// final List<Node> forms = xmlDocument.selectNodes("//g:form");
	// if (!CollectionUtil.isEmpty(forms)) {
	// for (final Node node : forms) {
	// // generateForm(node, model);
	// }
	// }
	//
	// final List<Node> dataTables = xmlDocument.selectNodes("//g:datatable");
	// if (!CollectionUtil.isEmpty(dataTables)) {
	// for (final Node node : dataTables) {
	// // generateDataTable(node, model);
	// }
	// }
	// }
	// }

	private void generateModel(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(EXTJS_MODEL_KEY, node);
			generate(outputFile, "extjs/extjs/model.ftl", data, model);
		}
	}

	private void generateStore(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(EXTJS_STORE_KEY, node);
			generate(outputFile, "extjs/extjs/store.ftl", data, model);
		}
	}

	private void generateExtJsController(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(EXTJS_CONTROLLER_KEY, node);
			generate(outputFile, "extjs/extjs/controller.ftl", data, model);
		}
	}

	private void generateController(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(SPRING_CONTROLLER_KEY, node);
			generate(outputFile, "extjs/spring/controller.ftl", data, model);
		}
	}

	private void generateEditView(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(EXTJS_EDIT_VIEW_KEY, node);
			generate(outputFile, "extjs/extjs/view/edit.ftl", data, model);
		}
	}

	private void generateListView(Node node, NodeModel model) throws GeneratorServiceException {
		if (node != null && model != null) {
			final Map<String, Object> data = getData(node);

			final File outputFile = getOutputFile(EXTJS_LIST_VIEW_KEY, node);
			generate(outputFile, "extjs/extjs/view/list.ftl", data, model);
		}
	}

	private void generateApp(Document xmlDocument, NodeModel model) throws GeneratorServiceException {
		if (xmlDocument != null && model != null) {
			final Map<String, Object> data = new HashMap<String, Object>();

			File outputFile = null;
			outputFile = getOutputFile(EXTJS_APP_KEY, null);
			generate(outputFile, "extjs/extjs/app.ftl", data, model);
		}
	}

	private Map<String, Object> getData(Node node) throws GeneratorServiceException {
		final Map<String, Object> data = new HashMap<String, Object>();
		if (node != null) {
			// copied from HibernateEngine
			final String packageName = node.valueOf("ancestor::p:package/@name");
			final String className = node.valueOf("@name");

			final String daoPackageName = getPackageName(packageName, DAO_KEY);
			final String daoName = getFileNameWithoutExtension(DAO_KEY, node);
			final String daoImplPackageName = getPackageName(packageName, DAO_IMPL_KEY);
			final String daoImplName = getFileNameWithoutExtension(DAO_IMPL_KEY, node);
			final String servicePackageName = getPackageName(packageName, SERVICE_KEY);
			final String serviceName = getFileNameWithoutExtension(SERVICE_KEY, node);
			final String serviceImplPackageName = getPackageName(packageName, SERVICE_IMPL_KEY);
			final String serviceImplName = getFileNameWithoutExtension(SERVICE_IMPL_KEY, node);

			data.put("packageName", packageName);
			data.put("className", className);

			final Node embeddedId = node.selectSingleNode("h:embedded-id");
			if (embeddedId != null) {
				final String embeddedIdPackageName = getPackageName(packageName, EMBEDDED_ID_KEY);
				final String embeddedIdName = getFileNameWithoutExtension(EMBEDDED_ID_KEY, node);

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
			// copied from HibernateEngine

			final String modelName = getFileNameWithoutExtension(EXTJS_MODEL_KEY, node);
			final String extJsControllerName = getFileNameWithoutExtension(EXTJS_CONTROLLER_KEY, node);
			final String editViewName = getFileNameWithoutExtension(EXTJS_EDIT_VIEW_KEY, node);
			final String listViewName = getFileNameWithoutExtension(EXTJS_LIST_VIEW_KEY, node);
			final String controllerPackageName = getPackageName(packageName, SPRING_CONTROLLER_KEY);
			final String controllerName = getFileNameWithoutExtension(SPRING_CONTROLLER_KEY, node);

			data.put("modelName", modelName);
			data.put("extJsControllerName", extJsControllerName);
			data.put("editViewName", editViewName);
			data.put("listViewName", listViewName);
			data.put("controllerPackageName", controllerPackageName);
			data.put("controllerName", controllerName);
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
