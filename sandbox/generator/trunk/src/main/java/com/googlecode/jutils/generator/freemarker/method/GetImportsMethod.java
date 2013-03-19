package com.googlecode.jutils.generator.freemarker.method;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.core.ClassUtil;
import com.googlecode.jutils.templater.freemarker.template.method.MethodUtil;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateBooleanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateSequenceModel;

public class GetImportsMethod implements TemplateMethodModelEx {
	private static final String[] IGNORED_TYPES = { "array", "String", "java.lang.String", "int", "Integer", "java.lang.Integer", "long", "Long", "java.lang.Long", "double",
			"Double", "java.lang.Double", "float", "Float", "java.lang.Float", "short", "Short", "java.lang.Short", "char", "Character", "java.lang.Character", "boolean",
			"Boolean", "java.lang.Boolean", "void" };
	private static final String[] ARRAY_LIST_TYPES = { "java.util.Collection", "java.util.List", "Collection", "List" };
	private static final String[] HASH_SET_TYPES = { "java.util.Set", "Set" };
	private static final String[] HASH_MAP_TYPES = { "java.util.Map", "Map" };

	@Override
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		final StringBuilder buffer = new StringBuilder();
		if (!CollectionUtil.isEmpty(args)) {
			final TemplateBooleanModel addImplementation = MethodUtil.getRequiredParameter(args, 0, TemplateBooleanModel.class);
			final SimpleScalar currentPackage = MethodUtil.getRequiredParameter(args, 1, SimpleScalar.class);
			final TemplateSequenceModel list = MethodUtil.getRequiredParameter(args, 2, TemplateSequenceModel.class);
			final List<String> types = getAsStringList(list);

			final Set<String> filteredTypes = getFilteredTypes(addImplementation.getAsBoolean(), currentPackage.getAsString(), types);
			if (!CollectionUtil.isEmpty(filteredTypes)) {
				for (final String type : filteredTypes) {
					buffer.append("import " + type + ";\n");
				}
			}
		}
		return new SimpleScalar(buffer.toString());
	}

	private List<String> getAsStringList(TemplateSequenceModel list) throws TemplateModelException {
		List<String> types = null;
		if (list != null && list.size() > 0) {
			types = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				final Object o = list.get(i);
				if (o instanceof TemplateScalarModel) {
					final TemplateScalarModel scalarModel = (TemplateScalarModel) o;
					types.add(scalarModel.getAsString());
				} else {
					types.add(o.toString());
				}
			}
		}
		return types;
	}

	private Set<String> getFilteredTypes(boolean addImplementation, String currentPackage, List<String> types) {
		Set<String> filteredTypes = null;
		if (!CollectionUtil.isEmpty(types)) {
			filteredTypes = new HashSet<String>();
			for (final String type : types) {
				if (!StringUtil.isBlank(type) && !ignoreType(type)) {
					final String packageName = ClassUtil.getPackageName(type);
					final String fqdn = getFqdn(type);

					if (!StringUtil.isBlank(fqdn) && !StringUtil.isBlank(packageName) && !StringUtil.equals(currentPackage, packageName)) {
						filteredTypes.add(fqdn);
					}

					if (addImplementation) {
						if (ArrayUtil.contains(fqdn, ARRAY_LIST_TYPES)) {
							filteredTypes.add("java.util.ArrayList");
						} else if (ArrayUtil.contains(fqdn, HASH_SET_TYPES)) {
							filteredTypes.add("java.util.HashSet");
						} else if (ArrayUtil.contains(fqdn, HASH_MAP_TYPES)) {
							filteredTypes.add("java.util.HashMap");
						}
					}
				}
			}
		}
		return filteredTypes;
	}

	private String getFqdn(String type) {
		String fqdn = "";
		if (!StringUtil.isBlank(type)) {
			final String packageName = ClassUtil.getPackageName(type);
			String className = ClassUtil.getShortClassName(type);
			if (!StringUtil.isBlank(packageName)) {
				fqdn = packageName + ".";
			}

			if (!StringUtil.isBlank(className)) {
				className = StringUtil.substringBefore(className, "<");

				fqdn += className;
			}

		}
		return fqdn;
	}

	private boolean ignoreType(String type) {
		boolean ignore = false;
		if (!StringUtil.isBlank(type) && ArrayUtil.contains(type, IGNORED_TYPES)) {
			ignore = true;
		}
		return ignore;
	}
}
