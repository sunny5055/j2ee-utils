package com.googlecode.jutils.pp.formatter;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.ArrayUtil;

public abstract class AbstractFormatter implements Formatter {

	public AbstractFormatter() {
		super();
	}

	@Override
	public boolean accept(File file) {
		boolean accept = false;
		if (file != null) {
			final String extension = FilenameUtils.getExtension(file.getName());
			accept = accept(extension);
		}
		return accept;
	}

	@Override
	public boolean accept(String extension) {
		boolean accept = false;
		if (!StringUtil.isBlank(extension)) {
			if (ArrayUtil.contains(extension, getExtensions())) {
				accept = true;
			}
		}
		return accept;
	}

	protected abstract String[] getExtensions();
}
