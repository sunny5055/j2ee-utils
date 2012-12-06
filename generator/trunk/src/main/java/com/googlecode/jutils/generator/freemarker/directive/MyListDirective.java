package com.googlecode.jutils.generator.freemarker.directive;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

public class MyListDirective implements TemplateDirectiveModel {
	private static final String PARAM_NAME_LIST = "list";
	private static final String PARAM_NAME_VAR = "var";
	private static final String PARAM_NAME_LIST_INDEX = "_index";
	private static final String PARAM_NAME_LIST_HAS_NEXT = "_has_next";
	private static final String PARAM_NAME_SEPARATOR = "separator";
	private static final String PARAM_NAME_ASSIGN_TO = "assignTo";
	private static final String DEFAULT_SEPARATOR = ", ";

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		final TemplateSequenceModel list = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_LIST, TemplateSequenceModel.class);
		final TemplateScalarModel var = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_VAR, TemplateScalarModel.class);
		final TemplateScalarModel separator = DirectiveUtil.getParameter(params, PARAM_NAME_SEPARATOR, TemplateScalarModel.class);
		final TemplateScalarModel assignTo = DirectiveUtil.getParameter(params, PARAM_NAME_ASSIGN_TO, TemplateScalarModel.class);

		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			final TemplateModel element = list.get(i);

			env.setVariable(var.getAsString(), element);
			env.setVariable(var.getAsString() + PARAM_NAME_LIST_INDEX, new SimpleNumber(i));
			TemplateBooleanModel hasNext = null;
			if (i + 1 < list.size()) {
				hasNext = TemplateBooleanModel.TRUE;
			} else {
				hasNext = TemplateBooleanModel.FALSE;
			}
			env.setVariable(var.getAsString() + PARAM_NAME_LIST_HAS_NEXT, hasNext);

			builder.append(DirectiveUtil.renderBody(body));
			if (i + 1 < list.size()) {
				if (separator != null) {
					builder.append(separator.getAsString());
				} else {
					builder.append(DEFAULT_SEPARATOR);
				}
			}

			env.setVariable(var.getAsString(), null);
			env.setVariable(var.getAsString() + PARAM_NAME_LIST_INDEX, null);
			env.setVariable(var.getAsString() + PARAM_NAME_LIST_HAS_NEXT, null);
		}

		if (assignTo != null) {
			env.setVariable(assignTo.getAsString(), new SimpleScalar(builder.toString()));
		} else {
			env.getOut().write(builder.toString());
		}
	}
}
