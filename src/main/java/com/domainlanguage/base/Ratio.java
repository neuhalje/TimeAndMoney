/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 * 
 * Ratio represents the unitless division of two quantities of the same type.
 * The key to its usefulness is that it defers the calculation of a decimal
 * value for the ratio. An object which has responsibility for the two values in
 * the ratio and understands their quantities can create the ratio, which can
 * then be used by any client in a unitless form, so that the client is not
 * required to understand the units of the quantity. At the same time, this
 * gives control of the precision and rounding rules to the client, when the
 * time comes to compute a decimal value for the ratio. The client typically has
 * the responsibilities that enable an appropriate choice of these parameters.
 *  
 */

package com.domainlanguage.base;

import java.math.BigDecimal;

public class Ratio {
    private BigDecimal numerator;
    private BigDecimal denominator;

    public static Ratio of(BigDecimal numerator, BigDecimal denominator) {
        return new Ratio(numerator, denominator);
    }

    public static Ratio of(long numerator, long denominator) {
        return new Ratio(BigDecimal.valueOf(numerator), BigDecimal.valueOf(denominator));
    }
    public static Ratio of(BigDecimal fractional) {
        return new Ratio(fractional, BigDecimal.valueOf(1));
    }

    public Ratio(BigDecimal numerator, BigDecimal denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public BigDecimal decimalValue(int scale, int roundingRule) {
        return numerator.divide(denominator, scale, roundingRule);
    }

    public boolean equals(Object anObject) {
        try {
            return equals((Ratio)anObject);
        } catch(ClassCastException ex) {
            return false;
        }
    }
    public boolean equals(Ratio other) {
        return 
        	other != null &&
        	this.numerator.equals(other.numerator) && this.denominator.equals(other.denominator);
    }

    public int hashCode() {
        return numerator.hashCode();
    }

    public Ratio times(BigDecimal multiplier) {
        return Ratio.of(numerator.multiply(multiplier), denominator);
    }

    public Ratio times(Ratio multiplier) {
        return Ratio.of(numerator.multiply(multiplier.numerator), denominator.multiply(multiplier.denominator));
    }

    public String toString() {
        return numerator.toString() + "/" + denominator;
    }
    
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    Ratio() {
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private BigDecimal getForPersistentMapping_Denominator() {
        return denominator;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Denominator(BigDecimal denominator) {
        this.denominator = denominator;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private BigDecimal getForPersistentMapping_Numerator() {
        return numerator;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Numerator(BigDecimal numerator) {
        this.numerator = numerator;
    }



}