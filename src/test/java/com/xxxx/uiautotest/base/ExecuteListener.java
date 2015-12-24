package com.xxxx.uiautotest.base;

import com.xxxx.uiautotest.util.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

/**
 * Created by yuyilong on 15/9/24.
 */
public class ExecuteListener implements WebDriverEventListener {
    private By lastFindBy;
    private String originalValue;

    /**
     * Description: overriding method.
     * 内容描述：监听导航事件,打印日志。
     *
     * @param url    url
     * @param driver driver对象
     */
    public void beforeNavigateTo(String url, WebDriver driver) {
        Log.logInfo("WebDriver navigating to:'" + url + "'");
    }

    /**
     * Description: overriding method.
     * 内容描述：监听改变值之前的事件,保存初始值。
     *
     * @param element 页面元素对象
     * @param driver  driver对象
     */
    public void beforeChangeValueOf(WebElement element, WebDriver driver) {
        originalValue = element.getAttribute("value");
    }

    /**
     * Description: overriding method.
     * 内容描述：监听值改变值之后的事件,打印日志。
     *
     * @param element 页面元素对象
     * @param driver  driver对象
     */
    public void afterChangeValueOf(WebElement element, WebDriver driver) {
        Log.logInfo("WebDriver changing value in element found " + lastFindBy + " from '" + originalValue + "' to '" + element.getAttribute("value") + "'");
    }

    /**
     * Description: overriding method.
     * 内容描述：实现接口方法,无实现逻辑。
     */
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        lastFindBy = by;
//        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    /**
     * Description: overriding method.
     * 内容描述：监听产生异常的事件,打印日志。
     *
     * @param error  Throwable对象
     * @param driver driver对象
     */
    public void onException(Throwable error, WebDriver driver) {
        if (error.getClass().equals(NoSuchElementException.class)) {
            Log.logError("WebDriver error: Element not found " + lastFindBy);
        } else {
            Log.logError("WebDriver error: unknown exception");
            Log.logError(error.getStackTrace());
        }
    }

    /**
     * Description: overriding method.
     * 内容描述：实现接口方法,无实现逻辑。
     */
    public void beforeNavigateBack(WebDriver driver) {
    }

    /**
     * Description: overriding method.
     * 内容描述：实现接口方法,无实现逻辑。
     */
    public void beforeNavigateForward(WebDriver driver) {
    }

    /**
     * Description: overriding method.
     * 内容描述：实现接口方法,无实现逻辑。
     */
    public void beforeClickOn(WebElement element, WebDriver driver) {
    }

    /**
     * Description: overriding method.
     * 内容描述：实现接口方法,无实现逻辑。
     */
    public void beforeScript(String script, WebDriver driver) {
    }

    /**
     * Description: overriding method.
     * 内容描述：监听点击事件,打印日志。
     *
     * @param element 页面元素对象
     * @param driver  driver对象
     */
    public void afterClickOn(WebElement element, WebDriver driver) {
        String locator = element.toString().split("-> ")[1];
        Log.logInfo("WebDriver clicking on:'" + locator.substring(0, locator.length() - 1) + "'");
    }

    /**
     * Description: overriding method.
     * 内容描述：实现接口方法,无实现逻辑。
     */
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
    }

    /**
     * Description: overriding method.
     * 内容描述：实现接口方法,无实现逻辑。
     */
    public void afterNavigateBack(WebDriver driver) {
    }

    /**
     * Description: overriding method.
     * 内容描述：实现接口方法,无实现逻辑。
     */
    public void afterNavigateForward(WebDriver driver) {
    }

    /**
     * Description: overriding method.
     * 内容描述：实现接口方法,无实现逻辑。
     */
    public void afterNavigateTo(String url, WebDriver driver) {
    }

    /**
     * Description: overriding method.
     * 内容描述：实现接口方法,无实现逻辑。
     */
    public void afterScript(String script, WebDriver driver) {
    }
}