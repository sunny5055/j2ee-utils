package com.googlecode.jutils.generator.freemarker.method;

import java.util.List;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.templater.freemarker.template.method.MethodUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class GetModifiersMethod implements TemplateMethodModel {

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TemplateModel exec(List args) throws TemplateModelException {
		TemplateModel value = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String abstractValue = MethodUtil.getRequiredParameter(args, 0, String.class);
			final String staticValue = MethodUtil.getRequiredParameter(args, 1, String.class);
			final String finalValue = MethodUtil.getRequiredParameter(args, 2, String.class);

			String modifiers = "";
			if (StringUtil.equalsIgnoreCase(abstractValue, "true")) {
				if (!StringUtil.isBlank(modifiers)) {
					modifiers += " ";
				}
				modifiers += "abstract";
			}
			if (StringUtil.equalsIgnoreCase(staticValue, "true")) {
				if (!StringUtil.isBlank(modifiers)) {
					modifiers += " ";
				}
				modifiers += "static";
			}
			if (StringUtil.equalsIgnoreCase(finalValue, "true")) {
				if (!StringUtil.isBlank(modifiers)) {
					modifiers += " ";
				}
				modifiers += "final";
			}

			value = new SimpleScalar(modifiers);
		}
		return value;
	}
}
