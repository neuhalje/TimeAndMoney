/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.external;

import java.util.Calendar;
import java.util.TimeZone;

import junit.framework.TestCase;

/**
 * The purpose of these tests is to verify that java.util.Calendar is working like we
 * expect it to. We ran into a small problem with HOUR and HOUR_OF_DAY in
 * CalendarDateTest.testConversionToJavaUtil(). These tests are mainly for peace of mind.
 */
public class JavaUtilCalendarQuirksTest extends TestCase {
    
    public void testHour() {
        TimeZone gmt = TimeZone.getTimeZone("Universal");
        Calendar test = Calendar.getInstance(gmt);
        test.set(Calendar.YEAR, 1969);
        test.set(Calendar.MONTH, Calendar.JULY);
        test.set(Calendar.DATE, 20);
        test.set(Calendar.HOUR, 5);
        test.set(Calendar.AM_PM, Calendar.PM);
        
        assertEquals(1969, test.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, test.get(Calendar.MONTH));
        assertEquals(20, test.get(Calendar.DATE));
        assertEquals(5, test.get(Calendar.HOUR));
        assertEquals(Calendar.PM, test.get(Calendar.AM_PM));
        assertEquals(17, test.get(Calendar.HOUR_OF_DAY));
    }
    public void testHourOfDay() {
        TimeZone gmt = TimeZone.getTimeZone("Universal");
        Calendar test = Calendar.getInstance(gmt);
        test.set(Calendar.YEAR, 1969);
        test.set(Calendar.MONTH, Calendar.JULY);
        test.set(Calendar.DATE, 20);
        test.set(Calendar.HOUR_OF_DAY, 3);
        
        assertEquals(1969, test.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, test.get(Calendar.MONTH));
        assertEquals(20, test.get(Calendar.DATE));
        assertEquals(3, test.get(Calendar.HOUR));
        assertEquals(Calendar.AM, test.get(Calendar.AM_PM));
        assertEquals(3, test.get(Calendar.HOUR_OF_DAY));
    }
}
