package com.domainlanguage.time;

import java.util.*;

/**
 * dates are taken from: http://www.opm.gov/fedhol/index.htm note: when a
 * holiday falls on a non-workday -- Saturday or Sunday -- the holiday usually
 * is observed on Monday (if the holiday falls on Sunday) or Friday (if the
 * holiday falls on Saturday). a holiday falls on a nonworkday will be referred
 * to as a "deferred" holiday.
 */
class _HolidayDates {

    static String[] COMMON_US_HOLIDAYS = new String[] {

            // 2004
            "2004/01/01", /* New Year's Day */
            "2004/01/19", /* Birthday of Martin Luther King */
            "2004/02/16", /* Washington's Birthday */
            "2004/05/31", /* Memorial Day */
            "2004/07/05", /* United States of America's Independence Day, July 4 *///revisit:defered
            "2004/09/06", /* Labor Day */
            "2004/11/25", /* Thanksgiving Day */
            "2004/12/24", /*
                           * Christmas Day, December 25 - Friday - deferred from
                           * Saturday
                           */
            "2004/12/31", /*
                           * New Year's Day for January 1, 2005 - Friday -
                           * deferred from Saturday
                           */

            // 2005
            "2005/01/17", /* Birthday of Martin Luther King */
            "2005/02/21", /* Washington's Birthday */
            "2005/05/30", /* Memorial Day */
            "2005/07/04", /* United States of America's Independence Day, July 4 */
            "2005/09/05", /* Labor Day */
            "2005/11/24", /* Thanksgiving Day */
            "2005/12/26", /*
                           * Christmas Day, December 25 - Monday - deferred from
                           * Sunday
                           */

            // 2006
            "2006/01/02", /* New Year's Day, January 1 */
            "2006/01/16", /* Birthday of Martin Luther King */
            "2006/02/20", /* Washington's Birthday */
            "2006/05/29", /* Memorial Day */
            "2006/07/04", /* United States of America's Independence Day, July 4 */
            "2006/09/04", /* Labor Day */
            "2006/11/23", /* Thanksgiving Day */
            "2006/12/25", /* Christmas Day, December 25 */
    };

    static Set defaultHolidays() {
        Set dates = new HashSet();
        String[] strings = COMMON_US_HOLIDAYS;
        for (int i = 0; i < strings.length; i++)
            dates.add(CalendarDate.from(strings[i], "yyyy/MM/dd"));
        return dates;
    }

}