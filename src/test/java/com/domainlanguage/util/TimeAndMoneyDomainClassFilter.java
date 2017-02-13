/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.util;

import java.util.*;

import junit.framework.*;

public class TimeAndMoneyDomainClassFilter implements ClassFilter {

    public boolean accepts(Class klass) {
        if (isTestCase(klass)) {
            return false;
        }
        if (isInnerClass(klass) && !isTimeAndMoney(klass.getDeclaringClass())) {
            return false;
        }
        if (isTimeAndMoney(klass)) {
            return true;
        }
        return false;
    }
    private boolean isInnerClass(Class klass) {
        return klass.getName().indexOf('$') > -1;
    }
    private boolean isTimeAndMoney(Class klass) {
        if (klass == null)
            return false;
        StringTokenizer parts=new StringTokenizer(klass.getName(), ".");
        boolean result=false;
        while (parts.hasMoreTokens()) {
            String next=parts.nextToken();
            if (next.equals("domainlanguage")) {
                result=true;
            }
            if (next.equals("tests") || next.equals("util") || next.equals("adt")) {
                result=false;
            }
        }
        return result;
    }
    private boolean isTestCase(Class klass) {
        Class superclass=klass.getSuperclass();
        if (superclass == null) {
            return false;
        }
        if (superclass == Object.class) {
            return false;
        }
        if (superclass == TestCase.class) {
            return true;
        }
        return isTestCase(superclass);
    }
}
