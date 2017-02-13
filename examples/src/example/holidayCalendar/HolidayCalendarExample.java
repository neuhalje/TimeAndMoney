/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.holidayCalendar;

import java.util.*;

import junit.framework.*;

import com.domainlanguage.time.*;

public class HolidayCalendarExample extends TestCase {

    public void testDeriveBirthday() {
        //Calculate Martin Luther King, Jr.'s birthday, January 15, for the
        // year 2005:
        DateSpecification mlkBirthday = DateSpecification.fixed(1, 15);
        // Then you can do checks like
        CalendarDate jan15_2005 = CalendarDate.from(2005, 1, 15);
        assertTrue(mlkBirthday.isSatisfiedBy(jan15_2005));
        //Derive the date(s) for an interval
        CalendarDate mlk2005 = mlkBirthday.firstOccurrenceIn(CalendarInterval.year(2005));
        assertEquals(jan15_2005, mlk2005);
        // Calculate all the birthdays in his lifetime
        CalendarInterval mlkLifetime = CalendarInterval.inclusive(1929, 1, 15, 1968, 4, 4);
        Iterator mlkBirthdays = mlkBirthday.iterateOver(mlkLifetime);
        assertEquals(CalendarDate.from(1929, 1, 15), mlkBirthdays.next());
        assertEquals(CalendarDate.from(1930, 1, 15), mlkBirthdays.next());
        // etc.
        // By the way, to calculate how long MLK lived,
        assertEquals(Duration.days(14325), mlkLifetime.length());
    }

    public void testDeriveThanksgiving() {
        //Calculate Thanksgiving, the 4th Thursday in November, for the
        // year 2005
        DateSpecification thanksgiving = DateSpecification.nthOccuranceOfWeekdayInMonth(11, Calendar.THURSDAY, 4);
        // With the specification, you can do checks like
        assertTrue(thanksgiving.isSatisfiedBy(CalendarDate.date(2005, 11, 24)));
        assertFalse(thanksgiving.isSatisfiedBy(CalendarDate.date(2005, 11, 25)));
        // Derive the date(s) for an interval
        assertEquals(CalendarDate.date(2005, 11, 24), thanksgiving.firstOccurrenceIn(CalendarInterval.year(2005)));

        // Calculate all the Thanksgivings over a three year interval.
        CalendarInterval y2002_2004 = CalendarInterval.inclusive(2002, 1, 1, 2004, 12, 31);
        assertEquals(CalendarDate.date(2002, 11, 28), thanksgiving.firstOccurrenceIn(y2002_2004));
        Iterator iterator = thanksgiving.iterateOver(y2002_2004);
        assertEquals(CalendarDate.date(2002, 11, 28), iterator.next());
        assertEquals(CalendarDate.date(2003, 11, 27), iterator.next());
        assertEquals(CalendarDate.date(2004, 11, 25), iterator.next());
        assertFalse(iterator.hasNext());
    }

}