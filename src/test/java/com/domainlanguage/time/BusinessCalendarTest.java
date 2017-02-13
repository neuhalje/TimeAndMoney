/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class BusinessCalendarTest {

    private BusinessCalendar businessCalendar() {
        BusinessCalendar cal = new BusinessCalendar();
        cal.addHolidays(_HolidayDates.defaultHolidays());
        return cal;
    }

    @Test
    public void testElapsedBusinessDays() {
        CalendarDate nov1 = CalendarDate.from(2004, 11, 1);
        CalendarDate nov30 = CalendarDate.from(2004, 11, 30);
        CalendarInterval interval = CalendarInterval.inclusive(nov1, nov30);
        assertEquals(Duration.days(30), interval.length());
        // 1 holiday (Thanksgiving on a Thursday) + 8 weekend days.
        assertEquals(21, businessCalendar().getElapsedBusinessDays(interval));
    }

    @Test
    public void testIsWeekend() {
        CalendarDate saturday = CalendarDate.from(2004, 1, 10);
        assertTrue(businessCalendar().isWeekend(saturday));

        CalendarDate sunday = saturday.nextDay();
        assertTrue(businessCalendar().isWeekend(sunday));

        CalendarDate day = sunday;
        for (int i = 0; i < 5; i++) {
            day = day.nextDay();
            assertFalse("it's a midweek day", businessCalendar().isWeekend(day));
        }
        day = day.nextDay();
        assertTrue("finally, the weekend is here...", businessCalendar().isWeekend(day));

        CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a
        assertFalse("a holiday is not necessarily a weekend day", businessCalendar().isWeekend(newYearEve));
    }

    @Test
    public void testIsHoliday() {
        CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a
        assertTrue("New Years Eve is a holiday.", businessCalendar().isHoliday(newYearEve));
        assertFalse("The day after New Years Eve is not a holiday.", businessCalendar().isHoliday(newYearEve.nextDay()));
    }

    @Test
    public void testIsBusinessDay() {
        CalendarDate day = CalendarDate.from(2004, 1, 12); // it's a Monday
        for (int i = 0; i < 5; i++) {
            assertTrue("another working day", businessCalendar().isBusinessDay(day));
            day = day.nextDay();
        }
        assertFalse("finally, saturday arrived ...", businessCalendar().isBusinessDay(day));
        assertFalse("... then sunday", businessCalendar().isBusinessDay(day.nextDay()));

        CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a
        assertFalse("hey, it's a holiday", businessCalendar().isBusinessDay(newYearEve));
    }

    @Test
    public void testNearestBusinessDay() {
        CalendarDate saturday = CalendarDate.from(2004, 1, 10);
        CalendarDate sunday = saturday.nextDay();
        CalendarDate monday = sunday.nextDay();
        assertEquals(monday, businessCalendar().nearestBusinessDay(saturday));
        assertEquals(monday, businessCalendar().nearestBusinessDay(sunday));
        assertEquals(monday, businessCalendar().nearestBusinessDay(monday));

        CalendarDate newYearEve = CalendarDate.from(2004, 1, 1); // it's a
        assertEquals("it's a holiday & a thursday; wait till friday", newYearEve.nextDay(), businessCalendar().nearestBusinessDay(newYearEve));

        CalendarDate christmas = CalendarDate.from(2004, 12, 24); // it's a
        assertEquals("it's a holiday & a friday; wait till monday", CalendarDate.from(2004, 12, 27), businessCalendar().nearestBusinessDay(christmas));
    }

    @Test
    public void testBusinessDaysIterator() {
        CalendarDate start = CalendarDate.from(2004, 2, 5);
        CalendarDate end = CalendarDate.from(2004, 2, 8);
        CalendarInterval interval = CalendarInterval.inclusive(start, end);
        Iterator it = businessCalendar().businessDaysOnly(interval.daysIterator());
        assertTrue(it.hasNext());
        assertEquals(start, it.next());
        assertTrue(it.hasNext());
        assertEquals(CalendarDate.from(2004, 2, 6), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testNextBusinessDayOverWeekend() {
        CalendarDate friday = CalendarDate.from(2006, 06, 16);
        CalendarDate monday = CalendarDate.from(2006, 06, 19);
        CalendarDate actual = businessCalendar().nextBusinessDay(friday);
        assertEquals(monday, actual);
    }

    @Test
    public void testNextBusinessDayOverWeekday() {
        CalendarDate monday = CalendarDate.from(2006, 06, 19);
        CalendarDate tuesday = CalendarDate.from(2006, 06, 20);
        CalendarDate actual = businessCalendar().nextBusinessDay(monday);
        assertEquals(tuesday, actual);
    }

    @Test
    public void testPlusBusinessDayZero() {
        CalendarDate monday = CalendarDate.from(2006, 06, 19);
        CalendarDate actual = businessCalendar().plusBusinessDays(monday, 0);
        assertEquals(monday, actual);
    }

    @Test
    public void testPlusNonBusinessDayZero() {
        CalendarDate saturday = CalendarDate.from(2006, 06, 17);
        CalendarDate monday = CalendarDate.from(2006, 06, 19);
        CalendarDate actual = businessCalendar().plusBusinessDays(saturday, 0);
        assertEquals(monday, actual);

    }

    @Test
    public void testMinusNonBusinessDayZero() {
        CalendarDate saturday = CalendarDate.from(2006, 06, 17);
        CalendarDate friday = CalendarDate.from(2006, 06, 16);
        CalendarDate actual = businessCalendar().minusBusinessDays(saturday, 0);
        assertEquals(friday, actual);

    }

    @Test
    public void testBusinessDayReverseIterator() {
        CalendarDate friday = CalendarDate.from(2006, 06, 16);
        CalendarDate nextTuesday = CalendarDate.from(2006, 06, 20);
        CalendarInterval interval = CalendarInterval.inclusive(friday, nextTuesday);
        Iterator it = businessCalendar().businessDaysOnly(interval.daysInReverseIterator());
        assertTrue(it.hasNext());
        assertEquals(nextTuesday, it.next());
        assertTrue(it.hasNext());
        CalendarDate nextMonday = CalendarDate.from(2006, 06, 19);
        assertEquals(nextMonday, it.next());
        assertTrue(it.hasNext());
        assertEquals(friday, it.next());
        assertFalse(it.hasNext());
    }

}