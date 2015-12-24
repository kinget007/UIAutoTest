package com.xxxx.uiautotest.base.operateFactory;

import com.xxxx.uiautotest.util.Log;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by yuyilong on 15/9/22.
 */
public class IosOperate extends AppOperate {

    private static IOSDriver driver;

    public IosOperate(IOSDriver driver) {
        super(driver);
        this.driver = driver;
    }

    /**
     * 模拟向左滑屏。
     */
    @Override
    public boolean swipeRightToLeft() {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        try {
            driver.swipe(width * 9 / 10, height / 2, width * 1 / 10, height / 2, 1000);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Js模拟滚动至目标文本的位置。
     *
     * @param TargetText the target text to be found.
     */
    public void scrollTo(String TargetText) {
        Log.logStep("[滑动页面以发现'" + TargetText + "'] ");
        Log.logInfo("[滑动页面以发现'" + TargetText + "'] ");
        driver.scrollTo(TargetText);
    }

    @Override
    public void acceptAlert() {
        Log.logInfo("accept alerts");
    }

    /**
     * Wait for element display
     * 模拟等待目标显示,在原生方法执行时添加日志。
     *
     * @param TargetText input log text.
     * @param element    the element wait for display.
     * @return boolean
     */
    @Override
    public Boolean IdentifyIsDisplay(String[] TargetText, WebElement element) {
        Log.logInfo("等待输入验证码");
        return element.isDisplayed();
    }

    /**
     * 模拟向上滑屏。
     */
    @Override
    public void scrollToUp(String TargetText) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        Log.logStep("向上滑动屏幕以发现[" + TargetText + "]");
        driver.swipe(width / 2, height * 3 / 4, width / 2, height / 6, 3000);
    }

    /**
     * 向下方滑动屏幕。
     */
    public void scrollToDown(String TargetText) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        Log.logStep("向下滑动屏幕以发现" + TargetText);
        while (true) {
            if (waitForText(5, new String[]{TargetText})) {
                break;
            }
            driver.swipe(width / 2, height * 9 / 10, width / 2, height * 8 / 10, 1000);
        }
    }

    /**
     * 返回至首页。
     * @param contents  [0]:主页的标示 [1]:返回按钮的标示
     */
    @Override
    public void backToHomePage(String[] contents) {
        int retryTimes;
        boolean notFind;

        while (!waitForText(6, contents[0])) {
            Log.logInfo("点击返回按钮");
            if (waitForText(20, contents[1])) {
                notFind = true;
                retryTimes = 1;
                while (notFind) {
                    Log.logInfo("第" + retryTimes + "次查找[返回]按钮......");
                    if (retryTimes > 3)
                        break;
                    notFind = elementExists(3, By.name(contents[1])) ? false : true;
                    retryTimes++;
                }
                if (!notFind) {
                    driver.findElement(By.name(contents[1])).click();
                } else {
                    Log.logInfo("点击[返回]按钮失败");
                }
            } else {
                break;
            }
        }
    }
}