/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

class FloatingDateSpecification extends AnnualDateSpecification {
    private int month;
    private int dayOfWeek;
    private int occurrence;

    FloatingDateSpecification(int month, int dayOfWeek, int occurrence) {
        this.month = month;
        this.dayOfWeek = dayOfWeek;
        this.occurrence = occurrence;
    }

    public CalendarDate ofYear(int year) {
        CalendarDate firstOfMonth = CalendarDate.date(year, month, 1);
        int dayOfWeekOffset = dayOfWeek - firstOfMonth.dayOfWeek();
        int dateOfFirstOccurrenceOfDayOfWeek = dayOfWeekOffset + (dayOfWeekOffset < 0 ? 8 : 1);
        int date = ((occurrence - 1) * 7) + dateOfFirstOccurrenceOfDayOfWeek;
        return CalendarDate.date(year, month, date);
    }

    public boolean isSatisfiedBy(CalendarDate date) {
        return ofYear(date.breachEncapsulationOf_year()).equals(date);
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    FloatingDateSpecification() {
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private int getForPersistentMapping_DayOfWeek() {
        return dayOfWeek;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_DayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private int getForPersistentMapping_Month() {
        return month;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Month(int month) {
        this.month = month;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private int getForPersistentMapping_Occurrence() {
        return occurrence;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Occurrence(int occurrence) {
        this.occurrence = occurrence;
    }
}