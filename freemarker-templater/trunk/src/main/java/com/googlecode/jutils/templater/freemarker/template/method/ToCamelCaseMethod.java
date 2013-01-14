package com.googlecode.jutils.templater.freemarker.template.method;

import java.util.List;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * The Class ToCamelCaseMethod.
 */
public class ToCamelCaseMethod implements TemplateMethodModel {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public TemplateModel exec(List args) throws TemplateModelException {
		SimpleScalar result = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String value = MethodUtil.getRequiredParameter(args, 0, String.class);
			if (!StringUtil.isBlank(value)) {
				final String stringValue = StringUtil.toCamelCase(value);
				result = new SimpleScalar(stringValue);
			}
		}
		return result;
	}
}
