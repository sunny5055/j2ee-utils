package com.googlecode.jutils.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;

/**
 * Class AnnotationUtil.
 */
public final class AnnotationUtil {

	/**
	 * Constructor of the class AnnotationUtil
	 */
	private AnnotationUtil() {
	}

	/**
	 * Method isAnnotationPresent.
	 * 
	 * @param annotationClass
	 *            the annotationClass
	 * @param annotatedElement
	 *            the annotatedElement
	 * @return boolean
	 */
	public static boolean isAnnotationPresent(Class<? extends Annotation> annotationClass, AnnotatedElement annotatedElement) {
		boolean annotationPresent = false;
		if (annotationClass != null && annotatedElement != null) {
			annotationPresent = annotatedElement.isAnnotationPresent(annotationClass);
		}
		return annotationPresent;
	}

	/**
	 * Method getAnnotation.
	 * 
	 * @param <E>
	 *            the element type
	 * @param annotationClass
	 *            the annotationClass
	 * @param annotatedElement
	 *            the annotatedElement
	 * @return Annotation
	 */
	public static <E extends Annotation> E getAnnotation(Class<E> annotationClass, AnnotatedElement annotatedElement) {
		E annotation = null;
		if (annotationClass != null && annotatedElement != null) {
			annotation = annotatedElement.getAnnotation(annotationClass);
		}
		return annotation;
	}

	/**
	 * Method getAnnotation.
	 * 
	 * @param <E>
	 *            the element type
	 * @param annotationClass
	 *            the annotationClass
	 * @param clazz
	 *            the clazz
	 * @return Annotation
	 */
	public static <E extends Annotation> Map<Field, E> getFieldAnnotations(Class<E> annotationClass, Class<?> clazz) {
		final Map<Field, E> annotations = new HashMap<Field, E>();
		if (annotationClass != null && clazz != null) {
			final List<Field> fields = ClassUtil.getFields(clazz);
			if (!CollectionUtils.isEmpty(fields)) {
				for (final Field field : fields) {
					if (isAnnotationPresent(annotationClass, field)) {
						final E annotation = getAnnotation(annotationClass, field);
						annotations.put(field, annotation);
					}
				}
			}
		}
		return annotations;
	}

	/**
	 * Gets the annotations.
	 * 
	 * @param <E>
	 *            the element type
	 * @param annotationClass
	 *            the annotation class
	 * @param clazz
	 *            the clazz
	 * @return the annotations
	 */
	public static <E extends Annotation> Map<String, E> getAnnotations(Class<E> annotationClass, Class<?> clazz) {
		final Map<String, E> annotations = new HashMap<String, E>();
		if (annotationClass != null && clazz != null) {
			final List<Field> fields = ClassUtil.getFields(clazz);
			if (!CollectionUtils.isEmpty(fields)) {
				for (final Field field : fields) {
					if (isAnnotationPresent(annotationClass, field)) {
						final E annotation = getAnnotation(annotationClass, field);
						annotations.put(field.getName(), annotation);
					}
				}
			}
		}
		return annotations;
	}

	/**
	 * Gets the field annotation.
	 * 
	 * @param <E>
	 *            the element type
	 * @param annotationClass
	 *            the annotation class
	 * @param clazz
	 *            the clazz
	 * @param fieldName
	 *            the field name
	 * @return the field annotation
	 */
	public static <E extends Annotation> E getFieldAnnotation(Class<E> annotationClass, Class<?> clazz, String fieldName) {
		E annotation = null;
		if (annotationClass != null && clazz != null && !StringUtil.isBlank(fieldName)) {
			final Field field = ClassUtil.findField(clazz, fieldName);
			if (field != null) {
				annotation = field.getAnnotation(annotationClass);
			}
		}
		return annotation;
	}

	/**
	 * Gets the field with annotation.
	 * 
	 * @param <E>
	 *            the element type
	 * @param annotationClass
	 *            the annotation class
	 * @param clazz
	 *            the clazz
	 * @return the field with annotation
	 */
	public static <E extends Annotation> List<Field> getFieldsWithAnnotation(Class<E> annotationClass, Class<?> clazz) {
		List<Field> fieldsWithAnnotation = null;
		if (annotationClass != null && clazz != null) {
			fieldsWithAnnotation = new ArrayList<Field>();
			final List<Field> fields = ClassUtil.getFields(clazz);
			if (!CollectionUtil.isEmpty(fields)) {
				for (final Field field : fields) {
					if (isAnnotationPresent(annotationClass, field)) {
						fieldsWithAnnotation.add(field);
					}
				}
			}
		}
		return fieldsWithAnnotation;
	}
}
