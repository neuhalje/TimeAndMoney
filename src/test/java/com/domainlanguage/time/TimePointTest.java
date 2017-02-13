/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

import junit.framework.*;

import com.domainlanguage.tests.*;

public class TimePointTest extends TestCase {
    private static final String AM = "AM";
    private static final String PM = "PM";

    private TimeZone gmt = TimeZone.getTimeZone("Universal");
    private TimeZone pt = TimeZone.getTimeZone("America/Los_Angeles");
    private TimeZone ct = TimeZone.getTimeZone("America/Chicago");
    private TimePoint dec19_2003 = TimePoint.atMidnightGMT(2003, 12, 19);
    private TimePoint dec20_2003 = TimePoint.atMidnightGMT(2003, 12, 20);
    private TimePoint dec21_2003 = TimePoint.atMidnightGMT(2003, 12, 21);
    private TimePoint dec22_2003 = TimePoint.atMidnightGMT(2003, 12, 22);

    public void testSerialization() {
        SerializationTester.assertCanBeSerialized(dec19_2003);
    }

    public void testCreationWithDefaultTimeZone() {
        TimePoint expected = TimePoint.atGMT(2004, 1, 1, 0, 0, 0, 0);
        assertEquals("at midnight", expected, TimePoint.atMidnightGMT(2004, 1,
                1));
        assertEquals("hours in 24hr clock", expected, TimePoint.atGMT(2004, 1,
                1, 0, 0));
        assertEquals("hours in 12hr clock", expected, TimePoint.at12hr(2004, 1,
                1, 12, AM, 0, 0, 0, gmt));
        assertEquals("date from formatted String", expected, TimePoint
                .parseGMTFrom("2004/1/1", "yyyy/MM/dd"));
        assertEquals("pm hours in 12hr clock", TimePoint.atGMT(2004, 1, 1, 12,
                0), TimePoint.at12hr(2004, 1, 1, 12, PM, 0, 0, 0, gmt));
    }

    public void testCreationWithTimeZone() {
        /*
         * TimePoints are based on miliseconds from the Epoc. They do not have a
         * "timezone". When that basic value needs to be converted to or from a
         * date or hours and minutes, then a Timezone must be specified or
         * assumed. The default is always GMT. So creation operations which
         * don't pass any Timezone assume the date, hours and minutes are GMT.
         * The TimeLibrary does not use the default TimeZone operation in Java,
         * the selection of the appropriate Timezone is left to the application.
         */
        TimePoint gmt10Hour = TimePoint.at(2004, 3, 5, 10, 10, 0, 0, gmt);
        TimePoint default10Hour = TimePoint.atGMT(2004, 3, 5, 10, 10, 0, 0);
        TimePoint pt2Hour = TimePoint.at(2004, 3, 5, 2, 10, 0, 0, pt);
        assertEquals(gmt10Hour, default10Hour);
        assertEquals(gmt10Hour, pt2Hour);

        TimePoint gmt6Hour = TimePoint.at(2004, 3, 5, 6, 0, 0, 0, gmt);
        TimePoint ct0Hour = TimePoint.at(2004, 3, 5, 0, 0, 0, 0, ct);
        TimePoint ctMidnight = TimePoint.atMidnight(2004, 3, 5, ct);
        assertEquals(gmt6Hour, ct0Hour);
        assertEquals(gmt6Hour, ctMidnight);
    }

    public void testStringFormat() {
        TimePoint point = TimePoint.at(2004, 3, 12, 5, 3, 14, 0, pt);
        // Try stupid date/time format, so that it couldn't work by accident.
        assertEquals("3-04-12 3:5:14", point.toString("M-yy-d m:h:s", pt));
        assertEquals("3-04-12", point.toString("M-yy-d", pt));
    }

