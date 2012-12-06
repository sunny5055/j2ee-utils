package com.googlecode.jutils.generator.freemarker.directive;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.googlecode.jutils.StringUtil;

import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

public class DirectiveUtil {
	@SuppressWarnings({ "rawtypes" })
	public static <T> T getRequiredParameter(Map params, String key, Class<T> clazz) throws TemplateModelException {
		T value = null;
		if (!StringUtil.isBlank(key) && clazz != null) {
			if (params.get(key) == null) {
				throw new TemplateModelException("The \"" + key + "\" parameter is missing.");
			}

			value = getParameter(params, key, clazz);
		}
		return value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getParameter(Map params, String key, Class<T> clazz) throws TemplateModelException {
		T value = null;
		if (!StringUtil.isBlank(key) && clazz != null) {
			value = (T) params.get(key);
			if (value != null && !clazz.isInstance(value)) {
				throw new TemplateModelException("The \"" + key + "\" parameter must be a " + clazz.getSimpleName());
			}
		}
		return value;
	}

	public static String renderBody(TemplateDirectiveBody body) throws TemplateException, IOException {
		String result = null;
		if (body != null) {
			final StringWriter writer = new StringWriter();
			body.render(writer);
			result = writer.toString();
			if (!StringUtil.isBlank(result)) {
				result = StringUtil.trimToEmpty(result);
			}
		}
		return result;
	}
}