package com.google.code.jee.utils.generator.freemarker.template;

import java.lang.reflect.Type;
import java.util.List;

import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.core.ClassUtil;

import freemarker.ext.beans.BeanModel;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class GetTypeParameterFromListMethod implements TemplateMethodModelEx {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		BeanModel value = null;
		if (!CollectionUtil.isEmpty(args)) {
			final AdapterTemplateModel argValue = (AdapterTemplateModel) args.get(0);
			if (argValue != null) {
				final Type wrappedObject = (Type) argValue.getAdaptedObject(Type.class);
				final Class<?>[] types = ClassUtil.getTypeParameters(wrappedObject);
				if (!ArrayUtil.isEmpty(types)) {
					final Class<?> type = types[0];
					value = new BeanModel(type, new DefaultObjectWrapper());
				}
			}
		}
		return value;
	}
}
