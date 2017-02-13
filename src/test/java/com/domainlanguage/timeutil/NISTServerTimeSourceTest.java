/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import com.domainlanguage.tests.CannedResponseServer;
import com.domainlanguage.time.TimePoint;
import com.domainlanguage.time.TimeSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NISTServerTimeSourceTest {
    private CannedResponseServer standInNISTServer;

    private static final TimePoint EXPECTED_TIME_POINT = TimePoint.from(1124679473000l);
    private static final String CANNED_RESPONSE = "\n53604 05-08-22 02:57:53 50 0 0 725.6 UTC(NIST) * \n";

    @Test
    public void testNISTTimeSource() throws Exception {
        //This would return a source that goes to the internet       
        //TimeSource source = NISTClient.timeSource();
        TimeSource source = NISTClient.timeSource(standInNISTServer.getHostName(), standInNISTServer.getPort());
        assertEquals(EXPECTED_TIME_POINT, source.now());
    }

    @Test
    public void testAsTimePoint() {
        assertEquals(EXPECTED_TIME_POINT, NISTClient.asTimePoint(CANNED_RESPONSE));
    }

    @Before
    public void setUp() throws Exception {
        standInNISTServer = new CannedResponseServer(CANNED_RESPONSE);
        standInNISTServer.start();
    }

    @After
    public void tearDown() throws Exception {
        if (standInNISTServer != null) {
            standInNISTServer.stop();
        } ;
    }

}