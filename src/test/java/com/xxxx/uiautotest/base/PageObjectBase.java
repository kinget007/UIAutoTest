package com.xxxx.uiautotest.base;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by yuyilong on 15/9/21.
 */
public class PageObjectBase {
    public PageObjectBase(WebDriver driver){
        if(driver.getClass().getSimpleName().toLowerCase().contains("android") || driver.getClass().getSimpleName().toLowerCase().contains("ios")){
            PageFactory.initElements(new AppiumFieldDecorator(driver, 6, TimeUnit.SECONDS), this);
        }else{
            PageFactory.initElements(driver,this);
        }
    }
}
