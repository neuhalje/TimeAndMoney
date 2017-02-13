/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.insuranceRates;

import junit.framework.*;

import com.domainlanguage.base.*;
import com.domainlanguage.intervals.*;
import com.domainlanguage.money.*;
import com.domainlanguage.time.*;

public class CalculateRate extends TestCase {
    private static final CalendarDate policyEffectiveDate = CalendarDate.date(2004, 11, 7);

    public void testLookUpRate() {
        CalendarDate birthdate = CalendarDate.date(1963, 4, 6);
        Duration ageOnEffectiveDate = birthdate.through(policyEffectiveDate).lengthInMonths();
        Money monthlyPremium = (Money) insuranceSchedule().get(ageOnEffectiveDate);
        assertEquals(Money.dollars(150.00), monthlyPremium);
    }
    public void testProrateFirstMonth() {
        Money monthlyPremium = Money.dollars(150.00);
        CalendarInterval entireMonth = policyEffectiveDate.month();
        CalendarInterval remainderOfMonth = policyEffectiveDate.through(entireMonth.end());
        Ratio partOfPayment = Ratio.of(remainderOfMonth.lengthInDaysInt(), entireMonth.lengthInDaysInt());
        Money firstPayment = monthlyPremium.applying(partOfPayment, Rounding.DOWN);
        assertEquals(Money.dollars(120.00), firstPayment);

        //Alternative, equivalent calculation
        partOfPayment = remainderOfMonth.length().dividedBy(entireMonth.length());
        firstPayment = new Proration().partOfWhole(monthlyPremium, partOfPayment);
        assertEquals(Money.dollars(120.00), firstPayment);
    }
    public void testQuarterlyPremiumPayment() {
        MoneyTimeRate premium = Money.dollars(150.00).per(Duration.months(1));
        Money quarterlyPayment = premium.over(Duration.months(3));
        assertEquals(Money.dollars(450.00), quarterlyPayment);
    }

    //revisit:
    public void xtestLookUpMoreComplicated() {
        //		BusinessCalendar paymentCalendar = null;
        //		CalendarInterval paymentQuarter = paymentCalendar.currentQuarter();
        //
        //				CalendarDate birthdate = null;
        //				Duration age = birthdate.until(paymentQuarter.start()).duration();
        //				Rate rate = insuranceSchedule.get(age);
        //		Money quarterlyPayment = rate.times(Duration.quarters(1));
        //		CalendarDate effectiveDate = null;
        //		CalendarInterval remainingQuarter =
        // paymentQuarter.cropForwardFrom(effectiveDate);
        //		BigDecimal ratio =
        // remainingQuarter.duration().dividedBy(paymentQuarter);
        //		Money firstPayment = quarterlyPayment.prorate(ratio);
    }

    private IntervalMap insuranceSchedule() {
        Interval age25_35 = Interval.over(Duration.years(25), true, Duration.years(35), false);
        Interval age35_45 = Interval.over(Duration.years(35), true, Duration.years(45), false);
        Interval age45_55 = Interval.over(Duration.years(45), true, Duration.years(55), false);
        Interval age55_65 = Interval.over(Duration.years(55), true, Duration.years(65), false);

        IntervalMap schedule = new LinearIntervalMap();
        schedule.put(age25_35, Money.dollars(100.00));
        schedule.put(age35_45, Money.dollars(150.00));
        schedule.put(age45_55, Money.dollars(200.00));
        schedule.put(age55_65, Money.dollars(250.00));
        return schedule;
    }

}