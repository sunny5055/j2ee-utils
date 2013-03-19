package com.googlecode.jutils.templater.freemarker.template.method;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.core.ClassUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

// TODO: Auto-generated Javadoc
/**
 * The Class RandomMethod.
 */
public class RandomMethod implements TemplateMethodModel {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	public TemplateModel exec(List args) throws TemplateModelException {
		TemplateModel result = null;
		if (!CollectionUtil.isEmpty(args)) {
			final String type = MethodUtil.getRequiredParameter(args, 0, String.class);
			final String length = MethodUtil.getParameter(args, 1, String.class);

			if (!StringUtil.isBlank(type)) {
				Object randomValue = null;
				final String typeClassName = ClassUtil.getShortClassName(type);
				if (StringUtil.equals(typeClassName, "String")) {
					Integer lengthValue = 10;
					if (!StringUtil.isBlank(length)) {
						lengthValue = Integer.valueOf(length);
					}
					if (lengthValue > 50) {
						lengthValue = 50;
					}
					randomValue = RandomStringUtils.randomAlphabetic(lengthValue);
				} else if (StringUtil.equals(typeClassName, "Double") || StringUtil.equals(typeClassName, "double")) {
					randomValue = new Random().nextDouble();
				} else if (StringUtil.equals(typeClassName, "Float") || StringUtil.equals(typeClassName, "float")) {
					randomValue = new Random().nextFloat();
				} else if (StringUtil.equals(typeClassName, "Integer") || StringUtil.equals(typeClassName, "int")) {
					Integer lengthValue = 1000;
					if (!StringUtil.isBlank(length)) {
						lengthValue = Integer.valueOf(length);
					}
					randomValue = new Random().nextInt(lengthValue);
				} else if (StringUtil.equals(typeClassName, "Long") || StringUtil.equals(typeClassName, "long")) {
					randomValue = new Random().nextLong();
				} else if (StringUtil.equals(typeClassName, "Boolean") || StringUtil.equals(typeClassName, "boolean")) {
					randomValue = new Random().nextBoolean();
				}
				if (randomValue != null) {
					result = new SimpleScalar(randomValue.toString());
				} else {
					result = new SimpleScalar("");
				}
			}
		}
		return result;
	}
}
