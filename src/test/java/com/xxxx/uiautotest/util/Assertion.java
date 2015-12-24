package com.xxxx.uiautotest.util;

import org.testng.Assert;

public class Assertion {

    public static boolean flag = true;

    /**
     * 比较两个对象是否一致。
     *
     * @param actual   the actual object.
     * @param expected the expected object.
     */
    public static void verifyEquals(Object actual, Object expected) {
        try {
            Assert.assertEquals(actual, expected);
        } catch (Error e) {
            flag = false;
        }
    }

    /**
     * 比较两个对象是否一致。
     *
     * @param actual   the actual object.
     * @param expected the expected object.
     * @param message  the message for description.
     */
    public static void verifyEquals(Object actual, Object expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (Error e) {
            flag = false;
        }
    }

}
