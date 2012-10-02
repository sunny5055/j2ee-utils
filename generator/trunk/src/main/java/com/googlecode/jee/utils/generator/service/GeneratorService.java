package com.googlecode.jee.utils.generator.service;

import com.googlecode.jee.utils.generator.exception.GeneratorServiceException;
import com.googlecode.jee.utils.generator.exporter.Exporter;

public interface GeneratorService {

	void generate(Exporter exporter) throws GeneratorServiceException;
}