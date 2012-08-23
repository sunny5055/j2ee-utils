package com.google.code.jee.utils.generator.freemarker.template;

import java.lang.reflect.Field;
import java.util.List;

import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.dal.dto.AbstractDto;
import com.google.code.jee.utils.generator.util.HibernateAnnotationUtil;

import freemarker.ext.beans.BeanModel;
import freemarker.template.AdapterTemplateModel;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class GetPrimaryKeyFieldMethod implements TemplateMethodModelEx {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object exec(List args) throws TemplateModelException {
		BeanModel value = null;
		if (!CollectionUtil.isEmpty(args)) {
			final AdapterTemplateModel argValue = (AdapterTemplateModel) args.get(0);
			if (argValue != null) {
				final Class<AbstractDto<?>> wrappedObject = (Class<AbstractDto<?>>) argValue.getAdaptedObject(Class.class);
				final Field primaryKey = HibernateAnnotationUtil.getPrimaryKey(wrappedObject);
				if (primaryKey != null) {
					value = new BeanModel(primaryKey, new DefaultObjectWrapper());
				}
			}
		}
		return value;
	}
}
