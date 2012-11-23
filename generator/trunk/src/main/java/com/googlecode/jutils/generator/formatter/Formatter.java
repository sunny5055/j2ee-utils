package com.googlecode.jutils.generator.formatter;

import java.io.File;
import java.io.IOException;

public interface Formatter {
	boolean accept(File file);

	void format(File file) throws IOException;
}
