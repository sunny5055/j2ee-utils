package com.googlecode.jutils.pp.action;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.io.IoUtil;
import com.googlecode.jutils.pp.settings.Settings;

@Component
public class CopyAction extends AbstractAction {

	@Override
	public void execute(Settings settings, File file, Map<String, Object> parameters) {
		if (file != null && settings != null) {
			final File destFile = getOutputFile(settings, file, parameters);

			try {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Copy file from " + file + " to " + destFile);
				}
				FileUtils.copyFile(file, destFile);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	private File getOutputFile(Settings settings, File sourceFile, Map<String, Object> parameters) {
		File outputFile = null;
		if (settings != null && sourceFile != null) {
			String relativePath = IoUtil.getRelativePath(settings.getSourceDirectory(), sourceFile);
			final String applyTo = (String) parameters.get("applyTo");
			String outputPath = (String) parameters.get("to");
			if (!StringUtil.isBlank(applyTo) && !StringUtil.isBlank(outputPath)) {
				if (!StringUtil.endsWith(outputPath, "/") && !StringUtil.endsWith(outputPath, "\\")) {
					outputPath = outputPath + "/";
				}

				if (isFileRegex(applyTo)) {
					relativePath = FilenameUtils.concat(outputPath, sourceFile.getName());
				} else {
					String path = StringUtil.replace(applyTo, "*", "");
					path = StringUtil.removeStart(path, "/");
					relativePath = StringUtil.replace(relativePath, path, outputPath);
				}
			}

			final String fileName = FilenameUtils.concat(settings.getOutputDirectory().getAbsolutePath(), relativePath);

			outputFile = new File(fileName);
		}

		return outputFile;
	}

	private boolean isFileRegex(String regex) {
		boolean file = false;
		if (!StringUtil.isBlank(regex)) {
			file = regex.matches("^[^\\.]+\\.[a-zA-Z]+$");
		}
		return file;
	}
}
