package com.xxxx.uiautotest.util;

/**
 * Created by yuyilong on 15/11/8.
 */
public class HandleSafetyTips extends Thread {
    private boolean needTap = false;
    private int tapx = 0;
    private int tapy = 0;
    private String udid;

    public HandleSafetyTips(String udid, int x, int y) {
        tapx = x;
        tapy = y;
        this.udid = udid;
        needTap = true;
    }

    public void run() {
        try {
            Log.logInfo(Thread.currentThread().getName() + " 开始点击小米安全提示");

            Thread.sleep(20000);
            if (needTap) {
                for (int i = 0; i < 2; i++) {
                    Log.logInfo("点击屏幕, x = " + tapx + ", y = " + tapy);
                    Log.logInfo("adb -s " + udid + " shell input tap " + tapx + " " + tapy);
                    Runtime.getRuntime().exec("adb -s " + udid + " shell input tap " + tapx + " " + tapy);
                    if (i == 0)
                        Thread.sleep(3000);
                }
                Log.logInfo("Thread of handleSafetyTips is interrupt");
                Log.logInfo("关闭小米安全提示");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
