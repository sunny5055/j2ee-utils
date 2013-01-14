package com.googlecode.jutils.generator.freemarker.method;

import java.util.List;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.core.ClassUtil;
import com.googlecode.jutils.templater.freemarker.template.method.MethodUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class IsPrimitiveMethod implements TemplateMethodModel {

	@Override
	@SuppressWarnings("rawtypes")
	public TemplateModel exec(List args) throws TemplateModelException {
		TemplateModel value = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String type = MethodUtil.getRequiredParameter(args, 0, String.class);
			if (!StringUtil.isBlank(type)) {
				final String className = ClassUtil.getShortClassName(type);
				if (!StringUtil.isBlank(className)) {
					Class<?> clazz = null;
					try {
						clazz = ClassUtil.getClass(className);
						if (clazz.isPrimitive()) {
							value = new SimpleScalar(Boolean.TRUE.toString());
						} else {
							value = new SimpleScalar(Boolean.FALSE.toString());
						}
					} catch (final ClassNotFoundException e) {
						value = new SimpleScalar(Boolean.FALSE.toString());
					}
				}
			}
		}
		return value;
	}
}
