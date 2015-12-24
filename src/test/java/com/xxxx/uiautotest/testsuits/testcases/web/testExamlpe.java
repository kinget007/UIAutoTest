package com.xxxx.uiautotest.testsuits.testcases.web;

import com.xxxx.uiautotest.base.AutoTestBase;
import com.xxxx.uiautotest.business.page_object.web.page_baidu;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by quqing on 15/12/10.
 */
public class testExamlpe extends AutoTestBase {
    public page_baidu elm;

    @BeforeClass(alwaysRun = true)
    public void BeforeClass() {
        elm = new page_baidu(driver);
    }

    @Test(groups = {"p0"})
    public void openbaidu() throws InterruptedException {
        driver.navigate().to("http://www.baidu.com");
        elm.searchInput.sendKeys("hello");
        elm.searchButton.click();
    }

    @Test(groups = {"p0"})
    public void HealthCheck() throws InterruptedException {
        webOperate.testLinksHealth("http://172.18.16.205:8080/api_manage/", 6000);
//        webOperate.testLinksHealth("http://172.18.16.205:8080/api_manage/","HTTP Status [456][06][0-9]",6000);
//        webOperate.testLinksHealth("http://172.18.16.205:8080/api_manage/","HTTP Status [456][06][0-9]|(?i)exception");
    }
}