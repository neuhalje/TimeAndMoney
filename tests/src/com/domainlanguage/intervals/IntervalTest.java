/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import java.math.BigDecimal;
import java.util.List;
import junit.framework.TestCase;
import com.domainlanguage.tests.SerializationTester;

public class IntervalTest extends TestCase {
    private Interval empty = Interval.open(new BigDecimal(1), new BigDecimal(1));
    private Interval c5_10c = Interval.closed(new BigDecimal(5), new BigDecimal(10));
    private Interval c1_10c = Interval.closed(new BigDecimal(1), new BigDecimal(10));
    private Interval c4_6c = Interval.closed(new BigDecimal(4), new BigDecimal(6));
    private Interval c5_15c = Interval.closed(new BigDecimal(5), new BigDecimal(15));
    private Interval c12_16c = Interval.closed(new BigDecimal(12), new BigDecimal(16));
    private Interval o10_12c = Interval.over(new BigDecimal(10), false, new BigDecimal(12), true);
    private Interval o1_1c = Interval.over(new BigDecimal(1), false, new BigDecimal(1), true);
    private Interval c1_1o = Interval.over(new BigDecimal(1), true, new BigDecimal(1), false);
    private Interval c1_1c = Interval.over(new BigDecimal(1), true, new BigDecimal(1), true);
    private Interval o1_1o = Interval.over(new BigDecimal(1), false, new BigDecimal(1), false);
    
    public static IntervalLimit exampleLimitForPersistentMappingTesting() {
        return IntervalLimit.upper(true, new Integer(78));
    }
//    private Interval o1_10o = Interval.open(new BigDecimal(1), new BigDecimal(10));
//    private Interval o10_12o = Interval.open(new BigDecimal(10), new BigDecimal(12));

    //TODO: either fix those tests, or delete them (Benny)
    //	public void testAssertions() {
    //		//Redundant, maybe, but with all the compiler default
    //		//confusion at the moment, I decided to throw this in.
    //		try {
    //			Interval.closed(new BigDecimal(2.0), new BigDecimal(1.0));
    //			fail("Lower bound mustn't be above upper bound.");
    //		} catch (AssertionError e) {
    //			//Correct. Do nothing.
    //		}
    //	}
    //
    //	public void testUpTo() {
    //		Interval range = Interval.upTo(new BigDecimal(5.5));
    //		assertTrue(range.includes(new BigDecimal(5.5)));
    //		assertTrue(range.includes(new BigDecimal(-5.5)));
    //		assertTrue(range.includes(new BigDecimal(Double.NEGATIVE_INFINITY)));
    //		assertTrue(!range.includes(new BigDecimal(5.5001)));
    //	}
    //
    //	public void testAndMore() {
    //		Interval range = Interval.andMore(5.5);
    //		assertTrue(range.includes(5.5));
    //		assertTrue(!range.includes(5.4999));
    //		assertTrue(!range.includes(-5.5));
    //		assertTrue(range.includes(Double.POSITIVE_INFINITY));
    //		assertTrue(range.includes(5.5001));
    //	}
    //
    public void testAbstractCreation() {
        Interval concrete = new Interval(new Integer(1), true, new Integer(3), true);
        Interval newInterval = concrete.newOfSameType(new Integer(1), false, new Integer(4), false);

        Interval expected = new Interval(new Integer(1), false, new Integer(4), false);
        assertEquals(expected, newInterval);
    }

    public void testSerialization() {
        SerializationTester.assertCanBeSerialized(c5_10c);
    }

    public void testToString() {
        assertEquals("[1, 10]", c1_10c.toString());
        assertEquals("(10, 12]", o10_12c.toString());
        assertEquals("{}", empty.toString());
        assertEquals("{10}", Interval.closed(new Integer(10), new Integer(10)).toString());
    }

