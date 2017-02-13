/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

public class HourOfDay {
    int value;
    
    public static HourOfDay value(int initial) {
        return new HourOfDay(initial);
    }
    public static HourOfDay value(int initial, String am_pm) {
        return HourOfDay.value(convertTo24hour(initial, am_pm));
    }
    private static int convertTo24hour(int hour, String am_pm) {
        if (!("AM".equalsIgnoreCase(am_pm) || "PM".equalsIgnoreCase(am_pm)))
            throw new IllegalArgumentException("AM PM indicator invalid: " + am_pm + ", please use AM or PM");
        if (hour < 0 | hour > 12)
            throw new IllegalArgumentException("Illegal value for 12 hour: " + hour + ", please use a value between 0 and 11");
        int translatedAmPm = "AM".equalsIgnoreCase(am_pm) ? 0 : 12;
        translatedAmPm -= (hour == 12) ? 12 : 0;
        return hour + translatedAmPm;
    }
    private HourOfDay(int initial) {
        if (initial < 0 || initial > 23)
            throw new IllegalArgumentException("Illegal value for 24 hour: " + initial + ", please use a value between 0 and 23");
        value = initial;
    }
    
    public boolean equals(Object another) {
        if (!(another instanceof HourOfDay))
            return false;
        return equals((HourOfDay)another);
    }
    public boolean equals(HourOfDay another) {
        return value == another.value;
    }
    public int hashCode() {
        return value;
    }

    public boolean isAfter(HourOfDay another) {
        return value > another.value;
    }

    public boolean isBefore(HourOfDay another) {
        return value < another.value;
    }

    public int value() {
        return value;
    }
    public String toString() {
        return String.valueOf(value);
    }
    private static Class getPrimitivePersistenceMappingType() {
        return Integer.TYPE;
    }
}
