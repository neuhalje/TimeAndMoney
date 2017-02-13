/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import org.junit.Test;

import static org.junit.Assert.*;

public class HourOfDayTest {

    @Test
    public void test24Simple() {
        assertEquals(22, HourOfDay.value(22).value());
    }

    @Test
    public void test12Simple() {
        assertEquals(HourOfDay.value(22), HourOfDay.value(10, "PM"));
        assertEquals(HourOfDay.value(3), HourOfDay.value(3, "am"));
    }

    @Test
    public void test24IllegalLessThanZero() {
        try {
            HourOfDay.value(-1);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }

    @Test
    public void test24GreaterThan() {
        try {
            HourOfDay.value(24);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }

    @Test
    public void test12IllegalLessThanZero() {
        try {
            HourOfDay.value(-1, "PM");
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }

    @Test
    public void test12GreaterThan() {
        try {
            HourOfDay.value(13, "AM");
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }

    @Test
    public void test12BadAmPm() {
        try {
            HourOfDay.value(5, "FD");
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail("Illegal Argument Not Caught");
    }

    @Test
    public void testLaterAfterEarlier() {
        HourOfDay later = HourOfDay.value(8);
        HourOfDay earlier = HourOfDay.value(6);
        assertTrue(later.isAfter(earlier));
    }

    @Test
    public void testEarlierAfterLater() {
        HourOfDay earlier = HourOfDay.value(8);
        HourOfDay later = HourOfDay.value(20);
        assertFalse(earlier.isAfter(later));
    }

    @Test
    public void testEqualAfterEqual() {
        HourOfDay anHour = HourOfDay.value(8);
        HourOfDay anotherHour = HourOfDay.value(8);
        assertFalse(anHour.isAfter(anotherHour));
    }

    @Test
    public void testLaterBeforeEarlier() {
        HourOfDay later = HourOfDay.value(8);
        HourOfDay earlier = HourOfDay.value(6);
        assertFalse(later.isBefore(earlier));
    }

    @Test
    public void testEarlierBeforeLater() {
        HourOfDay earlier = HourOfDay.value(8);
        HourOfDay later = HourOfDay.value(20);
        assertTrue(earlier.isBefore(later));
    }

    @Test
    public void testEqualBeforeEqual() {
        HourOfDay anHour = HourOfDay.value(8);
        HourOfDay anotherHour = HourOfDay.value(8);
        assertFalse(anHour.isBefore(anotherHour));
    }
}
