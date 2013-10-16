package com.matheszabi.stockmarkethours;

import java.util.TimeZone;

public final class LabeledInterval {
	private int startHourLocal;
	private int startMinuteLocal;
	private int endHourLocal;
	private int endMinuteLocal;
	private TimeZone timezoneLocal;
	private String label;

	LabeledInterval(int startHourLocal, int startMinuteLocal, int endHourLocal, int endMinuteLocal, TimeZone timeZone, String label) {
		this.startHourLocal = startHourLocal;
		this.startMinuteLocal =startMinuteLocal;
		this.endHourLocal = endHourLocal;
		this.endMinuteLocal = endMinuteLocal;
		this.timezoneLocal = timeZone;
		this.label = label;
	}

	/**
	 * @return the startHourLocal
	 */
	public int getStartHourLocal() {
		return startHourLocal;
	}

	/**
	 * @return the startMinuteLocal
	 */
	public int getStartMinuteLocal() {
		return startMinuteLocal;
	}

	/**
	 * @return the endHourLocal
	 */
	public int getEndHourLocal() {
		return endHourLocal;
	}

	/**
	 * @return the endMinuteLocal
	 */
	public int getEndMinuteLocal() {
		return endMinuteLocal;
	}

	/**
	 * @return the timezoneLocal
	 */
	public TimeZone getTimezoneLocal() {
		return timezoneLocal;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

}
