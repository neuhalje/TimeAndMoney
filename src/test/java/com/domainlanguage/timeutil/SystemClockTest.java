/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import com.domainlanguage.time.Duration;
import com.domainlanguage.time.TimePoint;
import com.domainlanguage.time.TimeSource;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;


public class SystemClockTest {
    @Test
    public void testSystemClockTimeSource() {
        // The following calls allow polymorphic substitution of TimeSources
        // either in applications or, more often, in testing.
        TimeSource source = SystemClock.timeSource();
        TimePoint expectedNow = TimePoint.from(new Date());
        TimePoint now = source.now();
        assertTrue(now.until(expectedNow).length().compareTo(
                Duration.milliseconds(50)) < 0);
    }


}