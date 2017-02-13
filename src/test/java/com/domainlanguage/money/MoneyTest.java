package com.domainlanguage.money;

import com.domainlanguage.base.Ratio;
import com.domainlanguage.base.Rounding;
import com.domainlanguage.tests.SerializationTester;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.*;

public class MoneyTest {
    private static Currency USD = Currency.getInstance("USD");
    private static Currency JPY = Currency.getInstance("JPY");
    private static Currency EUR = Currency.getInstance("EUR");

    private Money d15;
    private Money d2_51;
    private Money y50;
    private Money e2_51;
    private Money d100;

    @Before
    public void setUp() {
        d15 = Money.valueOf(new BigDecimal("15.0"), USD);
        d2_51 = Money.valueOf(new BigDecimal("2.51"), USD);
        e2_51 = Money.valueOf(new BigDecimal("2.51"), EUR);
        y50 = Money.valueOf(new BigDecimal("50"), JPY);
        d100 = Money.valueOf(new BigDecimal("100.0"), USD);
    }

    @Test
    public void testSerialization() {
        SerializationTester.assertCanBeSerialized(d15);
    }

    @Test
    public void testCreationFromDouble() {
        assertEquals(d15, Money.valueOf(15.0, USD));
        assertEquals(d2_51, Money.valueOf(2.51, USD));
        assertEquals(y50, Money.valueOf(50.1, JPY));
        assertEquals(d100, Money.valueOf(100, USD));
    }

    @Test
    public void testYen() {
        assertEquals("JPY 50", y50.toString());
        Money y80 = Money.valueOf(new BigDecimal("80"), JPY);
        Money y30 = Money.valueOf(30, JPY);
        assertEquals(y80, y50.plus(y30));
        assertEquals("mult", y80, y50.times(1.6));
    }

    @Test
    public void testConstructor() throws Exception {
        Money d69_99 = new Money(new BigDecimal("69.99"), USD);
        assertEquals(new BigDecimal("69.99"), d69_99.getAmount());
        assertEquals(USD, d69_99.getCurrency());
        try {
            new Money(new BigDecimal("69.999"), USD);
            fail("Money constructor shall never round, and shall not accept a value whose scale doesn't fit the Currency.");
        } catch (IllegalArgumentException correctResponse) {
        }
    }

    @Test
    public void testDivide() {
        assertEquals(Money.dollars(33.33), d100.dividedBy(3));
        assertEquals(Money.dollars(16.67), d100.dividedBy(6));
    }

    @Test
    public void testMultiply() {
        assertEquals(Money.dollars(150), d15.times(10));
        assertEquals(Money.dollars(1.5), d15.times(0.1));
        assertEquals(Money.dollars(70), d100.times(0.7));
    }

    @Test
    public void testMultiplyRounding() {
        assertEquals(Money.dollars(66.67), d100.times(0.66666667));
        assertEquals(Money.dollars(66.66), d100.times(0.66666667, Rounding.DOWN));
    }

    @Test
    public void testMultiplicationWithExplicitRounding() {
        assertEquals(Money.dollars(66.67), d100.times(new BigDecimal("0.666666"), Rounding.HALF_EVEN));
        assertEquals(Money.dollars(66.66), d100.times(new BigDecimal("0.666666"), Rounding.DOWN));
        assertEquals(Money.dollars(-66.66), d100.negated().times(new BigDecimal("0.666666"), Rounding.DOWN));
    }

