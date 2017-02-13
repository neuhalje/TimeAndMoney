/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import java.util.*;

import junit.framework.*;

public class IntervalSequenceTest extends TestCase {
    private Interval c5_10c = Interval.closed(new Integer(5), new Integer(10));
    private Interval o10_12c = Interval.over(new Integer(10), false, new Integer(12), true);
    private Interval o11_20c = Interval.over(new Integer(11), false, new Integer(20), true);
    private Interval o12_20o = Interval.open(new Integer(12), new Integer(20));
    private Interval c20_25c = Interval.closed(new Integer(20), new Integer(25));
    private Interval o25_30c = Interval.over(new Integer(25), false, new Integer(30), true);
    private Interval o30_35o = Interval.open(new Integer(30), new Integer(35));

    public void testIterate() {
        IntervalSequence intervalSequence = new IntervalSequence();
        assertTrue(intervalSequence.isEmpty());
        intervalSequence.add(c5_10c);
        intervalSequence.add(o10_12c);
        Iterator it = intervalSequence.iterator();
        assertTrue(it.hasNext());
        assertEquals(c5_10c, it.next());
        assertTrue(it.hasNext());
        assertEquals(o10_12c, it.next());
        assertFalse(it.hasNext());
        try {
            assertNull(it.next());
            fail("Should throw NoSuchElementException");
            //TODO: Should all iterators throw NoSuchElementException or null
            // after end?
        } catch (NoSuchElementException e) {
        }
    }

    public void testInsertedOutOfOrder() {
        IntervalSequence intervalSequence = new IntervalSequence();
        intervalSequence.add(o10_12c);
        intervalSequence.add(c5_10c);
        //Iterator behavior should be the same regardless of order of
        // insertion.
        Iterator it = intervalSequence.iterator();
        assertTrue(it.hasNext());
        assertEquals(c5_10c, it.next());
        assertTrue(it.hasNext());
        assertEquals(o10_12c, it.next());
        assertFalse(it.hasNext());

    }

    public void testGaps() {
        IntervalSequence intervalSequence = new IntervalSequence();
        intervalSequence.add(c5_10c);
        intervalSequence.add(o10_12c);
        intervalSequence.add(c20_25c);
        intervalSequence.add(o30_35o);
        IntervalSequence gaps = intervalSequence.gaps();
        Iterator it = gaps.iterator();
        assertTrue(it.hasNext());
        assertEquals(o12_20o, it.next());
        assertTrue(it.hasNext());
        assertEquals(o25_30c, it.next());
        assertFalse(it.hasNext());

    }

    public void testOverlapping() {
        IntervalSequence intervalSequence = new IntervalSequence();
        intervalSequence.add(o10_12c);
        intervalSequence.add(o11_20c);
        Iterator it = intervalSequence.iterator();
        assertTrue(it.hasNext());
        assertEquals(o10_12c, it.next());
        assertTrue(it.hasNext());
        assertEquals(o11_20c, it.next());
        assertFalse(it.hasNext());
    }
    
//    public void testIntersections() {
//        
//        IntervalSequence intervalSequence = new IntervalSequence();
//        intervalSequence.add(o10_12c);
//        intervalSequence.add(o11_20c);
//        intervalSequence.add(c20_25c);
//        
//        Iterator it = intervalSequence.intersections().iterator();
//        assertTrue(it.hasNext());
//        assertEquals(o11_12c, it.next());
//        assertTrue(it.hasNext());
//        assertEquals(c20_20c, it.next());
//        assertFalse(it.hasNext());
//    }
    
    public void testExtent() {
        IntervalSequence intervalSequence = new IntervalSequence();
        intervalSequence.add(c5_10c);
        intervalSequence.add(o10_12c);
        intervalSequence.add(c20_25c);
        assertEquals(Interval.closed(new Integer(5), new Integer(25)), intervalSequence.extent());
    }
}