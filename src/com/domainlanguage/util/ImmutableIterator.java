/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.util;

import java.util.*;

abstract public class ImmutableIterator implements Iterator {

    public void remove() {
        throw new UnsupportedOperationException("sorry, no can do :-(");
    }

}