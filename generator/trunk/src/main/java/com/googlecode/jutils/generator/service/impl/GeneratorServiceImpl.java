package com.googlecode.jutils.generator.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.generator.engine.Generator;
import com.googlecode.jutils.generator.exception.GeneratorServiceException;
import com.googlecode.jutils.generator.service.GeneratorService;
import com.googlecode.jutils.io.IoUtil;

@Service
public class GeneratorServiceImpl implements GeneratorService {
	protected static final Logger LOGGER = Logger.getLogger(GeneratorServiceImpl.class);

	@Autowired
	private List<Generator> engines;

	public GeneratorServiceImpl() {
		super();
		this.engines = new ArrayList<Generator>();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void generate(String xmlContent) throws GeneratorServiceException {
		if (!StringUtil.isBlank(xmlContent) && !CollectionUtil.isEmpty(engines)) {
			for (final Generator engine : engines) {
				engine.generate(xmlContent);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void generate(File xmlFile) throws GeneratorServiceException {
		if (xmlFile != null) {
			Reader reader = null;
			try {
				reader = new BufferedReader(new FileReader(xmlFile));
				generate(reader);
			} catch (final FileNotFoundException e) {
				throw new GeneratorServiceException(e);
			} catch (final Exception e) {
				throw new GeneratorServiceException(e);
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException e) {
						throw new GeneratorServiceException(e);
					}
				}
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void generate(InputStream inputStream) throws GeneratorServiceException {
		if (inputStream != null) {
			Reader reader = null;
			try {
				reader = new InputStreamReader(inputStream);
				generate(reader);
			} catch (final Exception e) {
				throw new GeneratorServiceException(e);
			}
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void generate(Reader reader) throws GeneratorServiceException {
		if (reader != null) {
			String xmlContent = null;
			try {
				xmlContent = IoUtil.toString(reader);
			} catch (final IOException e) {
				throw new GeneratorServiceException(e);
			}

			generate(xmlContent);
		}
	}
}
