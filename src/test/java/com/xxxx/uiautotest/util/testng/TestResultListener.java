package com.xxxx.uiautotest.util.testng;

import com.xxxx.uiautotest.util.Log;
import com.xxxx.uiautotest.base.AutoTestBase;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import java.text.SimpleDateFormat;
import java.util.*;

public class TestResultListener extends TestListenerAdapter {
    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        Log.logInfo(tr.getName() + " Failure");
        saveScreenShot(tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        Log.logInfo(tr.getName() + " Skipped");
        saveScreenShot(tr);
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        Log.logInfo(tr.getName() + " Success");
    }

    @Override
    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
        Log.logInfo(tr.getName() + " Start");
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
        Set<Integer> passedTestIds = new HashSet<Integer>();
        for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
            Log.logInfo("PassedTests : " + passedTest.getName());
            passedTestIds.add(getId(passedTest));
        }

        Set<Integer> skipTestIds = new HashSet<Integer>();
        for (ITestResult skipTest : testContext.getSkippedTests().getAllResults()) {
            Log.logInfo("skipTest : " + skipTest.getName());

            int skipTestId = getId(skipTest);

            if (skipTestIds.contains(skipTestId) || passedTestIds.contains(skipTestId)) {
                testsToBeRemoved.add(skipTest);
            } else {
                skipTestIds.add(skipTestId);
            }
        }

        Set<Integer> failedTestIds = new HashSet<Integer>();
        for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
            Log.logInfo("FailedTest : " + failedTest.getName());
            int failedTestId = getId(failedTest);

            if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId) ||
                    skipTestIds.contains(failedTestId)) {
                testsToBeRemoved.add(failedTest);
            } else {
                failedTestIds.add(failedTestId);
            }
        }

        for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator.hasNext(); ) {
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)) {
                Log.logInfo("Remove repeat Fail Test : " + testResult.getName());
                iterator.remove();
            }
        }

    }

    private int getId(ITestResult result) {
        int id = result.getTestClass().getName().hashCode();
        id = id + result.getMethod().getMethodName().hashCode();
        id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }

    private void saveScreenShot(ITestResult tr) {
        String filePath_testngReports = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String mDateTime = formatter.format(new Date());
        String fileName = tr.getName() + "-" + mDateTime;

        try {
            filePath_testngReports = AutoTestBase.ScreenShot(fileName);
        } catch (Exception e) {
            Log.logInfo(tr.getName() + " takeScreenshot Failure:");
        }

        if (null != filePath_testngReports && !"".equals(filePath_testngReports)) {
            Reporter.setCurrentTestResult(tr);
            Reporter.log("<img src=\"../screenShots/" + fileName + ".png" + "\" style=\"width:180px;height:300px;\"/>");

        }
    }
}