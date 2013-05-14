package com.googlecode.jutils.pp.freemarker.directive;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.jutils.StringUtil;
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

public class ResolveKeyDirective implements TemplateDirectiveModel {
	private static final String PARAM_NAME_MAP = "map";
	private static final String PARAM_NAME_KEY = "key";
	private static final String PARAM_NAME_VALUE = "value";
	private static final String PARAM_NAME_ASSIGN_TO = "assignTo";
	private static String START_TAG = "{";
	private static String END_TAG = "}";
	private static String PATTERN = "\\{[^\\{\\}]+\\}";

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		final TemplateHashModel map = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_MAP, TemplateHashModel.class);

		final TemplateScalarModel key = DirectiveUtil.getRequiredParameter(params, PARAM_NAME_KEY, TemplateScalarModel.class);
		final TemplateScalarModel value = DirectiveUtil.getParameter(params, PARAM_NAME_VALUE, TemplateScalarModel.class);
		String stringValue = null;
		if (value != null) {
			stringValue = value.getAsString();
		}
		final TemplateScalarModel assignTo = DirectiveUtil.getParameter(params, PARAM_NAME_ASSIGN_TO, TemplateScalarModel.class);

		String result = resolve(map, key.getAsString());
		if (!StringUtil.isBlank(result) && !StringUtil.isBlank(stringValue)) {
			result = String.format(result, stringValue);
		}

		if (assignTo != null) {
			env.setVariable(assignTo.getAsString(), new SimpleScalar(result));
		} else {
			env.getOut().write(result);
		}
	}

	private String resolve(TemplateHashModel map, String key) throws TemplateModelException {
		String value = null;
		if (map != null && !StringUtil.isBlank(key)) {
			final TemplateScalarModel model = (TemplateScalarModel) map.get(key);
			if (model != null) {
				value = model.getAsString();
				final Pattern pattern = Pattern.compile(PATTERN);
				final Matcher matcher = pattern.matcher(value);
				while (matcher.find()) {
					final String currentMatch = matcher.group();
					if (!StringUtil.isBlank(currentMatch)) {
						final String replaceValue = resolve(map, getKey(currentMatch));
						if (replaceValue != null) {
							value = StringUtil.replace(value, currentMatch, replaceValue);
						}
					}
				}
			}

		}
		return value;
	}

	private String getKey(String aString) {
		String key = null;
		if (!StringUtil.isBlank(aString)) {
			key = StringUtil.remove(aString, START_TAG);
			key = StringUtil.remove(key, END_TAG);
		}
		return key;
	}
}
