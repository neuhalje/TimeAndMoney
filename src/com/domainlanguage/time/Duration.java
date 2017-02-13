/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.io.*;
import java.util.*;

import com.domainlanguage.base.*;

public class Duration implements Comparable, Serializable {
	public static final Duration NONE = milliseconds(0);

	private long quantity;
	private TimeUnit unit;
	
	public static Duration milliseconds(long howMany) {
		return Duration.of(howMany, TimeUnit.millisecond);
	}

	public static Duration seconds(int howMany) {
		return Duration.of(howMany, TimeUnit.second);
	}
	
	public static Duration minutes(int howMany) {
		return Duration.of(howMany, TimeUnit.minute);
	}
	
	public static Duration hours(int howMany) {
		return Duration.of(howMany, TimeUnit.hour);
	}
	
	public static Duration days(int howMany) {
		return Duration.of(howMany, TimeUnit.day);
	}
	
	public static Duration daysHoursMinutesSecondsMilliseconds(int days, int hours, int minutes, int seconds, long milliseconds) {
		Duration result = Duration.days(days);
		if (hours != 0) result = result.plus(Duration.hours(hours));
		if (minutes != 0) result = result.plus(Duration.minutes(minutes));
		if (seconds != 0) result = result.plus(Duration.seconds(seconds));
		if (milliseconds != 0) result = result.plus(Duration.milliseconds(milliseconds));
		return result;
	}
	
	public static Duration weeks(int howMany) {
		return Duration.of(howMany, TimeUnit.week);
	}
	
	public static Duration months(int howMany) {
		return Duration.of(howMany, TimeUnit.month);
	}
	
	public static Duration quarters(int howMany) {
		return Duration.of(howMany, TimeUnit.quarter);
	}
	
	public static Duration years(int howMany) {
		return Duration.of(howMany, TimeUnit.year);
	}
	
	private static Duration of(long howMany, TimeUnit unit) {
		return new Duration(howMany, unit);
	}

	public Duration(long quantity, TimeUnit unit) {
		assertQuantityPositiveOrZero(quantity);
		this.quantity = quantity;
		this.unit = unit;
	}
    
	long inBaseUnits() {
		return quantity * unit.getFactor();
	}

	public Duration plus(Duration other) {
		assertNotConvertible(other);
		long newQuantity = this.inBaseUnits() + other.inBaseUnits();
		return new Duration(newQuantity, unit.baseUnit());
	}

	public Duration minus(Duration other) {
        assertNotConvertible(other);
		assert this.compareTo(other) >= 0;
		long newQuantity = this.inBaseUnits() - other.inBaseUnits();
		return new Duration(newQuantity, unit.baseUnit());
	}

    
	
	public TimePoint addedTo(TimePoint point) {
        return addAmountToTimePoint(inBaseUnits(), point);
	}
	
	public TimePoint subtractedFrom(TimePoint point) {
		return addAmountToTimePoint(-1 * inBaseUnits(), point);
	}

	public CalendarDate addedTo(CalendarDate day) {
//		only valid for days and larger units
		if (unit.compareTo(TimeUnit.day) < 0) return day;
		Calendar calendar = day.asJavaCalendarUniversalZoneMidnight();
		if (unit.equals(TimeUnit.day)) 
			calendar.add(Calendar.DATE, (int) quantity);
		else 
            addAmountToCalendar(inBaseUnits(), calendar);
		return CalendarDate._from(calendar);
	}

	public CalendarDate subtractedFrom(CalendarDate day) {
//		only valid for days and larger units
		if (unit.compareTo(TimeUnit.day) < 0) return day;
		Calendar calendar = day.asJavaCalendarUniversalZoneMidnight();
		if (unit.equals(TimeUnit.day)) 
			calendar.add(Calendar.DATE, -1 * (int)quantity);
		else 
            subtractAmountFromCalendar(inBaseUnits(), calendar);
		return CalendarDate._from(calendar);
	}

