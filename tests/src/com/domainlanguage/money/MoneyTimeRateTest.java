/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.money;

import java.math.*;

import junit.framework.*;

import com.domainlanguage.time.*;

public class MoneyTimeRateTest extends TestCase {
    public void testSimpleRate() {
        MoneyTimeRate rate = new MoneyTimeRate(Money.dollars(20.00), Duration.hours(1));
        assertEquals(Money.dollars(40.00), rate.over(Duration.hours(2)));
    }    
    public void testRounding(){
        MoneyTimeRate rate = new MoneyTimeRate(Money.dollars(100.00), Duration.minutes(3));
        try {
            rate.over(Duration.minutes(1));
            fail("ArtithmeticException should have been thrown. This case requires rounding.");
        } catch (ArithmeticException e) {}
    }
    public void testRoundingRate() {
        MoneyTimeRate rate = new MoneyTimeRate(Money.euros(100.00), Duration.minutes(3));
        assertEquals(Money.euros(new BigDecimal("33.33")), rate.over(Duration.minutes(1), BigDecimal.ROUND_DOWN));
    }

    public void testRoundingScalingRate() {
        MoneyTimeRate rate = new MoneyTimeRate(Money.euros(new BigDecimal("100.00")), Duration.minutes(3));
        assertEquals(Money.euros(new BigDecimal("33.33")), rate.over(Duration.minutes(1), 2, BigDecimal.ROUND_DOWN));
    }
    public void testEquals() {
        Money amount=Money.euros(11.00);
        MoneyTimeRate rate=amount.per(Duration.days(2));
        assertEquals(new MoneyTimeRate(Money.euros(11.00), Duration.days(2)), rate);
    }
}
