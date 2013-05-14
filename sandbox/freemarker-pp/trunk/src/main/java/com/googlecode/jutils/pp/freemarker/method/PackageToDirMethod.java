package com.googlecode.jutils.pp.freemarker.method;

import java.util.List;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.templater.freemarker.template.method.MethodUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class PackageToDirMethod implements TemplateMethodModel {

	@Override
	@SuppressWarnings("rawtypes")
	public TemplateModel exec(List args) throws TemplateModelException {
		SimpleScalar value = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String packageName = MethodUtil.getRequiredParameter(args, 0, String.class);

			if (!StringUtil.isBlank(packageName)) {
				final String directory = StringUtil.replace(packageName, ".", "/");
				value = new SimpleScalar(directory);
			}
		}
		return value;
	}
}
