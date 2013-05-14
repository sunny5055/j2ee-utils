package com.googlecode.jutils.pp.settings;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.io.IoUtil;
import com.googlecode.jutils.pp.action.ActionCall;

@SuppressWarnings("serial")
public class Settings implements Serializable {
	private File configFile;
	private File sourceDirectory;
	private File outputDirectory;
	private Map<String, Object> data;
	private List<ActionCall> actions;

	public Settings() {
		super();
		this.data = new HashMap<String, Object>();
		this.actions = new ArrayList<ActionCall>();
	}

	public Settings(File configFile) {
		this();
		this.configFile = configFile;
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public File getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void addData(Map<String, Object> data) {
		if (!MapUtil.isEmpty(data)) {
			this.data.putAll(data);
		}
	}

	public void addData(String key, Object value) {
		if (!StringUtil.isBlank(key) && value != null) {
			this.data.put(key, data);
		}
	}

	public void removeData(String key) {
		if (!StringUtil.isBlank(key)) {
			this.data.remove(key);
		}
	}

	public List<ActionCall> getActions() {
		return actions;
	}

	public void addActions(List<ActionCall> actions) {
		if (!CollectionUtil.isEmpty(actions)) {
			this.actions.addAll(actions);
		}
	}

	public void addAction(ActionCall action) {
		if (action != null) {
			this.actions.add(action);
		}
	}

	public File getOutputFile(File sourceFile) {
		File outputFile = null;
		if (sourceFile != null) {
			final String relativePath = IoUtil.getRelativePath(getSourceDirectory(), sourceFile);
			final String fileName = FilenameUtils.concat(getOutputDirectory().getAbsolutePath(), relativePath);

			outputFile = new File(fileName);
		}

		return outputFile;
	}

	public void inheritFrom(Settings parentSettings) {
		if (parentSettings != null) {
			if (parentSettings.getSourceDirectory() != null) {
				this.setSourceDirectory(parentSettings.getSourceDirectory());
			}
			if (parentSettings.getOutputDirectory() != null) {
				this.setOutputDirectory(parentSettings.getOutputDirectory());
			}
			if (!CollectionUtil.isEmpty(parentSettings.getActions())) {
				this.actions.addAll(parentSettings.getActions());
			}
			if (!MapUtil.isEmpty(parentSettings.getData())) {
				this.data.putAll(parentSettings.getData());
			}
		}
	}
}
