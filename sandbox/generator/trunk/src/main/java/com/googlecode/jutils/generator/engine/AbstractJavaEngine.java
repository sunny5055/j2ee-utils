package com.googlecode.jutils.generator.engine;

import java.util.Map;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.generator.formatter.impl.JavaFormatter;
import com.googlecode.jutils.generator.freemarker.directive.ResolvePackageForKeyDirective;
import com.googlecode.jutils.generator.freemarker.method.GetClassNameMethod;
import com.googlecode.jutils.generator.freemarker.method.GetFqdnMethod;
import com.googlecode.jutils.generator.freemarker.method.GetImportsMethod;
import com.googlecode.jutils.generator.freemarker.method.GetModifiersMethod;
import com.googlecode.jutils.generator.freemarker.method.GetPackageMethod;
import com.googlecode.jutils.generator.freemarker.method.GetTypeMethod;
import com.googlecode.jutils.generator.freemarker.method.IsPrimitiveMethod;

public abstract class AbstractJavaEngine extends AbstractEngine {
	public static final String JAVA_PATH_KEY = "path.java";
	public static final String RESOURCES_PATH_KEY = "path.resources";
	public static final String WEBAPP_PATH_KEY = "path.webapp";
	public static final String CONFIG_PATH_KEY = "path.config";
	public static final String TEST_JAVA_PATH_KEY = "path.test_java";
	public static final String TEST_RESOURCES_PATH_KEY = "path.test_resources";
	public static final String MODULE_API = "module_api";
	public static final String MODULE_SERVICE = "module_service";
	public static final String MODULE_WEB = "module_web";

	public AbstractJavaEngine() {
		super();
	}

	@Override
	protected void init() {
		super.init();

		loadConfigFile("classpath:/config/abstract-java-engine.properties");

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

			data.put("resolvePackageForKey", new ResolvePackageForKeyDirective());
		}
	}

	protected String getClassPathResource(String path) {
		return getClassPathResource(path, null);
	}

	protected String getClassPathResource(String path, String fileName) {
		String classPathResource = null;
		if (!StringUtil.isBlank(path)) {
			final String moduleApiPath = resolveKey(MODULE_API);
			final String moduleServicePath = resolveKey(MODULE_SERVICE);
			final String moduleWebPath = resolveKey(MODULE_WEB);

			final String javaPath = resolveKey(JAVA_PATH_KEY);
			final String resourcesPath = resolveKey(RESOURCES_PATH_KEY);
			final String testJavaPath = resolveKey(TEST_JAVA_PATH_KEY);
			final String testResourcesPath = resolveKey(TEST_RESOURCES_PATH_KEY);

			classPathResource = path;
			classPathResource = StringUtil.remove(classPathResource, moduleApiPath);
			classPathResource = StringUtil.remove(classPathResource, moduleServicePath);
			classPathResource = StringUtil.remove(classPathResource, moduleWebPath);

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
