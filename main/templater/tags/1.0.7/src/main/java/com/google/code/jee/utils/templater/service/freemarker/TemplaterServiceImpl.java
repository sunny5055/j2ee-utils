package com.google.code.jee.utils.templater.service.freemarker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.templater.exception.TemplateServiceException;
import com.google.code.jee.utils.templater.service.TemplaterService;
import com.google.code.jee.utils.templater.service.freemarker.template.ToCamelCaseMethod;
import com.google.code.jee.utils.templater.service.freemarker.template.ToPascalCaseMethod;
import com.google.code.jee.utils.templater.service.freemarker.template.ToUnderscoreCaseMethod;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * The Class TemplaterServiceImpl.
 */
@Service
public class TemplaterServiceImpl implements TemplaterService {
	@Autowired
	private Configuration configuration;

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContent(String templateName, Map<String, Object> data) throws TemplateServiceException {
		return getContent(templateName, data, configuration.getDefaultEncoding());
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContent(String templateName, Map<String, Object> data, String encoding) throws TemplateServiceException {
		String content = null;
		if (!StringUtil.isBlank(templateName) && !StringUtil.isBlank(encoding)) {
			Template template = null;
			try {
				template = configuration.getTemplate(templateName, encoding);
			} catch (final IOException e) {
				throw new TemplateServiceException(e);
			} catch (final Exception e) {
				throw new TemplateServiceException(e);
			}
			content = getContentFromTemplate(template, data);
		}
		return content;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromString(String templateContent, Map<String, Object> data) throws TemplateServiceException {
		return getContentFromString(templateContent, data, configuration.getDefaultEncoding());
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromString(String templateContent, Map<String, Object> data, String encoding) throws TemplateServiceException {
		String content = null;
		if (!StringUtil.isBlank(templateContent) && !StringUtil.isBlank(encoding)) {
			InputStream inputStream = null;
			try {
				inputStream = IOUtils.toInputStream(templateContent, encoding);
			} catch (final IOException e) {
				throw new TemplateServiceException(e);
			} catch (final Exception e) {
				throw new TemplateServiceException(e);
			}
			content = getContentFromInputStream(inputStream, data, encoding);
		}
		return content;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromFile(File templateAbsoluteFile, Map<String, Object> data) throws TemplateServiceException {
		return getContentFromFile(templateAbsoluteFile, data, configuration.getDefaultEncoding());
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromFile(File templateAbsoluteFile, Map<String, Object> data, String encoding) throws TemplateServiceException {
		String content = null;
		if (templateAbsoluteFile != null && !StringUtil.isBlank(encoding)) {
			Reader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateAbsoluteFile), encoding));
			} catch (final FileNotFoundException e) {
				throw new TemplateServiceException(e);
			} catch (final Exception e) {
				throw new TemplateServiceException(e);
			}
			content = getContentFromReader(reader, data, encoding);
		}
		return content;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromInputStream(InputStream inputStream, Map<String, Object> data) throws TemplateServiceException {
		return getContentFromInputStream(inputStream, data, configuration.getDefaultEncoding());
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromInputStream(InputStream inputStream, Map<String, Object> data, String encoding) throws TemplateServiceException {
		String content = null;
		if (inputStream != null && !StringUtil.isBlank(encoding)) {
			Reader reader;
			try {
				reader = new InputStreamReader(inputStream, encoding);
			} catch (final UnsupportedEncodingException e) {
				throw new TemplateServiceException(e);
			}
			content = getContentFromReader(reader, data, encoding);
		}
		return content;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromReader(Reader reader, Map<String, Object> data) throws TemplateServiceException {
		return getContentFromReader(reader, data, configuration.getDefaultEncoding());
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromReader(Reader reader, Map<String, Object> data, String encoding) throws TemplateServiceException {
		final Template template = getTemplate(reader, encoding);
		return getContentFromTemplate(template, data);
	}

	/**
	 * Gets the template.
	 * 
	 * @param templateName
	 *            the template name
	 * @param reader
	 *            the reader
	 * @return the template
	 * @throws TemplateServiceException
	 *             the template service exception
	 */
	private Template getTemplate(Reader reader, String encoding) throws TemplateServiceException {
		Template template = null;
		if (reader != null && !StringUtil.isBlank(encoding)) {
			try {
				template = new Template("", reader, configuration, encoding);
			} catch (final IOException e) {
				throw new TemplateServiceException(e);
			}
		}
		return template;
	}

	/**
	 * Gets the content from template.
	 * 
	 * @param template
	 *            the template
	 * @param data
	 *            the data
	 * @return the content from template
	 * @throws TemplateServiceException
	 *             the template service exception
	 */
	private String getContentFromTemplate(Template template, Map<String, Object> data) throws TemplateServiceException {
		String content = null;
		if (template != null) {
			addTemplateMethod(data);

			try {
				content = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
			} catch (final IOException e) {
				throw new TemplateServiceException(e);
			} catch (final TemplateException e) {
				throw new TemplateServiceException(e);
			}
		}
		return content;
	}

	private void addTemplateMethod(Map<String, Object> data) {
		if (data != null) {
			data.put("toUnderscoreCase", new ToUnderscoreCaseMethod());
			data.put("toCamelCase", new ToCamelCaseMethod());
			data.put("toPascalCase", new ToPascalCaseMethod());
		}
	}
}
