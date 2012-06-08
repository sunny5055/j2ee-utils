package com.google.code.jee.utils.ms.project.file.management.enumeration;

public enum MSRelationType {
	FINISH_FINISH(0),
	FINISH_START(1),
	START_FINISH(2),
	START_START(3);
	
	private final int value;
 
	private MSRelationType(int value) {
		this.value = value;
	}
 
	public int getValue() {
		return this.value;
	}
}
