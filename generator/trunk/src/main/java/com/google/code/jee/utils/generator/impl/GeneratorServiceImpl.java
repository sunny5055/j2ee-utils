package com.google.code.jee.utils.generator.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.dal.dto.AbstractDto;
import com.google.code.jee.utils.generator.GeneratorService;
import com.google.code.jee.utils.generator.freemarker.template.GetPrimaryKeyFieldMethod;
import com.google.code.jee.utils.generator.freemarker.template.GetTypeParameterFromListMethod;
import com.google.code.jee.utils.generator.freemarker.template.GetUniqueFieldsMethod;
import com.google.code.jee.utils.generator.util.HibernateAnnotationUtil;
import com.google.code.jee.utils.io.IoUtil;
import com.google.code.jee.utils.templater.exception.TemplateServiceException;
import com.google.code.jee.utils.templater.service.TemplaterService;

@Service
public class GeneratorServiceImpl implements GeneratorService {

	@Autowired
	private TemplaterService templaterService;

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public <E extends AbstractDto<?>> void generateNamedQueries(PropertiesConfiguration configuration, Class<E> clazz) throws TemplateServiceException, IOException {
		if (configuration != null && clazz != null) {
			final Map<String, Object> data = getData(configuration, clazz);

			final String namedQueries = templaterService.getContent("model/namedQueries.ftl", data);
			if (!StringUtil.isBlank(namedQueries)) {
				//TODO only for test, needs to be uppdated
				final String modelPackage = (String) data.get("modelPackage") + ".queries";
				final String rootPath = data.get("rootPath") + File.separator + convertToPath(modelPackage);

				final String modelName = (String) data.get("modelName");
				final String fileName = modelName + ".java";

				writeToFile(rootPath, fileName, namedQueries);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public <E extends AbstractDto<?>> void generateDao(PropertiesConfiguration configuration, Class<E> clazz) throws TemplateServiceException, IOException {
		if (configuration != null && clazz != null) {
			final Map<String, Object> data = getData(configuration, clazz);

			final String daoContent = templaterService.getContent("dao/interface.ftl", data);
			if (!StringUtil.isBlank(daoContent)) {
				final String daoPackage = (String) data.get("daoPackage");
				final String rootPath = data.get("rootPath") + File.separator + convertToPath(daoPackage);

				final String daoName = (String) data.get("daoName");
				final String fileName = daoName + ".java";

				writeToFile(rootPath, fileName, daoContent);
			}

			final String daoImplContent = templaterService.getContent("dao/implementation.ftl", data);
			if (!StringUtil.isBlank(daoImplContent)) {
				final String daoImplPackage = (String) data.get("daoImplPackage");
				final String rootPath = data.get("rootPath") + File.separator + convertToPath(daoImplPackage);

				final String daoImplName = (String) data.get("daoImplName");
				final String fileName = daoImplName + ".java";

				writeToFile(rootPath, fileName, daoImplContent);
			}

		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public <E extends AbstractDto<?>> void generateService(PropertiesConfiguration configuration, Class<E> clazz) throws TemplateServiceException, IOException {
		if (configuration != null && clazz != null) {
			final Map<String, Object> data = getData(configuration, clazz);

			final String serviceContent = templaterService.getContent("service/interface.ftl", data);
			if (!StringUtil.isBlank(serviceContent)) {
				final String servicePackage = (String) data.get("servicePackage");
				final String rootPath = data.get("rootPath") + File.separator + convertToPath(servicePackage);

				final String serviceName = (String) data.get("serviceName");
				final String fileName = serviceName + ".java";

				writeToFile(rootPath, fileName, serviceContent);
			}

			final String serviceImplContent = templaterService.getContent("service/implementation.ftl", data);
			if (!StringUtil.isBlank(serviceImplContent)) {
				final String serviceImplPackage = (String) data.get("serviceImplPackage");
				final String rootPath = data.get("rootPath") + File.separator + convertToPath(serviceImplPackage);

				final String serviceImplName = (String) data.get("serviceImplName");
				final String fileName = serviceImplName + ".java";

				writeToFile(rootPath, fileName, serviceImplContent);
			}

		}
	}

	private <E extends AbstractDto<?>> Map<String, Object> getData(PropertiesConfiguration configuration, Class<E> clazz) {
		Map<String, Object> data = null;
		if (configuration != null && clazz != null) {
			data = new HashMap<String, Object>();
			data.put("getTypeParameterFromList", new GetTypeParameterFromListMethod());
			data.put("getPrimaryKeyField", new GetPrimaryKeyFieldMethod());
			data.put("getUniqueFields", new GetUniqueFieldsMethod());

			for (final Iterator<String> it = configuration.getKeys(); it.hasNext();) {
				final String key = it.next();
				String value = configuration.getString(key);

				if (StringUtil.contains(value, "%1s")) {
					value = String.format(value, clazz.getSimpleName());
				}
				data.put(key, value);
			}

			data.put("modelName", clazz.getSimpleName());
			final Field primaryKey = HibernateAnnotationUtil.getPrimaryKey(clazz);
			if (primaryKey != null) {
				data.put("primaryKey", primaryKey);
			}

			final List<Field> allFields = HibernateAnnotationUtil.getAllFields(clazz);
			if (!CollectionUtil.isEmpty(allFields)) {
				data.put("allFields", allFields);
			}

			final List<Field> uniqueFields = HibernateAnnotationUtil.getUniqueFields(clazz);
			if (!CollectionUtil.isEmpty(uniqueFields)) {
				data.put("uniqueFields", uniqueFields);
			}

			final List<Field> columnFields = HibernateAnnotationUtil.getColumnFields(clazz);
			if (!CollectionUtil.isEmpty(columnFields)) {
				data.put("columnFields", columnFields);
			}

			final List<Field> manyToOneFields = HibernateAnnotationUtil.getManyToOneFields(clazz);
			if (!CollectionUtil.isEmpty(manyToOneFields)) {
				data.put("manyToOneFields", manyToOneFields);
			}

			final List<Field> oneToManyFields = HibernateAnnotationUtil.getOneToManyFields(clazz);
			if (!CollectionUtil.isEmpty(oneToManyFields)) {
				data.put("oneToManyFields", oneToManyFields);
			}

			final List<Field> manyToManyFields = HibernateAnnotationUtil.getManyToManyFields(clazz);
			if (!CollectionUtil.isEmpty(manyToManyFields)) {
				data.put("manyToManyFields", manyToManyFields);
			}

			final List<Field> associations = HibernateAnnotationUtil.getAssociations(clazz);
			if (!CollectionUtil.isEmpty(associations)) {
				data.put("associations", associations);
			}
		}
		return data;
	}

	private void writeToFile(String path, String fileName, String content) throws IOException, FileNotFoundException {
		if (!StringUtil.isBlank(path) && !StringUtil.isBlank(fileName) && !StringUtil.isBlank(content)) {
			final File rootDir = new File(path);
			if (!rootDir.exists()) {
				FileUtils.forceMkdir(rootDir);
			}
			final File file = new File(rootDir, fileName);

			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(file);
				IoUtil.write(content, outputStream);
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
			}
		}
	}

	private String convertToPath(String packageName) {
		String path = null;
		if (!StringUtil.isBlank(packageName)) {
			path = StringUtil.replace(packageName, ".", File.separator);
		}
		return path;
	}
}
