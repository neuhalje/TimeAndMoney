/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time;

import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TimeRateTest {

    @Test
    public void testSimpleRate() {
        TimeRate rate = new TimeRate(100.00, Duration.minutes(1));
        assertEquals(new BigDecimal(6000.00), rate.over(Duration.hours(1)));
    }

    @Test
    public void testRounding() {
        TimeRate rate = new TimeRate(100.00, Duration.minutes(3));
        try {
            rate.over(Duration.minutes(1));
            fail("ArtithmeticException should have been thrown. This case requires rounding.");
        } catch (ArithmeticException expected) {
        }
    }

    @Test
    public void testRoundingRate() {
        TimeRate rate = new TimeRate("100.00", Duration.minutes(3));
        assertEquals(new BigDecimal("33.33"), rate.over(Duration.minutes(1), BigDecimal.ROUND_DOWN));
    }

    //	TODO: failing test
    @Test
    @Ignore("PLEASE FIX ME")
    public void testRoundingScalingRate() {
        TimeRate rate = new TimeRate("100.00", Duration.minutes(3));
        assertEquals(new BigDecimal("33.33"), rate.over(Duration.minutes(1), 3, BigDecimal.ROUND_DOWN));
    }

    @Test
    public void testEquals() {
        TimeRate rate = new TimeRate(11, Duration.days(2));
        assertEquals(new TimeRate(11.00, Duration.days(2)), rate);
    }
}
