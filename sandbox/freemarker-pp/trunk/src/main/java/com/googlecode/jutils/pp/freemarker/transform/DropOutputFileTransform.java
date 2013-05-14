package com.googlecode.jutils.pp.freemarker.transform;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.googlecode.jutils.pp.io.FileOutputWriter;

import freemarker.template.TemplateModelException;
import freemarker.template.TemplateTransformModel;

public class DropOutputFileTransform implements TemplateTransformModel {

	@Override
	@SuppressWarnings("rawtypes")
	public Writer getWriter(Writer out, Map params) throws TemplateModelException, IOException {
		if (out != null) {
			final FileOutputWriter outputWriter = (FileOutputWriter) out;
			try {
				outputWriter.dropOutputFile();
			} catch (final IOException exc) {
				throw new TemplateModelException("Failed to drop the output file", exc);
			}
		}
		return out;
	}

}
