package com.google.code.jee.utils.ms.project.file.management.enumeration;

public enum MSConstraintType {
	AS_SOON_AS_POSSIBLE(0),
	AS_LATE_AS_POSSIBLE(1),
	MUST_START_ON(2),
	MUST_FINISH_ON(3),
	START_NO_EARLIER_THAN(4),
	START_NO_LATER_THAN(5),
	FINISH_NO_EARLIER_THAN(6),
	FINISH_NO_LATER_THAN(7);
	
	private final int value;
 
	private MSConstraintType(int value) {
		this.value = value;
	}
 
	public int getValue() {
		return this.value;
	}
}
