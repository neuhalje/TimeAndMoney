/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.money;

import java.math.BigDecimal;
import java.util.Currency;
import com.domainlanguage.time.Duration;
import com.domainlanguage.time.TimeRate;

public class MoneyTimeRate {
	private TimeRate rate;
	private Currency currency;

	public MoneyTimeRate(Money money, Duration duration) {
		rate = new TimeRate(money.getAmount(), duration);
		currency = money.getCurrency();
	}

	public Money over(Duration duration) {
		return over(duration, BigDecimal.ROUND_UNNECESSARY);
	}

	public Money over(Duration duration, int roundRule) {
		return over(duration, rate.scale(), roundRule);
	}

	public Money over(Duration duration, int scale, int roundRule) {
		return Money.valueOf(rate.over(duration, scale, roundRule), currency);
	}

    public boolean equals(Object other) {
        try {
            return equals((MoneyTimeRate) other);
        } catch(ClassCastException ex) {
            return false;
        }
    }
	public boolean equals(MoneyTimeRate another) {
		return 
            another != null &&
			this.rate.equals(another.rate) && 
			this.currency.equals(another.currency);
	}

	public String toString() {
		return rate.toString();
	}
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    MoneyTimeRate() {
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
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private TimeRate getForPersistentMapping_Rate() {
        return rate;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Rate(TimeRate rate) {
        this.rate = rate;
    }
}
