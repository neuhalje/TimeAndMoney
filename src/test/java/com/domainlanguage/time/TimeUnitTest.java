/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import com.domainlanguage.tests.SerializationTester;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class TimeUnitTest {
    public static TimeUnit exampleForPersistentMappingTesting() {
        return TimeUnit.second;
    }

    public static TimeUnit.Type exampleTypeForPersistentMappingTesting() {
        return TimeUnit.Type.hour;
    }

    @Test
    public void testSerialization() {
        SerializationTester.assertCanBeSerialized(TimeUnit.month);
    }

    @Test
    public void testToString() {
        assertEquals("month", TimeUnit.month.toString());
    }

    @Test
    public void testConvertibleToMilliseconds() {
        assertTrue(TimeUnit.millisecond.isConvertibleToMilliseconds());
        assertTrue(TimeUnit.hour.isConvertibleToMilliseconds());
        assertTrue(TimeUnit.day.isConvertibleToMilliseconds());
        assertTrue(TimeUnit.week.isConvertibleToMilliseconds());
        assertFalse(TimeUnit.month.isConvertibleToMilliseconds());
        assertFalse(TimeUnit.year.isConvertibleToMilliseconds());
    }

    @Test
    public void testComparison() {
        assertEquals(0, TimeUnit.hour.compareTo(TimeUnit.hour));
        assertTrue(TimeUnit.hour.compareTo(TimeUnit.millisecond) > 0);
        assertTrue(TimeUnit.millisecond.compareTo(TimeUnit.hour) < 0);
        assertTrue(TimeUnit.day.compareTo(TimeUnit.hour) > 0);
        assertTrue(TimeUnit.hour.compareTo(TimeUnit.day) < 0);

        assertTrue(TimeUnit.month.compareTo(TimeUnit.day) > 0);
        assertTrue(TimeUnit.day.compareTo(TimeUnit.month) < 0);
        assertTrue(TimeUnit.quarter.compareTo(TimeUnit.hour) > 0);

        assertEquals(0, TimeUnit.month.compareTo(TimeUnit.month));
        assertTrue(TimeUnit.quarter.compareTo(TimeUnit.year) < 0);
        assertTrue(TimeUnit.year.compareTo(TimeUnit.quarter) > 0);
    }

    @Test
    public void testJavaCalendarConstantForBaseType() {
        assertEquals(Calendar.MILLISECOND, TimeUnit.millisecond.javaCalendarConstantForBaseType());
        assertEquals(Calendar.MILLISECOND, TimeUnit.hour.javaCalendarConstantForBaseType());
        assertEquals(Calendar.MILLISECOND, TimeUnit.day.javaCalendarConstantForBaseType());
        assertEquals(Calendar.MILLISECOND, TimeUnit.week.javaCalendarConstantForBaseType());
        assertEquals(Calendar.MONTH, TimeUnit.month.javaCalendarConstantForBaseType());
        assertEquals(Calendar.MONTH, TimeUnit.quarter.javaCalendarConstantForBaseType());
        assertEquals(Calendar.MONTH, TimeUnit.year.javaCalendarConstantForBaseType());
    }

    @Test
    public void testIsConvertableTo() {
        assertTrue(TimeUnit.hour.isConvertibleTo(TimeUnit.minute));
        assertTrue(TimeUnit.minute.isConvertibleTo(TimeUnit.hour));
        assertTrue(TimeUnit.year.isConvertibleTo(TimeUnit.month));
        assertTrue(TimeUnit.month.isConvertibleTo(TimeUnit.year));
        assertFalse(TimeUnit.month.isConvertibleTo(TimeUnit.hour));
        assertFalse(TimeUnit.hour.isConvertibleTo(TimeUnit.month));
    }

    @Test
    public void testNextFinerUnit() {
        assertEquals(TimeUnit.minute, TimeUnit.hour.nextFinerUnit());
        assertEquals(TimeUnit.month, TimeUnit.quarter.nextFinerUnit());
    }
}