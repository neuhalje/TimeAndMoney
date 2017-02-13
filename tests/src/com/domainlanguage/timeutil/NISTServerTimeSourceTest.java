/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import junit.framework.TestCase;

import com.domainlanguage.tests.CannedResponseServer;
import com.domainlanguage.time.TimePoint;
import com.domainlanguage.time.TimeSource;

public class NISTServerTimeSourceTest extends TestCase {
    private CannedResponseServer standInNISTServer;

    private static final TimePoint EXPECTED_TIME_POINT = TimePoint.from(1124679473000l);
    private static final String CANNED_RESPONSE = "\n53604 05-08-22 02:57:53 50 0 0 725.6 UTC(NIST) * \n";
    
    public void testNISTTimeSource() throws Exception {
        //This would return a source that goes to the internet       
        //TimeSource source = NISTClient.timeSource();
        TimeSource source = NISTClient.timeSource(standInNISTServer.getHostName(), standInNISTServer.getPort());
        assertEquals(EXPECTED_TIME_POINT, source.now());
    }
    public void testAsTimePoint() {
        assertEquals(EXPECTED_TIME_POINT, NISTClient.asTimePoint(CANNED_RESPONSE));
    }

    public void setUp() throws Exception {
        super.setUp();

        standInNISTServer = new CannedResponseServer(CANNED_RESPONSE);
        standInNISTServer.start();
    }

    public void tearDown() throws Exception {
        super.tearDown();
        standInNISTServer.stop();
    }

}