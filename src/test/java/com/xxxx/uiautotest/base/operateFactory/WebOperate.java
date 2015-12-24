package com.xxxx.uiautotest.base.operateFactory;

import com.xxxx.uiautotest.util.Log;
import com.xxxx.uiautotest.util.Tools;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by quqing on 15/9/24.
 */
public class WebOperate {
    int maxWaitfor = 30;
    int stepTimeUnit = 3000;
    protected static WebDriver driver;
    protected static Actions actionDriver;
    protected static By tabFinder = null;
    protected static WebDriverOperate webTable = null;

    public final static String CLICK_BY_JAVASCRIPT = "return arguments[0].click();";

    public final static String ENSRUE_BEFORE_ALERT = "window.alert = function() {}";

    public final static String ENSURE_BEFORE_WINCLOSE = "window.close = function(){ window.opener=null; "
            + "window.open('','_self'); window.close();}";

    public final static String ENSURE_BEFORE_CONFIRM = "window.confirm = function() {return true}";

    public final static String DISMISS_BEFORE_CONFIRM = "window.confirm = function() {return false}";

    public final static String ENSURE_BEFORE_PROMPT = "window.prompt = function() {return true}";

    public final static String DISMISS_BEFORE_PROMPT = "win = \"w.prompt = function() {return false}";

    public final static String BROWSER_READY_STATUS = "return document.readyState";

    public final static String IS_AJAX_ACTIVE = "return Ajax.activeRequestCount";

    public final static String IS_JQUERY_ACTIVE = "return jQuery.active";

    public final static String MAKE_ELEMENT_UNHIDDEN = "arguments[0].style.visibility "
            + "= 'visible'; arguments[0].style.height = '1px'; "
            + "arguments[0].style.width = '1px'; arguments[0].style.opacity = 1";

