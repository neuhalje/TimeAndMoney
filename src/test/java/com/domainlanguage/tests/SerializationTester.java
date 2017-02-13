/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;

public class SerializationTester {

    public static void assertCanBeSerialized(Object serializable) throws AssertionFailedError {
        if (!Serializable.class.isInstance(serializable))
            throw new AssertionFailedError("Object doesn't implement java.io.Serializable interface");

        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayIn = null;
        try {
            out = new ObjectOutputStream(byteArrayOut);
            out.writeObject(serializable);
            out.close(); //this shouldn't matter
            byteArrayIn = new ByteArrayInputStream(byteArrayOut.toByteArray());
            in = new ObjectInputStream(byteArrayIn);
            Object deserialized = in.readObject();
            if (!serializable.equals(deserialized))
                throw new AssertionFailedError("Reconstituted object is expected to be equal to serialized");
            in.close(); //this shouldn't matter
        } catch (AssertionFailedError e) {
            throw e;
        } catch (Exception e) {
            Assert.fail("Exception while serializing: " + e);
        }
    }

}