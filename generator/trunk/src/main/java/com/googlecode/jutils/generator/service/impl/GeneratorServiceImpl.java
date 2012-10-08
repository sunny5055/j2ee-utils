package com.googlecode.jutils.generator.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.generator.exception.GeneratorServiceException;
import com.googlecode.jutils.generator.exporter.Exporter;
import com.googlecode.jutils.generator.service.GeneratorService;
import com.googlecode.jutils.io.IoUtil;
import com.googlecode.jutils.templater.exception.TemplateServiceException;
import com.googlecode.jutils.templater.service.TemplaterService;

@Service
public class GeneratorServiceImpl implements GeneratorService {
	@Autowired
	private TemplaterService templaterService;

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void generate(Exporter exporter) throws GeneratorServiceException {
		if (exporter != null) {
			final String content = getContent(exporter);
			if (!StringUtil.isBlank(content)) {
				final File outputDirectory = exporter.getOutputDirectory();
				final String outputFileName = exporter.getOutputFileName();

				if (outputDirectory != null && !StringUtil.isBlank(outputFileName)) {
					final File file = new File(outputDirectory, outputFileName);

					try {
						writeToFile(file, content);
					} catch (final FileNotFoundException e) {
						throw new GeneratorServiceException(e);
					} catch (final IOException e) {
						throw new GeneratorServiceException(e);
					}
				}
			}
		}
	}

	protected String getContent(Exporter exporter) throws GeneratorServiceException {
		String content = null;
		if (exporter != null) {
			final String templateName = exporter.getTemplateName();
			final Map<String, Object> data = exporter.getData();
			try {
				content = templaterService.getContent(templateName, data);
			} catch (final TemplateServiceException e) {
				throw new GeneratorServiceException(e);
			}
		}
		return content;
	}

	protected void writeToFile(File file, String content) throws IOException, FileNotFoundException {
		if (file != null && !StringUtil.isBlank(content)) {
			final File parent = file.getParentFile();
			if (!parent.exists()) {
				FileUtils.forceMkdir(parent);
			}

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

}
