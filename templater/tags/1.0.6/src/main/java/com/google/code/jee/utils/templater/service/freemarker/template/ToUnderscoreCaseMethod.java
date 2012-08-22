package com.google.code.jee.utils.templater.service.freemarker.template;

import java.util.List;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.CollectionUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * The Class ToUnderscoreCaseMethod.
 */
public class ToUnderscoreCaseMethod implements TemplateMethodModel {

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
				final String stringValue = StringUtil.toUnderscoreCase(argValue, true);
				value = new SimpleScalar(stringValue);
			}
		}
		return value;
	}
}
