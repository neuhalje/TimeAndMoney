/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.util;

import org.junit.Test;

import java.util.Iterator;

public class ImmutableIteratorTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        Iterator iterator = new ImmutableIterator() {
            public boolean hasNext() {
                return true;
            }

            public Object next() {
                return null;
            }
        };

        iterator.remove();
    }

}