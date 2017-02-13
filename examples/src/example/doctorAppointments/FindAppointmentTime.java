/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.doctorAppointments;

//import java.util.*;

import junit.framework.*;

public class FindAppointmentTime extends TestCase {

    /**
     * AppointmentCalendar can have IntervalKeyedMap of Appointments. Adding an
     * appointment calls for checking if the Map has an intersecting key.
     * IntervalSequence could intersect with an interval returning another
     * IntervalSequence. The IntervalSequence could also iterate through its
     * gaps (which are intervals). This would be the complement of the
     * IntervalSequence. Then finding an appointment time is taking the
     * intersection of the desired interval with the IntervalSequence of the
     * keys, then taking the complement of that, then asking for the iterator of
     * Duration matching desired appointment duration.
     * 
     * IntervalSequence keys = map.getKeys(); IntervalSequence available =
     * keys.intersect(desiredInterval).gaps(); Iterator it =
     * available.iterate(desiredDuration);
     *  
     */

    public void testFindAvailableTime() {
//        TimeZone pt = TimeZone.getTimeZone("America/Los_Angeles");
//        AppointmentCalendar calendar = new AppointmentCalendar(pt);

        /**
         * calendar.addHolidays(aCompositeSpec);
         * calendar.addHoliday(thanksgiving); calendar.addHoliday(christmas);
         * 
         * calendar.addOfficeHours(intervalspecification? Monday 9am-12noon)
         * 
         * calendar.addVacation(a two week CalendarInterval, 'Maui!')
         * 
         * 
         * calendar.addAppointment(a twenty Minute TimeInterval, 'Mr. Jones
         * hernia') calendar.addAppointment(a twenty minute TimeInterval, 'Mrs.
         * Smith's goiter');
         * 
         * Iterator available = calendar.availableAppointmentIterator(Duration,
         * DateInterval or TimeInterval);
         * 
         * assertEquals(timeInterval, available.next());
         * assertEquals(timeInterval, available.next());
         * assertEquals(timeInterval, available.next());
         * assertEquals(timeInterval, available.next());
         * addAppointment(chosenInterval, "Mr. Johnson's obsessive, compulsive
         * disorder");
         *  
         */
    }

    public void testReportSchedule() {
//        TimeZone pt = TimeZone.getTimeZone("America/Los_Angeles");
//        AppointmentCalendar calendar = new AppointmentCalendar(pt);
        //List appointments = calendar.dailySchedule(aCalendarDate);
        //assertEquals(#, appointments.size());
        //assertEquals(aTimePoint,
        // ((Appointment)appointments.get(0)).startTime());
    }
}