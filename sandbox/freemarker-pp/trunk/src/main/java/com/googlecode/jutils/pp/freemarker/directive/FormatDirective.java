package com.googlecode.jutils.pp.freemarker.directive;

import java.io.IOException;
import java.util.Map;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.templater.freemarker.template.directive.DirectiveUtil;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

public class FormatDirective implements TemplateDirectiveModel {
	private static final String PARAM_NAME_FORMAT = "format";
	private static final String PARAM_NAME_VALUE = "value";
	private static final String PARAM_NAME_ASSIGN_TO = "assignTo";

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		final TemplateScalarModel format = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_FORMAT, TemplateScalarModel.class);
		final TemplateScalarModel value = DirectiveUtil.getParameter(params, PARAM_NAME_VALUE, TemplateScalarModel.class);
		String stringValue = null;
		if (value != null) {
			stringValue = value.getAsString();
		}
		final TemplateScalarModel assignTo = DirectiveUtil.getParameter(params, PARAM_NAME_ASSIGN_TO, TemplateScalarModel.class);

		final String name = format(env, format.getAsString(), stringValue);
		if (assignTo != null) {
			env.setVariable(assignTo.getAsString(), new SimpleScalar(name));
		} else {
			env.getOut().write(name);
		}
	}

	protected String format(Environment env, String format, String value) throws TemplateModelException {
		String formattedValue = null;
		if (!StringUtil.isBlank(format)) {
			if (!StringUtil.isBlank(value)) {
				formattedValue = String.format(format, value);
			} else {
				formattedValue = format;
			}
		}
		return formattedValue;
	}
}
