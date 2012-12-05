package com.googlecode.jutils.generator.templater;

import java.util.List;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.core.ClassUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class GetTypeMethod implements TemplateMethodModel {

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TemplateModel exec(List args) throws TemplateModelException {
		TemplateModel result = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String type = (String) CollectionUtil.get(args, 0);
			final String value = (String) CollectionUtil.get(args, 1);
			final String key = (String) CollectionUtil.get(args, 2);

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
