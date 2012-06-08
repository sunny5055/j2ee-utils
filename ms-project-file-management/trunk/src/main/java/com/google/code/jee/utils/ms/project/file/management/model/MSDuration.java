package com.google.code.jee.utils.ms.project.file.management.model;

import net.sf.mpxj.Duration;

public class MSDuration {
	
	private Number duration;
	
	private Integer timeUnit;
	
	public MSDuration() {}
	
	public MSDuration(Duration duration) {
		this.duration = duration.getDuration();
		this.timeUnit = duration.getUnits().getValue();
	}

	/**
	 * @return the duration
	 */
	public Number getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Number duration) {
		this.duration = duration;
	}

	/**
	 * @return the timeUnit
	 */
	public Integer getTimeUnit() {
		return timeUnit;
	}

	/**
	 * @param timeUnit the timeUnit to set
	 */
	public void setTimeUnit(Integer timeUnit) {
		this.timeUnit = timeUnit;
	}

}
