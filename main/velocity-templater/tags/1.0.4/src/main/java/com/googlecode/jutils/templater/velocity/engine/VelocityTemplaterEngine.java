package com.googlecode.jutils.templater.velocity.engine;

import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.templater.engine.AbstractTemplaterEngine;
import com.googlecode.jutils.templater.exception.TemplaterServiceException;

@Component
public class VelocityTemplaterEngine extends AbstractTemplaterEngine {
	public static final String EXTENSION = "vm";

	@Autowired
	private org.apache.velocity.app.VelocityEngine configuration;

	public VelocityTemplaterEngine() {
		super();
	}

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
	public String getContent(String templateName, Map<String, Object> data) throws TemplaterServiceException {
		String content = null;
		if (!StringUtil.isBlank(templateName)) {
			if (data == null) {
				data = new HashMap<String, Object>();
			}

			if (!MapUtil.isEmpty(defaultData)) {
				data.putAll(defaultData);
			}

			final VelocityContext context = new VelocityContext(data);
			final StringWriter writer = new StringWriter();
			try {
				configuration.mergeTemplate(templateName, (String) configuration.getProperty(RuntimeConstants.INPUT_ENCODING), context, writer);
			} catch (final ResourceNotFoundException e) {
				throw new TemplaterServiceException(e);
			} catch (final ParseErrorException e) {
				throw new TemplaterServiceException(e);
			} catch (final MethodInvocationException e) {
				throw new TemplaterServiceException(e);
			}

			if (writer != null) {
				content = writer.toString();
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
			if (data == null) {
				data = new HashMap<String, Object>();
			}

			if (!MapUtil.isEmpty(defaultData)) {
				data.putAll(defaultData);
			}

			final VelocityContext context = new VelocityContext(data);
			final StringWriter writer = new StringWriter();
			try {
				configuration.evaluate(context, writer, "test", reader);
			} catch (final ParseErrorException e) {
				throw new TemplaterServiceException(e);
			} catch (final MethodInvocationException e) {
				throw new TemplaterServiceException(e);
			} catch (final ResourceNotFoundException e) {
				throw new TemplaterServiceException(e);
			}

			if (writer != null) {
				content = writer.toString();
			}
		}
		return content;
	}
}
