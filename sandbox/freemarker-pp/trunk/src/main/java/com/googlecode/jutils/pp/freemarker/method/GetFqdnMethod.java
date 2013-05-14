package com.googlecode.jutils.pp.freemarker.method;

import java.util.List;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.core.ClassUtil;
import com.googlecode.jutils.templater.freemarker.template.method.MethodUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class GetFqdnMethod implements TemplateMethodModel {

	@Override
	@SuppressWarnings("rawtypes")
	public TemplateModel exec(List args) throws TemplateModelException {
		SimpleScalar value = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String type = MethodUtil.getRequiredParameter(args, 0, String.class);
			if (!StringUtil.isBlank(type)) {
				String fqdn = "";
				final String packageName = ClassUtil.getPackageName(type);
				String className = ClassUtil.getShortClassName(type);
				if (!StringUtil.isBlank(packageName)) {
					fqdn = packageName + ".";
				}

				if (!StringUtil.isBlank(className)) {
					className = StringUtil.substringBefore(className, "<");

					fqdn += className;
				}

				value = new SimpleScalar(fqdn);
			}
		}
		return value;
	}
}
