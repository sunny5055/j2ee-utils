package com.googlecode.jutils.generator.util;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.collection.MapUtil;

public class PropertyUtil {
	private static String START_TAG = "{";
	private static String END_TAG = "}";
	private static String PATTERN = "\\{[^\\{\\}]+\\}";

	public static String resolve(Map<String, String> properties, String key) {
		String value = null;
		if (!MapUtil.isEmpty(properties) && !StringUtil.isBlank(key)) {
			value = properties.get(key);
			if (!StringUtil.isBlank(value)) {
				final Pattern pattern = Pattern.compile(PATTERN);
				final Matcher matcher = pattern.matcher(value);
				while (matcher.find()) {
					final String currentMatch = matcher.group();
					if (!StringUtil.isBlank(currentMatch)) {
						final String replaceValue = resolve(properties, getKey(currentMatch));
						if (replaceValue != null) {
							value = StringUtil.replace(value, currentMatch, replaceValue);
						}
					}
				}
			}

		}
		return value;
	}

	private static String getKey(String aString) {
		String key = null;
		if (!StringUtil.isBlank(aString)) {
			key = StringUtil.remove(aString, START_TAG);
			key = StringUtil.remove(key, END_TAG);
		}
		return key;
	}
}