    public void testIsBelow() {
        Interval range = Interval.closed(new BigDecimal(-5.5), new BigDecimal(6.6));
        assertFalse(range.isBelow(new BigDecimal(5.0)));
        assertFalse(range.isBelow(new BigDecimal(-5.5)));
        assertFalse(range.isBelow(new BigDecimal(-5.4999)));
        assertFalse(range.isBelow(new BigDecimal(6.6)));
        assertTrue(range.isBelow(new BigDecimal(6.601)));
        assertFalse(range.isBelow(new BigDecimal(-5.501)));
    }

    public void testIncludes() {
        Interval range = Interval.closed(new BigDecimal(-5.5), new BigDecimal(6.6));
        assertTrue(range.includes(new BigDecimal(5.0)));
        assertTrue(range.includes(new BigDecimal(-5.5)));
        assertTrue(range.includes(new BigDecimal(-5.4999)));
        assertTrue(range.includes(new BigDecimal(6.6)));
        assertFalse(range.includes(new BigDecimal(6.601)));
        assertFalse(range.includes(new BigDecimal(-5.501)));
    }

    public void testOpenInterval() {
        Interval exRange = Interval.over(new BigDecimal(-5.5), false, new BigDecimal(6.6), true);
        assertTrue(exRange.includes(new BigDecimal(5.0)));
        assertFalse(exRange.includes(new BigDecimal(-5.5)));
        assertTrue(exRange.includes(new BigDecimal(-5.4999)));
        assertTrue(exRange.includes(new BigDecimal(6.6)));
        assertFalse(exRange.includes(new BigDecimal(6.601)));
        assertFalse(exRange.includes(new BigDecimal(-5.501)));
    }

    public void testIsEmpty() {
        assertFalse(Interval.closed(new Integer(5), new Integer(6)).isEmpty());
        assertFalse(Interval.closed(new Integer(6), new Integer(6)).isEmpty());
        assertTrue(Interval.open(new Integer(6), new Integer(6)).isEmpty());
        assertTrue(c1_10c.emptyOfSameType().isEmpty());
    }

    public void testIntersects() {
        assertTrue("c5_10c.intersects(c1_10c)", c5_10c.intersects(c1_10c));
        assertTrue("c1_10c.intersects(c5_10c)", c1_10c.intersects(c5_10c));
        assertTrue("c4_6c.intersects(c1_10c)", c4_6c.intersects(c1_10c));
        assertTrue("c1_10c.intersects(c4_6c)", c1_10c.intersects(c4_6c));
        assertTrue("c5_10c.intersects(c5_15c)", c5_10c.intersects(c5_15c));
        assertTrue("c5_15c.intersects(c1_10c)", c5_15c.intersects(c1_10c));
        assertTrue("c1_10c.intersects(c5_15c)", c1_10c.intersects(c5_15c));
        assertFalse("c1_10c.intersects(c12_16c)", c1_10c.intersects(c12_16c));
        assertFalse("c12_16c.intersects(c1_10c)", c12_16c.intersects(c1_10c));
        assertTrue("c5_10c.intersects(c5_10c)", c5_10c.intersects(c5_10c));
        assertFalse("c1_10c.intersects(o10_12c)", c1_10c.intersects(o10_12c));
        assertFalse("o10_12c.intersects(c1_10c)", o10_12c.intersects(c1_10c));
    }

    public void testIntersection() {
        assertEquals(c5_10c, c5_10c.intersect(c1_10c));
        assertEquals(c5_10c, c1_10c.intersect(c5_10c));
        assertEquals(c4_6c, c4_6c.intersect(c1_10c));
        assertEquals(c4_6c, c1_10c.intersect(c4_6c));
        assertEquals(c5_10c, c5_10c.intersect(c5_15c));
        assertEquals(c5_10c, c5_15c.intersect(c1_10c));
        assertEquals(c5_10c, c1_10c.intersect(c5_15c));
        assertTrue(c1_10c.intersect(c12_16c).isEmpty());
        assertEquals(empty, c1_10c.intersect(c12_16c));
        assertEquals(empty, c12_16c.intersect(c1_10c));
        assertEquals(c5_10c, c5_10c.intersect(c5_10c));
        assertEquals(empty, c1_10c.intersect(o10_12c));
        assertEquals(empty, o10_12c.intersect(c1_10c));
    }