    public WebOperate(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * 判断页面所有的链接是否健康,判断条件 http status 400~469 500~569 600~669
     *
     * @param url     被测网页地址
     * @param waitFor 每次加载页面的最长等待时间
     * @return 匹配的字符串
     * @author quqing
     */
    public void testLinksHealth(String url, long... waitFor) {
        boolean isHealthPage;
        String href;
        String pageSource;
        String hasFind;
        List<String> hrefList = new ArrayList<String>();
        Map<String, String> actualResultMap = new LinkedHashMap<String, String>();
        Map<String, String> expectedResultMap = new LinkedHashMap<String, String>();

        try {
            driver.navigate().to(url);
            Thread.sleep(6000);
            List<WebElement> links = driver.findElements(By.xpath("//a"));
            Log.logInfo("links numbers -> " + links.size());

            for (WebElement link : links) {
                if (null != link.getText() && !"".equals(link.getText())) {
                    href = link.getAttribute("href");
                    if (href.startsWith("http:") || href.startsWith("https:") || href.startsWith("/")) {
                        hrefList.add(href + "!=!" + link.getText());
                        Log.logInfo(href);
                    }
                }
            }

            for (String sUrl : hrefList) {
                driver.get(sUrl.split("!=!")[0]);
                if (null != waitFor && waitFor.length > 0)
                    Thread.sleep(waitFor[0]);
                pageSource = driver.getPageSource();
                hasFind = Tools.findSubString(pageSource);
                Log.logInfo(sUrl.split("!=!")[0] + " -> " + sUrl.split("!=!")[1]);
                Log.logInfo("Page contains exception information -> " + hasFind);
                expectedResultMap.put(sUrl.split("!=!")[0], null);
                actualResultMap.put(sUrl.split("!=!")[0], hasFind);
                isHealthPage = (null == hasFind) ? true : false;
                Log.logInfo("Whether it is healthy page -> " + isHealthPage);
            }

            Assert.assertEquals(actualResultMap, expectedResultMap);
        } catch (Exception e) {
            Log.logError(e.getStackTrace());
            e.printStackTrace();
        }
    }

    /**
     * 查找自定义条件的字符串,判断条件用正则表达式描述
     *
     * @param url     被测网页地址
     * @param waitFor 每次加载页面的最长等待时间
     * @param regx    正则表达式（例如:匹配包含换行符（回车）的任意字符串的正则表达式：[\\s\\S]*? ）
     * @return 匹配的字符串
     * @author quqing
     */
    public void testLinksHealth(String url, String regx, long... waitFor) {
        boolean isHealthPage;
        String href;
        String pageSource;
        String hasFind;
        List<String> hrefList = new ArrayList<String>();
        Map<String, String> actualResultMap = new LinkedHashMap<String, String>();
        Map<String, String> expectedResultMap = new LinkedHashMap<String, String>();

        try {
            driver.navigate().to(url);
            Thread.sleep(6000);
            List<WebElement> links = driver.findElements(By.xpath("//a"));
            Log.logInfo("links numbers -> " + links.size());

            for (WebElement link : links) {
                if (null != link.getText() && !"".equals(link.getText())) {
                    href = link.getAttribute("href");
                    if (href.startsWith("http:") || href.startsWith("https:") || href.startsWith("/")) {
                        hrefList.add(href + "!=!" + link.getText());
                        Log.logInfo(href);
                    }
                }
            }

            for (String sUrl : hrefList) {
                driver.get(sUrl.split("!=!")[0]);
                if (null != waitFor && waitFor.length > 0)
                    Thread.sleep(waitFor[0]);
                pageSource = driver.getPageSource();
                hasFind = Tools.findSubString(pageSource, regx);
                Log.logInfo(sUrl.split("!=!")[0] + " -> " + sUrl.split("!=!")[1]);
                Log.logInfo("Page contains exception information -> " + hasFind);
                expectedResultMap.put(sUrl.split("!=!")[0], null);
                actualResultMap.put(sUrl.split("!=!")[0], hasFind);
                isHealthPage = (null == hasFind) ? true : false;
                Log.logInfo("Whether it is healthy page -> " + isHealthPage);
            }

            Assert.assertEquals(actualResultMap, expectedResultMap);
        } catch (Exception e) {
            Log.logError(e.getStackTrace());
            e.printStackTrace();
        }
    }

    /**
     * alert框中点击“确定”按钮
     */
    public void acceptAlert() {
        Log.logInfo("确定alert框");
        Alert alert = this.driver.switchTo().alert();
        alert.accept();
        driver.switchTo().defaultContent();
    }

    /**
     * alert框中点击“取消”按钮
     */
    public void dismissAlert() {
        Log.logInfo("取消alert框");
        Alert alert = this.driver.switchTo().alert();
        alert.dismiss();
        driver.switchTo().defaultContent();
    }

    /**
     * Description: clear error handles does not actruely.
     * 清理掉实际上并不存在的窗口句柄缓存。
     *
     * @param windowHandles the window handles Set.
     * @return Set
     */
    public Set<String> clearHandleCache(Set<String> windowHandles) {
        List<String> errors = new ArrayList<String>();
        for (String handle : windowHandles) {
            try {
                driver.switchTo().window(handle);
                driver.getTitle();
            } catch (Exception e) {
                Log.logError("window handle " + handle + " does not exist acturely!");
                errors.add(handle);
                Log.logError(e.getStackTrace());
            }
        }
        for (int i = 0; i < errors.size(); i++) {
            windowHandles.remove(errors.get(i));
        }
        return windowHandles;
    }

    /**
     * switch to window by title
     * 按照网页标题选择窗口，标题内容需要全部匹配。
     *
     * @param windowTitle the title of the window to be switched to
     */
    public void selectWindow(String windowTitle) {
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles = clearHandleCache(windowHandles);
        for (String handle : windowHandles) {
            driver.switchTo().window(handle);
            String title = driver.getTitle();
            if (windowTitle.equals(title)) {
                Log.logInfo("switch to window [ " + windowTitle + " ]");
                driver.switchTo().defaultContent();
                return;
            }
        }
        Log.logError("there is no window named [ " + windowTitle + " ]");
    }

    /**
     * switch to new window
     */
    public void switchWindow() {
        Log.logInfo("切换到新开窗口");
        String nowHandle = this.driver.getWindowHandle();
        Set<String> handles = this.driver.getWindowHandles();
        for (String handle : handles) {
            if (handle != nowHandle) {
                this.driver.switchTo().window(handle);
            }
        }
    }

    /**
     * 切换到指定handle窗口
     *
     * @param handle 窗口句柄
     */
    public void swithcWindow(String handle) {
        Log.logInfo("切换到handle为：" + handle + "的窗口");
        this.driver.switchTo().window(handle);
    }

    /**
     * switch to new window supporting, by deleting first hanlder
     * 选择最新弹出的窗口，需要预存第一个窗口的handle。
     *
     * @param firstHandle the first window handle
     */
    public void selectNewWindow(String firstHandle) {
        Set<String> handles = null;
        Iterator<String> it = null;
        handles = driver.getWindowHandles();
        handles.remove(firstHandle);
        it = handles.iterator();
        while (it.hasNext()) {
            driver.switchTo().window(it.next());
        }
        driver.switchTo().defaultContent();
        Log.logInfo("switch to new window");
    }

    /**
     * switch to new window supporting, by deleting original hanlde
     * 选择最新弹出的窗口，需要预存所有已有窗口的handles。
     *
     * @param originalHandles the old window handles
     */
    public void selectNewWindow(Set<String> originalHandles) {
        Set<String> newHandles = driver.getWindowHandles();
        Iterator<String> olds = originalHandles.iterator();
        while (olds.hasNext()) {
            newHandles.remove(olds.next());
        }
        Iterator<String> news = newHandles.iterator();
        while (news.hasNext()) {
            driver.switchTo().window(news.next());
        }
        driver.switchTo().defaultContent();
        Log.logInfo("switch to new window");
    }

    /**
     * Description: wait milliseconds.
     * 内容描述：进程等待，尽量避免使用。
     *
     * @param thread the thread to wait for other event.
     * @param millis time to wait, in millisecond
     */
    public void waitFor(Thread thread, long millis) {
        try {
            thread.join(millis);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Description: wait milliseconds.
     * 内容描述：进程等待，尽量避免使用。
     *
     * @param millis time to wait, in millisecond
     */
    public void waitFor(long millis) {
        waitFor(Thread.currentThread(), millis);
    }

    /**
     * judge if the browser is existing, using part of the page title
     * 按照网页标题判断页面是否存在，标题可使用部分内容匹配。
     *
     * @param browserTitle part of the title to see if browser exists
     * @return boolean
     */
    public boolean browserExists(String browserTitle) {
        try {
            String defaultHandle = driver.getWindowHandle();
            Set<String> windowHandles = driver.getWindowHandles();
            windowHandles = clearHandleCache(windowHandles);
            for (int i = 0; i <= 20; i++) {
                waitFor(500);
                if (driver.getWindowHandles().equals(windowHandles)) {
                    break;
                }
                if (i == 20 && !driver.getWindowHandles().equals(windowHandles)) {
                    return false;
                }
            }
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
                if (driver.getTitle().contains(browserTitle)) {
                    driver.switchTo().window(defaultHandle);
                    return true;
                }
            }
            driver.switchTo().window(defaultHandle);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * judge if the browser is present by title reg pattern in specified seconds
     * 在指定时间内按照网页标题判断页面是否存在，标题可使用部分内容匹配。
     *
     * @param browserTitle part of the title to see if browser exists
     * @param seconds      timeout in seconds
     * @return boolean
     */
    public boolean browserExists(String browserTitle, int seconds) {
        long start = System.currentTimeMillis();
        boolean isExist = false;
        while (!isExist && (System.currentTimeMillis() - start) < seconds * maxWaitfor) {
            isExist = browserExists(browserTitle);
        }
        return isExist;
    }

    /**
     * switch to window by title
     * 按照网页标题选择窗口，标题内容需要全部匹配，超时未出现则报错。
     *
     * @param windowTitle the title of the window to be switched to.
     * @param timeout     time to wait for the window appears, unit of seconds.
     */
    public void selectWindowWithTimeout(String windowTitle, int timeout) {
        Assert.assertTrue(browserExists(windowTitle, timeout), "window is not present after " + timeout + "seconds!");
        selectWindow(windowTitle);
    }

    /**
     * wait for alert disappears in the time unit of seconds
     * 在指定时间内等待，对话框（Dialog）消失，用以缓冲运行，增加健壮性。
     *
     * @param seconds time for wait, unit:second
     * @return boolean
     */
    public boolean waitForAlertDisappear(int seconds) {
        long start = System.currentTimeMillis();
        boolean exists = true;
        while ((System.currentTimeMillis() - start) < seconds * maxWaitfor) {
            try {
                driver.switchTo().alert();
            } catch (NoAlertPresentException ne) {
                exists = false;
                break;
            }
        }
        Assert.assertFalse(exists, "alert does not disappear in " + seconds + " seconds!");
        return exists;
    }

    /**
     * switch to parent window when child was closed unexpectly.
     * 在打开的子窗口被意外（被动、非工具预期的行为）关闭之后，切换回父窗口。
     *
     * @param handles      handles set when child windows are still alive.
     * @param childHandle  child window whitch to be closed.
     * @param parentHandle the parent handle of windows.
     */
    public void selectParentWindow(Set<String> handles, String childHandle, String parentHandle) {
        if (!handles.toString().contains(childHandle) || !handles.toString().contains(parentHandle)) {
            throw new IllegalArgumentException("you are using the wrong parameters!");
        }
        handles.remove(childHandle);
        driver.switchTo().window(parentHandle);
        waitForAlertDisappear(5);
    }

    /**
     * Description: switch to a window handle that exists now.
     * 切换到一个存在句柄（或者说当前还存在的）的窗口。
     */
    public void selectExistWindow() {
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles = clearHandleCache(windowHandles);
        String exist_0 = windowHandles.toArray()[0].toString();
        Assert.assertNotNull(exist_0);
        driver.switchTo().window(exist_0);
        Log.logInfo("switched to default window [ " + exist_0 + " ].");
    }

    /**
     * close window by window title and its index if has the same title, by string full pattern
     * 按照网页标题选择并且关闭窗口，重名窗口按照指定的重名的序号关闭，标题内容需要全部匹配。
     *
     * @param windowTitle the title of the window to be closed.
     * @param index       the index of the window which shared the same title, begins
     *                    with 1.
     */
    public void closeWindow(String windowTitle, int index) {
        List<String> winList = new ArrayList<String>();
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles = clearHandleCache(windowHandles);
        for (String handle : windowHandles) {
            driver.switchTo().window(handle);
            if (windowTitle.equals(driver.getTitle())) {
                winList.add(handle);
            }
        }
        driver.switchTo().window(winList.get(index - 1));
        driver.switchTo().defaultContent();
        driver.close();
        selectExistWindow();
        Log.logInfo("window [ " + windowTitle + " ] closed by index [" + index + "]");
    }

    /**
     * close the last window by the same window title, by string full pattern
     * 按照网页标题选择窗口，适用于无重名的窗口，标题内容需要全部匹配。
     *
     * @param windowTitle the title of the window to be closed.
     */
    public void closeWindow(String windowTitle) {
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles = clearHandleCache(windowHandles);
        for (String handle : windowHandles) {
            driver.switchTo().window(handle);
            if (windowTitle.equals(driver.getTitle())) {
                driver.switchTo().defaultContent();
                driver.close();
                break;
            }
        }
        selectExistWindow();
        Log.logInfo("window [ " + windowTitle + " ] closed ");
    }

    /**
     * close windows except specified window title, by string full pattern
     * 关闭除了指定标题页面之外的所有窗口，适用于例外窗口无重名的情况，标题内容需要全部匹配。
     *
     * @param windowTitle the title of the window not to be closed
     */
    public void closeWindowExcept(String windowTitle) {
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles = clearHandleCache(windowHandles);
        for (String handle : windowHandles) {
            driver.switchTo().window(handle);
            String title = driver.getTitle();
            if (!windowTitle.equals(title)) {
                driver.switchTo().defaultContent();
                driver.close();
            }
        }
        selectExistWindow();
        Log.logInfo("all windows closed except [ " + windowTitle + " ]");
    }

    /**
     * close windows except specified window hanlde, by string full pattern
     * 关闭除了指定句柄之外的所有窗口。
     *
     * @param windowHandle the hanlde of the window not to be closed.
     */
    public void closeWindowExceptHandle(String windowHandle) {
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles = clearHandleCache(windowHandles);
        for (String handle : windowHandles) {
            if (!windowHandle.equals(handle)) {
                driver.switchTo().window(handle);
                driver.switchTo().defaultContent();
                driver.close();
            }
        }
        driver.switchTo().window(windowHandle);
        Log.logInfo("all windows closed except handle [ " + windowHandle + " ]");
    }

    /**
     * close windows except specified window title, by string full pattern
     * 关闭除了指定标题页面之外的所有窗口，例外窗口如果重名，按照指定的重名顺序关闭，标题内容需要全部匹配。
     *
     * @param windowTitle the title of the window not to be closed
     * @param index       the index of the window to keep shared the same title with
     *                    others, begins with 1.
     */
    public void closeWindowExcept(String windowTitle, int index) {
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles = clearHandleCache(windowHandles);
        for (String handle : windowHandles) {
            driver.switchTo().window(handle);
            String title = driver.getTitle();
            if (!windowTitle.equals(title)) {
                driver.switchTo().defaultContent();
                driver.close();
            }
        }

        Object[] winArray = driver.getWindowHandles().toArray();
        winArray = driver.getWindowHandles().toArray();
        for (int i = 0; i < winArray.length; i++) {
            if (i + 1 != index) {
                driver.switchTo().defaultContent();
                driver.close();
            }
        }
        selectExistWindow();
        Log.logInfo("keep only window [ " + windowTitle + " ] by title index [ " + index + " ]");
    }

    /**
     * wait for new window which has no title in few seconds
     * 判断在指定的时间内是否有新的窗口弹出，无论其是否有标题。
     *
     * @param browserCount windows count before new window appears.
     * @param seconds      time unit in seconds.
     * @return boolean
     */
    public boolean isNewWindowExits(int browserCount, int seconds) {
        Set<String> windowHandles = null;
        boolean isExist = false;
        long begins = System.currentTimeMillis();
        while ((System.currentTimeMillis() - begins < seconds * maxWaitfor) && !isExist) {
            windowHandles = driver.getWindowHandles();
            windowHandles = driver.getWindowHandles();
            isExist = (windowHandles.size() > browserCount) ? true : false;
        }
        return isExist;
    }

    /**
     * wait for new window which has no title in few seconds
     * 判断在指定的时间内是否有新的窗口弹出，无论其是否有标题。
     *
     * @param oldHandles windows handle Set before new window appears.
     * @param seconds    time unit in seconds.
     * @return boolean
     */
    public boolean isNewWindowExits(Set<String> oldHandles, int seconds) {
        boolean isExist = false;
        Set<String> windowHandles = null;
        long begins = System.currentTimeMillis();
        while ((System.currentTimeMillis() - begins < seconds * maxWaitfor) && !isExist) {
            windowHandles = driver.getWindowHandles();
            windowHandles = driver.getWindowHandles();
            isExist = (windowHandles.size() > oldHandles.size()) ? true : false;
        }
        return isExist;
    }

    /**
     * Description: set element locate timeout.
     * 内容描述：设置对象查找超时时间.
     *
     * @param seconds timeout in timeunit of seconds.
     */
    public void setElementLocateTimeout(int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    /**
     * wait for and switch to frame when avilable in timeout setting
     * 在指定时间内等待，直到指定框架出现并且选择他。
     *
     * @param locator the id or name of frames.
     * @param seconds timeout in seconds
     * @return boolean
     */
    public boolean waitForAndSwitchToFrame(String locator, int seconds) {
        try {
            setElementLocateTimeout(seconds);
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator)) != null;
        } finally {
            setElementLocateTimeout(maxWaitfor);
        }
    }

    /**
     * wait for the element visiable in timeout setting
     * 在指定时间内等待，直到对象可见。
     *
     * @param element the element to be found.
     * @param seconds timeout in seconds.
     * @return boolean
     */
    public boolean waitForElementVisible(WebElement element, int seconds) {
        try {
            setElementLocateTimeout(seconds);
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            return wait.until(ExpectedConditions.visibilityOf(element)) != null;
        } finally {
            setElementLocateTimeout(maxWaitfor);
        }
    }

    /**
     * wait for the element present in timeout setting
     * 在指定时间内等待，直到对象出现在页面上。
     *
     * @param by      the element locator.
     * @param seconds timeout in seconds.
     * @return boolean
     */
    public boolean waitForElementPresent(By by, int seconds) {
        try {
            setElementLocateTimeout(seconds);
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            return wait.until(ExpectedConditions.presenceOfElementLocated(by)) != null;
        } finally {
            setElementLocateTimeout(maxWaitfor);
        }
    }

    /**
     * wait for the element visiable in timeout setting
     * 在指定时间内等待，直到对象可见。
     *
     * @param by      the element locator By
     * @param seconds timeout in seconds
     * @return boolean
     */
    public boolean waitForElementVisible(By by, int seconds) {
        try {
            setElementLocateTimeout(seconds);
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by)) != null;
        } finally {
            setElementLocateTimeout(maxWaitfor);
        }
    }

    /**
     * select a frame by name or id, throw exception when timeout.
     * 按照名称或者ID选择框架（frame），在指定时间内frame不存在则报错。
     *
     * @param nameOrId the name or id of the frame to select.
     * @param timeout  time to wait for the frame available, unit of seconds.
     */
    public void selectFrameWithTimeout(String nameOrId, int timeout) {
        waitForAndSwitchToFrame(nameOrId, timeout);
        Log.logInfo("select frame by name or id [ " + nameOrId + " ]");
    }

    /**
     * execute js functions to do something
     * 使用remote webdriver执行JS函数。
     *
     * @param js     js function string
     * @param report text content to be reported
     * @param args   js execute parameters
     */
    public void jsExecutor(String js, String report, Object args) {
        ((JavascriptExecutor) driver).executeScript(js, args);
        Log.logInfo(report);
    }

    /**
     * forcely click, by executing javascript
     * 在等到对象可见之后点击指定的对象，使用JavaScript执行的方式去操作，
     * 这种方法使用过后一般需要调用一次selectDefaultWindowFrame以确保运行稳定。
     *
     * @param element the webelement you want to operate
     */
    public void clickByJavaScript(WebElement element) {
        waitForElementVisible(element, maxWaitfor);
        jsExecutor(CLICK_BY_JAVASCRIPT, "click on element", element);
    }

    /**
     * rewrite the findElements method, adding user defined log
     * 按照指定的定位方式寻找象。
     *
     * @param by the locator of the elements to be find
     * @return the webelements you want to find
     */
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    /**
     * rewrite the findElement method, adding user defined log
     * 按照指定的定位方式寻找象。
     *
     * @param by the locator of the element to be find
     * @return the first element accord your locator
     */
    public WebElement findElement(By by) {
        List<WebElement> elements = findElements(by);
        return (elements.size() > 0) ? (elements.get(0)) : null;
    }

    /**
     * forcely click, by executing javascript
     * 在等到对象可见之后点击指定的对象，使用JavaScript执行的方式去操作，
     * 这种方法使用过后一般需要调用一次selectDefaultWindowFrame以确保运行稳定。
     *
     * @param by the locator you want to find the element
     */
    public void jsClick(By by) {
        waitForElementVisible(by, maxWaitfor);
        jsExecutor(CLICK_BY_JAVASCRIPT, "click on element [ " + by.toString() + " ] ",
                findElement(by));
    }

    /**
     * doubleclick on the element to be find by By
     * 在等到对象可见之后双击指定的对象.
     *
     * @param element the webelement you want to operate
     */
    public void doubleClick(WebElement element) {
        waitForElementVisible(element, maxWaitfor);
        actionDriver.doubleClick(element);
        actionDriver.perform();
        Log.logInfo("doubleClick on element ");
    }

    /**
     * 模拟按键按下
     *
     * @param key 键盘描述
     * @param by  the locator you want to find the element
     */
    public void keyDown(Keys key, By by) {
        Log.logInfo("按下按键： " + by.toString());
        actionDriver.keyDown(key).perform();
    }

    /**
     * 模拟按键弹起
     *
     * @param key 键盘描述
     * @param by  the locator you want to find the element
     */
    public void keyUp(Keys key, By by) {
        Log.logInfo("按键弹起： " + by.toString());
        actionDriver.keyDown(key).perform();
    }

    /**
     * 鼠标左键点击住不放
     *
     * @param by the locator you want to find the element
     */
    public void clickAndHold(By by) {
        Log.logInfo("左键点击住不放： " + by.toString());
        actionDriver.clickAndHold(findElement(by)).perform();
    }

    /**
     * 鼠标左键弹起
     *
     * @param by the locator you want to find the element
     */
    public void release(By by) {
        Log.logInfo("鼠标左键弹起： " + by.toString());
        actionDriver.release(findElement(by)).perform();
    }

    /**
     * 鼠标移动到元素的中心点
     *
     * @param by the locator you want to find the element
     */
    public void moveToElement(By by) {
        Log.logInfo("鼠标移动到元素： " + by.toString());
        actionDriver.moveToElement(findElement(by)).perform();
    }

    /**
     * doubleclick on the element
     * 在等到对象可见之后双击指定的对象.
     *
     * @param by the locator you want to find the element
     */
    public void doubleClick(By by) {
        waitForElementVisible(by, maxWaitfor);
        actionDriver.doubleClick(findElement(by));
        actionDriver.perform();
        Log.logInfo("doubleClick on element [ " + by.toString() + " ] ");
    }

    /**
     * 拖拽
     *
     * @param source        the source locator you want to find the element
     * @param targetElement the target webelement you want to operate
     */
    public void dragAndDrop(By source, WebElement targetElement) {
        Log.logInfo("鼠标拖拽： " + source.toString() + "到" + targetElement);
        actionDriver.dragAndDrop(findElement(source), targetElement).perform();
    }

    /**
     * right click on the element to be find by By
     * 在等到对象可见之后鼠标右键点击指定的对象.
     *
     * @param element the webelement you want to operate
     */
    public void rightClick(WebElement element) {
        waitForElementVisible(element, maxWaitfor);
        actionDriver.contextClick(element);
        actionDriver.perform();
        Log.logInfo("rightClick on element ");
    }

    /**
     * right click on the element
     * 在等到对象可见之后鼠标右键点击指定的对象。
     *
     * @param by the locator you want to find the element
     */
    public void rightClick(By by) {
        waitForElementVisible(by, maxWaitfor);
        actionDriver.contextClick(findElement(by));
        actionDriver.perform();
        Log.logInfo("rightClick on element [ " + by.toString() + " ] ");
    }

    /**
     * rewrite the submit method, adding user defined log
     * 在等到指定对象可见之后在该对象上做确认/提交的操作。
     *
     * @param element the webelement you want to operate
     */
    public void submit(WebElement element) {
        waitForElementVisible(element, maxWaitfor);
        element.submit();
        Log.logInfo("submit on element");
    }

    /**
     * rewrite the submit method, submit on the element to be find by By
     * 在等到指定对象可见之后在该对象上做确认/提交的操作。
     *
     * @param by the locator you want to find the element
     */
    public void submit(By by) {
        waitForElementVisible(by, maxWaitfor);
        findElement(by).submit();
        Log.logInfo("submit on element [ " + by.toString() + " ]");
    }

    /**
     * execute js functions to do something
     * 使用remote webdriver执行JS函数。
     *
     * @param js     js function string
     * @param report text content to be reported
     */
    public void jsExecutor(String js, String report) {
        ((JavascriptExecutor) driver).executeScript(js);
        Log.logInfo(report);
    }

    /**
     * readonly text box or richtext box input
     * 使用DOM（Documnet Object Modal）修改页面中对象的文本属性值，使用ID定位对象则返回唯一对象，其余返回数组。
     *
     * @param by      the attribute of the element, default support is
     *                TagName/Name/Id
     * @param byValue the attribute value of the element
     * @param text    the text you want to input to element
     * @param index   the index of the elements shared the same attribute value
     * @throws IllegalArgumentException 非法参数异常
     */
    public void sendKeysByDOM(String by, String byValue, String text, int index) {
        String js = null;

        if (by.equalsIgnoreCase("tagname")) {
            js = "document.getElementsByTagName('" + byValue + "')[" + index + "].value='" + text + "'";
        } else if (by.equalsIgnoreCase("name")) {
            js = "document.getElementsByName('" + byValue + "')[" + index + "].value='" + text + "'";
        } else if (by.equalsIgnoreCase("id")) {
            js = "document.getElementById('" + byValue + "').value='" + text + "'";
        } else {
            throw new IllegalArgumentException("only can find element by TagName/Name/Id");
        }

        jsExecutor(js, "input text [ " + text + " ] to element [ " + by + " ]");
    }

    /**
     * readonly text box or richtext box input, finding elements by element id
     * 按照ID定位页面中对象，并使用DOM（Documnet Object Modal）修改其文本属性值。
     *
     * @param elementId the id of the element
     * @param text      the text you want to input to element
     */
    public void sendKeysById(String elementId, String text) {
        sendKeysByDOM("Id", elementId, text, 0);
    }

    /**
     * readonly text box or richtext box input, finding elements by element name
     * 按照名称（Name）和序号定位页面中对象，并使用DOM（Documnet Object Modal）修改其文本属性值。
     *
     * @param elementName  the name of the element
     * @param text         the text you want to input to element
     * @param elementIndex the index of the elements shared the same name, begins with 0
     */
    public void sendKeysByName(String elementName, String text, int elementIndex) {
        sendKeysByDOM("Name", elementName, text, elementIndex);
    }

    /**
     * readonly text box or richtext box input, finding elements by element tag name
     * 按照标签名称（TagName）和序号定位页面中对象，并使用DOM（Documnet Object
     * Modal）修改其文本属性值。
     *
     * @param elementTagName the tag name of the element
     * @param text           the text you want to input to element
     * @param elementIndex   the index of the elements shared the same tag name, begins
     *                       with 0
     */
    public void sendKeysByTagName(String elementTagName, String text, int elementIndex) {
        sendKeysByDOM("TagName", elementTagName, text, elementIndex);
    }

    /**
     * sendKeys by using keybord event on element
     * 使用键盘模拟的方法在指定的对象上输入指定的文本。
     *
     * @param element the webelement you want to operate
     * @param text    the text you want to input to element
     */
    public void sendKeysByKeybord(WebElement element, String text) {
        waitForElementVisible(element, maxWaitfor);
        actionDriver.sendKeys(element, text);
        actionDriver.perform();
        Log.logInfo("send text [ " + text + " ] to WebEdit");
    }

    /**
     * sendKeys by using keybord event on element to be found by By
     * 使用键盘模拟的方法在指定的对象上输入指定的文本。
     *
     * @param by   the locator you want to find the element
     * @param text the text you want to input to element
     */
    public void sendKeysByKeybord(By by, String text) {
        waitForElementVisible(by, maxWaitfor);
        actionDriver.sendKeys(findElement(by), text);
        actionDriver.perform();
        Log.logInfo("input text [ " + text + " ] to element [ " + by.toString() + " ]");
    }

    /**
     * edit rich text box created by kindeditor
     * 使用JS调用KindEditor对象本身的接口，在页面KindEditor对象中输入指定的文本。
     *
     * @param editorId kindeditor id
     * @param text     the text you want to input to element
     */
    public void sendKeysOnKindEditor(String editorId, String text) {
        String javascript = "KE.html('" + editorId + "','<p>" + text + "</p>');";
        jsExecutor(javascript, "input text [ " + text + " ] to kindeditor");
    }

    /**
     * select an item from a picklist by index
     * 按照指定序号选择下拉列表中的选项。
     *
     * @param element the picklist element
     * @param index   the index of the item to be selected
     */
    public void selectByIndex(WebElement element, int index) {
        waitForElementVisible(element, maxWaitfor);
        Select select = new Select(element);
        select.selectByIndex(index);
        Log.logInfo("item selected by index [ " + index + " ]");
    }

    /**
     * select an item from a picklist by index
     * 按照指定序号选择下拉列表中的选项。
     *
     * @param by    the locator you want to find the element
     * @param index the index of the item to be selected
     */
    public void selectByIndex(By by, int index) {
        waitForElementVisible(by, maxWaitfor);
        Select select = new Select(findElement(by));
        select.selectByIndex(index);
        Log.logInfo("item selected by index [ " + index + " ] on [ " + by.toString() + " ]");
    }

    /**
     * select an item from a picklist by item value
     * 按照指定选项的实际值（不是可见文本值，而是对象的“value”属性的值）选择下拉列表中的选项。
     *
     * @param element   the picklist element
     * @param itemValue the item value of the item to be selected
     */
    public void selectByValue(WebElement element, String itemValue) {
        waitForElementVisible(element, maxWaitfor);
        Select select = new Select(element);
        select.selectByValue(itemValue);
        Log.logInfo("item selected by item value [ " + itemValue + " ]");
    }

    /**
     * select an item from a picklist by item value
     * 按照指定选项的实际值（不是可见文本值，而是对象的“value”属性的值）选择下拉列表中的选项。
     *
     * @param by        the locator you want to find the element
     * @param itemValue the item value of the item to be selected
     */
    public void selectByValue(By by, String itemValue) {
        waitForElementVisible(by, maxWaitfor);
        Select select = new Select(findElement(by));
        select.selectByValue(itemValue);
        Log.logInfo("item selected by item value [ " + itemValue + " ] on [ " + by.toString() + " ]");
    }

    /**
     * select an item from a picklist by item value
     * 按照指定选项的可见文本值（用户直接可以看到的文本）选择下拉列表中的选项。
     *
     * @param element the picklist element
     * @param text    the item value of the item to be selected
     */
    public void selectByVisibleText(WebElement element, String text) {
        waitForElementVisible(element, maxWaitfor);
        Select select = new Select(element);
        select.selectByVisibleText(text);
        Log.logInfo("item selected by visible text [ " + text + " ]");
    }

    /**
     * select an item from a picklist by item value
     * 按照指定选项的可见文本值（用户直接可以看到的文本）选择下拉列表中的选项。
     *
     * @param by   the locator you want to find the element
     * @param text the item value of the item to be selected
     */
    public void selectByVisibleText(By by, String text) {
        waitForElementVisible(by, maxWaitfor);
        Select select = new Select(findElement(by));
        select.selectByVisibleText(text);
        Log.logInfo("item selected by visible text [ " + text + " ] on [ " + by.toString() + " ]");
    }

    /**
     * set the checkbox on or off
     * 将指定的复选框对象设置为选中或者不选中状态。
     *
     * @param element the checkbox element
     * @param onOrOff on or off to set the checkbox
     */
    public void setCheckBox(WebElement element, String onOrOff) {
        waitForElementVisible(element, maxWaitfor);
        if ((onOrOff.toLowerCase().contains("on") && !element.isSelected())
                || (onOrOff.toLowerCase().contains("off") && element.isSelected())) {
            element.click();
        }
        Log.logInfo("the checkbox is set to [ " + onOrOff.toUpperCase() + " ]");
    }

    /**
     * set the checkbox on or off
     * 将指定的复选框对象设置为选中或者不选中状态。
     *
     * @param by      the locator you want to find the element
     * @param onOrOff on or off to set the checkbox
     */
    public void setCheckBox(By by, String onOrOff) {
        waitForElementVisible(by, maxWaitfor);
        WebElement checkBox = findElement(by);
        if ((onOrOff.toLowerCase().contains("on") && !checkBox.isSelected())
                || (onOrOff.toLowerCase().contains("off") && checkBox.isSelected())) {
            checkBox.click();
        }
        Log.logInfo("the checkbox [ " + by.toString() + " ] is set to [ " + onOrOff.toUpperCase() + " ]");
    }

    /**
     * find elements displayed on the page
     * 按照指定的定位方式寻找所有可见的对象。
     *
     * @param by the way to locate webelements
     * @return displayed webelement list
     */
    public List<WebElement> findDisplayedElments(By by) {
        List<WebElement> elementList = new ArrayList<WebElement>();
        WebElement element;
        List<WebElement> elements = driver.findElements(by);
        Iterator<WebElement> it = elements.iterator();
        while ((element = it.next()) != null && element.isDisplayed()) {
            elementList.add(element);
        }
        int eleNum = elementList.size();
        if (eleNum > 0) {
            Log.logInfo("got" + eleNum + "displayed elements [ " + by.toString() + " ]");
        } else {
            Log.logWarn("there is not displayed element found by [" + by.toString() + " ]");
        }
        return elementList;
    }

    /**
     * find elements displayed on the page
     * 按照指定的定位方式寻找第一可见的对象。
     *
     * @param by the way to locate webelement
     * @return the first displayed webelement
     */
    public WebElement findDisplayedElment(By by) {
        List<WebElement> elements = findDisplayedElments(by);
        return (elements.size() > 0) ? elements.get(0) : null;
    }

    /**
     * store the WebDriverWebTable object, it only changes on By changing
     * 缓存WebTable对象，在WebTable对象不为空的情况下（为空则直接新建对象），
     * 如果定位方式相同则直接返回原有对象，否则重新创建WebTable对象。
     *
     * @param tabBy the element locator By
     */
    public WebDriverOperate tableCache(By tabBy) {
        waitForElementVisible(tabBy, maxWaitfor);
        if (tabFinder == null) {
            tabFinder = tabBy;
            return new WebDriverOperate(driver, tabBy);
        } else {
            if (tabBy.toString().equals(tabFinder.toString())) {
                return webTable;
            } else {
                tabFinder = tabBy;
                return new WebDriverOperate(driver, tabBy);
            }
        }
    }

    /**
     * refresh the webtable on the same locator, only if it changes
     * 如果同一定位方式的WebTable内容发生变化需要重新定位，则需要刷新WebTable。
     */
    public void tableRefresh() {
        WebOperate.tabFinder = By.id("测男");
        WebOperate.webTable = null;
    }

    /**
     * get row count of a webtable 返回一个WebTable的行的总数。
     *
     * @param tabBy By, by which you can locate the webTable
     * @return the row count of the webTable
     */
    public int tableRowCount(By tabBy) {
        webTable = tableCache(tabBy);
        int rowCount = webTable.rowCount();
        Log.logInfo("the webTable " + tabBy.toString() + " has row count: [ " + rowCount + " ]");
        return rowCount;
    }

    /**
     * get column count of a specified webtable row
     * 返回一个WebTable在制定行上的列的总数。
     *
     * @param tabBy  By, by which you can locate the webTable
     * @param rowNum row index of your webTable to count
     * @return the column count of the row in webTable
     */
    public int tableColCount(By tabBy, int rowNum) {
        webTable = tableCache(tabBy);
        int colCount = webTable.colCount(rowNum);
        Log.logInfo("count columns of the webTable " + tabBy.toString() + " on the row [ " + rowNum + " ], got: [ " + colCount
                + " ]");
        return colCount;
    }

    /**
     * get the element in the webTable cell by row and col index
     * 返回WebTable中指定行、列和类型的子元素，如按钮、链接、输入框等。
     *
     * @param tabBy By, by which you can locate the webTable
     * @param row   row index of the webTable.
     * @param col   column index of the webTable.
     * @param type  the element type, such as "img"/"a"/"input" or
     *              "image/link/button/webedit"
     * @param index element index in the specified cell, begins with 1.
     * @return the webTable cell WebElement
     */
    public WebElement tableChildElement(By tabBy, int row, int col, String type, int index) {
        return tableCache(tabBy).childItem(row, col, type, index);
    }

    /**
     * get the cell text of the webTable on specified row and column
     * 返回WebTable的指定行和列的中的文本内容。
     *
     * @param tabBy By, by which you can locate the webTable
     * @param row   row index of the webTable.
     * @param col   column index of the webTable.
     * @return the cell text
     */
    public String tableCellText(By tabBy, int row, int col) {
        webTable = tableCache(tabBy);
        String text = webTable.cellText(row, col);
        Log.logInfo("the text of cell[" + row + "," + col + "] is: [ " + text + " ]");
        return text;
    }

    /**
     * wait for window appears in the time unit seconds
     * 在指定时间内等待窗口出现，超时则报错，用以缓冲运行，增加健壮性。
     *
     * @param browserTitle the title of the browser window.
     * @param seconds      timeout in timeunit of seconds.
     * @return if the window exists.
     */
    public boolean waitForWindowPresent(String browserTitle, int seconds) {
        Assert.assertTrue(browserExists(browserTitle, seconds), "window is not present after " + seconds + " seconds!");
        return true;
    }

    /**
     * wait for window appears in the time unit seconds
     * 在指定时间内等待新窗口出现，超时则报错，用以缓冲运行，增加健壮性。
     *
     * @param oldCount 旧窗口的句柄.
     * @param seconds  等待时间,单位:秒.
     * @return boolean
     */
    public boolean waitForNewWindowOpened(int oldCount, int seconds) {
        Assert.assertTrue(isNewWindowExits(oldCount, seconds), "new window did not opened in " + seconds + " seconds!");
        return true;
    }

    /**
     * wait for window appears in the time unit seconds
     * 在指定时间内等待新窗口出现，超时则报错，用以缓冲运行，增加健壮性。
     *
     * @param oldHandles 旧窗口的句柄集合.
     * @param seconds    等待时间,单位:秒.
     * @return boolean
     */
    public boolean waitForNewWindowOpened(Set<String> oldHandles, int seconds) {
        Assert.assertTrue(isNewWindowExits(oldHandles, seconds), "new window did not opened in " + seconds + " seconds!");
        return true;
    }

    /**
     * get some value from js functions.
     * 使用remote webdriver执行JS函数并且获得返回值。
     *
     * @param js js function string
     * @return Object
     */
    public Object jsReturner(String js) {
        return ((JavascriptExecutor) driver).executeScript(js);
    }

    /**
     * use js to judge if the jQuery page load completed.
     * 用js返回值判断jQuery页面是否加载完毕。
     *
     * @return boolean
     */
    public boolean jQueryLoadSucceed() {
        Long completed = (Long) jsReturner(IS_JQUERY_ACTIVE);
        return (completed == 0);
    }

    /**
     * use js to judge if the jQuery page load completed.
     * 用js返回值判断jQuery页面是否加载完毕，超时未加载完毕则报错。
     *
     * @param timeout max time used to load page.
     * @return boolean
     */
    public boolean waitForJQueryToLoad(int timeout) {
        long start = System.currentTimeMillis();
        boolean loadCompleted = false;
        while (!loadCompleted && ((System.currentTimeMillis() - start) < timeout * 1000)) {
            loadCompleted = jQueryLoadSucceed();
            waitFor(stepTimeUnit);
        }
        Assert.assertTrue(loadCompleted, "the jQuery page did not load complete in " + timeout + " seconds!");
        return true;
    }

    /**
     * use js to judge if the ajax page load completed.
     * 用js返回值判断AJAX页面是否加载完毕。
     *
     * @return boolean
     */
    public boolean ajaxLoadSucceed() {
        Long ajaxLoadCompleted = (Long) jsReturner(IS_AJAX_ACTIVE);
        return (ajaxLoadCompleted == 0);
    }

    /**
     * use js to judge if the ajax page load completed.
     * 用js返回值判断AJAX页面是否加载完毕，超时未加载完毕则报错。
     *
     * @param timeout max time used to load page.
     * @return boolean
     */
    public boolean waitForAjaxToLoad(int timeout) {
        long start = System.currentTimeMillis();
        boolean loadCompleted = false;
        while (!loadCompleted && ((System.currentTimeMillis() - start) < timeout * 1000)) {
            loadCompleted = ajaxLoadSucceed();
            waitFor(stepTimeUnit);
        }
        Assert.assertTrue(loadCompleted, "the ajax page did not load complete in " + timeout + " seconds!");
        return true;
    }

    /**
     * use js to judge if the browser load completed.
     * 用js返回值判断页面是否加载完毕。
     *
     * @return boolean
     */
    public boolean pageLoadSucceed() {
        Object loadCompleted = jsReturner(BROWSER_READY_STATUS);
        return loadCompleted.toString().toLowerCase().equals("complete");
    }

    /**
     * use js to judge if the browser load completed.
     * 用js返回值判断页面是否加载完毕，超时未加载完毕则报错。
     *
     * @param timeout max time used to load page.
     * @return boolean
     */
    public boolean waitForPageToLoad(int timeout) {
        long start = System.currentTimeMillis();
        boolean loadCompleted = false;
        while (!loadCompleted && ((System.currentTimeMillis() - start) < timeout * 1000)) {
            loadCompleted = pageLoadSucceed();
            waitFor(stepTimeUnit);
        }
        Assert.assertTrue(loadCompleted, "the page did not load complete in " + timeout + " seconds!");
        return true;
    }

    /**
     * judge if the alert is present in specified seconds
     * 在指定的时间内判断弹出的对话框（Dialog）是否存在。
     *
     * @param seconds timeout in seconds
     * @return boolean
     */
    public boolean alertExists(int seconds) {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < seconds * 1000) {
            try {
                driver.switchTo().alert();
                return true;
            } catch (NoAlertPresentException ne) {
            }
        }
        return false;
    }

