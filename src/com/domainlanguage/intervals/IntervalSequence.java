/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import java.util.*;

public class IntervalSequence {
    List intervals;

    public IntervalSequence() {
        intervals = new ArrayList();
    }
    
    public Iterator iterator() {
        return intervals.iterator();
    }

    public void add(Interval interval) {
        intervals.add(interval);
        Collections.sort(intervals); 
    }

    public boolean isEmpty() {
        return intervals.isEmpty();
    }

    public IntervalSequence gaps() {
        IntervalSequence gaps = new IntervalSequence();
        if (intervals.size() < 2)
            return new IntervalSequence();
        for (int i = 1; i < intervals.size(); i++) {
            Interval left = (Interval) intervals.get(i - 1);
            Interval right = (Interval) intervals.get(i);
            Interval gap = left.gap(right);
            if (!gap.isEmpty())
                gaps.add(gap);
        }
        return gaps;
    }

    public Interval extent() {
        if (intervals.isEmpty())
            return null;
        //TODO: Add a creation method to Interval for empty(), if it can be
        // polymorphic.
        if (intervals.size() == 1)
            return (Interval) intervals.get(0);
        Interval left = (Interval) intervals.get(0);
        Interval right = (Interval) intervals.get(intervals.size() - 1);
        return left.newOfSameType(left.lowerLimit(), left.includesLowerLimit(), right.upperLimit(), right.includesUpperLimit());
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private List getForPersistentMapping_Intervals() {
        return intervals;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Intervals(List intervals) {
        this.intervals = intervals;
    }
}