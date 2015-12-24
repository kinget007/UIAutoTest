package com.xxxx.uiautotest.testsuits.testdata;

import org.testng.annotations.DataProvider;

/**
 * Created by yuyilong on 15/12/24.
 */
public class Example_data {
    @DataProvider(name = "Example_data")
    public static Object[][] loginData_error() {
        return new Object[][] {
                {
                        new String("test1"),
                        new String("test2")
                }
        };
    }
}
