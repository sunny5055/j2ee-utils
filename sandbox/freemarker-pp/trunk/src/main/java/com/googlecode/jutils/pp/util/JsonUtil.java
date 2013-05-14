package com.googlecode.jutils.pp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.googlecode.jutils.StringUtil;

public final class JsonUtil {

	private JsonUtil() {
		super();
	}

	public static String toString(Object value) {
		String jsonString = null;
		if (value != null) {
			final Gson gson = getGson();
			jsonString = gson.toJson(value);
		}
		return jsonString;
	}

	public static JsonObject toJson(Object value) {
		JsonObject jsonObject = null;
		if (value != null) {
			final Gson gson = getGson();
			jsonObject = (JsonObject) gson.toJsonTree(value);
		}
		return jsonObject;
	}

	public static JsonObject toJson(String value) {
		JsonObject jsonObject = null;
		if (!StringUtil.isBlank(value)) {
			final Gson gson = getGson();
			jsonObject = gson.fromJson(value, JsonElement.class).getAsJsonObject();
		}
		return jsonObject;
	}

	public static Map<String, Object> toMap(String value) {
		Map<String, Object> map = null;
		if (value != null) {
			final JsonObject jsonObject = toJson(value);
			map = toMap(jsonObject);
		}
		return map;
	}

	public static Map<String, Object> toMap(JsonObject jsonObject) {
		Map<String, Object> map = null;
		if (jsonObject != null) {
			map = new HashMap<String, Object>();
			for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				final String key = entry.getKey();
				final JsonElement value = entry.getValue();
				if (value.isJsonObject()) {
					final Map<String, Object> values = toMap(value.getAsJsonObject());
					map.put(key, values);
				} else if (value.isJsonArray()) {
					final JsonArray array = (JsonArray) value;
					final List<String> values = new ArrayList<String>();
					for (final Iterator<JsonElement> it = array.iterator(); it.hasNext();) {
						final JsonElement item = it.next();
						values.add(item.getAsString());
					}

					map.put(key, values);
				} else {
					map.put(key, value.getAsString());
				}
			}
		}
		return map;
	}

	public static List<String> toList(JsonArray jsonArray) {
		List<String> list = null;
		if (jsonArray != null) {
			list = new ArrayList<String>();
			for (final Iterator<JsonElement> it = jsonArray.iterator(); it.hasNext();) {
				final JsonElement item = it.next();
				list.add(item.getAsString());
			}

		}
		return list;
	}

	private static Gson getGson() {
		final GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();

		return builder.create();
	}
}
