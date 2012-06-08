package com.google.code.jee.utils.ms.project.file.management.enumeration;

public enum MSTimeUnit {
	MINUTES(0),
	HOURS(1),
	DAYS(2),
	WEEKS(3),
	MONTHS(4),
	PERCENT(5),
	YEARS(6),
	ELAPSED_MINUTES(7),
	ELAPSED_HOURS(8),
	ELAPSED_DAYS(9),
	ELAPSED_WEEKS(10),
	ELAPSED_MONTHS(11),
	ELAPSED_YEARS(12),
	ELAPSED_PERCENT(13);
	
	private final int value;
 
	private MSTimeUnit(int value) {
		this.value = value;
	}
 
	public int getValue() {
		return this.value;
	}

}