    /**
     * wait for alert appears in the time unit of seconds
     * 在指定时间内等待，对话框（Dialog）出现，用以缓冲运行，增加健壮性。
     *
     * @param seconds time for wait, unit:second
     * @return boolean
     */
    public boolean waitForAlertPresent(int seconds) {
        Assert.assertTrue(alertExists(seconds), "alert does not appear in " + seconds + " seconds!");
        return true;
    }

    /**
     * judge if the element is existing
     * 判断指定的对象是否存在。
     *
     * @param by the element locator By
     * @return boolean
     */
    public boolean elementExists(By by) {
        try {
            return (findElements(by).size() > 0) ? true : false;
        } catch (NoSuchElementException ne) {
            return false;
        }
    }

    /**
     * judge if the element is present in specified seconds
     * 在指定的时间内判断指定的对象是否存在。
     *
     * @param by      the element locator By
     * @param seconds timeout in seconds
     * @return boolean
     */
    public boolean elementExists(By by, int seconds) {
        long start = System.currentTimeMillis();
        boolean exists = false;
        setElementLocateTimeout(1);
        while (!exists && ((System.currentTimeMillis() - start) < seconds * 1000)) {
            exists = findElements(by).size() > 0;
        }
        setElementLocateTimeout(maxWaitfor);
        return exists;
    }

