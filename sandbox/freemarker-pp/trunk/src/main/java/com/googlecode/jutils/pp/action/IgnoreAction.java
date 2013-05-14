package com.googlecode.jutils.pp.action;

import java.io.File;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.googlecode.jutils.pp.settings.Settings;

@Component
public class IgnoreAction extends AbstractAction {

	@Override
	public void execute(Settings settings, File file, Map<String, Object> parameters) {
		return;
	}
}
