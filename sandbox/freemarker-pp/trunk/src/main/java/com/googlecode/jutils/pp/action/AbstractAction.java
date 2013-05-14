package com.googlecode.jutils.pp.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jutils.StringUtil;

public abstract class AbstractAction implements Action {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractAction.class);

	@Override
	public String getName() {
		String name = getClass().getSimpleName();
		name = StringUtil.remove(name, "Action");
		return StringUtil.toLowercaseFirstCharacter(name);
	}
}
