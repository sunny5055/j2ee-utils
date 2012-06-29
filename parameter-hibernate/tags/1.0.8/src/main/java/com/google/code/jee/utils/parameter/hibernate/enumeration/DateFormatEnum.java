package com.google.code.jee.utils.parameter.hibernate.enumeration;

public enum DateFormatEnum {
	DATE_TIME("dd/MM/yyyy HH:mm:ss"),
	DATE_ONLY("dd/MM/yyyy"),
	TIME_ONLY("HH:mm:ss");
	
	private final String dateFormat;
	 
	private DateFormatEnum(String dateFormat) {
		this.dateFormat = dateFormat;
	}
 
	public String getValue() {
		return this.dateFormat;
	}
}
