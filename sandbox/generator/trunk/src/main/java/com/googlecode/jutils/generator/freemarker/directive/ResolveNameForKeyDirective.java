package com.googlecode.jutils.generator.freemarker.directive;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.generator.engine.AbstractEngine;
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

public class ResolveNameForKeyDirective implements TemplateDirectiveModel {
	private static final String ENGINE_KEY = "engineKey";
	private static final String PARAM_NAME_KEY = "key";
	private static final String PARAM_NAME_VALUE = "value";
	private static final String PARAM_NAME_ASSIGN_TO = "assignTo";

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		final TemplateScalarModel engineKey = DirectiveUtil.getRequiredVariable(env, ENGINE_KEY, TemplateScalarModel.class);

		final TemplateScalarModel key = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_KEY, TemplateScalarModel.class);
		final TemplateScalarModel value = DirectiveUtil.getParameter(params, PARAM_NAME_VALUE, TemplateScalarModel.class);
		String stringValue = null;
		if (value != null) {
			stringValue = value.getAsString();
		}
		final TemplateScalarModel assignTo = DirectiveUtil.getParameter(params, PARAM_NAME_ASSIGN_TO, TemplateScalarModel.class);

		final String name = resolveName(env, engineKey.getAsString(), key.getAsString(), stringValue);
		if (assignTo != null) {
			env.setVariable(assignTo.getAsString(), new SimpleScalar(name));
		} else {
			env.getOut().write(name);
		}
	}

	protected String resolveName(Environment env, String engineKey, String key, String value) throws TemplateModelException {
		String name = null;
		if (!StringUtil.isBlank(engineKey) && !StringUtil.isBlank(key)) {
			final TemplateHashModel properties = DirectiveUtil.getVariable(env, "properties", TemplateHashModel.class);
			if (properties != null) {
				final TemplateScalarModel namePattern = (TemplateScalarModel) properties.get(engineKey + "." + AbstractEngine.FILE_NAME_PATTERN + "." + key);
				if (namePattern != null) {
					if (!StringUtil.isBlank(value)) {
						name = String.format(namePattern.getAsString(), value);
					} else {
						name = namePattern.getAsString();
					}

					if (!StringUtil.isBlank(name)) {
						name = FilenameUtils.getBaseName(name);
					}
				}
			}
		}
		return name;
	}
}
