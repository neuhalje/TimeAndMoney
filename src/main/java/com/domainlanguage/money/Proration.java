/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.money;

import java.math.*;

import com.domainlanguage.base.*;

public class Proration {

	static Money sum(Money[] elements) {
		Money sum = Money.valueOf(0, elements[0].getCurrency());
		for (int i = 0; i < elements.length; i++) 
			sum = sum.plus(elements[i]);
		return sum;
	}

	static BigDecimal sum(BigDecimal[] elements) {
		BigDecimal sum = new BigDecimal(0);
		for (int i = 0; i < elements.length; i++) 
			sum = sum.add(elements[i]);
		return sum;
	}
	
	static Ratio[] ratios(BigDecimal[] proportions) {
		BigDecimal total = sum(proportions);
		Ratio[] ratios = new Ratio[proportions.length];
		for (int i = 0; i < ratios.length; i++) 
			ratios[i] = Ratio.of(proportions[i], total);
		return ratios;
	}

	private static int defaultScaleForIntermediateCalculations(Money total) {
		return total.getCurrency().getDefaultFractionDigits() + 1;
	}

	public Money[] dividedEvenlyIntoParts(Money total, int n) {
		Money lowResult = total.dividedBy(BigDecimal.valueOf(n), Rounding.DOWN);
		Money[] lowResults = new Money[n];
		for (int i = 0; i < n; i++) lowResults[i] = lowResult;
		Money remainder = total.minus(sum(lowResults));
		return distributeRemainderOver(lowResults, remainder);
	}

	public Money[] proratedOver(Money total, long[] longProportions) {
		BigDecimal[] proportions = new BigDecimal[longProportions.length];
		for (int i = 0; i < longProportions.length; i++) {
			proportions[i] = BigDecimal.valueOf(longProportions[i]);
		}
		return proratedOver(total, proportions);
	}
	
	public Money[] proratedOver(Money total, BigDecimal[] proportions) {
		Money[] simpleResult = new Money[proportions.length];
		int scale = defaultScaleForIntermediateCalculations(total);
		Ratio[] ratios = ratios(proportions);
		for (int i = 0; i < ratios.length; i++) {
			BigDecimal multiplier = ratios[i].decimalValue(scale, Rounding.DOWN);
			simpleResult[i] = total.times(multiplier, Rounding.DOWN);
		}
		Money remainder = total.minus(sum(simpleResult));
		return distributeRemainderOver(simpleResult, remainder);
	}

	public Money partOfWhole(Money total, long portion, long whole) {
		return partOfWhole(total, Ratio.of(portion, whole));
	}

	public Money partOfWhole(Money total, Ratio ratio) {
		int scale = defaultScaleForIntermediateCalculations(total);
		BigDecimal multiplier = ratio.decimalValue(scale, Rounding.DOWN);
		return total.times(multiplier, Rounding.DOWN);
	}
	
	Money[] distributeRemainderOver(Money[] amounts, Money remainder) {
		int increments = remainder.dividedBy(remainder.minimumIncrement()).decimalValue(0, Rounding.UNNECESSARY).intValue();
		assert increments <= amounts.length; 

		Money[] results = new Money[amounts.length];
		for (int i = 0; i < increments; i++) 
			results[i] = amounts[i].incremented();
		for (int i = increments; i < amounts.length; i++) 
			results[i] = amounts[i];
		return results; 
	}
	
}
