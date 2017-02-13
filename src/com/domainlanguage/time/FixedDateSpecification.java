/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

class FixedDateSpecification extends AnnualDateSpecification {
    private int month;
    private int day;

    FixedDateSpecification(int month, int day) {
        this.month = month;
        this.day = day;
    }

    public CalendarDate ofYear(int year) {
        return CalendarDate.date(year, month, day);
    }

    public boolean isSatisfiedBy(CalendarDate date) {
        return day == date.breachEncapsulationOf_day() && month == date.breachEncapsulationOf_month();
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    FixedDateSpecification() {
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private int getForPersistentMapping_Day() {
        return day;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Day(int day) {
        this.day = day;
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

}