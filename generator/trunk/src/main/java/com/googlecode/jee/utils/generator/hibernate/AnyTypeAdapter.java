package com.googlecode.jee.utils.generator.hibernate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

@SuppressWarnings("serial")
public class AnyTypeAdapter extends XmlAdapter<Object, Object> {
	@Override
	public Object unmarshal(Object value) {
		return value;
	}

	@Override
	public Object marshal(Object value) {
		return value;
	}
}
