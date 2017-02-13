/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import org.junit.Test;

import static org.junit.Assert.*;

public class MinuteOfHourTest {
    @Test
    public void testSimple() {
        assertEquals(11, MinuteOfHour.value(11).value());
        assertEquals(MinuteOfHour.value(23), MinuteOfHour.value(23));
    }

    @Test
    public void testIllegalLessThanZero() {
        try {
            MinuteOfHour.value(-1);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }

    @Test
    public void testGreaterThan() {
        try {
            HourOfDay.value(60);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }

    @Test
    public void testLaterAfterEarlier() {
        MinuteOfHour later = MinuteOfHour.value(45);
        MinuteOfHour earlier = MinuteOfHour.value(15);
        assertTrue(later.isAfter(earlier));
    }

    @Test
    public void testEarlierAfterLater() {
        MinuteOfHour earlier = MinuteOfHour.value(15);
        MinuteOfHour later = MinuteOfHour.value(45);
        assertFalse(earlier.isAfter(later));
    }

    @Test
    public void testEqualAfterEqual() {
        MinuteOfHour anMinute = MinuteOfHour.value(45);
        MinuteOfHour anotherMinute = MinuteOfHour.value(45);
        assertFalse(anMinute.isAfter(anotherMinute));
    }

    @Test
    public void testLaterBeforeEarlier() {
        MinuteOfHour later = MinuteOfHour.value(45);
        MinuteOfHour earlier = MinuteOfHour.value(15);
        assertFalse(later.isBefore(earlier));
    }

    @Test
    public void testEarlierBeforeLater() {
        MinuteOfHour earlier = MinuteOfHour.value(15);
        MinuteOfHour later = MinuteOfHour.value(45);
        assertTrue(earlier.isBefore(later));
    }

    @Test
    public void testEqualBeforeEqual() {
        MinuteOfHour anMinute = MinuteOfHour.value(15);
        MinuteOfHour anotherMinute = MinuteOfHour.value(15);
        assertFalse(anMinute.isBefore(anotherMinute));
    }
}
