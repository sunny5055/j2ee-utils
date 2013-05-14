package com.googlecode.jutils.pp.settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.CollectionUtil;
import com.googlecode.jutils.collection.MapUtil;
import com.googlecode.jutils.pp.action.ActionCall;
import com.googlecode.jutils.pp.dataloader.DataLoader;
import com.googlecode.jutils.pp.dataloader.exception.DataLoaderException;
import com.googlecode.jutils.pp.settings.exception.SettingsBuilderException;
import com.googlecode.jutils.pp.util.JsonUtil;

@Component
public class SettingsBuilder {
	protected static final Logger LOGGER = LoggerFactory.getLogger(SettingsBuilder.class);

	@Autowired
	private List<DataLoader> dataLoaders;

	public SettingsBuilder() {
		super();
		this.dataLoaders = new ArrayList<DataLoader>();
	}

	public Settings createSettings(File configFile) throws SettingsBuilderException {
		Settings settings = null;
		if (configFile != null) {
			String content = null;
			try {
				content = FileUtils.readFileToString(configFile);
			} catch (final IOException e) {
				throw new SettingsBuilderException(e);
			}

			if (!StringUtil.isBlank(content)) {
				final JsonObject jsonObject = JsonUtil.toJson(content);
				settings = createSettings(configFile, jsonObject);
			}
		}
		return settings;
	}

	private Settings createSettings(File configFile, JsonObject jsonObject) throws SettingsBuilderException {
		Settings settings = null;
		if (configFile != null && jsonObject != null) {
			settings = new Settings(configFile);

			if (jsonObject.has("extends")) {
				String filePath = null;
				if (!StringUtil.isBlank(configFile.getParent())) {
					filePath = FilenameUtils.concat(configFile.getParent(), jsonObject.get("extends").getAsString());
				} else {
					filePath = FilenameUtils.normalize(jsonObject.get("extends").getAsString());
				}
				if (!StringUtil.isBlank(filePath)) {
					final File parentConfigFile = new File(filePath);
					final Settings parentSettings = createSettings(parentConfigFile);
					if (parentSettings != null) {
						settings.inheritFrom(parentSettings);
					}
				}
			}

			if (jsonObject.has("sourceDirectory")) {
				final String sourceDirectory = jsonObject.get("sourceDirectory").getAsString();
				File directory = new File(configFile.getParentFile(), sourceDirectory);
				if (!directory.exists()) {
					directory = new File(sourceDirectory);
				}
				settings.setSourceDirectory(directory);
			}

			if (jsonObject.has("outputDirectory")) {
				final String outputDirectory = jsonObject.get("outputDirectory").getAsString();
				settings.setOutputDirectory(new File(outputDirectory));
			}

			if (jsonObject.has("actions")) {
				final JsonArray jsonArray = jsonObject.get("actions").getAsJsonArray();
				final List<ActionCall> actions = buildActions(jsonArray);
				settings.addActions(actions);
			}

			if (jsonObject.has("data")) {
				final JsonObject jsonData = jsonObject.get("data").getAsJsonObject();
				final Map<String, Object> data = buildData(configFile, jsonData);
				settings.addData(data);
			}
		}
		return settings;
	}

	public void validate(Settings settings) throws SettingsBuilderException {
		if (settings != null) {
			if (settings.getSourceDirectory() == null) {
				throw new SettingsBuilderException("sourceDirectory is required");
			}
			if (settings.getOutputDirectory() == null) {
				throw new SettingsBuilderException("outputDirectory is required");
			}
			if (CollectionUtil.isEmpty(settings.getActions())) {
				throw new SettingsBuilderException("actions is required");
			}
		}
	}

	@SuppressWarnings("unchecked")
	private List<ActionCall> buildActions(JsonArray jsonArray) {
		List<ActionCall> actions = null;
		if (jsonArray != null) {
			actions = new ArrayList<ActionCall>();
			for (final Iterator<JsonElement> it = jsonArray.iterator(); it.hasNext();) {
				final Map<String, Object> map = JsonUtil.toMap(it.next().getAsJsonObject());

				final String name = (String) map.get("name");
				final String applyTo = (String) map.get("applyTo");
				Map<String, Object> parameters = (Map<String, Object>) map.get("parameters");
				if (MapUtil.isEmpty(parameters)) {
					parameters = new HashMap<String, Object>();
				}
				parameters.put("applyTo", applyTo);

				final ActionCall action = new ActionCall(name, applyTo);
				action.setParameters(parameters);

				actions.add(action);
			}
		}
		return actions;
	}

	private Map<String, Object> buildData(File configFile, JsonObject jsonObject) {
		Map<String, Object> data = null;
		if (jsonObject != null) {
			data = new HashMap<String, Object>();

			for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				final String key = entry.getKey();
				final JsonElement value = entry.getValue();
				if (value.isJsonObject()) {
					final Map<String, Object> values = buildData(configFile, value.getAsJsonObject());
					data.put(key, values);
				} else if (value.isJsonArray()) {
					final JsonArray array = (JsonArray) value;
					final List<Object> values = new ArrayList<Object>();
					for (final Iterator<JsonElement> it = array.iterator(); it.hasNext();) {
						final JsonElement item = it.next();
						values.add(item.getAsString());
					}

					data.put(key, values);
				} else {
					final String item = value.getAsString();
					if (item.matches("(\\w+)\\s*\\([^\\)]*\\)")) {
						final String name = StringUtil.substringBefore(item, "(");
						final String[] parameters = StringUtil.split(StringUtil.substringBetween(item, "(", ")"), ",");

						final DataLoader dataLoader = getDataLoader(name);
						if (dataLoader != null) {
							Object result = null;
							try {
								result = dataLoader.execute(configFile, (Object[]) parameters);
							} catch (final DataLoaderException e) {
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug(e.getMessage(), e);
								}
							}
							if (result != null) {
								data.put(key, result);
							}
						} else {
							data.put(key, item);
						}
					} else {
						data.put(key, value.getAsString());
					}
				}
			}
		}
		return data;
	}

	private DataLoader getDataLoader(String name) {
		DataLoader dataLoader = null;
		if (!StringUtil.isBlank(name)) {
			if (!CollectionUtil.isEmpty(dataLoaders)) {
				for (final DataLoader d : dataLoaders) {
					if (d.getName().equalsIgnoreCase(name)) {
						dataLoader = d;
						break;
					}
				}
			}
		}
		return dataLoader;
	}
}
