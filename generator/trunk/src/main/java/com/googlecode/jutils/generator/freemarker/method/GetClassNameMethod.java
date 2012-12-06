package com.googlecode.jutils.generator.freemarker.method;

import java.util.List;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.core.ClassUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class GetClassNameMethod implements TemplateMethodModel {

	@Override
	@SuppressWarnings("rawtypes")
	public TemplateModel exec(List args) throws TemplateModelException {
		SimpleScalar value = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String type = (String) args.get(0);
			if (!StringUtil.isBlank(type)) {
				final String className = ClassUtil.getShortClassName(type);
				value = new SimpleScalar(className);
			}
		}
		return value;
	}
}
