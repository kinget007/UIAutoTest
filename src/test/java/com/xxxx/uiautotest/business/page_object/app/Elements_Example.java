package com.xxxx.uiautotest.business.page_object.app;

import com.xxxx.uiautotest.base.PageObjectBase;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by yuyilong on 15/6/4.
 */
public class Elements_Example extends PageObjectBase {

    public Elements_Example(WebDriver driver) {
        super(driver);
    }

    /**
     * 首页－Tab（xxxx)
     */
    @AndroidFindBy(xpath = "xxxx")
    @iOSFindBy(xpath = "xxxx")
    public WebElement TabTest;
}