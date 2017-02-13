/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

public interface IntervalMap {
    void put(Interval keyInterval, Object value);

    Object get(Comparable key);

    void remove(Interval keyInterval);

    boolean containsKey(Comparable key);

    boolean containsIntersectingKey(Interval interval);
}