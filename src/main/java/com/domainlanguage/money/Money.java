/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.Iterator;
import java.util.Locale;
import com.domainlanguage.base.Ratio;
import com.domainlanguage.base.Rounding;
import com.domainlanguage.time.Duration;

public class Money implements Comparable, Serializable {
	private static final Currency USD = Currency.getInstance("USD");
	private static final Currency EUR = Currency.getInstance("EUR");
	private static final int DEFAULT_ROUNDING_MODE = Rounding.HALF_EVEN;
	
	private BigDecimal amount;
	private Currency currency;
	
	/**
     * The constructor does not complex computations and requires simple, inputs
     * consistent with the class invariant. Other creation methods are available
     * for convenience.
     */
	public Money(BigDecimal amount, Currency currency) {
		if (amount.scale() != currency.getDefaultFractionDigits()) throw new IllegalArgumentException("Scale of amount does not match currency");
		this.currency = currency;
		this.amount = amount;
	}

	/**
     * This creation method is safe to use. It will adjust scale, but will not
     * round off the amount.
     */
	public static Money valueOf (BigDecimal amount, Currency currency) {
		return Money.valueOf (amount, currency, Rounding.UNNECESSARY);
	}

	/**
     * For convenience, an amount can be rounded to create a Money.
     */
	public static Money valueOf(BigDecimal rawAmount, Currency currency, int roundingMode) {
		BigDecimal amount = rawAmount.setScale(currency.getDefaultFractionDigits(), roundingMode);
		return new Money(amount, currency);
	}

	/**
     * WARNING: Because of the indefinite precision of double, this method must
     * round off the value.
     */
	public static Money valueOf(double dblAmount, Currency currency) {
		return Money.valueOf(dblAmount, currency, DEFAULT_ROUNDING_MODE);
	}

	/**
     * Because of the indefinite precision of double, this method must round off
     * the value. This method gives the client control of the rounding mode.
     */
	public static Money valueOf(double dblAmount, Currency currency, int roundingMode) {
		BigDecimal rawAmount = new BigDecimal(dblAmount);
		return Money.valueOf(rawAmount, currency, roundingMode);
	}

	/**
     * WARNING: Because of the indefinite precision of double, thismethod must
     * round off the value.
     */
	public static Money dollars(double amount) {
		return Money.valueOf(amount, USD);
	}

	/**
     * This creation method is safe to use. It will adjust scale, but will not
     * round off the amount.
     */
	public static Money dollars (BigDecimal amount) {
		return Money.valueOf(amount, USD);
	}

	/**
     * WARNING: Because of the indefinite precision of double, this method must
     * round off the value.
     */
	public static Money euros(double amount) {
		return Money.valueOf(amount, EUR);
	}

	/**
     * This creation method is safe to use. It will adjust scale, but will not
     * round off the amount.
     */
	public static Money euros (BigDecimal amount) {
		return Money.valueOf(amount, EUR);
	}
    
    public static Money sum(Collection monies) {
        //TODO Return Default Currency
        if (monies.isEmpty())
            return Money.dollars(0.00);
        Iterator iterator = monies.iterator();
        Money sum = (Money) iterator.next();
        while (iterator.hasNext()){
            Money each = (Money) iterator.next();
            sum = sum.plus(each);
        }
        return sum;
    }

	/**
	 * How best to handle access to the internals? It is needed for
	 * database mapping, UI presentation, and perhaps a few other
	 * uses. Yet giving public access invites people to do the
	 * real work of the Money object elsewhere.
	 * Here is an experimental approach, giving access with a 
	 * warning label of sorts. Let us know how you like it.
	 */
	public BigDecimal breachEncapsulationOfAmount() {
		return amount;
	}

	public Currency breachEncapsulationOfCurrency() {
		return currency;
	}
	
	
	/**
     * This probably should be Currency responsibility. Even then, it may need
     * to be customized for specialty apps because there are other cases, where
     * the smallest increment is not the smallest unit.
     */
	Money minimumIncrement() {
		BigDecimal one = new BigDecimal(1);
		BigDecimal increment = one.movePointLeft(currency.getDefaultFractionDigits());
		return Money.valueOf(increment, currency);
	}
	
	Money incremented() {
		return this.plus(minimumIncrement());
	}

	boolean hasSameCurrencyAs(Money arg) {
		return currency.equals(arg.currency);
	}

	public Money negated() {
		return Money.valueOf(amount.negate(), currency);
	}
	
	public Money abs() {
		return Money.valueOf(amount.abs(), currency);
	}
	
