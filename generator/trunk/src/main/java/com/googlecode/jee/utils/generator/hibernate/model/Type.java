package com.googlecode.jee.utils.generator.hibernate.model;

import java.io.Serializable;

public interface Type extends Serializable {

	boolean isCollectionType();

	boolean isEntityType();

	String getName();

}