    @Test
    public void testMinimumIncrement() {
        assertEquals(Money.valueOf(0.01, USD), d100.minimumIncrement());
        assertEquals(Money.valueOf(1, JPY), y50.minimumIncrement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionOfDifferentCurrencies() {
        d15.plus(e2_51);
        fail("added different currencies");
    }

    @Test
    public void testDivisionByMoney() {
        assertEquals(new BigDecimal(2.50), Money.dollars(5.00).dividedBy(Money.dollars(2.00)).decimalValue(1, Rounding.UNNECESSARY));
        assertEquals(new BigDecimal(1.25), Money.dollars(5.00).dividedBy(Money.dollars(4.00)).decimalValue(2, Rounding.UNNECESSARY));
        assertEquals(new BigDecimal(5), Money.dollars(5.00).dividedBy(Money.dollars(1.00)).decimalValue(0, Rounding.UNNECESSARY));
        try {
            Money.dollars(5.00).dividedBy(Money.dollars(2.00)).decimalValue(0, Rounding.UNNECESSARY);
            fail("dividedBy(Money) does not allow rounding.");
        } catch (ArithmeticException correctBehavior) {
        }
        try {
            Money.dollars(10.00).dividedBy(Money.dollars(3.00)).decimalValue(5, Rounding.UNNECESSARY);
            fail("dividedBy(Money) does not allow rounding.");
        } catch (ArithmeticException correctBehavior) {
        }
    }

    @Test
    public void testCloseNumbersNotEqual() {
        Money d2_51a = Money.dollars(2.515);
        Money d2_51b = Money.dollars(2.5149);
        assertTrue(!d2_51a.equals(d2_51b));
    }

    @Test
    public void testCompare() {
        assertTrue(d15.isGreaterThan(d2_51));
        assertTrue(d2_51.isLessThan(d15));
        assertTrue(!d15.isGreaterThan(d15));
        assertTrue(!d15.isLessThan(d15));
        try {
            d15.isGreaterThan(e2_51);
            fail();
        } catch (Exception correctBehavior) {
        }
    }

    @Test
    public void testDifferentCurrencyNotEqual() {
        assertTrue(!d2_51.equals(e2_51));
    }

    @Test
    public void testEquals() {
        Money d2_51a = Money.dollars(2.51);
        assertEquals(d2_51a, d2_51);
    }

    @Test
    public void testEqualsNull() {
        Money d2_51a = Money.dollars(2.51);
        Object objectNull = null;
        assertFalse(d2_51a.equals(objectNull));

        //This next test seems just like the previous, but it's not
        //The Java Compiler early binds message sends and
        //it will bind the next call to equals(Money) and
        //the previous will bind to equals(Object)
        //I renamed the original equals(Money) to
        //equalsMoney(Money) to prevent wrong binding.
        Money moneyNull = null;
        assertFalse(d2_51a.equals(moneyNull));
    }

    @Test
    public void testHash() {
        Money d2_51a = Money.dollars(2.51);
        assertEquals(d2_51a.hashCode(), d2_51.hashCode());
    }


    @Test
    public void testNegation() {
        assertEquals(Money.dollars(-15), d15.negated());
        assertEquals(e2_51, e2_51.negated().negated());
    }

    @Test
    public void testPositiveNegative() {
        assertTrue(d15.isPositive());
        assertTrue(Money.dollars(-10).isNegative());
        assertFalse(Money.dollars(0).isPositive());
        assertFalse(Money.dollars(0).isNegative());
        assertTrue(Money.dollars(0).isZero());
    }

    @Test
    public void testPrint() {
        assertEquals("$ 15.00", d15.toString(Locale.US));
        assertEquals("USD 15.00", d15.toString(Locale.UK));
    }
    // TODO: Formatted printing of Money
    //	public void testLocalPrinting() {
    //		assertEquals("$15.00", d15.localString());
    //		assertEquals("2,51 DM", m2_51.localString());
    //	}

    @Test
    public void testRound() {
        Money dRounded = Money.dollars(1.2350);
        assertEquals(Money.dollars(1.24), dRounded);
    }

    @Test
    public void testSubtraction() {
        assertEquals(Money.dollars(12.49), d15.minus(d2_51));
    }

    @Test
    public void testApplyRatio() {
        Ratio oneThird = Ratio.of(1, 3);
        Money result = Money.dollars(100).applying(oneThird, 1, Rounding.UP);
        assertEquals(Money.dollars(33.40), result);
    }

    @Test
    public void testIncremented() {
        assertEquals(Money.dollars(2.52), d2_51.incremented());
        assertEquals(Money.valueOf(51, JPY), y50.incremented());
    }

    @Test
    public void testFractionalPennies() {
//        CurrencyPolicy(USD, 0.0025); 
//        Smallest unit.unit Any Money based on this CurrencyPolicy must be some multiple of the
//        smallest unit. "Scale" is insufficient, because the limit is not always a number of demial places.
//        Money someFee = Money.dollars(0.0025);
//        Money wholeMoney = someFee.times(4);
//        assertEquals(Money.dollars(0.01), wholeMoney);

    }

}