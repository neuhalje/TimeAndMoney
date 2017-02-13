/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.TimePoint;
import com.domainlanguage.time.TimeSource;
import org.junit.After;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.*;

public class ClockTest {

    TimePoint dec1_5am_gmt = TimePoint.atGMT(2004, 12, 1, 5, 0);
    TimeZone gmt = TimeZone.getTimeZone("Universal");
    TimeZone pt = TimeZone.getTimeZone("America/Los_Angeles");
    TimeZone ct = TimeZone.getTimeZone("America/Chicago");
    TimeSource dummySourceDec1_5h = dummyTimeSource(dec1_5am_gmt);

    @After
    public void tearDown() {
        Clock.reset();
    }

    @Test
    public void testNow() {
        Clock.setTimeSource(dummySourceDec1_5h);
        assertEquals(dec1_5am_gmt, Clock.now());
    }

    //[ 1466694 ] Clock.now() should use default TimeSource
    @Test
    public void testNowDoesntBreak() {
        Exception possibleNullPointerException = null;
        try {
            Clock.now();
        } catch (Exception exceptionalEvent) {
            possibleNullPointerException = exceptionalEvent;
        }
        assertNull(possibleNullPointerException);
    }

    @Test
    public void testToday() {
        Clock.setTimeSource(dummySourceDec1_5h);

        Clock.setDefaultTimeZone(gmt);
        assertEquals(CalendarDate.date(2004, 12, 1), Clock.today());
        assertEquals(dec1_5am_gmt, Clock.now());

        Clock.setDefaultTimeZone(pt);
        assertEquals(CalendarDate.date(2004, 11, 30), Clock.today());
        assertEquals(dec1_5am_gmt, Clock.now());

    }

    @Test
    public void testTodayWithoutTimeZone() {
        Clock.setTimeSource(dummySourceDec1_5h);

        try {
            Clock.today();
            fail("Clock cannot answer today() without a timezone.");
        } catch (RuntimeException e) {
            assertTrue("Correctly threw exception", true);
        }

    }

    private TimeSource dummyTimeSource(final TimePoint returnValueForNow) {
        return new TimeSource() {
            public TimePoint now() {
                return returnValueForNow;
            }
        };
    }
}