    public void testGreaterOfLowerLimits() {
        assertEquals(new BigDecimal(5), c5_10c.greaterOfLowerLimits(c1_10c));
        assertEquals(new BigDecimal(5), c1_10c.greaterOfLowerLimits(c5_10c));
        assertEquals(new BigDecimal(12), c1_10c.greaterOfLowerLimits(c12_16c));
        assertEquals(new BigDecimal(12), c12_16c.greaterOfLowerLimits(c1_10c));
    }

    public void testLesserOfUpperLimits() {
        assertEquals(new BigDecimal(10), c5_10c.lesserOfUpperLimits(c1_10c));
        assertEquals(new BigDecimal(10), c1_10c.lesserOfUpperLimits(c5_10c));
        assertEquals(new BigDecimal(6), c4_6c.lesserOfUpperLimits(c12_16c));
        assertEquals(new BigDecimal(6), c12_16c.lesserOfUpperLimits(c4_6c));
    }

    public void testCoversInterval() {
        assertFalse(c5_10c.covers(c1_10c));
        assertTrue(c1_10c.covers(c5_10c));
        assertFalse(c4_6c.covers(c1_10c));
        assertTrue(c1_10c.covers(c4_6c));
        assertTrue(c5_10c.covers(c5_10c));
        Interval halfOpen5_10 = Interval.over(new BigDecimal(5), false, new BigDecimal(10), true);
        assertTrue("closed incl left-open", c5_10c.covers(halfOpen5_10));
        assertTrue("left-open incl left-open", halfOpen5_10.covers(halfOpen5_10));
        assertFalse("left-open doesn't include closed", halfOpen5_10.covers(c5_10c));
        //TODO: Need to test other half-open case and full-open case.
    }

    public void testGap() {
        Interval c1_3c = Interval.closed(new Integer(1), new Integer(3));
        Interval c5_7c = Interval.closed(new Integer(5), new Integer(7));
        Interval o3_5o = Interval.open(new Integer(3), new Integer(5));
        Interval c2_3o = Interval.over(new Integer(2), true, new Integer(3), false);

        assertEquals(o3_5o, c1_3c.gap(c5_7c));
        assertTrue(c1_3c.gap(o3_5o).isEmpty());
        assertTrue(c1_3c.gap(c2_3o).isEmpty());
        assertTrue(c2_3o.gap(o3_5o).isSingleElement());
    }

    public void testRelativeComplementDisjoint() {
        Interval c1_3c = Interval.closed(new Integer(1), new Integer(3));
        Interval c5_7c = Interval.closed(new Integer(5), new Integer(7));
        List complement = c1_3c.complementRelativeTo(c5_7c);
        assertEquals(1, complement.size());
        assertEquals(c5_7c, complement.get(0));
    }

    public void testRelativeComplementDisjointAdjacentOpen() {
        Interval c1_3o = Interval.over(new Integer(1), true, new Integer(3), false);
        Interval c3_7c = Interval.closed(new Integer(3), new Integer(7));
        List complement = c1_3o.complementRelativeTo(c3_7c);
        assertEquals(1, complement.size());
        assertEquals(c3_7c, complement.get(0));
    }

    public void testRelativeComplementOverlapLeft() {
        Interval c1_5c = Interval.closed(new Integer(1), new Integer(5));
        Interval c3_7c = Interval.closed(new Integer(3), new Integer(7));
        List complement = c3_7c.complementRelativeTo(c1_5c);
        Interval c1_3o = Interval.over(new Integer(1), true, new Integer(3), false);
        assertEquals(1, complement.size());
        assertEquals(c1_3o, complement.get(0));
    }

