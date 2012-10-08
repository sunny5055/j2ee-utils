package com.googlecode.jutils.generator.service;

import com.googlecode.jutils.generator.exception.GeneratorServiceException;
import com.googlecode.jutils.generator.exporter.Exporter;

public interface GeneratorService {

	void generate(Exporter exporter) throws GeneratorServiceException;
}