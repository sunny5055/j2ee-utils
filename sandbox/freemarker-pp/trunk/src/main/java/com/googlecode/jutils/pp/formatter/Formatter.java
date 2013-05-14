package com.googlecode.jutils.pp.formatter;

import java.io.File;

import com.googlecode.jutils.pp.formatter.exception.FormatterException;

public interface Formatter {
	boolean accept(File file);

	boolean accept(String extension);

	void format(File file) throws FormatterException;
}
