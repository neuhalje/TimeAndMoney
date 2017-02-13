/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import java.io.*;
import java.net.*;

import com.domainlanguage.time.*;

/**
 * National Institute of Standards and Technology provides an Internet time
 * server.
 */

public class NISTClient {
	static final String SERVER_DEFAULT="time.nist.gov";
	static final int PORT_DEFAULT=13;
	
    public static TimeSource timeSource() {
        return timeSource(SERVER_DEFAULT, PORT_DEFAULT);
    }
    
	protected static TimeSource timeSource(final String serverName, final int port) {
		return new TimeSource() {
			public TimePoint now() {
				try {
					return NISTClient.now(serverName, port);
				} catch (IOException e) {
					throw new RuntimeException(
							"Problem obtaining network time: " + e.getMessage());
				}
			}
		};
	}

	static TimePoint now(String serverName, int port) throws IOException {
		byte buffer[] = new byte[256];
		Socket socket = new Socket(serverName, port);
		try {
			int length = socket.getInputStream().read(buffer);
			String nistTime = new String(buffer, 0, length);
			return asTimePoint(nistTime);
		} finally {
			socket.close();
		}
	}

    protected static TimePoint asTimePoint(String nistRawFormattedString) {
        String nistGist = nistRawFormattedString.substring(7, 24);
        String pattern = "y-M-d HH:mm:ss";
        return TimePoint.parseGMTFrom(nistGist, pattern);
    }
}