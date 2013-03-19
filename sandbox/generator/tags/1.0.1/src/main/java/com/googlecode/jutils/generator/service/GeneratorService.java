package com.googlecode.jutils.generator.service;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import com.googlecode.jutils.generator.exception.GeneratorServiceException;

public interface GeneratorService {

	void generate(String xmlContent) throws GeneratorServiceException;

	void generate(File xmlFile) throws GeneratorServiceException;

	void generate(InputStream inputStream) throws GeneratorServiceException;

	void generate(Reader reader) throws GeneratorServiceException;
}