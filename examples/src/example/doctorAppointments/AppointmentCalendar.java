/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.doctorAppointments;

import java.util.*;

import com.domainlanguage.time.*;

class AppointmentCalendar {
    TimeZone defaultZone;
    Set events = new HashSet();
    
    AppointmentCalendar(TimeZone zone) {
        defaultZone = zone;
    }

    void add(Appointment anEvent) {
        events.add(anEvent);
    }

    List dailyScheduleFor(CalendarDate calDate) {
        List daysAppointments = new ArrayList();
        TimeInterval day = calDate.asTimeInterval(defaultZone);
        Iterator it = events.iterator();
        while (it.hasNext()) {
            Appointment event = (Appointment) it.next();
            if (event.getTimeInterval().intersects(day)) {
                daysAppointments.add(event);
            }
        }
        return daysAppointments;
    }
    
}