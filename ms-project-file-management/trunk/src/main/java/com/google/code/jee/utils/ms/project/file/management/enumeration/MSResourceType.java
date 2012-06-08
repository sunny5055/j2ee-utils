package com.google.code.jee.utils.ms.project.file.management.enumeration;

public enum MSResourceType {
	MATERIAL(0),
	WORK(1),
	COST(2);
	
	private final int value;
 
	private MSResourceType(int value) {
		this.value = value;
	}
 
	public int getValue() {
		return this.value;
	}
	
}
