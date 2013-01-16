package com.googlecode.jutils.spring;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;

/**
 * The Class EnhancedPropertyPlaceholderConfigurer.
 */
public class EnhancedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private Map<String, String> properties;

	/**
	 * {@inheritedDoc}
	 */
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) {
		final Map<String, String> tmpProperties = new HashMap<String, String>(props.size());
		super.processProperties(beanFactoryToProcess, props);
		for (final Entry<Object, Object> entry : props.entrySet()) {
			tmpProperties.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}
		this.properties = Collections.unmodifiableMap(tmpProperties);
	}

	/**
	 * Gets the property.
	 * 
	 * @param key
	 *            the key
	 * @return the property
	 */
	public String getProperty(String key) {
		String value = null;
		if (!StringUtil.isBlank(key) && !MapUtil.isEmpty(properties)) {
			if (properties.containsKey(key)) {
				value = properties.get(key);
			}
		}
		return value;
	}

	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	public Map<String, String> getProperties() {
		return this.properties;
	}
}