	public boolean isNegative() {
		return amount.compareTo(new BigDecimal(0)) < 0;
	}
	
	public boolean isPositive() {
		return amount.compareTo(new BigDecimal(0)) > 0;
	}
	
	public boolean isZero() {
		return this.equals(Money.valueOf(0.0, currency));
	}
	
	public Money plus(Money other) {
        assertHasSameCurrencyAs(other);
		return Money.valueOf(amount.add(other.amount), currency);
	}
	
	public Money minus(Money other) {
		return this.plus(other.negated());
	}

	public Money dividedBy (BigDecimal divisor, int roundingMode) {
		BigDecimal newAmount = amount.divide(divisor, roundingMode);
		return Money.valueOf(newAmount,currency);
	}
	
	public Money dividedBy (double divisor) {
		return dividedBy(divisor, DEFAULT_ROUNDING_MODE);
	}

	public Money dividedBy (double divisor, int roundingMode) {
		return dividedBy(new BigDecimal(divisor), roundingMode);
	}
	
	public Ratio dividedBy (Money divisor) {
		assertHasSameCurrencyAs(divisor);
		return Ratio.of(amount, divisor.amount);
    }

	public Money applying (Ratio ratio, int roundingRule) {
	    return applying(ratio, currency.getDefaultFractionDigits(), roundingRule);
	}

	public Money applying (Ratio ratio, int scale, int roundingRule) {
		BigDecimal newAmount = ratio.times(amount).decimalValue(scale, roundingRule);
		return Money.valueOf(newAmount, currency);
	}
	
	/**
     * TODO: Many apps require carrying extra precision in intermediate
     * calculations. The use of Ratio is a beginning, but need a comprehensive
     * solution. Currently, an invariant of Money is that the scale is the
     * currencies standard scale, but this will probably have to be suspended or
     * elaborated in intermediate calcs, or handled with defered calculations
     * like Ratio.
     */

	public Money times(BigDecimal factor) {
		return times(factor, DEFAULT_ROUNDING_MODE);
	}
	
	/**
     * TODO: BigDecimal.multiply() scale is sum of scales of two multiplied
     * numbers. So what is scale of times?
     */
	public Money times(BigDecimal factor, int roundingMode) {
		return Money.valueOf(amount.multiply(factor), currency, roundingMode);
	}
	
	public Money times (double amount, int roundingMode) {
		return times(new BigDecimal(amount), roundingMode);
	}

	public Money times(double amount) {
		return times(new BigDecimal(amount));
	}

	public Money times(int i) {
		return times(new BigDecimal(i));
	}

	public int compareTo(Object other) {
		return compareTo((Money)other);
	}
	
	public int compareTo(Money other) {
		if (!hasSameCurrencyAs(other)) 
            throw new IllegalArgumentException("Compare is not defined between different currencies");
		return amount.compareTo(other.amount);
	}
	
	public boolean isGreaterThan(Money other) {
		return (compareTo(other) > 0);
	}
	
	public boolean isLessThan(Money other) {
		return (compareTo(other) < 0);
	}
	
	public boolean equals(Object other) {
        try {
            return equals((Money)other);
        } catch(ClassCastException ex) {
            return false;
        }
	}
    
    public boolean equals(Money other) {
        return 
            other != null &&
            hasSameCurrencyAs(other) && 
            amount.equals(other.amount);
    }
    
	public int hashCode() {
		return amount.hashCode();
	}
	
	public String toString() {
		return currency.getSymbol() + " " + amount;
	}

	public String toString(Locale locale) {
		return currency.getSymbol(locale) + " " + amount;
	}

    public MoneyTimeRate per(Duration duration) {
       return new MoneyTimeRate(this, duration);
    }
    
//  TODO: Provide some currency-dependent formatting. Java 1.4 Currency doesn't
//  do it.
//  public String formatString() {
//      return currency.formatString(amount());
//  }
//  public String localString() {
//      return currency.getFormat().format(amount());
//  }
    
    BigDecimal getAmount() {
        return amount;
    }

    Currency getCurrency() {
        return currency;
    }
    
    private void assertHasSameCurrencyAs(Money aMoney) {
        if (!hasSameCurrencyAs(aMoney))
            throw new IllegalArgumentException(aMoney.toString() + " is not same currency as " + this.toString());
    }

    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    Money() {
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private BigDecimal getForPersistentMapping_Amount() {
        return amount;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Amount(BigDecimal amount) {
        this.amount = amount;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private Currency getForPersistentMapping_Currency() {
        return currency;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Currency(Currency currency) {
        this.currency = currency;
    }

    
}