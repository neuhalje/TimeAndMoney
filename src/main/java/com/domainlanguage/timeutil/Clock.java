/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import java.util.TimeZone;
import com.domainlanguage.time.*;

public class Clock {
	private static TimeSource timeSource;
	private static TimeZone defaultTimeZone;

	public static TimeZone defaultTimeZone() {
		//There is no reasonable automatic default.
		return defaultTimeZone;
	}
	
	public static void setDefaultTimeZone(TimeZone defaultTimeZone) {
		Clock.defaultTimeZone = defaultTimeZone;
	}
	
	public static TimeSource timeSource() {
		if (timeSource==null) {
			setTimeSource(SystemClock.timeSource());
		}
		return timeSource;
	}
	
	public static void setTimeSource(TimeSource timeSource) {
		Clock.timeSource = timeSource;
	}
	
	public static TimePoint now() {
		return timeSource().now();
	}
	
	public static CalendarDate today() {
		if (defaultTimeZone()==null) throw new RuntimeException("CalendarDate cannot be computed without setting a default TimeZone.");
		return now().calendarDate(defaultTimeZone());
	}

	public static void reset() {
		defaultTimeZone = null;
		timeSource = null;
	}
}
