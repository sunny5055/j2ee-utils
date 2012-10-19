package com.googlecode.jee.utils.generator.hibernate.listener;

import javax.xml.bind.Unmarshaller.Listener;

import org.apache.log4j.Logger;

import com.googlecode.jee.utils.generator.hibernate.model.Entity;
import com.googlecode.jee.utils.generator.hibernate.model.Property;

public class UnmarshallerListener extends Listener {
	protected static final Logger LOGGER = Logger.getLogger(UnmarshallerListener.class);

	@Override
	public void beforeUnmarshal(Object target, Object parent) {
		super.beforeUnmarshal(target, parent);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(target + " " + parent);
		}
	}

	@Override
	public void afterUnmarshal(Object target, Object parent) {
		super.afterUnmarshal(target, parent);
		if (target instanceof Property) {
			final Property property = (Property) target;
			property.setEntity((Entity) parent);
		}
	}

}
