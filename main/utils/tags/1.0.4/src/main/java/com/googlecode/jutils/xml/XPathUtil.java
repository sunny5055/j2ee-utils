package com.googlecode.jutils.xml;

import org.dom4j.Document;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.convertor.ConvertorUtil;

public class XPathUtil {

	private XPathUtil() {
		super();
	}

	public static Integer getIntegerValue(Document document, String query) {
		Integer value = null;
		if (document != null && !StringUtil.isBlank(query)) {
			final String stringValue = document.valueOf(query);
			if (!StringUtil.isBlank(stringValue)) {
				value = ConvertorUtil.toInteger(stringValue);
			}
		}
		return value;
	}
}
