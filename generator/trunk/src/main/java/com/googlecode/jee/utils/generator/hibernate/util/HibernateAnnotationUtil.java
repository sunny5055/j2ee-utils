package com.googlecode.jee.utils.generator.hibernate.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.core.AnnotationUtil;
import com.googlecode.jutils.dal.dto.AbstractDto;

public final class HibernateAnnotationUtil {

	private HibernateAnnotationUtil() {
	}

	public static <E extends AbstractDto<?>> Field getPrimaryKey(Class<E> clazz) {
		Field primaryKey = null;
		if (clazz != null) {
			List<Field> idFields = null;

			idFields = AnnotationUtil.getFieldsWithAnnotation(Id.class, clazz);
			primaryKey = CollectionUtil.getFirstElementFromList(idFields);

			if (primaryKey == null) {
				idFields = AnnotationUtil.getFieldsWithAnnotation(EmbeddedId.class, clazz);
				primaryKey = CollectionUtil.getFirstElementFromList(idFields);
			}
		}
		return primaryKey;
	}

	public static <E extends AbstractDto<?>> List<Field> getAllFields(Class<E> clazz) {
		List<Field> fields = null;
		if (clazz != null) {
			fields = new ArrayList<Field>();

			final List<Field> columnFields = getColumnFields(clazz);
			if (!CollectionUtil.isEmpty(columnFields)) {
				fields.addAll(columnFields);
			}

			final List<Field> associations = getAssociations(clazz);
			if (!CollectionUtil.isEmpty(associations)) {
				fields.addAll(associations);
			}
		}
		return fields;
	}

	public static <E extends AbstractDto<?>> List<Field> getUniqueFields(Class<E> clazz) {
		List<Field> uniqueFields = null;
		if (clazz != null) {
			final List<Field> fields = getColumnFields(clazz);
			if (!CollectionUtil.isEmpty(fields)) {
				uniqueFields = new ArrayList<Field>();

				for (final Field field : fields) {
					final Column column = AnnotationUtil.getAnnotation(Column.class, field);
					if (column != null && column.unique()) {
						uniqueFields.add(field);
					}
				}
			}
		}
		return uniqueFields;
	}

	public static <E extends AbstractDto<?>> List<Field> getAssociations(Class<E> clazz) {
		List<Field> associations = null;
		if (clazz != null) {
			associations = new ArrayList<Field>();

			final List<Field> manyToOneFields = getManyToOneFields(clazz);
			if (!CollectionUtil.isEmpty(manyToOneFields)) {
				associations.addAll(manyToOneFields);
			}

			final List<Field> oneToManyFields = getOneToManyFields(clazz);
			if (!CollectionUtil.isEmpty(oneToManyFields)) {
				associations.addAll(oneToManyFields);
			}

			final List<Field> manyToManyFields = getManyToManyFields(clazz);
			if (!CollectionUtil.isEmpty(manyToManyFields)) {
				associations.addAll(manyToManyFields);
			}
		}
		return associations;
	}

	public static <E extends AbstractDto<?>> List<Field> getColumnFields(Class<E> clazz) {
		List<Field> fields = null;
		if (clazz != null) {
			final Map<Field, Column> mapFields = AnnotationUtil.getFieldAnnotations(Column.class, clazz);
			if (!MapUtil.isEmpty(mapFields)) {
				final Field primaryKey = getPrimaryKey(clazz);
				if (primaryKey != null) {
					mapFields.remove(primaryKey);
				}
				fields = CollectionUtil.toList(mapFields.keySet());
			}
		}
		return fields;
	}

	public static <E extends AbstractDto<?>> List<Field> getManyToOneFields(Class<E> clazz) {
		List<Field> fields = null;
		if (clazz != null) {
			final Map<Field, ManyToOne> mapFields = AnnotationUtil.getFieldAnnotations(ManyToOne.class, clazz);
			fields = CollectionUtil.toList(mapFields.keySet());
		}
		return fields;
	}

	public static <E extends AbstractDto<?>> List<Field> getOneToManyFields(Class<E> clazz) {
		List<Field> fields = null;
		if (clazz != null) {
			final Map<Field, OneToMany> mapFields = AnnotationUtil.getFieldAnnotations(OneToMany.class, clazz);
			fields = CollectionUtil.toList(mapFields.keySet());
		}
		return fields;
	}

	public static <E extends AbstractDto<?>> List<Field> getManyToManyFields(Class<E> clazz) {
		List<Field> fields = null;
		if (clazz != null) {
			final Map<Field, ManyToMany> mapFields = AnnotationUtil.getFieldAnnotations(ManyToMany.class, clazz);
			fields = CollectionUtil.toList(mapFields.keySet());
		}
		return fields;
	}
}
