package com.googlecode.jutils.templater.freemarker.engine;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.templater.engine.AbstractTemplaterEngine;
import com.googlecode.jutils.templater.exception.TemplaterServiceException;
import com.googlecode.jutils.templater.freemarker.template.directive.AddToDirective;
import com.googlecode.jutils.templater.freemarker.template.directive.MyListDirective;
import com.googlecode.jutils.templater.freemarker.template.directive.XPathDirective;
import com.googlecode.jutils.templater.freemarker.template.method.RandomMethod;
import com.googlecode.jutils.templater.freemarker.template.method.ToCamelCaseMethod;
import com.googlecode.jutils.templater.freemarker.template.method.ToPascalCaseMethod;
import com.googlecode.jutils.templater.freemarker.template.method.ToUnderscoreCaseMethod;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class FreemarkerTemplaterEngine extends AbstractTemplaterEngine {
	public static final String EXTENSION = "ftl";

	@Autowired
	private Configuration configuration;

	public FreemarkerTemplaterEngine() {
		super();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public boolean accept(String extension) {
		boolean accept = false;
		if (!StringUtil.isBlank(extension)) {
			accept = StringUtil.equalsIgnoreCase(extension, EXTENSION);
		}
		return accept;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void addTemplateLoaderPath(File file) {
		if (file != null) {
			final List<TemplateLoader> templateLoaders = new ArrayList<TemplateLoader>();
			templateLoaders.add(configuration.getTemplateLoader());

			TemplateLoader templateLoader = null;
			try {
				if (file.isFile()) {
					templateLoader = new FileTemplateLoader(file.getParentFile());
				} else {
					templateLoader = new FileTemplateLoader(file);
				}
			} catch (final IOException e) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(e.getMessage(), e);
				}
			}
			if (templateLoader != null) {
				templateLoaders.add(templateLoader);
			}

			final TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]);
			configuration.setTemplateLoader(new MultiTemplateLoader(loaders));
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void process(String templateName, Map<String, Object> data, Writer writer) throws TemplaterServiceException {
		if (!StringUtil.isBlank(templateName) && writer != null) {
			final Template template = getTemplate(templateName, data);
			if (template != null) {
				processTemplate(template, data, writer);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void processFromReader(Reader reader, Map<String, Object> data, Writer writer) throws TemplaterServiceException {
		if (reader != null && writer != null) {
			final Template template = getTemplate(reader, data);
			if (template != null) {
				processTemplate(template, data, writer);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContent(String templateName, Map<String, Object> data) throws TemplaterServiceException {
		String content = null;
		if (!StringUtil.isBlank(templateName)) {
			final Template template = getTemplate(templateName, data);
			if (template != null) {
				content = processTemplateIntoString(template, data);
			}
		}
		return content;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromReader(Reader reader, Map<String, Object> data) throws TemplaterServiceException {
		String content = null;
		if (reader != null) {
			final Template template = getTemplate(reader, data);
			if (template != null) {
				content = processTemplateIntoString(template, data);
			}
		}
		return content;
	}

	private Template getTemplate(Reader reader, Map<String, Object> data) throws TemplaterServiceException {
		Template template = null;
		try {
			template = new Template("", reader, configuration, configuration.getDefaultEncoding());
		} catch (final IOException e) {
			throw new TemplaterServiceException(e);
		}
		return template;
	}

	private Template getTemplate(String templateName, Map<String, Object> data) throws TemplaterServiceException {
		Template template = null;
		try {
			template = configuration.getTemplate(templateName, configuration.getDefaultEncoding());
		} catch (final IOException e) {
			throw new TemplaterServiceException(e);
		}
		return template;
	}

	private void processTemplate(Template template, Map<String, Object> data, Writer writer) throws TemplaterServiceException {
		if (template != null && writer != null) {
			if (data == null) {
				data = new HashMap<String, Object>();
			}

			addTemplateMethod(data);

			try {
				template.process(data, writer);
			} catch (final TemplateException e) {
				throw new TemplaterServiceException(e);
			} catch (final IOException e) {
				throw new TemplaterServiceException(e);
			}
		}
	}

	private String processTemplateIntoString(Template template, Map<String, Object> data) throws TemplaterServiceException {
		String content = null;
		if (template != null) {
			if (data == null) {
				data = new HashMap<String, Object>();
			}

			addTemplateMethod(data);

			try {
				content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
			} catch (final IOException e) {
				throw new TemplaterServiceException(e);
			} catch (final TemplateException e) {
				throw new TemplaterServiceException(e);
			}
		}

		return content;
	}

	private void addTemplateMethod(Map<String, Object> data) {
		if (data != null) {
			data.put("toUnderscoreCase", new ToUnderscoreCaseMethod());
			data.put("toCamelCase", new ToCamelCaseMethod());
			data.put("toPascalCase", new ToPascalCaseMethod());
			data.put("addTo", new AddToDirective());
			data.put("myList", new MyListDirective());
			data.put("xPath", new XPathDirective());
			data.put("random", new RandomMethod());

			if (!MapUtil.isEmpty(defaultData)) {
				data.putAll(defaultData);
			}
		}
	}
}
