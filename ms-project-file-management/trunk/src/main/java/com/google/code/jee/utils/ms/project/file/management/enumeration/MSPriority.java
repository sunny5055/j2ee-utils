package com.google.code.jee.utils.ms.project.file.management.enumeration;

public enum MSPriority {
	LOWEST(100),
	VERY_LOW(200),
	LOWER(300),
	LOW(400),
	MEDIUM(500),
	HIGH(600),
	HIGHER(700),
	VERY_HIGH(800),
	HIGHEST(900),
	DO_NOT_LEVEL(1000);
	
	private final int value;
 
	private MSPriority(int value) {
		this.value = value;
	}
 
	public int getValue() {
		return this.value;
	}
}