    /**
     * Description: wait until window.
     *
     * @param count   init window count.
     * @param timeout for waiting.
     */
    public void waitForPageSyncronize(int count, int timeout) {
        long begins = System.currentTimeMillis();
        int windowCount = driver.getWindowHandles().size();
        windowCount = driver.getWindowHandles().size();
        while (windowCount != count && System.currentTimeMillis() - begins < timeout * 1000) {
            windowCount = driver.getWindowHandles().size();
        }
    }

    /**
     * wait for the element not visiable in timeout setting
     * 在指定时间内等待，直到对象不可见。
     *
     * @param by      the element locator.
     * @param seconds timeout in seconds.
     * @return boolean
     */
    public boolean waitForElementNotVisible(By by, int seconds) {
        try {
            setElementLocateTimeout(seconds);
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(by)) != null;
        } finally {
            setElementLocateTimeout(maxWaitfor);
        }
    }

    /**
     * wait for the element clickable in timeout setting
     * 在指定时间内等待，直到对象能够被点击。
     *
     * @param by      the element locator By
     * @param seconds timeout in seconds
     * @return boolean
     */
    public boolean waitForElementClickable(By by, int seconds) {
        try {
            setElementLocateTimeout(seconds);
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            return wait.until(ExpectedConditions.elementToBeClickable(by)) != null;
        } finally {
            setElementLocateTimeout(seconds);
        }
    }

    /**
     * wait for text appears on element in timeout setting
     * 在指定时间内等待，直到指定对象上出现指定文本。
     *
     * @param by      the element locator By
     * @param text    the text to be found of element
     * @param seconds timeout in seconds
     * @return boolean
     */
    public boolean waitForTextOnElement(By by, String text, int seconds) {
        try {
            setElementLocateTimeout(seconds);
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(by, text)) != null;
        } finally {
            setElementLocateTimeout(maxWaitfor);
        }
    }

    /**
     * wait for text appears in element attributes in timeout setting
     * 在指定时间内等待，直到指定对象的某个属性值等于指定文本。
     *
     * @param by      the element locator By
     * @param text    the text to be found in element attributes
     * @param seconds timeout in seconds
     * @return boolean
     */
    public boolean waitForTextOfElementAttr(By by, String text, int seconds) {
        try {
            setElementLocateTimeout(seconds);
            WebDriverWait wait = new WebDriverWait(driver, seconds);
            return wait.until(ExpectedConditions.textToBePresentInElementValue(by, text)) != null;
        } finally {
            setElementLocateTimeout(maxWaitfor);
        }
    }

    /**
     * make the alert dialog not to appears
     * 通过JS函数重载，在对话框（Alert）出现之前点击掉它，或者说等价于不让其出现。
     */
    public void ensrueBeforeAlert() {
        jsExecutor(ENSRUE_BEFORE_ALERT, "rewrite js to ensure alert before it appears");
    }

    /**
     * make the warn dialog not to appears when window.close()
     * 通过JS函数重载，在浏览器窗口关闭之前除去它的告警提示。
     */
    public void ensureBeforeWinClose() {
        jsExecutor(ENSURE_BEFORE_WINCLOSE, "rewrite js to ensure window close event");
    }

    /**
     * make the confirm dialog not to appears choose default option OK
     * 通过JS函数重载，在确认框（Confirm）出现之前点击确认，或者说等价于不让其出现而直接确认。
     */
    public void ensureBeforeConfirm() {
        jsExecutor(ENSURE_BEFORE_CONFIRM, "rewrite js to ensure confirm before it appears");
    }

    /**
     * make the confirm dialog not to appears choose default option Cancel
     * 通过JS函数重载，在确认框（Confirm）出现之前点击取消，或者说等价于不让其出现而直接取消。
     */
    public void dismissBeforeConfirm() {
        jsExecutor(DISMISS_BEFORE_CONFIRM,
                "rewrite js to dismiss confirm before it appears");
    }

    /**
     * make the prompt dialog not to appears choose default option OK
     * 通过JS函数重载，在提示框（Prompt）出现之前点击确认，或者说等价于不让其出现而直接确认。
     */
    public void ensureBeforePrompt() {
        jsExecutor(ENSURE_BEFORE_PROMPT, "rewrite js to ensure prompt before it appears");
    }

    /**
     * make the prompt dialog not to appears choose default option Cancel
     * 通过JS函数重载，在提示框（Prompt）出现之前点击取消，或者说等价于不让其出现而直接取消。
     */
    public void dismisBeforePrompt() {
        jsExecutor(DISMISS_BEFORE_PROMPT, "rewrite js to dismiss prompt before it appears");
    }

    /**
     * use js to make the element to be un-hidden
     * 使用JS执行的方法强制让某些隐藏的控件显示出来。
     *
     * @param element the element to be operate
     */
    public void makeElementUnHidden(WebElement element) {
        jsExecutor(MAKE_ELEMENT_UNHIDDEN, "rewrite js to make elements to be visible",
                element);
    }

    /**
     * use js to make the element to be un-hidden
     * 使用JS执行的方法强制让某些隐藏的控件显示出来。
     *
     * @param by the By locator to find the element
     */
    public void makeElementUnHidden(By by) {
        jsExecutor(MAKE_ELEMENT_UNHIDDEN, "rewrite js to make elements to be visible",
                findElement(by));
    }

    /**
     * 页面滚动到指定元素的位置
     *
     * @param element the element to be operate
     */
    public void scrollTo(WebElement element) {
        Log.logInfo("将页面滚动到指定元素的位置");
        ((JavascriptExecutor) this.driver).executeScript("arguments[0].scrollIntoView();", element);
    }

    /**
     * wait for and switch to frame when avilable in timeout setting
     * 在指定时间内等待，直到指定框架出现并且选择他。
     *
     * @param by      .
     * @param seconds timeout in seconds
     * @return boolean
     */
    public boolean waitForAndSwitchToFrame(By by, int seconds) {
        try {
            setElementLocateTimeout(seconds);
            WebDriverWait wait = new WebDriverWait(driver, seconds, maxWaitfor);
            return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by)) != null;
        } finally {
            setElementLocateTimeout(stepTimeUnit);
        }
    }

    /**
     * select a frame by locator, throw exception when timeout.
     * 按照locator选择框架（frame），在指定时间内frame不存在则报错。
     *
     * @param by      the name or id of the frame to select.
     * @param timeout time to wait for the frame available, unit of seconds.
     */
    public void selectFrameWithTimeout(By by, int timeout) {
        waitForAndSwitchToFrame(by, timeout);
        Log.logInfo("select frame by locator [ " + by + " ]");
    }
}
