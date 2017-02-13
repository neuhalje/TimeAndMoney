/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

public class MinuteOfHour {
    int value;
    
    public static MinuteOfHour value(int initial) {
        return new MinuteOfHour(initial);
    }
    private MinuteOfHour(int initial) {
        if (initial < 0 || initial > 59)
            throw new IllegalArgumentException("Illegal value for minute: " + initial + ", please use a value between 0 and 59");
        value = initial;
    }
    
    public boolean equals(Object another) {
        if (!(another instanceof MinuteOfHour))
            return false;
        return equals((MinuteOfHour)another);
    }
    public boolean equals(MinuteOfHour another) {
        return value == another.value;
    }
    public int hashCode() {
        return value;
    }

    public boolean isAfter(MinuteOfHour another) {
        return value > another.value;
    }

    public boolean isBefore(MinuteOfHour another) {
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
