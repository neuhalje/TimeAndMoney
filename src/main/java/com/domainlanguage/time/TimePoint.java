/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.io.*;
import java.text.*;
import java.util.*;

public class TimePoint implements Comparable, Serializable {
	private static final TimeZone GMT = TimeZone.getTimeZone("Universal");

	long millisecondsFromEpoc;

// CREATION METHODS
	
	public static TimePoint atMidnightGMT(int year, int month, int date) {
		return atMidnight(year, month, date, GMT);
	}

	public static TimePoint atMidnight(int year, int month, int date, TimeZone zone) {
		return at(year, month, date, 0, 0, 0, 0, zone);
	}

	public static TimePoint atGMT(int year, int month, int date, int hour, int minute) {
		return atGMT(year, month, date, hour, minute, 0, 0);
	}
	
	public static TimePoint atGMT(int year, int month, int date, int hour, int minute, int second) {
		return atGMT(year, month, date, hour, minute, second, 0);
	}

	public static TimePoint at(int year, int month, int date, int hour, int minute, int second, TimeZone zone) {
		return at(year, month, date, hour, minute, second, 0, zone);
	}

	public static TimePoint atGMT(int year, int month, int date, int hour, int minute, int second, int millisecond) {
		return at(year, month, date, hour, minute, second, millisecond, GMT);
	}
	
	public static TimePoint at12hr(int year, int month, int date, int hour, String am_pm, int minute, int second, int millisecond, TimeZone zone) {
		return at(year, month, date, convertedTo24hour(hour, am_pm), minute, second, millisecond, zone);
	}

	private static int convertedTo24hour(int hour, String am_pm) {
		int translatedAmPm = "AM".equalsIgnoreCase(am_pm) ? 0 : 12;
		translatedAmPm -= (hour == 12) ? 12 : 0;
		return hour + translatedAmPm;
	}

	public static TimePoint at(int year, int month, int date, int hour, int minute, int second, int millisecond, TimeZone zone) {
		Calendar calendar = Calendar.getInstance(zone);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, millisecond);
		return from(calendar);
	}

	public static TimePoint parseGMTFrom(String dateString, String pattern) {
		return parseFrom(dateString, pattern, GMT);
	}
	
	public static TimePoint parseFrom(String dateString, String pattern, TimeZone zone) {
		DateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(zone);
		Date date = format.parse(dateString, new ParsePosition(0));
		return from(date);
	}
	
	public static TimePoint from(Date javaDate) {
		return from(javaDate.getTime());
	}

	public static TimePoint from(Calendar calendar) {
		return from(calendar.getTime());
	}

	public static TimePoint from(long milliseconds) {
		TimePoint result =  new TimePoint(milliseconds);
		//assert FAR_FUTURE == null || result.isBefore(FAR_FUTURE);
		//assert FAR_PAST == null || result.isAfter(FAR_PAST);
		return result;

	}
	
	private TimePoint(long milliseconds) {
		this.millisecondsFromEpoc = milliseconds;
	}
    
	
	
// BEHAVIORAL METHODS
	public boolean equals(Object other) {
		return 
		//revisit: maybe use: Reflection.equalsOverClassAndNull(this, other)
			(other instanceof TimePoint) &&
			((TimePoint) other).millisecondsFromEpoc == this.millisecondsFromEpoc;
	}
	
	public int hashCode() {
		return (int) millisecondsFromEpoc;
	}

	public TimePoint backToMidnight(TimeZone zone) {
		return calendarDate(zone).asTimeInterval(zone).start();
	}
	
	public CalendarDate calendarDate(TimeZone zone) {
		return CalendarDate.from(this, zone);
	}
	
	public boolean isSameDayAs(TimePoint other, TimeZone zone) {
		return this.calendarDate(zone).equals(other.calendarDate(zone));
	}

	public String toString() {
		return asJavaUtilDate().toString(); //for better readability
		//return String.valueOf(millisecondsFromEpoc);
	}

	public String toString(String pattern, TimeZone zone) {
		DateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(zone);
		return format.format(asJavaUtilDate());
	}

	public boolean isBefore(TimePoint other) {
		return this.millisecondsFromEpoc < other.millisecondsFromEpoc;
	}

	public boolean isAfter(TimePoint other) {
		return this.millisecondsFromEpoc > other.millisecondsFromEpoc;
	}

	public int compareTo(Object other) {
		TimePoint otherPoint = (TimePoint)other;
		if (this.isBefore(otherPoint)) return -1;
		if (this.isAfter(otherPoint)) return 1;
		return 0;
	}
	
	public TimePoint nextDay() {
		return this.plus(Duration.days(1));
	}
	
	public Date asJavaUtilDate() {
		return new Date(millisecondsFromEpoc);
	}

	public Calendar asJavaCalendar(TimeZone zone) {
		Calendar result = Calendar.getInstance(zone);
		result.setTime(asJavaUtilDate());
		return result;
	}

	public Calendar asJavaCalendar() {
		return asJavaCalendar(GMT);
	}

	
// CONVENIENCE METHODS
// (Responsibility lies elsewhere, but language is more fluid with a method
// here.)
	
	public boolean isBefore(TimeInterval interval) {
		return interval.isAfter(this);
	}

	public boolean isAfter(TimeInterval interval) {
		return interval.isBefore(this);
	}
	
	public TimePoint plus(Duration duration) {
		return duration.addedTo(this);
	}

	public TimePoint minus(Duration duration) {
		return duration.subtractedFrom(this);
	}

	public TimeInterval until(TimePoint end) {
		return TimeInterval.over(this, end);
	}

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    TimePoint() {
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private long getForPersistentMapping_MillisecondsFromEpoc() {
        return millisecondsFromEpoc;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_MillisecondsFromEpoc(long millisecondsFromEpoc) {
        this.millisecondsFromEpoc = millisecondsFromEpoc;
    }
	
}
