/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import java.util.*;

public class TimeOfDay {
    private HourOfDay hour;
    private MinuteOfHour minute;
    
    public static TimeOfDay hourAndMinute(int hour, int minute) {
        return new TimeOfDay(hour, minute);
    }
    
    private TimeOfDay(int hour, int minute) {
        this.hour = HourOfDay.value(hour);
        this.minute = MinuteOfHour.value(minute);
    }

    public CalendarMinute on(CalendarDate date) {
        return CalendarMinute.dateAndTimeOfDay(date, this);
    }
    public String toString() {
        return hour.toString() + ":" + minute.toString();
    }
    public boolean equals(Object anotherObject) {
        if (!(anotherObject instanceof TimeOfDay))
            return false;
        return equals((TimeOfDay)anotherObject);
    }
    public boolean equals(TimeOfDay another) {
        if (another == null)
            return false;
        return hour.equals(another.hour) && minute.equals(another.minute);
    }
    public int hashCode() {
        return hour.hashCode() ^ minute.hashCode();
    }

    public boolean isAfter(TimeOfDay another) {
        return
            hour.isAfter(another.hour) ||
            hour.equals(another) && minute.isAfter(another.minute);
    }

    public boolean isBefore(TimeOfDay another) {
        return
            hour.isBefore(another.hour) ||
            hour.equals(another) && minute.isBefore(another.minute);
    }

    int getHour() {
        return hour.value();
    }

    int getMinute() {
        return minute.value();
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    TimeOfDay() {
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private int getForPersistentMapping_Hour() {
        return hour.value();
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Hour(int hour) {
        this.hour = HourOfDay.value(hour);
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private int getForPersistentMapping_Minute() {
        return minute.value();
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Minute(int minute) {
        this.minute = MinuteOfHour.value(minute);
    }

    public TimePoint asTimePointGiven(CalendarDate date, TimeZone timeZone) {
        CalendarMinute timeOfDayOnDate = on(date);
        return timeOfDayOnDate.asTimePoint(timeZone);
    }
}
