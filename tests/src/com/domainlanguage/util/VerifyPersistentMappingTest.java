/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.util;

import junit.framework.*;

public class VerifyPersistentMappingTest extends TestCase {
    boolean hasFailures;
    
    public void test() throws Exception {
        hasFailures=false;
        ClassGenerator generator=new ClassGenerator(new TimeAndMoneyDomainClassFilter()) {
            protected void next(Class klass) throws Exception {
                PersistentMappingVerification verification;
                verification = PersistentMappingVerification.on(klass);
                if (!verification.isPersistableRequirementsSatisfied()) {
                    hasFailures=true;
                    System.err.println(formatFailure(verification));
                }
            }
        };
        generator.go();
        if (hasFailures) {
            fail("Failed Test. See System.err for details");
        }
    }

    String formatFailure(PersistentMappingVerification verification) {
        return verification.formatFailure();
    }
}
