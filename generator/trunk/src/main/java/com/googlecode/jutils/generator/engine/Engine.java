package com.googlecode.jutils.generator.engine;

import com.googlecode.jutils.generator.exception.GeneratorServiceException;

public interface Engine {
	String printHelp();

	void generate(String xmlContent) throws GeneratorServiceException;
}
