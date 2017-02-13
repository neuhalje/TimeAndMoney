/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.money;

import junit.framework.*;

public class ProrationTest extends TestCase {
    private Proration proration = new Proration();

    public void testAllocate1() {
        long[] proportions = { 1, 1 };
        Money[] result = proration.proratedOver(Money.dollars(0.01), proportions);
        assertEquals(Money.dollars(0.01), result[0]);
        assertEquals(Money.dollars(0), result[1]);
    }

    public void testProrateOver2() {
        long[] proportions = { 3, 7 };
        Money[] result = proration.proratedOver(Money.dollars(0.05), proportions);
        assertEquals(Money.dollars(0.02), result[0]);
        assertEquals(Money.dollars(0.03), result[1]);
    }

    public void testProrateOver10() throws Exception {
        long[] proportions = { 17, 2, 1, 35, 35, 10 };
        Money[] result = proration.proratedOver(Money.dollars(0.10), proportions);
        assertEquals(Money.dollars(0.02), result[0]);
        assertEquals(Money.dollars(0.01), result[1]);
        assertEquals(Money.dollars(0.00), result[2]);
        assertEquals(Money.dollars(0.03), result[3]);
        assertEquals(Money.dollars(0.03), result[4]);
        assertEquals(Money.dollars(0.01), result[5]);
        Money sum = Money.dollars(0.0);
        for (int i = 0; i < result.length; i++)
            sum = sum.plus(result[i]);
        assertEquals(Money.dollars(0.10), sum);
    }

    public void testProrateZeroTotal() {
        long[] proportions = { 3, 7 };
        Money[] result = proration.proratedOver(Money.dollars(0), proportions);
        assertEquals(Money.dollars(0), result[0]);
        assertEquals(Money.dollars(0), result[1]);
    }

    public void testProrateTotalIndivisibleBy3() {
        Money[] actual = proration.dividedEvenlyIntoParts(Money.dollars(100), 3);
        Money[] expected = { Money.dollars(33.34), Money.dollars(33.33), Money.dollars(33.33) };
        for (int i = 0; i < expected.length; i++)
            assertEquals(expected[i], actual[i]);
    }

    public void testProrateOnlyOneShortOfEven() {
        Money[] prorated = proration.dividedEvenlyIntoParts(Money.dollars(1.09), 10);
        for (int i = 0; i < 9; i++)
            assertEquals(Money.dollars(0.11), prorated[i]);
        assertEquals(Money.dollars(0.10), prorated[9]);
    }

    public void testDistributeRemainder() {
        Money[] startingValues = new Money[4];
        startingValues[0] = Money.dollars(1.00);
        startingValues[1] = Money.dollars(2.00);
        startingValues[2] = Money.dollars(3.00);
        startingValues[3] = Money.dollars(4.00);
        Money[] result = proration.distributeRemainderOver(startingValues, Money.dollars(0.02));
        assertEquals(Money.dollars(1.01), result[0]);
        assertEquals(Money.dollars(2.01), result[1]);
        assertEquals(Money.dollars(3.00), result[2]);
        assertEquals(Money.dollars(4.00), result[3]);
    }

    public void testSumMoney() {
        Money[] startingValues = new Money[4];
        startingValues[0] = Money.dollars(1.00);
        startingValues[1] = Money.dollars(2.00);
        startingValues[2] = Money.dollars(3.00);
        startingValues[3] = Money.dollars(4.00);
        assertEquals(Money.dollars(10.00), Proration.sum(startingValues));
    }

    public void testPartOfWhole() {
        Money total = Money.dollars(10.00);
        long portion = 3l;
        long whole = 9l;
        assertEquals(Money.dollars(3.33), proration.partOfWhole(total, portion, whole));
    }

}