package com.googlecode.jee.utils.generator.hibernate.model;

import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.googlecode.jee.utils.generator.hibernate.AnyTypeAdapter;

@XmlJavaTypeAdapter(AnyTypeAdapter.class)
public interface Value extends Serializable {
	boolean isNullable();
}