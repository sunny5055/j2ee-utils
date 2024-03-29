package com.googlecode.jutils.templater.freemarker.template.directive;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.googlecode.jutils.StringUtil;

import freemarker.core.Environment;
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
			final Object tmpValue = params.get(key);

			if (tmpValue != null) {
				if (!clazz.isInstance(tmpValue)) {
					throw new TemplateModelException("The \"" + key + "\" parameter must be a " + clazz.getSimpleName());
				} else {
					value = (T) tmpValue;
				}
			}
		}
		return value;
	}

	public static <T> T getRequiredVariable(Environment env, String key, Class<T> clazz) throws TemplateModelException {
		T value = null;
		if (!StringUtil.isBlank(key) && clazz != null) {
			if (env.getVariable(key) == null) {
				throw new TemplateModelException("The \"" + key + "\" variable is missing.");
			}

			value = getVariable(env, key, clazz);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getVariable(Environment env, String key, Class<T> clazz) throws TemplateModelException {
		T value = null;
		if (!StringUtil.isBlank(key) && clazz != null) {
			final Object tmpValue = env.getVariable(key);

			if (tmpValue != null) {
				if (!clazz.isInstance(tmpValue)) {
					throw new TemplateModelException("The \"" + key + "\" variable must be a " + clazz.getSimpleName());
				} else {
					value = (T) tmpValue;
				}
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