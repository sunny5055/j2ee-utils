package com.googlecode.jutils.generator.engine;

import java.util.Map;

import com.googlecode.jutils.generator.formatter.impl.JavaFormatter;
import com.googlecode.jutils.generator.freemarker.method.GetClassNameMethod;
import com.googlecode.jutils.generator.freemarker.method.GetFqdnMethod;
import com.googlecode.jutils.generator.freemarker.method.GetImportsMethod;
import com.googlecode.jutils.generator.freemarker.method.GetModifiersMethod;
import com.googlecode.jutils.generator.freemarker.method.GetPackageMethod;
import com.googlecode.jutils.generator.freemarker.method.GetTypeMethod;
import com.googlecode.jutils.generator.freemarker.method.IsPrimitiveMethod;

public abstract class AbstractJavaEngine extends AbstractEngine {

	public AbstractJavaEngine() {
		super();
	}

	@Override
	protected void init() {
		super.init();

		this.defaultProperties.put("path.java", "src/main/java");
		this.defaultProperties.put("path.resources", "src/main/resources");
		this.defaultProperties.put("path.webapp", "src/main/webapp");
		this.defaultProperties.put("path.config", "src/main/config");
		this.defaultProperties.put("path.test_java", "src/test/java");
		this.defaultProperties.put("path.test_resources", "src/test/resources");

		this.defaultFormatters.put("java", new JavaFormatter());
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
}
