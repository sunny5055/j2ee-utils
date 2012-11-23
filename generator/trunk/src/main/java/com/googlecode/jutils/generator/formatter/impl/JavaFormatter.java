package com.googlecode.jutils.generator.formatter.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import com.googlecode.jutils.generator.formatter.Formatter;

import de.hunsicker.jalopy.Jalopy;

public class JavaFormatter implements Formatter {
	private File jalopyConvention;

	public JavaFormatter() {
		super();
	}

	public File getJalopyConvention() {
		return jalopyConvention;
	}

	public void setJalopyConvention(File jalopyConvention) {
		this.jalopyConvention = jalopyConvention;
	}

	@Override
	public boolean accept(File file) {
		boolean accept = false;
		if (file != null) {
			accept = FilenameUtils.isExtension(file.getName(), "java");
		}
		return accept;
	}

	@Override
	public void format(File file) throws IOException {
		if (accept(file)) {
			final Jalopy jalopy = new Jalopy();
			if (jalopyConvention != null) {
				Jalopy.setConvention(jalopyConvention);
			}
			jalopy.setInput(file);
			jalopy.setOutput(file);
			jalopy.format();
		}
	}
}
