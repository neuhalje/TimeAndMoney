/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

import junit.framework.*;

import com.domainlanguage.tests.*;

public class CalendarIntervalTest extends TestCase {
	private CalendarDate may1 = CalendarDate.date(2004, 5, 1);
	private CalendarDate may2 = CalendarDate.date(2004, 5, 2);
	private CalendarDate may3 = CalendarDate.date(2004, 5, 3);
	private CalendarDate may14 = CalendarDate.date(2004, 5, 14);
	private CalendarDate may20 = CalendarDate.date(2004, 5, 20);
	private CalendarDate may31 = CalendarDate.date(2004, 5, 31);
	private CalendarDate apr15 = CalendarDate.date(2004, 4, 15);
	private CalendarDate jun1 = CalendarDate.date(2004, 6, 1);
	private CalendarInterval may = CalendarInterval.inclusive(2004, 5, 1, 2004, 5, 31);
	private TimeZone ct = TimeZone.getTimeZone("America/Chicago");

    public void testSerialization() {
        SerializationTester.assertCanBeSerialized(may);
    }

    public void testTranslationToTimeInterval() {
        TimeInterval day = may20.asTimeInterval(ct);
        assertEquals("May20Ct", TimePoint.atMidnight(2004, 5, 20, ct), day.start());
    }

    public void testIncludes() {
        assertFalse("apr15", may.includes(apr15));
        assertTrue("may1", may.includes(may1));
        assertTrue("may20", may.includes(may20));
        assertFalse("jun1", may.includes(jun1));
        assertTrue("may", may.covers(may));
    }

    public void testEquals() {
        assertTrue(may.equals(CalendarInterval.inclusive(may1, may31)));
        assertFalse(may.equals(may1));
        assertFalse(may.equals(CalendarInterval.inclusive(may1, may20)));
    }

    public void testDaysAdd() {
        assertEquals(may20, may1.plusDays(19));
    }

    public void testDaysIterator() {
        Iterator iterator = CalendarInterval.inclusive(may1, may3).daysIterator();
        assertTrue(iterator.hasNext());
        assertEquals(may1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(may2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(may3, iterator.next());
        assertFalse(iterator.hasNext());

    }

    public void testSubintervalIterator() {
        CalendarInterval may1_3 = CalendarInterval.inclusive(may1, may3);
        Iterator iterator = may1_3.subintervalIterator(Duration.days(1));
        assertTrue(iterator.hasNext());
        assertEquals(may1, ((CalendarInterval)iterator.next()).start());
        assertTrue(iterator.hasNext());
        assertEquals(may2, ((CalendarInterval)iterator.next()).start());
        assertTrue(iterator.hasNext());
        assertEquals(may3, ((CalendarInterval)iterator.next()).start());
        assertFalse(iterator.hasNext());

        iterator = may1_3.subintervalIterator(Duration.days(2));
        assertTrue(iterator.hasNext());
        assertEquals(may1.through(may2), iterator.next());
        assertFalse(iterator.hasNext());

        try {
            iterator = may1_3.subintervalIterator(Duration.hours(25));
            fail("CalendarInterval should not accept subinterval length that is not a multiple of days.");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        iterator = may1_3.subintervalIterator(Duration.months(1));
        assertFalse(iterator.hasNext());

        CalendarInterval apr15_jun1 = CalendarInterval.inclusive(apr15, jun1);
        iterator = apr15_jun1.subintervalIterator(Duration.months(1));
        assertTrue(iterator.hasNext());
        assertEquals(apr15.through(may14), iterator.next());
        assertFalse(iterator.hasNext());
    }

    public void testLength() {
        assertEquals(Duration.days(3), may1.through(may3).length());
        CalendarInterval may2002_july2004 = CalendarInterval.inclusive(2002, 5, 1, 2004, 7, 1);
        // (5/1/2002-4/30/2003) 365 days + (-4/30/2004) 366 + (5/1-7/31) 31+30+1 = 793 days
        assertEquals(Duration.days(793), may2002_july2004.length());
        assertEquals(Duration.months(26), may2002_july2004.lengthInMonths());
        assertEquals(Duration.months(1), apr15.through(may14).lengthInMonths());
    }

    public void testComplements() {
        CalendarInterval may1Onward = CalendarInterval.inclusive(may1, null);
        CalendarInterval may2Onward = CalendarInterval.inclusive(may2, null);
        List complementList = may2Onward.complementRelativeTo(may1Onward);
        assertEquals(1, complementList.size());
        
        CalendarInterval complement = (CalendarInterval) complementList.iterator().next();
        assertTrue(complement.isClosed());
        assertEquals(may1, complement.start());
        assertEquals(may1, complement.end());
    }

    public void testSingleDateCalendarIntervalCompare() {
        CalendarInterval may1_may1 = CalendarInterval.inclusive(may1, may1);
        assertEquals(may1, may1_may1.start());
        assertEquals(may1, may1_may1.end());
        assertEquals(0, may1.compareTo(may1_may1.start()));
        assertEquals(0, may1_may1.start().compareTo(may1));
        CalendarInterval may1_may2 = CalendarInterval.inclusive(may1, may2);
        assertTrue(may1.compareTo(may1_may2) < 0);
        assertTrue(may1_may2.compareTo(may1_may1) > 0);
    }
}