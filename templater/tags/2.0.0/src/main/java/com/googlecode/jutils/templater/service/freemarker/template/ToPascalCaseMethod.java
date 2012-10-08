package com.googlecode.jutils.templater.service.freemarker.template;

import java.util.List;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * The Class ToPascalCaseMethod.
 */
public class ToPascalCaseMethod implements TemplateMethodModel {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public TemplateModel exec(List args) throws TemplateModelException {
		SimpleScalar value = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String argValue = (String) args.get(0);
			if (!StringUtil.isBlank(argValue)) {
				final String stringValue = StringUtil.toPascalCase(argValue);
				value = new SimpleScalar(stringValue);
			}
		}
		return value;
	}
}
