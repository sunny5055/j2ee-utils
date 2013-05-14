package com.googlecode.jutils.pp.dataloader;

import java.io.File;

import com.googlecode.jutils.pp.dataloader.exception.DataLoaderException;

public interface DataLoader {
	String getName();

	Object execute(File configFile, Object... values) throws DataLoaderException;
}
