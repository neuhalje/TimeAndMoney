/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

interface TimeUnitConversionFactors {
    int millisecondsPerSecond = 1000;
    int millisecondsPerMinute = 60 * millisecondsPerSecond;
    int millisecondsPerHour = 60 * millisecondsPerMinute;
    int millisecondsPerDay = 24 * millisecondsPerHour;
    int millisecondsPerWeek = 7 * millisecondsPerDay;
    int monthsPerQuarter = 3;
    int monthsPerYear = 12;
}