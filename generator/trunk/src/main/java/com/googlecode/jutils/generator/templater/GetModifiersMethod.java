package com.googlecode.jutils.generator.templater;

import java.util.List;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;

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
			final String abstractValue = (String) CollectionUtil.get(args, 0);
			final String staticValue = (String) CollectionUtil.get(args, 1);
			final String finalValue = (String) CollectionUtil.get(args, 2);

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
