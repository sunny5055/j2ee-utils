package com.googlecode.jutils.generator.freemarker.directive;

import java.io.IOException;
import java.util.Map;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.generator.engine.JpaEngine;
import com.googlecode.jutils.templater.freemarker.template.directive.DirectiveUtil;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

public class ResolvePackageForKeyDirective implements TemplateDirectiveModel {
	private static final String ENGINE_KEY = "engineKey";
	private static final String PARAM_NAME_PACKAGE_NAME = "packageName";
	private static final String PARAM_NAME_KEY = "key";
	private static final String PARAM_NAME_ASSIGN_TO = "assignTo";

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		final TemplateScalarModel engineKey = DirectiveUtil.getRequiredVariable(env, ENGINE_KEY, TemplateScalarModel.class);

		final TemplateScalarModel packageName = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_PACKAGE_NAME, TemplateScalarModel.class);
		final TemplateScalarModel key = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_KEY, TemplateScalarModel.class);
		final TemplateScalarModel assignTo = DirectiveUtil.getParameter(params, PARAM_NAME_ASSIGN_TO, TemplateScalarModel.class);

		final String result = resolvePackageName(env, engineKey.getAsString(), key.getAsString(), packageName.getAsString());

		if (assignTo != null) {
			env.setVariable(assignTo.getAsString(), new SimpleScalar(result));
		} else {
			env.getOut().write(result);
		}
	}

	private String resolvePackageName(Environment env, String engineKey, String key, String packageName) throws TemplateModelException {
		String result = null;
		if (!StringUtil.isBlank(engineKey) && !StringUtil.isBlank(key) && !StringUtil.isBlank(packageName)) {
			result = packageName;
			final TemplateHashModel properties = DirectiveUtil.getVariable(env, "properties", TemplateHashModel.class);
			if (properties != null) {
				final TemplateScalarModel entityPackageProp = (TemplateScalarModel) properties.get(engineKey + "." + JpaEngine.ENTITY_KEY + ".package");
				if (entityPackageProp != null) {
					result = StringUtil.removeEnd(result, entityPackageProp.getAsString());
				}

				final TemplateScalarModel packageProp = (TemplateScalarModel) properties.get(engineKey + "." + key + ".package");
				if (packageProp != null) {
					result += packageProp.getAsString();
				}
			}
		}
		return result;
	}
}
