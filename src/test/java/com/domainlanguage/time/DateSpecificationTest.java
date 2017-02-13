/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import org.junit.Test;

import java.util.Calendar;
import java.util.Iterator;

import static org.junit.Assert.*;

public class DateSpecificationTest {

    @Test
    public void testFixedDate() {
        CalendarInterval y2004 = CalendarInterval.year(2004);
        DateSpecification independenceDay = DateSpecification.fixed(7, 4);
        assertEquals(CalendarDate.date(2004, 7, 4), ((AnnualDateSpecification) independenceDay).ofYear(2004));
        assertEquals(CalendarDate.date(2004, 7, 4), independenceDay.firstOccurrenceIn(y2004));
        assertTrue(independenceDay.isSatisfiedBy(CalendarDate.date(2004, 7, 4)));
        assertFalse(independenceDay.isSatisfiedBy(CalendarDate.date(2004, 7, 3)));
        assertTrue(independenceDay.isSatisfiedBy(CalendarDate.date(1970, 7, 4)));
    }

    @Test
    public void testNthWeekdayInMonth() {
        DateSpecification thanksgiving = DateSpecification.nthOccuranceOfWeekdayInMonth(11, Calendar.THURSDAY, 4);
        assertEquals(CalendarDate.date(2004, 11, 25), ((AnnualDateSpecification) thanksgiving).ofYear(2004));

        CalendarInterval y2004 = CalendarInterval.year(2004);
        assertEquals(CalendarDate.date(2004, 11, 25), thanksgiving.firstOccurrenceIn(y2004));
        assertTrue(thanksgiving.isSatisfiedBy(CalendarDate.date(2004, 11, 25)));
        assertFalse(thanksgiving.isSatisfiedBy(CalendarDate.date(2002, 11, 25)));
        CalendarInterval y2002 = CalendarInterval.year(2002);
        assertEquals(CalendarDate.date(2002, 11, 28), thanksgiving.firstOccurrenceIn(y2002));
        assertTrue(thanksgiving.isSatisfiedBy(CalendarDate.date(2002, 11, 28)));

        // Calculate all the Thanksgivings over a three year interval.
        CalendarInterval y2002_2004 = CalendarInterval.inclusive(2002, 1, 1, 2004, 12, 31);
        assertEquals(CalendarDate.date(2002, 11, 28), thanksgiving.firstOccurrenceIn(y2002_2004));
        Iterator iterator = thanksgiving.iterateOver(y2002_2004);
        assertTrue(iterator.hasNext());
        assertEquals(CalendarDate.date(2002, 11, 28), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(CalendarDate.date(2003, 11, 27), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(CalendarDate.date(2004, 11, 25), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSelectFirstFromInterval() {
        CalendarInterval y2002_2004 = CalendarInterval.inclusive(2002, 1, 1, 2004, 12, 31);
        CalendarInterval ylate2002_2004 = CalendarInterval.inclusive(2002, 8, 1, 2004, 12, 31);
        CalendarInterval ylate2002 = CalendarInterval.inclusive(2002, 8, 1, 2002, 12, 31);
        CalendarInterval ylate2002_early2003 = CalendarInterval.inclusive(2002, 8, 1, 2003, 6, 30);
        DateSpecification independenceDay = DateSpecification.fixed(7, 4);
        assertEquals(CalendarDate.date(2002, 7, 4), independenceDay.firstOccurrenceIn(y2002_2004));
        assertEquals(CalendarDate.date(2003, 7, 4), independenceDay.firstOccurrenceIn(ylate2002_2004));
        assertNull(independenceDay.firstOccurrenceIn(ylate2002));
        assertNull(independenceDay.firstOccurrenceIn(ylate2002_early2003));
    }

    @Test
    public void testIterateThroughInterval() {
        DateSpecification independenceDay = DateSpecification.fixed(7, 4);
        CalendarInterval ylate2002_early2005 = CalendarInterval.inclusive(2002, 8, 1, 2005, 6, 31);
        Iterator it = independenceDay.iterateOver(ylate2002_early2005);
        assertTrue(it.hasNext());
        assertEquals(CalendarDate.date(2003, 7, 4), it.next());
        assertTrue(it.hasNext());
        assertEquals(CalendarDate.date(2004, 7, 4), it.next());
        assertFalse(it.hasNext());
        assertNull(it.next());
    }
}