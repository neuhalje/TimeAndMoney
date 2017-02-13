/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import junit.framework.TestCase;

public class MinuteOfHourTest extends TestCase {
    public void testSimple() {
        assertEquals(11, MinuteOfHour.value(11).value());
        assertEquals(MinuteOfHour.value(23), MinuteOfHour.value(23));
    }
    public void testIllegalLessThanZero() {
        try {
            MinuteOfHour.value(-1);
        }catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }
    public void testGreaterThan() {
        try {
            HourOfDay.value(60);
        }catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }
    public void testLaterAfterEarlier() {
        MinuteOfHour later = MinuteOfHour.value(45);
        MinuteOfHour earlier = MinuteOfHour.value(15);
        assertTrue(later.isAfter(earlier));
    }
    public void testEarlierAfterLater() {
        MinuteOfHour earlier = MinuteOfHour.value(15);
        MinuteOfHour later = MinuteOfHour.value(45);
        assertFalse(earlier.isAfter(later));
    }
    public void testEqualAfterEqual() {
        MinuteOfHour anMinute = MinuteOfHour.value(45);
        MinuteOfHour anotherMinute = MinuteOfHour.value(45);
        assertFalse(anMinute.isAfter(anotherMinute));
    }
    public void testLaterBeforeEarlier() {
        MinuteOfHour later = MinuteOfHour.value(45);
        MinuteOfHour earlier = MinuteOfHour.value(15);
        assertFalse(later.isBefore(earlier));
    }
    public void testEarlierBeforeLater() {
        MinuteOfHour earlier = MinuteOfHour.value(15);
        MinuteOfHour later = MinuteOfHour.value(45);
        assertTrue(earlier.isBefore(later));
    }
    public void testEqualBeforeEqual() {
        MinuteOfHour anMinute = MinuteOfHour.value(15);
        MinuteOfHour anotherMinute = MinuteOfHour.value(15);
        assertFalse(anMinute.isBefore(anotherMinute));
    }
}
