/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

abstract public class Tests {

    static public List allTestCases(TestSuite[] suites) {
        List testCases = new ArrayList();
        LinkedList suiteQueue = new LinkedList(Arrays.asList(suites));
        while (!suiteQueue.isEmpty()) {
            TestSuite suite = (TestSuite) suiteQueue.removeFirst();
            for (Enumeration enumeration = suite.tests(); enumeration.hasMoreElements();) {
                Test each = (Test) enumeration.nextElement();
                if (each instanceof TestSuite)
                    suiteQueue.addLast(each);
                else if (each instanceof TestCase)
                    testCases.add(each);
                else
                    throw new RuntimeException("not a testcase nor a suite: " + each);
            }
        }
        return testCases;
    }

    static public Set allTestCaseNames(TestSuite[] suites) {
        Set names = new HashSet();
        for (Iterator iterator = allTestCases(suites).iterator(); iterator.hasNext();) {
            TestCase each = (TestCase) iterator.next();
            names.add(each.getClass().getName());
        }
        return names;
    }

    static public List allTestNames(TestSuite[] suites) {
        List names = new ArrayList();
        for (Iterator iterator = allTestCases(suites).iterator(); iterator.hasNext();) {
            TestCase each = (TestCase) iterator.next();
            names.add(each.getClass() + "-" + each.getName());
        }
        return names;
    }

}