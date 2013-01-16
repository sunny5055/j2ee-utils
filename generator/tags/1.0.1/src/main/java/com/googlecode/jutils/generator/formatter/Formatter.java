package com.googlecode.jutils.generator.formatter;

import java.io.File;

import com.googlecode.jutils.generator.formatter.exception.FormatterException;

public interface Formatter {
	boolean accept(File file);

	boolean accept(String extension);

	void format(File file) throws FormatterException;
}
