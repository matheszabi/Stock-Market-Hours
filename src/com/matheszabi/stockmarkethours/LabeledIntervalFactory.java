package com.matheszabi.stockmarkethours;

import java.util.TimeZone;

// http://en.wikipedia.org/wiki/List_of_market_opening_times
public class LabeledIntervalFactory {

	public static LabeledInterval createLondon(){//NYSE
		return new LabeledInterval(8, 0, 16, 30, TimeZone.getTimeZone("Europe/London"), "London");
	}

	public static LabeledInterval createNewYork() {
		return new LabeledInterval(9, 30, 16, 0, TimeZone.getTimeZone("America/New_York"), "New York");
	}
	
	// Open: 08:00 (Eurex)	 8:00 (floor)  9:00 (Xetra)
	// Close 22:00 (Eurex) 	20:00 (floor) 17:30 (Xetra)
	// Xetra = SIX Swiss Exchange
	public static LabeledInterval createFrankfurt() {
		return new LabeledInterval(8, 0, 17, 30, TimeZone.getTimeZone(""), "Frankfurt");
	}
	public static LabeledInterval createHongKong() {
		return new LabeledInterval(9, 15, 16, 0, TimeZone.getTimeZone(""), "Hong Kong");
	}
	
	public static LabeledInterval createShanghai() {
		return new LabeledInterval(9, 30, 15, 0, TimeZone.getTimeZone(""), "Shanghai");
	}
	
}
