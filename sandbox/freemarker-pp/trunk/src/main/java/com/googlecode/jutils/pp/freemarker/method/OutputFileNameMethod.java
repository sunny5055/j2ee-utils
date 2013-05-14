package com.googlecode.jutils.pp.freemarker.method;

import java.util.List;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.pp.io.FileOutputWriter;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class OutputFileNameMethod implements TemplateMethodModel {
	private FileOutputWriter writer;

	public OutputFileNameMethod(FileOutputWriter writer) {
		super();
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
			final String fileName = writer.getOutputFile().getName();
			if (!StringUtil.isBlank(fileName)) {
				result = new SimpleScalar(fileName);
			}
		}
		return result;
	}
}