    private Date javaUtilDateDec20_2003() {
        Calendar calendar = Calendar.getInstance(gmt);
        calendar.clear(); // non-deterministic without this!!!
        calendar.set(Calendar.YEAR, 2003);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DATE, 20);
        return calendar.getTime();
    }

    public void testAsJavaUtilDate() {
        TimePoint dec20_2003 = TimePoint.atMidnightGMT(2003, 12, 20);
        assertEquals(javaUtilDateDec20_2003(), dec20_2003.asJavaUtilDate());
    }

    public void testBackToMidnight() {
        TimePoint threeOClock = TimePoint.atGMT(2004, 11, 22, 3, 0);
        assertEquals(TimePoint.atMidnightGMT(2004, 11, 22), threeOClock
                .backToMidnight(gmt));
        TimePoint thirteenOClock = TimePoint.atGMT(2004, 11, 22, 13, 0);
        assertEquals(TimePoint.atMidnightGMT(2004, 11, 22), thirteenOClock
                .backToMidnight(gmt));
    }

    public void testFromString() {
        TimePoint expected = TimePoint.atGMT(2004, 3, 29, 22, 44, 58, 0);
        String source = "2004-Mar-29 10:44:58 PM";
        String pattern = "yyyy-MMM-dd hh:mm:ss a";
        assertEquals(expected, TimePoint.parseGMTFrom(source, pattern));
    }

    public void testEquals() {
        TimePoint createdFromJavaDate = TimePoint
                .from(javaUtilDateDec20_2003());
        TimePoint dec5_2003 = TimePoint.atMidnightGMT(2003, 12, 5);
        TimePoint dec20_2003 = TimePoint.atMidnightGMT(2003, 12, 20);
        assertEquals(createdFromJavaDate, dec20_2003);
        assertTrue(createdFromJavaDate.equals(dec20_2003));
        assertFalse(createdFromJavaDate.equals(dec5_2003));
    }

    public void testEqualsOverYearMonthDay() {
        TimePoint thePoint = TimePoint.atGMT(2000, 1, 1, 8, 0);
        TimeZone gmt = TimeZone.getTimeZone("Universal");

        assertTrue("exactly the same", TimePoint.atGMT(2000, 1, 1, 8, 0)
                .isSameDayAs(thePoint, gmt));
        assertTrue("same second", TimePoint.atGMT(2000, 1, 1, 8, 0, 0, 500)
                .isSameDayAs(thePoint, gmt));
        assertTrue("same minute", TimePoint.atGMT(2000, 1, 1, 8, 0, 30, 0)
                .isSameDayAs(thePoint, gmt));
        assertTrue("same hour", TimePoint.atGMT(2000, 1, 1, 8, 30, 0, 0)
                .isSameDayAs(thePoint, gmt));
        assertTrue("same day", TimePoint.atGMT(2000, 1, 1, 20, 0).isSameDayAs(
                thePoint, gmt));
        assertTrue("midnight (in the moring), start of same day", TimePoint
                .atMidnightGMT(2000, 1, 1).isSameDayAs(thePoint, gmt));

        assertFalse("midnight (night), start of next day", TimePoint
                .atMidnightGMT(2000, 1, 2).isSameDayAs(thePoint, gmt));
        assertFalse("next day", TimePoint.atGMT(2000, 1, 2, 8, 0).isSameDayAs(
                thePoint, gmt));
        assertFalse("next month", TimePoint.atGMT(2000, 2, 1, 8, 0)
                .isSameDayAs(thePoint, gmt));
        assertFalse("next year", TimePoint.atGMT(2001, 1, 1, 8, 0).isSameDayAs(
                thePoint, gmt));
    }

    public void testBeforeAfter() {
        TimePoint dec5_2003 = TimePoint.atMidnightGMT(2003, 12, 5);
        TimePoint dec20_2003 = TimePoint.atMidnightGMT(2003, 12, 20);
        assertTrue(dec5_2003.isBefore(dec20_2003));
        assertFalse(dec20_2003.isBefore(dec20_2003));
        assertFalse(dec20_2003.isBefore(dec5_2003));
        assertFalse(dec5_2003.isAfter(dec20_2003));
        assertFalse(dec20_2003.isAfter(dec20_2003));
        assertTrue(dec20_2003.isAfter(dec5_2003));

        TimePoint oneSecondLater = TimePoint.atGMT(2003, 12, 20, 0, 0, 1, 0);
        assertTrue(dec20_2003.isBefore(oneSecondLater));
        assertFalse(dec20_2003.isAfter(oneSecondLater));
    }

    public void testIncrementDuration() {
        Duration twoDays = Duration.days(2);
        assertEquals(dec22_2003, dec20_2003.plus(twoDays));
    }

    public void testDecrementDuration() {
        Duration twoDays = Duration.days(2);
        assertEquals(dec19_2003, dec21_2003.minus(twoDays));
    }

    // This is only an integration test. The primary responsibility is in
    // TimePeriod
    public void testBeforeAfterPeriod() {
        TimeInterval period = TimeInterval.closed(dec20_2003, dec22_2003);
        assertTrue(dec19_2003.isBefore(period));
        assertFalse(dec19_2003.isAfter(period));
        assertFalse(dec20_2003.isBefore(period));
        assertFalse(dec20_2003.isAfter(period));
        assertFalse(dec21_2003.isBefore(period));
        assertFalse(dec21_2003.isAfter(period));
    }

    public void testNextDay() {
        assertEquals(dec20_2003, dec19_2003.nextDay());
    }

    public void testCompare() {
        assertTrue(dec19_2003.compareTo(dec20_2003) < 0);
        assertTrue(dec20_2003.compareTo(dec19_2003) > 0);
        assertTrue(dec20_2003.compareTo(dec20_2003) == 0);
    }

    // This test verifies bug #1336072 fix
    // The problem is Duration.days(25) overflowed and became negative
    // on a conversion from a long to int in the bowels of the model.
    // We made the conversion unnecessary
    public void testPotentialProblemDueToOldUsageOf_Duration_toBaseUnitsUsage() {
        TimePoint start = TimePoint.atGMT(2005, 10, 1, 0, 0);
        TimePoint end1 = start.plus(Duration.days(24));
        TimePoint end2 = start.plus(Duration.days(25));
        assertTrue("Start timepoint is before end1", start.isBefore(end1));
        assertTrue("and should of course be before end2", start.isBefore(end2));
    }

    // TimePoint.at() ignores the minute parameter.
    public void testNotIgnoringMinuteParameter() {
        TimePoint point = TimePoint.at(2006, 03, 22, 13, 45, 59, 499, gmt);
        assertEquals("2006-03-22 13:45:59:499", point.toString(
                "yyyy-MM-dd HH:mm:ss:SSS", gmt));
        TimePoint pointNoMilli = TimePoint.at(2006, 03, 22, 13, 45, 59, gmt);
        assertEquals("2006-03-22 13:45:59:000", pointNoMilli.toString(
                "yyyy-MM-dd HH:mm:ss:SSS", gmt));
    }

    public void testAtWithTimeZone() {
        TimePoint someTime = TimePoint.at(2006, 6, 8, 16, 45, 33, TimeZone
                .getDefault());
        Calendar someTimeAsJavaCalendar = someTime.asJavaCalendar(TimeZone
                .getDefault());

        assertEquals(2006, someTimeAsJavaCalendar.get(Calendar.YEAR));
        assertEquals(Calendar.JUNE, someTimeAsJavaCalendar.get(Calendar.MONTH));
        assertEquals(8, someTimeAsJavaCalendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(16, someTimeAsJavaCalendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(45, someTimeAsJavaCalendar.get(Calendar.MINUTE));
        assertEquals(33, someTimeAsJavaCalendar.get(Calendar.SECOND));
    }

}