	public Ratio dividedBy (Duration divisor) {
		assert unit.isConvertibleTo(divisor.unit);
		return Ratio.of(inBaseUnits(), divisor.inBaseUnits());
	}

	public boolean equals(Object object) {
		if (!(object instanceof Duration)) return false;
		Duration other = (Duration) object;
		if (!isConvertibleTo(other)) return false;
		return this.inBaseUnits() == other.inBaseUnits();
	}

	public String toString() {
		return toNormalizedString(unit.descendingUnitsForDisplay());
	}

	public String toNormalizedString() {
		return toNormalizedString(unit.descendingUnits());
	}
	
	public TimeUnit normalizedUnit() {
		TimeUnit[] units = unit.descendingUnits();
		long baseAmount = inBaseUnits();
		for (int i = 0; i < units.length; i++) {
			TimeUnit aUnit = units[i];
			long remainder = baseAmount % aUnit.getFactor();
			if (remainder == 0) return aUnit;
		}
		return null;
		
	}
	
	public int hashCode() {
		return (int) quantity;
	}

	public int compareTo(Object arg) {
		Duration other = (Duration) arg;
		assertNotConvertible(other);
		long difference = this.inBaseUnits() - other.inBaseUnits();
		if (difference > 0) return 1;
		if (difference < 0) return -1;
		return 0;
	}
	
	public TimeInterval startingFrom(TimePoint start) {
		return TimeInterval.startingFrom(start, this);
	}
	
	public CalendarInterval startingFrom(CalendarDate start) {
		return CalendarInterval.startingFrom(start, this);
	}

	public TimeInterval preceding(TimePoint end) {
		return TimeInterval.preceding(end, this);
	}
    TimePoint addAmountToTimePoint(long amount, TimePoint point) {
        if (unit.isConvertibleToMilliseconds()) {
            return TimePoint.from(amount + point.millisecondsFromEpoc);
        } else {
            Calendar calendar = point.asJavaCalendar();
            addAmountToCalendar(amount, calendar);
            return TimePoint.from(calendar);
        }
    }
    void addAmountToCalendar(long amount, Calendar calendar) {
        if (unit.isConvertibleToMilliseconds()) {
            calendar.setTimeInMillis(calendar.getTimeInMillis() + amount);
        } else {
            assert (amount >= Integer.MIN_VALUE && amount <= Integer.MAX_VALUE);
            calendar.add(unit.javaCalendarConstantForBaseType(), (int) amount);
        }
    }
    void subtractAmountFromCalendar(long amount, Calendar calendar) {
        addAmountToCalendar(-1 * amount, calendar);
    }
    
    private void assertNotConvertible(Duration other) {
        if (!other.unit.isConvertibleTo(this.unit))
            throw new IllegalArgumentException(other.toString() + " is not convertible to: " + this.toString());
    }
    private void assertQuantityPositiveOrZero(long quantity) {
        if (quantity < 0)
            throw new IllegalArgumentException("Quantity: "+quantity+" must be zero or positive");
    }
    private boolean isConvertibleTo(Duration other) {
        return this.unit.isConvertibleTo(other.unit);
    }
    private String toNormalizedString(TimeUnit[] units) {
        StringBuffer buffer = new StringBuffer();
        long remainder = inBaseUnits();
        boolean first = true;       
        for (int i = 0; i < units.length; i++) {
            TimeUnit aUnit = units[i];
            long portion = remainder / aUnit.getFactor();           
            if (portion > 0) {
                if (!first)
                    buffer.append(", ");
                else
                    first = false;
                buffer.append(aUnit.toString(portion));
            }
            remainder = remainder % aUnit.getFactor();
        }
        return buffer.toString();
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    Duration() {
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private long getForPersistentMapping_Quantity() {
        return quantity;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Quantity(long quantity) {
        this.quantity = quantity;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private TimeUnit getForPersistentMapping_Unit() {
        return unit;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Unit(TimeUnit unit) {
        this.unit = unit;
    }
}
