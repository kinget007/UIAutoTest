package com.xxxx.uiautotest.base.operateFactory;

import com.xxxx.uiautotest.util.Log;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by yuyilong on 15/9/22.
 */
public class AndroidOperate extends AppOperate {

    private static AndroidDriver driver;

    public AndroidOperate(AndroidDriver driver) {
        super(driver);
        this.driver = driver;
    }

    /**
     * override original method
     * 按下指定的键,在原生方法执行时添加日志。
     *
     * @param keyEvent the element to be found.
     * @param LogText  input log text.
     */
    public void sendKeyEvent(int keyEvent, String LogText) {
        Log.logStep("[" + LogText + "] ");
        Log.logInfo("[" + LogText + "] ");
        driver.pressKeyCode(keyEvent);
    }

    /**
     * 向左滑屏
     */
    public boolean swipeRightToLeft() {
        boolean isToHomePage = false;
        try {
            sendKeyEvent(AndroidKeyCode.KEYCODE_DPAD_RIGHT, "滑动引导页");
            isToHomePage = true;
        } catch (Exception e) {
            Log.logInfo(e.getStackTrace());
        }
        return isToHomePage;
    }

    public void acceptAlert() {
        Log.logInfo("accept alerts");
        if (waitForText(20, "记住我的选择", "禁止", "允许")) {
            Log.logInfo("点击[允许]");
            driver.findElement(By.name("允许")).click();
        }
    }

    @Override
    public void scrollToUp(String TargetText) {
        scrollTo(TargetText);
    }

    @Override
    public void scrollToDown(String TargetText) {
        scrollTo(TargetText);
    }


    @Override
    public Boolean IdentifyIsDisplay(String[] TargetText, WebElement element) {
        Log.logInfo("等待输入验证码");
        return waitForText(10, TargetText);
    }

    /**
     * 返回至首页。
     */
    @Override
    public void backToHomePage(String[] contents) {
        int times = 0;
        while (!waitForText(5, contents)) {
            if (times >= 30) {
                Log.logInfo("尝试多次未能返回到首页,终止操作!");
                break;
            }
            Log.logInfo("点击返回按钮");
            driver.pressKeyCode(AndroidKeyCode.BACK);
            sendKeyEvent(AndroidKeyCode.BACK, "点击返回按钮");
            times++;
        }
    }
}
