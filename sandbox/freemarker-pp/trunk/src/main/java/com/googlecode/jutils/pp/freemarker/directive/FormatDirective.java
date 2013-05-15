package com.googlecode.jutils.pp.freemarker.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.templater.freemarker.template.directive.DirectiveUtil;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

public class FormatDirective implements TemplateDirectiveModel {
	private static final String PARAM_NAME_FORMAT = "format";
	private static final String PARAM_NAME_VALUES = "values";
	private static final String PARAM_NAME_ASSIGN_TO = "assignTo";

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		final TemplateScalarModel format = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_FORMAT, TemplateScalarModel.class);
		final TemplateSequenceModel values = DirectiveUtil.getParameter(params, PARAM_NAME_VALUES, TemplateSequenceModel.class);		
		Object[] args = null;
		if (values != null) {
			final List<String> list = new ArrayList<String>();
			for (int i = 0; i < values.size(); i++) {
				final TemplateModel v = values.get(i);
				if (v instanceof TemplateScalarModel) {
					list.add(((TemplateScalarModel) v).getAsString());
				} else {
					list.add(values.get(i).toString());
				}
			}
			args = list.toArray(new String[0]);
		}

		final TemplateScalarModel assignTo = DirectiveUtil.getParameter(params, PARAM_NAME_ASSIGN_TO, TemplateScalarModel.class);

		final String name = format(env, format.getAsString(), args);
		if (assignTo != null) {
			env.setVariable(assignTo.getAsString(), new SimpleScalar(name));
		} else {
			env.getOut().write(name);
		}
	}

	protected String format(Environment env, String format, Object... values) throws TemplateModelException {
		String formattedValue = null;
		if (!StringUtil.isBlank(format)) {
			if (!ArrayUtil.isEmpty(values)) {
				formattedValue = String.format(format, values);
			} else {
				formattedValue = format;
			}
		}
		return formattedValue;
	}
}
