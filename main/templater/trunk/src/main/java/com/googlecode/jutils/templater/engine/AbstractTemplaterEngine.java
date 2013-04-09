package com.googlecode.jutils.templater.engine;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractTemplaterEngine.
 */
public abstract class AbstractTemplaterEngine implements TemplaterEngine {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractTemplaterEngine.class);
	protected Map<String, Object> defaultData;

	/**
	 * Instantiates a new abstract templater engine.
	 */
	public AbstractTemplaterEngine() {
		super();
		this.defaultData = new HashMap<String, Object>();
	}

	/**
	 * Getter : return the default data.
	 * 
	 * @return the default data
	 */
	public Map<String, Object> getDefaultData() {
		return defaultData;
	}

	/**
	 * Sets the default data.
	 * 
	 * @param defaultData
	 *            the default data
	 */
	public void setDefaultData(Map<String, Object> defaultData) {
		this.defaultData = defaultData;
	}

}
