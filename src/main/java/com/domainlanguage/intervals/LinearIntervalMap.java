/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import java.util.*;

public class LinearIntervalMap implements IntervalMap {
    private Map keyValues;

    public LinearIntervalMap() {
        keyValues = new HashMap();
    }
    
    public void put(Interval keyInterval, Object value) {
        remove(keyInterval);
        keyValues.put(keyInterval, value);
    }

    public void remove(Interval keyInterval) {
        List intervalSequence = intersectingKeys(keyInterval);
        for (Iterator iter = intervalSequence.iterator(); iter.hasNext();) {
            Interval oldInterval = (Interval) iter.next();
            Object oldValue = keyValues.get(oldInterval);
            keyValues.remove(oldInterval);
            List complementIntervalSequence = keyInterval.complementRelativeTo(oldInterval);
            directPut(complementIntervalSequence, oldValue);
        }
    }

    private void directPut(List intervalSequence, Object value) {
        for (Iterator iter = intervalSequence.iterator(); iter.hasNext();)
            keyValues.put(iter.next(), value);
    }

    public Object get(Comparable key) {
        Interval keyInterval = findKeyIntervalContaining(key);
        //		if (keyInterval == null) return null;
        return keyValues.get(keyInterval);
    }

    public boolean containsKey(Comparable key) {
        return findKeyIntervalContaining(key) != null;
    }

    private Interval findKeyIntervalContaining(Comparable key) {
        if (key == null)
            return null;
        Iterator it = keyValues.keySet().iterator();
        while (it.hasNext()) {
            Interval interval = (Interval) it.next();
            if (interval.includes(key))
                return interval;
        }
        return null;
    }

    private List intersectingKeys(Interval otherInterval) {
        List intervalSequence = new ArrayList();
        Iterator it = keyValues.keySet().iterator();
        while (it.hasNext()) {
            Interval keyInterval = (Interval) it.next();
            if (keyInterval.intersects(otherInterval))
                intervalSequence.add(keyInterval);
        }
        return intervalSequence;
    }

    public boolean containsIntersectingKey(Interval otherInterval) {
        return !intersectingKeys(otherInterval).isEmpty();
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private Map getForPersistentMapping_KeyValues() {
        return keyValues;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_KeyValues(Map keyValues) {
        this.keyValues = keyValues;
    }

}

