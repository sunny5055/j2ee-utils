package com.google.code.jee.utils.templater.service.freemarker.template;

import java.util.List;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.CollectionUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class ToCamelCaseMethod implements TemplateMethodModel {
	@Override
	@SuppressWarnings("rawtypes")
	public TemplateModel exec(List args) throws TemplateModelException {
		SimpleScalar value = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String argValue = (String) args.get(0);
			if (!StringUtil.isBlank(argValue)) {
				final String stringValue = StringUtil.toCamelCase(argValue);
				value = new SimpleScalar(stringValue);
			}
		}
		return value;
	}
}
