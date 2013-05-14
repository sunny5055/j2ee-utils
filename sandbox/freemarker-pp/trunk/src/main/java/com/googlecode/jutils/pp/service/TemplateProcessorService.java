package com.googlecode.jutils.pp.service;

import com.googlecode.jutils.pp.action.exception.ActionException;
import com.googlecode.jutils.pp.settings.Settings;

public interface TemplateProcessorService {

	void process(Settings settings) throws ActionException;
}
