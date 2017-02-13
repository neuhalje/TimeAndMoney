/**
 * Copyright (c) 2006 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import java.util.*;

import junit.framework.*;

/**
 * TimeOfDayTest
 *
 * @author davem
 */
public class TimeOfDayTest extends TestCase {

    private static final TimeZone CST = TimeZone.getTimeZone("CST");
    private CalendarDate feb17 = CalendarDate.from(2006, 2, 17);
    private TimeOfDay midnight = TimeOfDay.hourAndMinute(0, 0);
    private TimeOfDay morning = TimeOfDay.hourAndMinute(10, 20);
    private TimeOfDay noon = TimeOfDay.hourAndMinute(12, 0);
    private TimeOfDay afternoon = TimeOfDay.hourAndMinute(15, 40);
    private TimeOfDay twoMinutesBeforeMidnight = TimeOfDay.hourAndMinute(23, 58);
    
    /**
     * Constructs a TimeOfDayTest.
     */
    public TimeOfDayTest() {
        super();
    }

    /**
     * Constructs a TimeOfDayTest.
     * @param name
     */
    public TimeOfDayTest(String name) {
        super(name);
    }

    public void testOnStartOfDay() {
        CalendarMinute feb17AtStartOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, 0, 0);
        assertEquals(feb17AtStartOfDay, midnight.on(feb17));
    }

    public void testOnMiddleOfDay() {
        CalendarMinute feb17AtMiddleOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, 12, 0);
        assertEquals(feb17AtMiddleOfDay, noon.on(feb17));
    }

    public void testOnEndOfDay() {
        CalendarMinute feb17AtEndOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, 23, 58);
        assertEquals(feb17AtEndOfDay, twoMinutesBeforeMidnight.on(feb17));
    }

    public void testEquals() {
        assertEquals(TimeOfDay.hourAndMinute(0, 0), midnight);
        assertEquals(TimeOfDay.hourAndMinute(10, 20), morning);
        assertEquals(TimeOfDay.hourAndMinute(12, 0), noon);
        assertEquals(TimeOfDay.hourAndMinute(15, 40), afternoon);
        assertEquals(TimeOfDay.hourAndMinute(23, 58), twoMinutesBeforeMidnight);
    }

    public void testHashCode() {
        assertEquals(TimeOfDay.hourAndMinute(0, 0).hashCode(), midnight.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(10, 20).hashCode(), morning.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(12, 0).hashCode(), noon.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(15, 40).hashCode(), afternoon.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(23, 58).hashCode(), twoMinutesBeforeMidnight.hashCode());
    }

    public void testAfterWithEarlierTimeOfDay() {
        assertTrue("expected twoMinutesBeforeMidnight to be after midnight", twoMinutesBeforeMidnight.isAfter(midnight));
        assertTrue("expected afternoon to be after morning", afternoon.isAfter(morning));
        assertTrue("expected noon to be after midnight", noon.isAfter(midnight));
    }

    public void testAfterWithLaterTimeOfDay() {
        assertFalse("expected midnight not after twoMinutesBeforeMidnight", midnight.isAfter(twoMinutesBeforeMidnight));
        assertFalse("expected morning not after afternoon", morning.isAfter(afternoon));
        assertFalse("expected noon not after twoMinutesBeforeMidnight", noon.isAfter(twoMinutesBeforeMidnight));
    }

    public void testAfterWithSameTimeOfDay() {
        assertFalse("expected midnight not after midnight", midnight.isAfter(midnight));
        assertFalse("expected morning not after morning", morning.isAfter(morning));
        assertFalse("expected afternoon not after afternoon", afternoon.isAfter(afternoon));
        assertFalse("expected noon not after noon", noon.isAfter(noon));
    }

    public void testBeforeWithEarlierTimeOfDay() {
        assertFalse("expected twoMinutesBeforeMidnight not after midnight", twoMinutesBeforeMidnight.isBefore(midnight));
        assertFalse("expected afternoon not after morning", afternoon.isBefore(morning));
        assertFalse("expected noon not after midnight", noon.isBefore(midnight));
    }

    public void testBeforeWithLaterTimeOfDay() {
        assertTrue("expected midnight not after twoMinutesBeforeMidnight", midnight.isBefore(twoMinutesBeforeMidnight));
        assertTrue("expected morning not after afternoon", morning.isBefore(afternoon));
        assertTrue("expected noon not after twoMinutesBeforeMidnight", noon.isBefore(twoMinutesBeforeMidnight));
    }

    public void testBeforeWithSameTimeOfDay() {
        assertFalse("expected midnight not after midnight", midnight.isBefore(midnight));
        assertFalse("expected morning not after morning", morning.isBefore(morning));
        assertFalse("expected afternoon not after afternoon", afternoon.isBefore(afternoon));
        assertFalse("expected noon not after noon", noon.isBefore(noon));
    }

    public void testGetHour() {
        assertEquals(0, midnight.getHour());
        assertEquals(10, morning.getHour());
        assertEquals(12, noon.getHour());
        assertEquals(15, afternoon.getHour());
        assertEquals(23, twoMinutesBeforeMidnight.getHour());
    }

    public void testGetMinute() {
        assertEquals(0, midnight.getMinute());
        assertEquals(20, morning.getMinute());
        assertEquals(0, noon.getMinute());
        assertEquals(40, afternoon.getMinute());
        assertEquals(58, twoMinutesBeforeMidnight.getMinute());
    }
    public void testAsTimePoint() {
        TimeOfDay fiveFifteen=TimeOfDay.hourAndMinute(17,15);
        CalendarDate mayEleventh=CalendarDate.date(2006,5, 11);
        TimePoint mayEleventhAtFiveFifteen=fiveFifteen.asTimePointGiven(mayEleventh, CST);
        assertEquals(TimePoint.at(2006,5,11,17,15,0,0,CST), mayEleventhAtFiveFifteen);
    }
}
