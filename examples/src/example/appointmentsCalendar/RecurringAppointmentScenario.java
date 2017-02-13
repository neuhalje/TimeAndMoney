/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package example.appointmentsCalendar;

import java.util.*;

import junit.framework.*;
import com.domainlanguage.time.*;

public class RecurringAppointmentScenario extends TestCase {
    private static final TimeZone HONOLULU_TIME = TimeZone.getTimeZone("Pacific/Honolulu");

    public void testDailyMeetingAlert() {
/**
 * Daily stand-up meeting at 10:00am each work day. (We work in Honolulu, of course.)
 * Notify 5 minutes before meeting starts.
 * Derive the TimePoint at which I should notify on April 19 2006.
 */
    TimeOfDay scheduledMeetingTime = TimeOfDay.hourAndMinute(10, 0);
    CalendarDate dayOfMeeting = CalendarDate.from(2006, 4, 19);
    CalendarMinute meetingTimeThisDay = scheduledMeetingTime.on(dayOfMeeting);
    CalendarMinute sameMeetingTimeThisDay = dayOfMeeting.at(scheduledMeetingTime);
    assertEquals(meetingTimeThisDay, sameMeetingTimeThisDay);
    TimePoint meetingTimePoint = meetingTimeThisDay.asTimePoint(HONOLULU_TIME);
    assertEquals(TimePoint.at(2006, 4, 19, 10, 0, 0, 0, HONOLULU_TIME), meetingTimePoint);
        
    //The expressions can be strung together.
    assertEquals(
       TimePoint.at(2006, 4, 19, 9, 55, 0, 0, HONOLULU_TIME),
       meetingTimeThisDay.asTimePoint(HONOLULU_TIME).minus(Duration.minutes(5))
    );
    }
}
