package com.googlecode.jutils.pp.freemarker.transform;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.pp.io.FileOutputWriter;

import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import freemarker.template.TemplateTransformModel;

public class ChangeOutputFileTransform implements TemplateTransformModel {

	@Override
	@SuppressWarnings("rawtypes")
	public Writer getWriter(Writer out, Map params) throws TemplateModelException, IOException {
		if (out != null && !MapUtil.isEmpty(params)) {
			try {
				out.flush();
			} catch (final IOException exc) {
				throw new TemplateModelException("Failed to change the outout file", exc);
			}

			final TemplateScalarModel name = (TemplateScalarModel) params.get("name");
			if (name != null) {
				final FileOutputWriter outputWriter = (FileOutputWriter) out;
				try {
					outputWriter.changeOutputFile(name.getAsString());
				} catch (final IOException exc) {
					throw new TemplateModelException("Failed to change the outout file", exc);
				}
			}
		}
		return out;
	}

}
