package com.google.code.jee.utils.generator;

import java.io.IOException;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.google.code.jee.utils.dal.dto.AbstractDto;
import com.google.code.jee.utils.templater.exception.TemplateServiceException;

public interface GeneratorService {
	<E extends AbstractDto<?>> void generateNamedQueries(PropertiesConfiguration configuration, Class<E> clazz) throws TemplateServiceException, IOException;

	<E extends AbstractDto<?>> void generateDao(PropertiesConfiguration configuration, Class<E> clazz) throws TemplateServiceException, IOException;

	<E extends AbstractDto<?>> void generateService(PropertiesConfiguration configuration, Class<E> clazz) throws TemplateServiceException, IOException;
}
