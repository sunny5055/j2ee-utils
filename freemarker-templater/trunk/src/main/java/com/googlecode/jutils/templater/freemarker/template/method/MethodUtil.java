package com.googlecode.jutils.templater.freemarker.template.method;

import java.util.List;

import com.googlecode.jutils.collection.CollectionUtil;

import freemarker.template.TemplateModelException;

/**
 * The Class MethodUtil.
 */
public class MethodUtil {

	/**
	 * Gets the required parameter.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param params
	 *            the params
	 * @param index
	 *            the index
	 * @param clazz
	 *            the clazz
	 * @return the required parameter
	 * @throws TemplateModelException
	 *             the template model exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T getRequiredParameter(List params, int index, Class<T> clazz) throws TemplateModelException {
		T value = null;
		if (!CollectionUtil.isEmpty(params) && clazz != null) {
			if (CollectionUtil.get(params, index) == null) {
				throw new TemplateModelException("Parameter [" + index + "] is missing.");
			}
			value = getParameter(params, index, clazz);
		}
		return value;
	}

	/**
	 * Gets the parameter.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param params
	 *            the params
	 * @param index
	 *            the index
	 * @param clazz
	 *            the clazz
	 * @return the parameter
	 * @throws TemplateModelException
	 *             the template model exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T getParameter(List params, int index, Class<T> clazz) throws TemplateModelException {
		T value = null;
		if (!CollectionUtil.isEmpty(params) && clazz != null) {
			final Object tmpValue = CollectionUtil.get(params, index);

			if (tmpValue != null) {
				if (!clazz.isInstance(tmpValue)) {
					throw new TemplateModelException("Parameter [" + index + "] must be a " + clazz.getSimpleName());
				} else {
					value = (T) tmpValue;
				}
			}
		}
		return value;
	}
}