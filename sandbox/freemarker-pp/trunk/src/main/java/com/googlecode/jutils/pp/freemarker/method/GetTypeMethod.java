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

public class GetTypeMethod implements TemplateMethodModel {

	@Override
	@SuppressWarnings({ "rawtypes" })
	public TemplateModel exec(List args) throws TemplateModelException {
		TemplateModel result = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String type = MethodUtil.getRequiredParameter(args, 0, String.class);
			final String value = MethodUtil.getParameter(args, 1, String.class);
			final String key = MethodUtil.getParameter(args, 2, String.class);

			if (!StringUtil.isBlank(type)) {
				String fullType = null;
				final String typeClassName = ClassUtil.getShortClassName(type);
				if (!StringUtil.isBlank(value)) {
					final String valueClassName = ClassUtil.getShortClassName(value);
					if (StringUtil.equalsIgnoreCase(typeClassName, "array")) {
						fullType = valueClassName + "[]";
					} else if (!StringUtil.isBlank(key)) {
						final String keyClassName = ClassUtil.getShortClassName(key);
						fullType = typeClassName + "<" + keyClassName + ", " + valueClassName + ">";
					} else {
						fullType = typeClassName + "<" + valueClassName + ">";
					}
				} else {
					fullType = typeClassName;
				}

				result = new SimpleScalar(fullType);
			}
		}
		return result;
	}
}
