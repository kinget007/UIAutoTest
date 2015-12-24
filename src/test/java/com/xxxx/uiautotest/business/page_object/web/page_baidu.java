package com.xxxx.uiautotest.business.page_object.web;

import com.xxxx.uiautotest.base.PageObjectBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by yuyilong on 15/9/21.
 */
public class page_baidu extends PageObjectBase {

    public page_baidu(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = ".//*[@id='kw']")
    public WebElement searchInput;

    @FindBy(xpath = ".//*[@id='su']")
    public WebElement searchButton;

}
