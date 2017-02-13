/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

class ConcreteCalendarInterval extends CalendarInterval {
    private CalendarDate start;
    private CalendarDate end;

    static ConcreteCalendarInterval from(CalendarDate start, CalendarDate end) {
        return new ConcreteCalendarInterval(start, end);
    }

    ConcreteCalendarInterval(CalendarDate start, CalendarDate end) {
        this.start = start;
        this.end = end;
    }

    public TimeInterval asTimeInterval(TimeZone zone) {
        TimePoint startPoint = start.asTimeInterval(zone).start();
        TimePoint endPoint = end.asTimeInterval(zone).end();
        return TimeInterval.over(startPoint, endPoint);
    }

    public Comparable upperLimit() {
        return end;
    }

    public Comparable lowerLimit() {
        return start;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    ConcreteCalendarInterval() {
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private CalendarDate getForPersistentMapping_End() {
        return end;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_End(CalendarDate end) {
        this.end = end;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private CalendarDate getForPersistentMapping_Start() {
        return start;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Start(CalendarDate start) {
        this.start = start;
    }

}