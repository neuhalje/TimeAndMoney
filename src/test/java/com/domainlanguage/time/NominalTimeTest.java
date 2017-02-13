/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class NominalTimeTest {

    private static final TimeZone HONOLULU_TIME = TimeZone.getTimeZone("Pacific/Honolulu");

    @Test
    public void testCombineNominalTimes() {

        TimeOfDay fiveFifteenPM = TimeOfDay.hourAndMinute(17, 15);
        CalendarDate april19_2006 = CalendarDate.from(2006, 4, 19);
        CalendarMinute expectedCombination = CalendarMinute.dateHourAndMinute(
                2006, 4, 19, 17, 15);
        assertEquals(expectedCombination, fiveFifteenPM.on(april19_2006));
        assertEquals(expectedCombination, april19_2006.at(fiveFifteenPM));
    }

    @Test
    public void testConvertNominalTimeToTimePoint() {
        CalendarMinute calendarMinute = CalendarMinute.dateHourAndMinute(2006,
                4, 19, 17, 15);
        TimePoint expectedTimePoint = TimePoint.at(2006, 4, 19, 17, 15, 0, 0,
                HONOLULU_TIME);
        assertEquals(expectedTimePoint, calendarMinute.asTimePoint(HONOLULU_TIME));
    }
}
