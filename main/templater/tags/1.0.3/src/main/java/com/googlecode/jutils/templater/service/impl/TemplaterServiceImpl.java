package com.googlecode.jutils.templater.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.templater.engine.TemplaterEngine;
import com.googlecode.jutils.templater.exception.TemplaterServiceException;
import com.googlecode.jutils.templater.service.TemplaterService;

/**
 * The Class TemplaterServiceImpl.
 */
@Service
public class TemplaterServiceImpl implements TemplaterService {
	@Autowired
	private List<TemplaterEngine> engines;

	/**
	 * Instantiates a new templater service impl.
	 */
	public TemplaterServiceImpl() {
		super();
		this.engines = new ArrayList<TemplaterEngine>();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void addTemplateLoaderPath(File file) {
		if (file != null) {
			for (final TemplaterEngine templaterEngine : engines) {
				templaterEngine.addTemplateLoaderPath(file);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void process(String templateName, Map<String, Object> data, Writer writer) throws TemplaterServiceException {
		if (!StringUtil.isBlank(templateName) && writer != null) {
			final TemplaterEngine templaterEngine = getTemplaterEngine(getTemplateType(templateName));
			if (templaterEngine == null) {
				throw new TemplaterServiceException("Unable to find a templater engine for template : " + templateName);
			}

			templaterEngine.process(templateName, data, writer);
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void processFromString(String templateContent, String templateType, Map<String, Object> data, Writer writer) throws TemplaterServiceException {
		if (!StringUtil.isBlank(templateContent) && !StringUtil.isBlank(templateType) && writer != null) {
			InputStream inputStream = null;
			try {
				inputStream = IOUtils.toInputStream(templateContent);
			} catch (final Exception e) {
				throw new TemplaterServiceException(e);
			}
			processFromInputStream(inputStream, templateType, data, writer);
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void processFromFile(File templateFile, Map<String, Object> data, Writer writer) throws TemplaterServiceException {
		if (templateFile != null && writer != null) {
			final String templateType = getTemplateType(templateFile.getName());
			Reader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile)));
			} catch (final FileNotFoundException e) {
				throw new TemplaterServiceException(e);
			} catch (final Exception e) {
				throw new TemplaterServiceException(e);
			}

			if (reader != null) {
				final TemplaterEngine templaterEngine = getTemplaterEngine(templateType);
				if (templaterEngine == null) {
					throw new TemplaterServiceException("Unable to find a templater engine for type : " + templateType);
				}

				templaterEngine.addTemplateLoaderPath(templateFile);
				templaterEngine.processFromReader(reader, data, writer);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void processFromInputStream(InputStream inputStream, String templateType, Map<String, Object> data, Writer writer) throws TemplaterServiceException {
		if (inputStream != null && !StringUtil.isBlank(templateType) && writer != null) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(inputStream);
			} catch (final Exception e) {
				throw new TemplaterServiceException(e);
			}
			processFromReader(reader, templateType, data, writer);
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void processFromReader(Reader readerTemplate, String templateType, Map<String, Object> data, Writer writer) throws TemplaterServiceException {
		if (readerTemplate != null && !StringUtil.isBlank(templateType) && writer != null) {
			final TemplaterEngine templaterEngine = getTemplaterEngine(templateType);
			if (templaterEngine == null) {
				throw new TemplaterServiceException("Unable to find a templater engine for type : " + templateType);
			}

			templaterEngine.processFromReader(readerTemplate, data, writer);
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContent(String templateName, Map<String, Object> data) throws TemplaterServiceException {
		String content = null;
		if (!StringUtil.isBlank(templateName)) {
			final TemplaterEngine templaterEngine = getTemplaterEngine(getTemplateType(templateName));
			if (templaterEngine == null) {
				throw new TemplaterServiceException("Unable to find a templater engine for template : " + templateName);
			}

			content = templaterEngine.getContent(templateName, data);
		}
		return content;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromString(String templateContent, String templateType, Map<String, Object> data) throws TemplaterServiceException {
		String content = null;
		if (!StringUtil.isBlank(templateContent) && !StringUtil.isBlank(templateType)) {
			InputStream inputStream = null;
			try {
				inputStream = IOUtils.toInputStream(templateContent);
			} catch (final Exception e) {
				throw new TemplaterServiceException(e);
			}
			content = getContentFromInputStream(inputStream, templateType, data);
		}
		return content;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromFile(File templateFile, Map<String, Object> data) throws TemplaterServiceException {
		String content = null;
		if (templateFile != null) {
			final String templateType = getTemplateType(templateFile.getName());
			Reader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile)));
			} catch (final FileNotFoundException e) {
				throw new TemplaterServiceException(e);
			} catch (final Exception e) {
				throw new TemplaterServiceException(e);
			}

			if (reader != null) {
				final TemplaterEngine templaterEngine = getTemplaterEngine(templateType);
				if (templaterEngine == null) {
					throw new TemplaterServiceException("Unable to find a templater engine for type : " + templateType);
				}

				templaterEngine.addTemplateLoaderPath(templateFile);
				content = templaterEngine.getContentFromReader(reader, data);
			}
		}
		return content;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromInputStream(InputStream inputStream, String templateType, Map<String, Object> data) throws TemplaterServiceException {
		String content = null;
		if (inputStream != null) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(inputStream);
			} catch (final Exception e) {
				throw new TemplaterServiceException(e);
			}
			content = getContentFromReader(reader, templateType, data);
		}
		return content;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public String getContentFromReader(Reader readerTemplate, String templateType, Map<String, Object> data) throws TemplaterServiceException {
		String content = null;
		if (readerTemplate != null && !StringUtil.isBlank(templateType)) {
			final TemplaterEngine templaterEngine = getTemplaterEngine(templateType);
			if (templaterEngine == null) {
				throw new TemplaterServiceException("Unable to find a templater engine for type : " + templateType);
			}

			content = templaterEngine.getContentFromReader(readerTemplate, data);
		}
		return content;
	}

	/**
	 * Getter : return the template type.
	 * 
	 * @param templateName
	 *            the template name
	 * @return the template type
	 */
	private String getTemplateType(String templateName) {
		String templateType = null;
		if (!StringUtil.isBlank(templateName)) {
			templateType = FilenameUtils.getExtension(templateName);
		}
		return templateType;
	}

	/**
	 * Getter : return the templater engine.
	 * 
	 * @param templateType
	 *            the template type
	 * @return the templater engine
	 */
	private TemplaterEngine getTemplaterEngine(String templateType) {
		TemplaterEngine templaterEngine = null;
		if (!StringUtil.isBlank(templateType) && !CollectionUtil.isEmpty(engines)) {
			for (final TemplaterEngine engine : engines) {
				if (engine.accept(templateType)) {
					templaterEngine = engine;
					break;
				}
			}
		}
		return templaterEngine;
	}
}
