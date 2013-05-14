package com.googlecode.jutils.pp.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jutils.StringUtil;

public abstract class AbstractDataLoader implements DataLoader {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataLoader.class);

	@Override
	public String getName() {
		String name = getClass().getSimpleName();
		name = StringUtil.remove(name, "DataLoader");
		return StringUtil.toLowercaseFirstCharacter(name);
	}
}
