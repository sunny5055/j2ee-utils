package com.googlecode.jutils.pp.freemarker.method;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.io.IoUtil;
import com.googlecode.jutils.pp.io.FileOutputWriter;
import com.googlecode.jutils.pp.settings.Settings;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class RealOutputFileMethod implements TemplateMethodModel {
	private Settings settings;
	private FileOutputWriter writer;

	public RealOutputFileMethod(Settings settings, FileOutputWriter writer) {
		super();
		this.settings = settings;
		this.writer = writer;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public TemplateModel exec(List args) throws TemplateModelException {
		SimpleScalar result = null;
		if (!CollectionUtil.isEmpty(args)) {
			throw new TemplateModelException(getClass().getSimpleName() + " does not support parameters.");
		} else {
			final String relativePath = IoUtil.getRelativePath(settings.getOutputDirectory(), writer.getOutputFile());
			if (!StringUtil.isBlank(relativePath)) {
				final String realPath = FilenameUtils.concat(settings.getOutputDirectory().getAbsolutePath(), relativePath);
				if (!StringUtil.isBlank(realPath)) {
					result = new SimpleScalar(realPath);
				}
			}
		}
		return result;
	}
}
