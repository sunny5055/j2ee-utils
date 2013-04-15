package com.googlecode.jutils.generator.engine;

import java.util.Map;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.generator.formatter.impl.JavaFormatter;
import com.googlecode.jutils.generator.freemarker.method.GetClassNameMethod;
import com.googlecode.jutils.generator.freemarker.method.GetFqdnMethod;
import com.googlecode.jutils.generator.freemarker.method.GetImportsMethod;
import com.googlecode.jutils.generator.freemarker.method.GetModifiersMethod;
import com.googlecode.jutils.generator.freemarker.method.GetPackageMethod;
import com.googlecode.jutils.generator.freemarker.method.GetTypeMethod;
import com.googlecode.jutils.generator.freemarker.method.IsPrimitiveMethod;

public abstract class AbstractJavaEngine extends AbstractEngine {
	protected static final String JAVA_PATH_KEY = "path.java";
	protected static final String RESOURCES_PATH_KEY = "path.resources";
	protected static final String WEBAPP_PATH_KEY = "path.webapp";
	protected static final String CONFIG_PATH_KEY = "path.config";
	protected static final String TEST_JAVA_PATH_KEY = "path.test_java";
	protected static final String TEST_RESOURCES_PATH_KEY = "path.test_resources";
	protected static final String MODULE_MODELE = "module_modele";
	protected static final String MODULE_SERVICE = "module_service";
	protected static final String MODULE_WEB = "module_web";

	public AbstractJavaEngine() {
		super();
	}

	@Override
	protected void init() {
		super.init();

		this.defaultProperties.put(MODULE_MODELE, "%1s-model");
		this.defaultProperties.put(MODULE_SERVICE, "%1s-service");
		this.defaultProperties.put(MODULE_WEB, "%1s-web");

		this.defaultProperties.put(JAVA_PATH_KEY, "src/main/java");
		this.defaultProperties.put(RESOURCES_PATH_KEY, "src/main/resources");
		this.defaultProperties.put(WEBAPP_PATH_KEY, "src/main/webapp");
		this.defaultProperties.put(CONFIG_PATH_KEY, "src/main/config");
		this.defaultProperties.put(TEST_JAVA_PATH_KEY, "src/test/java");
		this.defaultProperties.put(TEST_RESOURCES_PATH_KEY, "src/test/resources");

		this.defaultFormatters.add(new JavaFormatter());
	}

	@Override
	protected void addFreemarkerExt(Map<String, Object> data) {
		if (data != null) {
			super.addFreemarkerExt(data);

			data.put("getImports", new GetImportsMethod());
			data.put("getPackage", new GetPackageMethod());
			data.put("getClassName", new GetClassNameMethod());
			data.put("getFqdn", new GetFqdnMethod());
			data.put("getModifiers", new GetModifiersMethod());
			data.put("getType", new GetTypeMethod());
			data.put("isPrimitive", new IsPrimitiveMethod());
		}
	}

	protected String getClassPathResource(String path) {
		return getClassPathResource(path, null);
	}

	protected String getClassPathResource(String path, String fileName) {
		String classPathResource = null;
		if (!StringUtil.isBlank(path)) {
			final String javaPath = resolveKey(JAVA_PATH_KEY);
			final String resourcesPath = resolveKey(RESOURCES_PATH_KEY);
			final String testJavaPath = resolveKey(TEST_JAVA_PATH_KEY);
			final String testResourcesPath = resolveKey(TEST_RESOURCES_PATH_KEY);

			classPathResource = path;
			classPathResource = StringUtil.remove(classPathResource, javaPath);
			classPathResource = StringUtil.remove(classPathResource, resourcesPath);
			classPathResource = StringUtil.remove(classPathResource, testJavaPath);
			classPathResource = StringUtil.remove(classPathResource, testResourcesPath);
			classPathResource = StringUtil.removeStart(classPathResource, "/");
			classPathResource = StringUtil.removeStart(classPathResource, "\\");
			classPathResource = StringUtil.removeEnd(classPathResource, "/");
			classPathResource = StringUtil.removeEnd(classPathResource, "\\");

			if (!StringUtil.isBlank(fileName)) {
				classPathResource += "/" + fileName;
			}
		}
		return classPathResource;
	}
}
