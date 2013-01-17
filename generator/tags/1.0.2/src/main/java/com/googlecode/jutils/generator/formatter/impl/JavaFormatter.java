package com.googlecode.jutils.generator.formatter.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.googlecode.jutils.generator.formatter.exception.FormatterException;

import de.hunsicker.jalopy.Jalopy;

public class JavaFormatter extends AbstractFormatter {
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
	public void format(File file) throws FormatterException {
		if (accept(file)) {
			final Jalopy jalopy = new Jalopy();
			try {
				if (jalopyConvention != null) {
					Jalopy.setConvention(jalopyConvention);
				}
				jalopy.setInput(file);
				jalopy.setOutput(file);
				jalopy.format();
			} catch (final FileNotFoundException e) {
				throw new FormatterException(e.getMessage());
			} catch (final IOException e) {
				throw new FormatterException(e.getMessage());
			}
		}
	}

	@Override
	protected String[] getExtensions() {
		return new String[] { "java" };
	}

}
