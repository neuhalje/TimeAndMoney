/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.util;

import java.util.*;

import junit.framework.*;

public class ImmutableIteratorTest extends TestCase {

    public void testRemove() {
        Iterator iterator = new ImmutableIterator() {
            public boolean hasNext() {
                return true;
            }

            public Object next() {
                return null;
            }
        };
        try {
            iterator.remove();
            fail("remove is unsupported");
        } catch (UnsupportedOperationException expected) {
        }
    }

}