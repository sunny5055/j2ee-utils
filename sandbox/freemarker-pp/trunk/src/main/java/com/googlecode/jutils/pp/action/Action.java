package com.googlecode.jutils.pp.action;

import java.io.File;
import java.util.Map;

import com.googlecode.jutils.pp.action.exception.ActionException;
import com.googlecode.jutils.pp.settings.Settings;

public interface Action {
	String getName();

	void execute(Settings settings, File file, Map<String, Object> parameters) throws ActionException;
}
