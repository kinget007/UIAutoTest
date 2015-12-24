package com.xxxx.uiautotest.base;

import com.xxxx.uiautotest.base.operateFactory.AndroidOperate;
import com.xxxx.uiautotest.base.operateFactory.AppOperate;
import com.xxxx.uiautotest.base.operateFactory.IosOperate;
import com.xxxx.uiautotest.base.operateFactory.WebOperate;
import com.xxxx.uiautotest.util.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuyilong on 15/6/2.
 */
public class AutoTestBase {
    protected static WebDriver driver;
    protected static AppOperate operateBase;
    protected static WebOperate webOperate;
    protected String port;
    protected static String date;
    protected static String time;
    protected static int timeout;
    protected static String platformName;
    protected static String udid;
    protected static String browser_name;

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Description: Prepare before Suite.
     * 测试套件运行前准备:启动driver server、driver client，配置相关参数。
     *
     * @param filePath        app的安装包路径
     * @param appName         app的安装包名
     * @param platformName    app平台 android/ios
     * @param platformVersion app运行平台的版本
     * @param deviceName      app设备名称,ios参数,例如:iPhone 6
     * @param appPackage      app包名,android参数,例如:com.pingan.yzt
     * @param appActivity     android参数,例如:com.pingan.yzt.SplashActivityPro
     * @param port            app的服务端口
     * @param udid            app的设备识别符
     * @param timeout         web/app等待超时的时间,单位:秒
     * @param browser_name    web的浏览器类别,例如:chrome
     * @param remote_url      web的远程运行的url,例如:http://172.1.2.3:8888/wd/hub
     */
    @BeforeSuite(alwaysRun = true)
    @Parameters({"filePath", "appName", "platformName", "platformVersion", "deviceName", "appPackage", "appActivity", "port", "udid", "timeout", "browser_name", "remote_url"})
    public void beforeSuite(String filePath, String appName, String platformName, String platformVersion, String deviceName, String appPackage, String appActivity, String port, String udid, int timeout, String browser_name, String remote_url) {
        this.port = port;
        this.timeout = timeout;
        AutoTestBase.platformName = platformName;
        AutoTestBase.udid = udid;
        AutoTestBase.browser_name = browser_name;

        try {
            File dateFile = new File("date.txt");
            if (dateFile.exists()) {
                date = Tools.readAll("date.txt").split(":")[0].trim();
                time = Tools.readAll("date.txt").split(":")[1].trim();
                Log.logInfo("当前日期:" + date);
                Log.logInfo("当前时间:" + time);
            } else {
                Log.logInfo("date.txt 文件不存在！");
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (platformName.toLowerCase().contains("android") || platformName.toLowerCase().contains("ios")) {
            File appDir = new File(filePath);
            File app = new File(appDir, appName);

            StartAppiumServer startAppiumServer = new StartAppiumServer(port, udid);

            System.setProperty("date", date);
            System.setProperty("time", time);
            System.setProperty("deviceID", udid);

            Log.logInfo("MobileCapabilityType.filePath = " + filePath);
            Log.logInfo("MobileCapabilityType.appName = " + appName);
            Log.logInfo("MobileCapabilityType.platformName = " + platformName);
            Log.logInfo("MobileCapabilityType.platformVersion = " + platformVersion);
            Log.logInfo("MobileCapabilityType.deviceName = " + deviceName);
            Log.logInfo("MobileCapabilityType.appPackage = " + appPackage);
            Log.logInfo("MobileCapabilityType.appActivity = " + appActivity);
            Log.logInfo("MobileCapabilityType.port = " + port);
            Log.logInfo("MobileCapabilityType.udid = " + udid);

            startAppiumServer.start();

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, "1.0");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
            capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
            capabilities.setCapability(MobileCapabilityType.UDID, udid);
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, appPackage);
            capabilities.setCapability("unicodeKeyboard", "True");
            capabilities.setCapability("resetKeyboard", "True");
            capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, appActivity);

            if (platformName.toLowerCase().contains("android")) {
                String[] miUdid = StaticConfig.getMiUdid();
                capabilities.setCapability("noSign", true);

                try {
                    if (Tools.isMatch(udid, miUdid)) {   //处理小米安装过程中的安全提示
                        int tapx;
                        int tapy;
                        Map<String, String> miResolution = StaticConfig.getMiResolution();

                        tapx = Integer.parseInt(miResolution.get(udid).split("x")[0]);
                        tapy = Integer.parseInt(miResolution.get(udid).split("x")[1]);
                        HandleSafetyTips handleSafetyTipsOne = new HandleSafetyTips(udid, tapx * 7 / 10, tapy * 95 / 100);
                        HandleSafetyTips handleSafetyTipsTwo = new HandleSafetyTips(udid, tapx * 7 / 10, tapy * 7 / 10);
                        handleSafetyTipsOne.start();
                        driver = new AndroidDriver(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
                        handleSafetyTipsTwo.start();
                    } else {
                        driver = new AndroidDriver(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                operateBase = new AndroidOperate((AndroidDriver) driver);
            } else {
                capabilities.setCapability("autoAcceptAlerts", true);
                capabilities.setCapability("language", "zh-Hans");
                try {
//                    driver = new EventFiringWebDriver(new IOSDriver(new URL("http://127.0.0.1:"+port+"/wd/hub"), capabilities)).register(executeListener);
                    driver = new IOSDriver(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                operateBase = new IosOperate((IOSDriver) driver);
            }
        } else {
            System.setProperty("log.info.file", "web_" + browser_name + ".log");
//            System.setProperty("log.appium.file", "appium_" + browser_name + ".log");
            Log.logInfo("CapabilityType.BROWSER_NAME = " + browser_name);
            Log.logInfo("Remote_url = " + remote_url);

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.BROWSER_NAME, browser_name);

            String driverServer;
            String driverServerPath = "tools/driverServer/";
            String processName = null;
            ExecuteListener executeListener = new ExecuteListener();

            if (browser_name.toLowerCase().contains("chrome")) {
                driverServer = Tools.isWindows() ? "chromedriver.exe" : "chromedriver";
                processName = Tools.isWindows() ? "chrome.exe" : "\"Google Chrome\"";
                System.setProperty("webdriver.chrome.driver", driverServerPath + driverServer);
            } else if (browser_name.toLowerCase().contains("ie")) {
                driverServer = "IEDriverServer.exe";
                processName = "iexplore.exe";
                System.setProperty("webdriver.ie.driver", driverServerPath + driverServer);
            } else if (browser_name.toLowerCase().contains("safari")) {
                driverServer = Tools.isWindows() ? "safari.exe" : "safari";
                processName = Tools.isWindows() ? "safari.exe" : "Safari";
                System.setProperty("webdriver.safari.driver", driverServerPath + driverServer);
            } else if (browser_name.toLowerCase().contains("opera")) {
                driverServer = Tools.isWindows() ? "operadriver.exe" : "operadriver";
                processName = Tools.isWindows() ? "opera.exe" : "Opera";
                System.setProperty("webdriver.opera.driver", driverServerPath + driverServer);
            } else if (browser_name.toLowerCase().contains("firefox")) {
                processName = Tools.isWindows() ? "firefox.exe" : "Firefox";
            }

            Tools.killProcess(processName);

            try {
                if (null != remote_url && !"".equals(remote_url)) {
                    if (browser_name.toLowerCase().contains("chrome")) {
                        driver = new EventFiringWebDriver(new RemoteWebDriver(new URL(remote_url), DesiredCapabilities.chrome())).register(executeListener);
                    } else if (browser_name.toLowerCase().contains("firefox")) {
                        driver = new EventFiringWebDriver(new RemoteWebDriver(new URL(remote_url), DesiredCapabilities.firefox())).register(executeListener);
                    } else if (browser_name.toLowerCase().contains("ie")) {
                        driver = new EventFiringWebDriver(new RemoteWebDriver(new URL(remote_url), DesiredCapabilities.internetExplorer())).register(executeListener);
                    } else if (browser_name.toLowerCase().contains("safari")) {
                        driver = new EventFiringWebDriver(new RemoteWebDriver(new URL(remote_url), DesiredCapabilities.safari())).register(executeListener);
                    } else if (browser_name.toLowerCase().contains("opera")) {
                        driver = new EventFiringWebDriver(new RemoteWebDriver(new URL(remote_url), DesiredCapabilities.opera())).register(executeListener);
                    }
                } else {
                    if (browser_name.toLowerCase().contains("chrome")) {
                        driver = new EventFiringWebDriver(new ChromeDriver()).register(executeListener);
                    } else if (browser_name.toLowerCase().contains("firefox")) {
                        driver = new EventFiringWebDriver(new FirefoxDriver()).register(executeListener);
                    } else if (browser_name.toLowerCase().contains("ie")) {
                        driver = new EventFiringWebDriver(new InternetExplorerDriver()).register(executeListener);
                    } else if (browser_name.toLowerCase().contains("safari")) {
                        driver = new EventFiringWebDriver(new SafariDriver()).register(executeListener);
                    } else if (browser_name.toLowerCase().contains("opera")) {
                        driver = new EventFiringWebDriver(new OperaDriver()).register(executeListener);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            webOperate = new WebOperate(driver);
            driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
        }
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    /**
     * Description: Back to home page after method.
     * app测试方法执行完成后,回到首页。
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        if (platformName.toLowerCase().contains("android")) {
            operateBase.backToHomePage(new String[]{""});
        }else if (platformName.toLowerCase().contains("ios")){
            operateBase.backToHomePage(new String[]{""});
        }
    }

    /**
     * Description: Driver quit after suit.
     * 测试套件执行后关闭driver。
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuit() {
        if (platformName.toLowerCase().contains("android") || platformName.toLowerCase().contains("ios")) {
            ((AppiumDriver) driver).removeApp("com.pingan.yizhangtong");
        }
        driver.quit();
    }

    /**
     * Description: Screen Shot.
     * 实现屏幕截屏功能。
     *
     * @param fileName 截屏文件名
     * @throws IOException IO异常
     */
    public static String ScreenShot(String fileName) throws IOException {
        String filePath_screenShots;
        String filePath_testngReports = "output" + File.separator + date + File.separator + time + File.separator + udid + File.separator + "testngReports" + File.separator;
        if (platformName.toLowerCase().contains("android") || platformName.toLowerCase().contains("ios")) {
            filePath_screenShots = "output" + File.separator + date + File.separator + time + File.separator + udid + File.separator + "screenShots" + File.separator;
        } else {
            filePath_screenShots = "output" + File.separator + date + File.separator + time + File.separator + browser_name + File.separator + "screenShots" + File.separator;
        }
        FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), new File(filePath_screenShots + fileName + ".png"));
        return filePath_testngReports;
    }
}