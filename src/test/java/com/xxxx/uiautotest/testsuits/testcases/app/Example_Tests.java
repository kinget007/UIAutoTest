package com.xxxx.uiautotest.testsuits.testcases.app;

import com.xxxx.uiautotest.base.AutoTestBase;
import com.xxxx.uiautotest.business.page_object.app.Elements_Example;
import com.xxxx.uiautotest.util.Log;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Created by yuyilong on 15/12/24.
 */
public class Example_Tests extends AutoTestBase {
    private Elements_Example elements_example;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        elements_example = new Elements_Example(driver);
    }

    @Parameters({"test"})
    @Test(priority = 1, groups = {"p0"}, alwaysRun = true)
    public void example(String test) throws Throwable {
        Log.logFlow("登录－点击TabTest");
        operateBase.click(elements_example.TabTest, "点击首页上面的[TabTest]按钮");
        Assert.assertEquals("actual","expected","message");
    }
}
