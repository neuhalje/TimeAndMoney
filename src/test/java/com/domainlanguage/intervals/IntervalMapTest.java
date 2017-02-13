/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class IntervalMapTest {

    @Test
    public void testLookup() {
        IntervalMap map = new LinearIntervalMap();
        map.put(Interval.closed(new Integer(1), new Integer(3)), "one-three");
        map.put(Interval.closed(new Integer(5), new Integer(9)), "five-nine");
        map.put(Interval.open(new Integer(9), new Integer(12)), "ten-eleven");

        assertFalse(map.containsKey(new Integer(0)));
        assertTrue(map.containsKey(new Integer(1)));
        assertTrue(map.containsKey(new Integer(2)));
        assertTrue(map.containsKey(new Integer(3)));
        assertFalse(map.containsKey(new Integer(4)));
        assertTrue(map.containsKey(new Integer(5)));
        assertTrue(map.containsKey(new Integer(9)));
        assertTrue(map.containsKey(new Integer(11)));
        assertFalse(map.containsKey(new Integer(12)));
        assertFalse(map.containsKey(new Integer(13)));
        assertFalse(map.containsKey(null));

        assertNull(map.get(new Integer(0)));
        assertEquals("one-three", map.get(new Integer(1)));
        assertEquals("one-three", map.get(new Integer(2)));
        assertEquals("one-three", map.get(new Integer(3)));
        assertNull(map.get(new Integer(4)));
        assertEquals("five-nine", map.get(new Integer(5)));
        assertEquals("five-nine", map.get(new Integer(9)));
        assertEquals("ten-eleven", map.get(new Integer(10)));
        assertEquals("ten-eleven", map.get(new Integer(11)));
        assertNull(map.get(new Integer(12)));
        assertNull(map.get(new Integer(13)));
        assertNull(map.get(null));
    }

    @Test
    public void testRemove() {
        IntervalMap map = new LinearIntervalMap();
        map.put(Interval.closed(new Integer(1), new Integer(10)), "one-ten");
        map.remove(Interval.closed(new Integer(3), new Integer(5)));
        assertEquals("one-ten", map.get(new Integer(2)));
        assertNull(map.get(new Integer(3)));
        assertNull(map.get(new Integer(4)));
        assertNull(map.get(new Integer(5)));
        assertEquals("one-ten", map.get(new Integer(6)));
    }

    @Test
    public void testConstructionOverwriteOverlap() {
        IntervalMap map = new LinearIntervalMap();
        map.put(Interval.closed(new Integer(1), new Integer(3)), "one-three");
        map.put(Interval.closed(new Integer(5), new Integer(9)), "five-nine");
        map.put(Interval.open(new Integer(9), new Integer(12)), "ten-eleven");
        assertEquals("ten-eleven", map.get(new Integer(10)));
        assertEquals("ten-eleven", map.get(new Integer(11)));
        assertNull(map.get(new Integer(12)));

        Interval eleven_thirteen = Interval.closed(new Integer(11), new Integer(13));
        assertTrue(map.containsIntersectingKey(eleven_thirteen));
        map.put(eleven_thirteen, "eleven-thirteen");
        assertEquals("ten-eleven", map.get(new Integer(10)));
        assertEquals("eleven-thirteen", map.get(new Integer(11)));
        assertEquals("eleven-thirteen", map.get(new Integer(12)));
    }

    @Test
    public void testConstructionOverwriteMiddle() {
        IntervalMap map = new LinearIntervalMap();
        map.put(Interval.closed(new Integer(1), new Integer(3)), "one-three");
        map.put(Interval.closed(new Integer(5), new Integer(9)), "five-nine");
        map.put(Interval.open(new Integer(9), new Integer(12)), "ten-eleven");
        assertEquals("five-nine", map.get(new Integer(6)));
        assertEquals("five-nine", map.get(new Integer(7)));
        assertEquals("five-nine", map.get(new Integer(8)));
        assertEquals("five-nine", map.get(new Integer(9)));

        Interval seven_eight = Interval.closed(new Integer(7), new Integer(8));
        assertTrue(map.containsIntersectingKey(seven_eight));
        map.put(seven_eight, "seven-eight");
        assertEquals("five-nine", map.get(new Integer(6)));
        assertEquals("seven-eight", map.get(new Integer(7)));
        assertEquals("seven-eight", map.get(new Integer(8)));
        assertEquals("five-nine", map.get(new Integer(9)));
    }

    @Test
    public void testConstructionOverwriteMultiple() {
        IntervalMap map = new LinearIntervalMap();
        map.put(Interval.closed(new Integer(1), new Integer(2)), "one-two");
        map.put(Interval.closed(new Integer(3), new Integer(4)), "three-four");
        map.put(Interval.closed(new Integer(5), new Integer(6)), "five-six");
        map.put(Interval.closed(new Integer(8), new Integer(9)), "eight-nine");
        map.put(Interval.closed(new Integer(3), new Integer(8)), "three-eight");
        assertEquals("one-two", map.get(new Integer(2)));
        assertEquals("three-eight", map.get(new Integer(3)));
        assertEquals("three-eight", map.get(new Integer(4)));
        assertEquals("three-eight", map.get(new Integer(5)));
        assertEquals("three-eight", map.get(new Integer(6)));
        assertEquals("three-eight", map.get(new Integer(7)));
        assertEquals("three-eight", map.get(new Integer(8)));
        assertEquals("eight-nine", map.get(new Integer(9)));
    }

}