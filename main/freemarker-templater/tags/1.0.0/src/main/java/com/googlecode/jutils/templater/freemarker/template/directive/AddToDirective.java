package com.googlecode.jutils.templater.freemarker.template.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jutils.StringUtil;

import freemarker.core.Environment;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

public class AddToDirective implements TemplateDirectiveModel {
	private static final String PARAM_NAME_ELEMENT = "element";
	private static final String PARAM_NAME_ASSIGN_TO = "assignTo";

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		final TemplateModel element = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_ELEMENT, TemplateModel.class);
		final TemplateScalarModel assignTo = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_ASSIGN_TO, TemplateScalarModel.class);

		final List<Object> list = getList(env, assignTo.getAsString());
		addElement(list, element);

		env.setVariable(assignTo.getAsString(), new SimpleSequence(list));
	}

	private void addElement(final List<Object> list, final TemplateModel element) throws TemplateModelException {
		if (list != null && element != null) {
			if (element instanceof TemplateScalarModel) {
				final TemplateScalarModel value = (TemplateScalarModel) element;
				list.add(value.getAsString());
			} else {
				final TemplateSequenceModel values = (TemplateSequenceModel) element;
				for (int i = 0; i < values.size(); i++) {
					final TemplateModel value = values.get(i);
					addElement(list, value);
				}
			}
		}
	}

	private List<Object> getList(Environment env, String assignTo) throws TemplateModelException {
		List<Object> list = null;
		if (env != null && !StringUtil.isBlank(assignTo)) {
			final SimpleSequence sequence = (SimpleSequence) env.getVariable(assignTo);

			list = new ArrayList<Object>();
			if (sequence != null) {
				for (int i = 0; i < sequence.size(); i++) {
					final Object value = sequence.get(i);
					list.add(value);
				}
			}
		}
		return list;
	}
}