    public void testRelativeComplementOverlapRight() {
        Interval c1_5c = Interval.closed(new Integer(1), new Integer(5));
        Interval c3_7c = Interval.closed(new Integer(3), new Integer(7));
        List complement = c1_5c.complementRelativeTo(c3_7c);
        Interval o5_7c = Interval.over(new Integer(5), false, new Integer(7), true);
        assertEquals(1, complement.size());
        assertEquals(o5_7c, complement.get(0));
    }

    public void testRelativeComplementAdjacentClosed() {
        Interval c1_3c = Interval.closed(new Integer(1), new Integer(3));
        Interval c5_7c = Interval.closed(new Integer(5), new Integer(7));
        List complement = c1_3c.complementRelativeTo(c5_7c);
        assertEquals(1, complement.size());
        assertEquals(c5_7c, complement.get(0));
    }

    public void testRelativeComplementEnclosing() {
        Interval c3_5c = Interval.closed(new Integer(3), new Integer(5));
        Interval c1_7c = Interval.closed(new Integer(1), new Integer(7));
        List complement = c1_7c.complementRelativeTo(c3_5c);
        assertEquals(0, complement.size());
    }

    public void testRelativeComplementEqual() {
        Interval c1_7c = Interval.closed(new Integer(1), new Integer(7));
        List complement = c1_7c.complementRelativeTo(c1_7c);
        assertEquals(0, complement.size());
    }

    public void testRelativeComplementEnclosed() {
        Interval c3_5c = Interval.closed(new Integer(3), new Integer(5));
        Interval c1_7c = Interval.closed(new Integer(1), new Integer(7));
        Interval c1_3o = Interval.over(new Integer(1), true, new Integer(3), false);
        Interval o5_7c = Interval.over(new Integer(5), false, new Integer(7), true);
        List complement = c3_5c.complementRelativeTo(c1_7c);
        assertEquals(2, complement.size());
        assertEquals(c1_3o, complement.get(0));
        assertEquals(o5_7c, complement.get(1));
    }

    public void testRelativeComplementEnclosedEndPoint() {
        Interval o3_5o = Interval.open(new Integer(3), new Integer(5));
        Interval c3_5c = Interval.closed(new Integer(3), new Integer(5));
        List complement = o3_5o.complementRelativeTo(c3_5c);
        assertEquals(2, complement.size());
        assertTrue(((Interval) complement.get(0)).includes(new Integer(3)));
    }

    public void testIsSingleElement() {
        assertTrue(o1_1c.isSingleElement());
        assertTrue(c1_1c.isSingleElement());
        assertTrue(c1_1o.isSingleElement());
        assertFalse(c1_10c.isSingleElement());
        assertFalse(o1_1o.isSingleElement());
    }

    public void testEqualsForOnePointIntervals() {
        assertEquals(o1_1c, c1_1o);
        assertEquals(o1_1c, c1_1c);
        assertEquals(c1_1o, c1_1c);
        assertFalse(o1_1c.equals(o1_1o));
    }

    public void testEqualsForEmptyIntervals() {
        assertEquals(c1_10c.emptyOfSameType(), c4_6c.emptyOfSameType());
    }

    public void testRelativeComplementEnclosedOpen() {
        Interval o3_5o = Interval.open(new Integer(3), new Integer(5));
        Interval c1_7c = Interval.closed(new Integer(1), new Integer(7));
        Interval c1_3c = Interval.closed(new Integer(1), new Integer(3));
        Interval c5_7c = Interval.closed(new Integer(5), new Integer(7));
        List complement = o3_5o.complementRelativeTo(c1_7c);
        assertEquals(2, complement.size());
        assertEquals(c1_3c, complement.get(0));
        assertEquals(c5_7c, complement.get(1));
    }

}